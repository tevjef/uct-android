package com.tevinjeffrey.rutgersct.ui.trackedsections

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Fade
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.afollestad.materialdialogs.MaterialDialog
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.data.search.UCTSubscription
import com.tevinjeffrey.rutgersct.ui.IntroActivity
import com.tevinjeffrey.rutgersct.ui.SearchViewModel
import com.tevinjeffrey.rutgersct.ui.base.BaseFragment
import com.tevinjeffrey.rutgersct.ui.chooser.ChooserFragment
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoFragment
import com.tevinjeffrey.rutgersct.ui.utils.CircleSharedElementCallback
import com.tevinjeffrey.rutgersct.ui.utils.CircleView
import com.tevinjeffrey.rutgersct.ui.utils.ItemClickListener
import com.tevinjeffrey.rutgersct.utils.Utils
import jonathanfinerty.once.Once
import kotlinx.android.synthetic.main.error_view.try_again
import kotlinx.android.synthetic.main.fragment_tracked_sections.fab
import kotlinx.android.synthetic.main.fragment_tracked_sections.list
import kotlinx.android.synthetic.main.fragment_tracked_sections.swipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_tracked_sections.toolbar
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class TrackedSectionsFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener, ItemClickListener<UCTSubscription, View> {

  @Inject lateinit var subcomponent: TrackedSectionsSubcomponent

  private lateinit var searchFlowViewModel: SearchViewModel
  private lateinit var viewModel: TrackedSectionsViewModel

  private val adapter = TrackedSectionsAdapter(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    viewModel = ViewModelProviders.of(activity).get(TrackedSectionsViewModel::class.java)
    searchFlowViewModel = ViewModelProviders.of(activity).get(SearchViewModel::class.java)
    super.onCreate(savedInstanceState)
    retainInstance = true
  }

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val themedInflater = inflater.cloneInContext(Utils.wrapContextTheme(activity, R.style.RutgersCT))
    val rootView = themedInflater.inflate(R.layout.fragment_tracked_sections, container, false) as ViewGroup

    if (!Once.beenDone(CORRUPT_SECTIONS) && !Once.beenDone(IntroActivity.TOUR_STARTED)) {
      // Show alert
      MaterialDialog.Builder(parentActivity)
          .title("Oops!")
          .titleColor(ContextCompat.getColor(parentActivity, R.color.primary))
          .positiveText("Ok")
          .content(
              "We were unable to restore your tracked sections after the latest update. We are sorry for the inconvenience.")
          .positiveColor(ContextCompat.getColor(parentActivity, R.color.primary))
          .show()
      Once.markDone(CORRUPT_SECTIONS)
    }
    return rootView
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    val layoutManager = LinearLayoutManager(parentActivity)
    layoutManager.orientation = LinearLayoutManager.VERTICAL
    layoutManager.isSmoothScrollbarEnabled = true
    list.addItemDecoration(DividerItemDecoration(list.context, DividerItemDecoration.VERTICAL))
    list.layoutManager = layoutManager
    list.setHasFixedSize(true)
    list.adapter = adapter

    swipeRefreshLayout.setOnRefreshListener(this)
    swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT)
    swipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green)

    setToolbar(toolbar)

    fab.setOnClickListener { startChooserFragment() }
    try_again.setOnClickListener { onRefresh() }

    viewModel.trackedSectionsLiveData.observe(this, Observer { model ->
      if (model == null) {
        return@Observer
      }

      if (model.error != null) {
        showError(model.error!!)
        return@Observer
      }

      swipeRefreshLayout.isRefreshing = model.isLoading

      adapter.swapData(model.data)
    })

    viewModel.loadTrackedSections()
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.menu_tracked_sections, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_refresh -> {
        onRefresh()
        true
      }
      R.id.action_webreg -> {
        launchWebReg()
        true
      }
      R.id.action_rate -> {
        launchMarket()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun injectTargets() {
    subcomponent.inject(viewModel)
  }

  override fun onItemClicked(subscription: UCTSubscription, view: View) {
    Timber.i("Selected subscription: %s", subscription)
    searchFlowViewModel.university = subscription.university
    searchFlowViewModel.subject = subscription.subject
    searchFlowViewModel.semester = subscription.semester
    searchFlowViewModel.course = subscription.course
    searchFlowViewModel.section = subscription.section
    startSectionInfoFragment(SectionInfoFragment.newInstance(), view)
  }

  override fun onRefresh() {
    // Retrieve section and while showing the loading animation.
    viewModel.loadTrackedSections()
  }

  override fun showError(t: Throwable) {
    val message: String
    val resources = context.resources
    message = when (t) {
      is UnknownHostException -> resources.getString(R.string.no_internet)
      is SocketTimeoutException -> resources.getString(R.string.timed_out)
      else -> t.message ?: ""
    }

    showSnackBar(message, {
      onRefresh()
    })
  }

  private fun launchMarket() {
    val uri = Uri.parse(
        "market://details?id=" + parentActivity.applicationContext.packageName)
    val rateAppIntent = Intent(Intent.ACTION_VIEW, uri)
    if (parentActivity.packageManager.queryIntentActivities(rateAppIntent, 0).size > 0) {
      startActivity(rateAppIntent)
    }
  }

  private fun launchWebReg() {
    val url = "http://webreg.rutgers.edu"
    val i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse(url)
    startActivity(i)
  }

  private fun startChooserFragment() {
    val chooserFragment = ChooserFragment.newInstance()
    val ft = fragmentManager.beginTransaction()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      toolbar!!.transitionName = getString(R.string.transition_name_tool_background)
      ft.addSharedElement(toolbar, getString(R.string.transition_name_tool_background))
      exitTransition = Fade(Fade.OUT).setDuration(resources.getInteger(R.integer.exit_anim).toLong())
      chooserFragment.allowEnterTransitionOverlap = false
      chooserFragment.allowReturnTransitionOverlap = false
    } else {
      ft.setCustomAnimations(
          R.animator.enter,
          R.animator.exit,
          R.animator.pop_enter,
          R.animator.pop_exit
      )
    }
    startFragment(this@TrackedSectionsFragment, chooserFragment, ft)
  }

  private fun startSectionInfoFragment(sectionInfoFragment: SectionInfoFragment, view: View) {
    val ft = this.fragmentManager.beginTransaction()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      val circleView = ButterKnife.findById<CircleView>(view, R.id.section_number_background)
      fab!!.transitionName = getString(R.string.transition_name_fab)
      ft.addSharedElement(fab, getString(R.string.transition_name_fab))

      circleView.transitionName = getString(R.string.transition_name_circle_view)
      ft.addSharedElement(circleView, getString(R.string.transition_name_circle_view))

      val tsfSectionEnter = TransitionInflater
          .from(parentActivity)
          .inflateTransition(R.transition.tsf_section_enter)

      val tsfSectionReturn = TransitionInflater
          .from(parentActivity)
          .inflateTransition(R.transition.tsf_section_return)

      sectionInfoFragment.enterTransition = tsfSectionEnter
      sectionInfoFragment.returnTransition = tsfSectionReturn

      reenterTransition = Fade(Fade.IN).addTarget(RecyclerView::class.java)
      exitTransition = Fade(Fade.OUT).addTarget(RecyclerView::class.java)

      sectionInfoFragment.allowReturnTransitionOverlap = false
      sectionInfoFragment.allowEnterTransitionOverlap = false

      val sharedElementsEnter = TransitionInflater
          .from(parentActivity)
          .inflateTransition(R.transition.tsf_shared_element_enter)
      val sharedElementsReturn = TransitionInflater
          .from(parentActivity)
          .inflateTransition(R.transition.tsf_shared_element_return)

      sectionInfoFragment.sharedElementEnterTransition = sharedElementsEnter
      sectionInfoFragment.sharedElementReturnTransition = sharedElementsReturn

      val sharedelementCallback = CircleSharedElementCallback()
      sectionInfoFragment.setEnterSharedElementCallback(sharedelementCallback)
      sharedElementsEnter.addListener(sharedelementCallback.transitionCallback)
    } else {
      ft.setCustomAnimations(
          R.animator.enter,
          R.animator.exit,
          R.animator.pop_enter,
          R.animator.pop_exit
      )
    }
    startFragment(this, sectionInfoFragment, ft)
  }

  companion object {
    val TAG = TrackedSectionsFragment::class.java.simpleName
    val CORRUPT_SECTIONS = "corrupt_sections"
  }
}
