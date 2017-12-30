package com.tevinjeffrey.rutgersct.ui.courseinfo

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.transition.Fade
import android.support.transition.TransitionInflater
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.data.model.Course
import com.tevinjeffrey.rutgersct.data.model.Section
import com.tevinjeffrey.rutgersct.data.model.openSections
import com.tevinjeffrey.rutgersct.ui.SearchViewModel
import com.tevinjeffrey.rutgersct.ui.base.BaseFragment
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoFragment
import com.tevinjeffrey.rutgersct.ui.utils.CircleSharedElementCallback
import com.tevinjeffrey.rutgersct.ui.utils.CircleView
import com.tevinjeffrey.rutgersct.ui.utils.ItemClickListener
import com.tevinjeffrey.rutgersct.utils.Utils
import kotlinx.android.synthetic.main.course_info_app_bar.appBar
import kotlinx.android.synthetic.main.course_info_app_bar.courseTitleText
import kotlinx.android.synthetic.main.course_info_app_bar.openSections
import kotlinx.android.synthetic.main.course_info_app_bar.shortenedCourseInfo
import kotlinx.android.synthetic.main.course_info_app_bar.toolbar
import kotlinx.android.synthetic.main.course_info_app_bar.totalSections
import kotlinx.android.synthetic.main.fragment_course_info.list
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class CourseInfoFragment : BaseFragment(), ItemClickListener<Section, View> {
  override fun fragmentName() = "CourseInfoFragment"

  @Inject lateinit var subcomponent: CourseInfoSubcomponent

  private val headerViews = ArrayList<View>()
  private var selectedCourse: Course? = null

  private lateinit var searchViewModel: SearchViewModel
  private lateinit var viewModel: CourseInfoViewModel

  override fun onAttach(context: Context) {
    searchViewModel = ViewModelProviders.of(parentActivity).get(SearchViewModel::class.java)
    viewModel = ViewModelProviders.of(parentActivity).get(CourseInfoViewModel::class.java)
    selectedCourse = searchViewModel.course
    super.onAttach(context)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    retainInstance = true
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    setToolbar(toolbar)
    parentActivity.supportActionBar?.setDisplayShowTitleEnabled(false)

    headerViews.add(createCourseMetaDataView())

    val layoutManager = LinearLayoutManager(parentActivity)
    layoutManager.orientation = LinearLayoutManager.VERTICAL
    list.layoutManager = layoutManager
    list.setHasFixedSize(true)
    list.adapter = CourseInfoAdapter(headerViews,
        selectedCourse?.sections.orEmpty(), this)

    setCourseTitle()
    setShortenedCourseInfo()
    setOpenSections()
    setTotalSections()
  }

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val themedInflater = inflater.cloneInContext(Utils.wrapContextTheme(
        parentActivity,
        R.style.RutgersCT_Accent
    ))
    return themedInflater.inflate(R.layout.fragment_course_info, container, false)
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.menu_course_info, menu)
  }

  override fun injectTargets() {
    subcomponent.inject(viewModel)
  }

  override fun onItemClicked(data: Section, view: View) {
    Timber.i("Selected section: %s", data)
    searchViewModel.section = data
    val bundle = Bundle()
    startSectionInfoFragment(bundle, view)
  }

  private fun createCourseMetaDataView(): View {
    val root = parentActivity
        .layoutInflater
        .inflate(R.layout.course_info_metadata, null) as ViewGroup
    for (data in selectedCourse?.metadata.orEmpty()) {
      val metadata = LayoutInflater.from(parentActivity).inflate(R.layout.metadata, null) as ViewGroup
      val title = metadata.findViewById<TextView>(R.id.metadata_title)
      val description = metadata.findViewById<TextView>(R.id.metadata_text)
      description.movementMethod = LinkMovementMethod()
      title.text = data.title
      description.text = Html.fromHtml(data.content)
      root.addView(metadata)
    }
    return root
  }

  private fun setCourseTitle() {
    courseTitleText.text = selectedCourse?.name
  }

  private fun setOpenSections() {
    openSections.text = selectedCourse?.openSections().toString()
  }

  private fun setShortenedCourseInfo() {
    //String offeringUnitCode = selectedCourse.getOfferingUnitCode();
    val subject = searchViewModel.subject
    val course = searchViewModel.course
    if (subject != null) {
      val shortenedCourseInfo = subject.number + ": " + subject.name + " › " + course?.number
      this.shortenedCourseInfo.text = shortenedCourseInfo
    }
  }

  private fun setTotalSections() {
    totalSections.text = selectedCourse?.sections?.size.toString()
  }

  private fun startSectionInfoFragment(b: Bundle, clickedView: View) {
    val sectionInfoFragment = SectionInfoFragment()

    val ft = fragmentManager!!.beginTransaction()

    val circleView = clickedView.findViewById<CircleView>(R.id.section_number_background)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

      val circleTransitionName = getString(R.string.transition_name_circle_view)
      circleView.transitionName = circleTransitionName
      ft.addSharedElement(circleView, circleTransitionName)

      appBar.transitionName = null

      val cifSectionEnter = TransitionInflater
          .from(parentActivity)
          .inflateTransition(R.transition.cif_section_enter)

      val cifSectionReturn = TransitionInflater
          .from(parentActivity)
          .inflateTransition(R.transition.cif_section_return)

      sectionInfoFragment.enterTransition = cifSectionEnter
      sectionInfoFragment.returnTransition = cifSectionReturn

      reenterTransition = Fade(Fade.IN).setDuration(200)

      val cifExit = TransitionInflater
          .from(parentActivity)
          .inflateTransition(R.transition.cif_exit)

      exitTransition = cifExit

      sectionInfoFragment.allowReturnTransitionOverlap = false
      sectionInfoFragment.allowEnterTransitionOverlap = false

      val sharedElementsEnter = TransitionInflater
          .from(parentActivity).inflateTransition(R.transition.cif_shared_element_enter)

      val sharedElementsReturn = TransitionInflater
          .from(parentActivity).inflateTransition(R.transition.cif_shared_element_return)

      sectionInfoFragment.sharedElementEnterTransition = sharedElementsEnter
      sectionInfoFragment.sharedElementReturnTransition = sharedElementsReturn

      val sharedelementCallback = CircleSharedElementCallback(circleTransitionName)
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

    sectionInfoFragment.arguments = b
    startFragment(this, sectionInfoFragment, ft)
  }
}