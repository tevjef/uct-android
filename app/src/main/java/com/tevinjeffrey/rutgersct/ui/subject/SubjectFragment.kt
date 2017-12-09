package com.tevinjeffrey.rutgersct.ui.subject

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.data.model.Subject
import com.tevinjeffrey.rutgersct.data.model.extensions.Utils.SemesterUtils.readableString
import com.tevinjeffrey.rutgersct.ui.SearchViewModel
import com.tevinjeffrey.rutgersct.ui.base.MVPFragment
import com.tevinjeffrey.rutgersct.ui.course.CourseFragment
import com.tevinjeffrey.rutgersct.ui.utils.ItemClickListener
import com.tevinjeffrey.rutgersct.utils.Utils
import kotlinx.android.synthetic.main.error_view.try_again
import kotlinx.android.synthetic.main.fragment_subjects.list
import kotlinx.android.synthetic.main.fragment_subjects.swipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_subjects.toolbar
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class SubjectFragment : MVPFragment(), SwipeRefreshLayout.OnRefreshListener, ItemClickListener<Subject, View> {
  @Inject
  internal lateinit var subcomponent: SubjectSubcomponent

  private var snackbar: Snackbar? = null

  private lateinit var searchFlowViewModel: SearchViewModel
  private lateinit var model: SubjectViewModel

  private val adapter = SubjectAdapter(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    model = ViewModelProviders.of(activity).get(SubjectViewModel::class.java)
    searchFlowViewModel = ViewModelProviders.of(activity).get(SearchViewModel::class.java)
    super.onCreate(savedInstanceState)
    retainInstance = true
    initRecyclerView()
    initSwipeLayout()
    initToolbar()

    try_again.setOnClickListener { onRefresh() }

    model.loadSubjectData(
        searchFlowViewModel.university?.topic_name.orEmpty(),
        searchFlowViewModel.semester?.season.toString(),
        searchFlowViewModel.semester?.year.toString())
        .observe(this, Observer { model ->
          if (model?.error != null) {
            showError(model.error)
            return@Observer
          }
          adapter.swapData(model?.data ?: emptyList())
        })
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val themedInflater = inflater.cloneInContext(Utils.wrapContextTheme(activity, R.style.RutgersCT))
    return themedInflater.inflate(R.layout.fragment_subjects, container, false)
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.menu_fragment_main, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.action_refresh -> {
        this.onRefresh()
        return true
      }
      else -> return super.onOptionsItemSelected(item)
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    dismissSnackbar()
  }

  fun initRecyclerView() {
    val layoutManager = LinearLayoutManager(parentActivity)
    layoutManager.orientation = LinearLayoutManager.VERTICAL
    layoutManager.isSmoothScrollbarEnabled = true
    list.addItemDecoration(DividerItemDecoration(list.context, DividerItemDecoration.VERTICAL))
    list.layoutManager = layoutManager
    list.setHasFixedSize(true)
    list.adapter = adapter
  }

  fun initSwipeLayout() {
    swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT)
    swipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green)
    swipeRefreshLayout.setOnRefreshListener(this)
  }

  fun initToolbar() {
    setToolbarTitle()
    setToolbar(toolbar)
  }

  override fun injectTargets() {
    subcomponent.inject(model)
    subcomponent.inject(this)
  }

  override fun onItemClicked(subject: Subject, view: View) {
    Timber.i("Selected subject: %s", subject)
    searchFlowViewModel.subject = subject
    startCourseFragement(Bundle())
  }

  override fun onRefresh() {
    model.loadSubjects(
        searchFlowViewModel.university?.topic_name.orEmpty(),
        searchFlowViewModel.semester?.season.toString(),
        searchFlowViewModel.semester?.year.toString())
  }

  fun showError(t: Throwable) {
    val message: String
    val resources = context.resources
    message = when (t) {
      is UnknownHostException -> resources.getString(R.string.no_internet)
      is SocketTimeoutException -> resources.getString(R.string.timed_out)
      else -> t.message ?: ""
    }

    showSnackBar(message)
  }

  fun showLoading(pullToRefresh: Boolean) {
    swipeRefreshLayout.isRefreshing = pullToRefresh
  }

  private fun dismissSnackbar() {
    snackbar?.dismiss()
  }

  private fun setToolbarTitle() {
    val title = searchFlowViewModel.university?.abbr + " " + readableString(searchFlowViewModel.semester)
    super.setToolbarTitle(toolbar, title)
  }

  private fun showSnackBar(message: CharSequence) {
    snackbar = makeSnackBar(message)
    snackbar?.setAction(R.string.retry) { onRefresh() }
    snackbar?.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
      override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
        snackbar!!.removeCallback(this)
      }

      override fun onShown(transientBottomBar: Snackbar?) {}
    })
    snackbar!!.show()
  }

  private fun startCourseFragement(b: Bundle) {
    val courseFragment = CourseFragment()
    courseFragment.arguments = b
    val ft = fragmentManager.beginTransaction()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      val sfTransition = TransitionInflater.from(parentActivity).inflateTransition(R.transition.sf_exit)
      exitTransition = sfTransition.excludeTarget(Toolbar::class.java, true)
      courseFragment.allowEnterTransitionOverlap = false
    } else {
      ft.setCustomAnimations(
          R.animator.enter,
          R.animator.exit,
          R.animator.pop_enter,
          R.animator.pop_exit
      )
    }

    startFragment(this, courseFragment, ft)
  }
}