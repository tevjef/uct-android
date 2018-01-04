package com.tevinjeffrey.rutgersct.ui.subject

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.transition.TransitionInflater
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.data.model.Subject
import com.tevinjeffrey.rutgersct.data.model.string
import com.tevinjeffrey.rutgersct.ui.SearchViewModel
import com.tevinjeffrey.rutgersct.ui.base.BaseFragment
import com.tevinjeffrey.rutgersct.ui.course.CourseFragment
import com.tevinjeffrey.rutgersct.ui.utils.ItemClickListener
import com.tevinjeffrey.rutgersct.utils.Utils
import com.tevinjeffrey.rutgersct.utils.wrapTheme
import kotlinx.android.synthetic.main.error_view.try_again
import kotlinx.android.synthetic.main.fragment_subjects.list
import kotlinx.android.synthetic.main.fragment_subjects.swipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_subjects.toolbar
import timber.log.Timber
import javax.inject.Inject

class SubjectFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener, ItemClickListener<Subject, View> {
  override fun fragmentName() = "SubjectFragment"

  @Inject internal lateinit var subcomponent: SubjectSubcomponent

  private lateinit var searchFlowViewModel: SearchViewModel
  private lateinit var viewModel: SubjectViewModel

  private val adapter = SubjectAdapter(this)

  override fun onAttach(context: Context) {
    viewModel = ViewModelProviders.of(parentActivity).get(SubjectViewModel::class.java)
    searchFlowViewModel = ViewModelProviders.of(parentActivity).get(SearchViewModel::class.java)
    super.onAttach(context)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    retainInstance = true

    viewModel.subjectData.observe(this, Observer { model ->
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
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val themedInflater = inflater.cloneInContext(parentActivity.wrapTheme(R.style.RutgersCT))
    return themedInflater.inflate(R.layout.fragment_subjects, container, false)
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

    setToolbarTitle()
    setToolbar(toolbar)

    try_again.setOnClickListener { onRefresh() }
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.menu_fragment_main, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_refresh -> {
        this.onRefresh()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun injectTargets() {
    subcomponent.inject(viewModel)
  }

  override fun onItemClicked(data: Subject, view: View) {
    Timber.i("Selected subject: %s", data)
    searchFlowViewModel.subject = data
    startCourseFragement(Bundle())
  }

  override fun onRefresh() {
    viewModel.loadSubjects(
        searchFlowViewModel.university?.topic_name.orEmpty(),
        searchFlowViewModel.semester?.season.toString(),
        searchFlowViewModel.semester?.year.toString())
  }

  private fun setToolbarTitle() {
    val title = searchFlowViewModel.university?.abbr + " " + searchFlowViewModel.semester?.string()
    super.setToolbarTitle(toolbar, title)
  }

  private fun startCourseFragement(b: Bundle) {
    val courseFragment = CourseFragment()
    courseFragment.arguments = b
    val ft = fragmentManager!!.beginTransaction()

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