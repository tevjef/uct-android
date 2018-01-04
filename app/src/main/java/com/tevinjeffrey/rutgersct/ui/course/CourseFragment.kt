package com.tevinjeffrey.rutgersct.ui.course

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.transition.ChangeBounds
import android.support.transition.Fade
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.data.model.Course
import com.tevinjeffrey.rutgersct.data.model.Subject
import com.tevinjeffrey.rutgersct.ui.SearchViewModel
import com.tevinjeffrey.rutgersct.ui.base.BaseFragment
import com.tevinjeffrey.rutgersct.ui.courseinfo.CourseInfoFragment
import com.tevinjeffrey.rutgersct.ui.utils.ItemClickListener
import com.tevinjeffrey.rutgersct.utils.Utils
import com.tevinjeffrey.rutgersct.utils.wrapTheme
import kotlinx.android.synthetic.main.error_view.try_again
import kotlinx.android.synthetic.main.fragment_courses.list
import kotlinx.android.synthetic.main.fragment_courses.swipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_courses.toolbar
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CourseFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener, ItemClickListener<Course, View> {
  override fun fragmentName() = "CourseFragment"

  @Inject lateinit var subcomponent: CourseSubcomponent

  private lateinit var selectedSubject: Subject

  private lateinit var searchViewModel: SearchViewModel
  private lateinit var viewModel: CourseViewModel

  private val adapter = CourseAdapter(this)

  override fun onAttach(context: Context) {
    searchViewModel = ViewModelProviders.of(parentActivity).get(SearchViewModel::class.java)
    viewModel = ViewModelProviders.of(parentActivity).get(CourseViewModel::class.java)
    super.onAttach(context)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    selectedSubject = searchViewModel.subject!!
    super.onCreate(savedInstanceState)
    retainInstance = true

    viewModel.courseLiveData.observe(this, Observer { model ->
      if (model == null) {
        return@Observer
      }

      if (model.error != null) {
        showError(model.error)
        return@Observer
      }

      swipeRefreshLayout.isRefreshing = model.isLoading

      if (model.data.isNotEmpty()) {
        adapter.swapData(model.data)
        dismissSnackbar()
      }
    })

    onRefresh()
  }

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val themedInflater = inflater.cloneInContext(parentActivity.wrapTheme(R.style.RutgersCT))
    return themedInflater.inflate(R.layout.fragment_courses, container, false)
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

    swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT)
    swipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green)
    swipeRefreshLayout.setOnRefreshListener(this)

    toolbar.title = selectedSubject.number + ": " + selectedSubject.name
    setToolbar(toolbar)

    try_again.setOnClickListener { onRefresh() }
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater?.inflate(R.menu.menu_fragment_main, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_refresh -> {
        onRefresh()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun injectTargets() {
    subcomponent.inject(viewModel)
  }

  override fun onItemClicked(data: Course, view: View) {
    Timber.i("Selected course: %s", data)
    searchViewModel.course = data
    startCourseInfoFragment(Bundle.EMPTY)
  }

  override fun onRefresh() {
    viewModel.loadCourses(searchViewModel.subject?.topic_name.orEmpty())
  }

  override fun showError(t: Throwable) {
    val message: String = when (t) {
      is UnknownHostException -> resources.getString(R.string.no_internet)
      is SocketTimeoutException -> resources.getString(R.string.timed_out)
      else -> t.message ?: ""
    }

    showSnackBar(message, {
      onRefresh()
    })
  }

  private fun startCourseInfoFragment(b: Bundle) {
    val courseInfoFragment = CourseInfoFragment()
    val ft = fragmentManager?.beginTransaction()?.apply {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val changeBoundsTransition = ChangeBounds()
        changeBoundsTransition.interpolator = DecelerateInterpolator()

        courseInfoFragment.enterTransition = Fade(Fade.IN).setStartDelay(250)
        courseInfoFragment.returnTransition = Fade(Fade.OUT).setDuration(50)

        courseInfoFragment.allowReturnTransitionOverlap = false
        courseInfoFragment.allowEnterTransitionOverlap = false

        courseInfoFragment.sharedElementEnterTransition = changeBoundsTransition
        courseInfoFragment.sharedElementReturnTransition = changeBoundsTransition

        addSharedElement(toolbar, getString(R.string.transition_name_tool_background))
      } else {
        setCustomAnimations(
            R.animator.enter,
            R.animator.exit,
            R.animator.pop_enter,
            R.animator.pop_exit
        )
      }
      courseInfoFragment.arguments = b
      startFragment(this@CourseFragment, courseInfoFragment, this)
    }
  }
}