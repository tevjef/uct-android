package com.tevinjeffrey.rutgersct.ui.course

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.transition.ChangeBounds
import android.transition.Fade
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
import com.tevinjeffrey.rutgersct.ui.base.MVPFragment
import com.tevinjeffrey.rutgersct.ui.courseinfo.CourseInfoFragment
import com.tevinjeffrey.rutgersct.ui.utils.ItemClickListener
import com.tevinjeffrey.rutgersct.utils.Utils
import kotlinx.android.synthetic.main.error_view.try_again
import kotlinx.android.synthetic.main.fragment_courses.list
import kotlinx.android.synthetic.main.fragment_courses.swipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_courses.toolbar
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CourseFragment : MVPFragment(), SwipeRefreshLayout.OnRefreshListener, ItemClickListener<Course, View> {
  @Inject lateinit var subcomponent: CourseSubcomponent

  private lateinit var selectedSubject: Subject
  private var snackbar: Snackbar? = null

  private lateinit var searchFlowViewModel: SearchViewModel
  private lateinit var model: CourseViewModel

  private val adapter = CourseFragmentAdapter(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    retainInstance = true

    searchFlowViewModel = ViewModelProviders.of(activity).get(SearchViewModel::class.java)
    selectedSubject = searchFlowViewModel.subject!!

    initRecyclerView()
    initSwipeLayout()
    initToolbar()

    try_again.setOnClickListener { onRefresh() }

    model = ViewModelProviders.of(activity).get(CourseViewModel::class.java)
    model.loadCourseLiveData().observe(this, Observer { courseModel ->
      adapter.swapData(courseModel?.data ?: emptyList())
    })

    subcomponent.inject(model)
  }

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val themedInflater = inflater.cloneInContext(Utils.wrapContextTheme(activity, R.style.RutgersCT))
    return themedInflater.inflate(R.layout.fragment_courses, container, false)
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater!!.inflate(R.menu.menu_fragment_main, menu)
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

  override fun onDestroyView() {
    super.onDestroyView()
    snackbar?.dismiss()
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
    toolbar.title = selectedSubject.number + ": " + selectedSubject.name
    setToolbar(toolbar)
  }

  override fun onItemClicked(course: Course, view: View) {
    Timber.i("Selected course: %s", course)
    searchFlowViewModel.course = course
    startCourseInfoFragment(Bundle.EMPTY)
  }

  override fun onRefresh() {
    model.loadCourses()
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

  private fun startCourseInfoFragment(b: Bundle) {
    val courseInfoFragment = CourseInfoFragment()
    val ft = fragmentManager.beginTransaction()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      val changeBoundsTransition = ChangeBounds()
      changeBoundsTransition.interpolator = DecelerateInterpolator()

      courseInfoFragment.enterTransition = Fade(Fade.IN).setStartDelay(250)
      courseInfoFragment.returnTransition = Fade(Fade.OUT).setDuration(50)

      courseInfoFragment.allowReturnTransitionOverlap = false
      courseInfoFragment.allowEnterTransitionOverlap = false

      courseInfoFragment.sharedElementEnterTransition = changeBoundsTransition
      courseInfoFragment.sharedElementReturnTransition = changeBoundsTransition

      ft.addSharedElement(toolbar, getString(R.string.transition_name_tool_background))
    } else {
      ft.setCustomAnimations(
          R.animator.enter,
          R.animator.exit,
          R.animator.pop_enter,
          R.animator.pop_exit
      )
    }
    courseInfoFragment.arguments = b
    startFragment(this, courseInfoFragment, ft)
  }
}