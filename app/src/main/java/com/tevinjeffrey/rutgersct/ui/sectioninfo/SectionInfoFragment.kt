package com.tevinjeffrey.rutgersct.ui.sectioninfo

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.app.ActivityManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import butterknife.ButterKnife
import com.tevinjeffrey.rmp.common.Professor
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.ui.SearchViewModel
import com.tevinjeffrey.rutgersct.ui.base.BaseFragment
import com.tevinjeffrey.rutgersct.ui.utils.RatingLayoutInflater
import com.tevinjeffrey.rutgersct.utils.Utils
import kotlinx.android.synthetic.main.fragment_section_info.fab
import kotlinx.android.synthetic.main.section_info_app_bar.courseTitleText
import kotlinx.android.synthetic.main.section_info_app_bar.creditsLayout
import kotlinx.android.synthetic.main.section_info_app_bar.creditsText
import kotlinx.android.synthetic.main.section_info_app_bar.indexNumberText
import kotlinx.android.synthetic.main.section_info_app_bar.instructorsText
import kotlinx.android.synthetic.main.section_info_app_bar.sectionNumberText
import kotlinx.android.synthetic.main.section_info_app_bar.semesterText
import kotlinx.android.synthetic.main.section_info_app_bar.toolbar
import kotlinx.android.synthetic.main.section_info_metadata.sectionMetadata
import kotlinx.android.synthetic.main.section_info_rmp.ratingsContainer
import kotlinx.android.synthetic.main.section_info_rmp.ratingsRoot
import kotlinx.android.synthetic.main.section_info_times.sectionTimesContainer
import javax.inject.Inject

class SectionInfoFragment : BaseFragment() {

  @Inject lateinit var subcomponent: SectionInfoSubcomponent

  private lateinit var searchViewModel: SearchViewModel
  private lateinit var sectionInfoViewModel: SectionInfoViewModel
  private lateinit var viewModel: SectionInfoViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    searchViewModel = ViewModelProviders.of(activity).get(SearchViewModel::class.java)
    viewModel = ViewModelProviders.of(activity).get(SectionInfoViewModel::class.java)
    viewModel.searchViewModel = searchViewModel
    super.onCreate(savedInstanceState)
    retainInstance = true
  }

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val contextThemeWrapper: Context
    // create ContextThemeWrapper from the original Activity Context with the custom theme
    if (searchViewModel.section?.status == "Open") {
      contextThemeWrapper = Utils.wrapContextTheme(activity, R.style.RutgersCT_Green)
    } else {
      contextThemeWrapper = Utils.wrapContextTheme(activity, R.style.RutgersCT_Red)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.setTaskDescription(
            ActivityManager.TaskDescription(null, null,
                ContextCompat.getColor(container?.context, R.color.red)
            )
        )
      }
    }

    // clone the inflater using the ContextThemeWrapper
    val themedInflater = inflater.cloneInContext(contextThemeWrapper)
    return themedInflater.inflate(R.layout.fragment_section_info, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    setToolbar(toolbar)
    parentActivity.supportActionBar?.setDisplayShowTitleEnabled(false)

    setSectionNumber()
    setSectionIndex()
    setSectionCredits()
    setCourseTitle()
    showSectionMetadata()
    setTimes()
    setInstructors()
    setSemester()

    showFab(false)

    viewModel.sectionInfoLiveData.observe(this, Observer { model ->
      if (model == null) {
        return@Observer
      }

      if (model.error != null) {
        return@Observer
      }

      showSectionTracked(model.isSectionAdded, model.shouldAnimateFabIn)
    })

    viewModel.rmpLiveData.observe(this, Observer { model ->
      if (model == null) {
        return@Observer
      }

      model.professorNotFound.forEach {
        addRMPView(RatingLayoutInflater(parentActivity, null)
            .getErrorLayout(it, searchViewModel.section))
      }

      model.professor.forEach {
        addRMPProfessor(it)
      }

      if (model.showRatingsLayout) {
        ratingsContainer.visibility = VISIBLE
      } else {
        val progress = ratingsRoot.findViewById<ViewGroup>(R.id.rmp_progressview)
        progress.visibility = GONE
      }
    })
    fab.setOnClickListener { sectionInfoViewModel.toggleFab() }
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.menu_fragment_info, menu)
  }

  fun addRMPProfessor(professor: Professor) {
    val ratingLayoutInflater = RatingLayoutInflater(parentActivity, professor)
    addRMPView(ratingLayoutInflater.professorLayout)
  }

  override fun injectTargets() {
    subcomponent.inject(viewModel)
  }

  fun showFab(animate: Boolean) {
    if (animate) {
      fab.animate()
          .scaleX(1f)
          .scaleY(1f)
          .alpha(1f)
          .setInterpolator(DecelerateInterpolator())
          .setStartDelay(400)
          .setDuration(250)
          .start()
    } else {
      fab.animate()
          .scaleX(1f)
          .scaleY(1f)
          .alpha(1f)
          .setInterpolator(DecelerateInterpolator())
          .setDuration(0)
          .start()
    }
  }

  private fun showSectionTracked(sectionIsAdded: Boolean, shouldAnimateView: Boolean) {
    val COLOR = ContextCompat.getColor(parentActivity, R.color.accent)
    val COLOR_DARK = ContextCompat.getColor(parentActivity, R.color.accent_dark)
    val ROTATION_NORMAL = 0
    val ROTATION_ADDED = 225
    val DURATION = 500

    if (shouldAnimateView) {
      if (sectionIsAdded) {
        ViewCompat.animate(fab).setDuration(DURATION.toLong()).setInterpolator(DecelerateInterpolator())
            .rotation((if (sectionIsAdded) ROTATION_ADDED else ROTATION_NORMAL).toFloat())
        //I would much prefer to animate from the current coolor to the next but the fab has
        // no method to get the current color and I'm not desparate enough to manage it myself.
        // As for now, the fab will only animate on user click. Not from a db update.
        val colorAnim = ObjectAnimator.ofInt(this, "backgroundColor",
            if (sectionIsAdded) COLOR else COLOR_DARK,
            if (sectionIsAdded) COLOR_DARK else COLOR
        )
        colorAnim.duration = 500
        colorAnim.addUpdateListener { animation ->
          if (fab != null) {
            fab.backgroundTintList = ColorStateList.valueOf(animation.animatedValue as Int)
          }
        }
        colorAnim.setEvaluator(ArgbEvaluator())
        colorAnim.start()
      }
    } else {
      //Using ViewCompat to set the tint list is bugged on pre lollipop.
      fab.backgroundTintList = ColorStateList.valueOf(if (sectionIsAdded) COLOR_DARK else COLOR)
      ViewCompat.setRotation(fab, (if (sectionIsAdded) ROTATION_ADDED else ROTATION_NORMAL).toFloat())
    }
  }

  private fun addRMPView(view: View) {
    view.alpha = 0f
    view.animate().setStartDelay(200).alpha(1f).start()
    ratingsContainer.addView(view)
  }

  private fun setCourseTitle() {
    toolbar.title = ""
    courseTitleText.text = searchViewModel.course?.name
  }

  private fun setInstructors() {
    if (searchViewModel.section?.instructors?.size != 0) {
      instructorsText.text = com.tevinjeffrey.rutgersct.data.model.extensions.Utils.InstructorUtils
          .toString(searchViewModel.section?.instructors)
    } else {
      instructorsText.visibility = View.GONE
    }
  }

  private fun setSectionCredits() {
    val credits = searchViewModel.section?.credits

    if (TextUtils.isEmpty(credits) || credits == "-1") {
      creditsLayout.visibility = View.GONE
    }
    creditsText.text = credits
  }

  private fun setSectionIndex() {
    indexNumberText.text = searchViewModel.section?.call_number
  }

  private fun setSectionNumber() {
    sectionNumberText.text = searchViewModel.section?.number
  }

  private fun setSemester() {
    semesterText.text = com.tevinjeffrey.rutgersct.data.model.extensions.Utils.SemesterUtils
        .readableString(searchViewModel.semester)
  }

  private fun setTimes() {
    val inflater = LayoutInflater.from(parentActivity)

    //sort times so that Monday > Tuesday and Lecture > Recitation
    for (time in searchViewModel.section?.meetings.orEmpty()) {

      val timeLayout = inflater.inflate(R.layout.section_item_time, sectionTimesContainer, false)

      val dayText = ButterKnife.findById<TextView>(timeLayout, R.id.section_item_time_info_day_text)
      val timeText = ButterKnife.findById<TextView>(timeLayout, R.id.section_item_time_info_meeting_time_text)
      val meetingTimeText = ButterKnife.findById<TextView>(timeLayout, R.id.section_item_time_info_meeting_type)

      if (TextUtils.isEmpty(time.day)) {
        dayText.text = time.class_type
      } else {
        dayText.text = time.day
      }

      if (!TextUtils.isEmpty(time.start_time)) {
        timeText.text = time.start_time + " - " + time.end_time
      }

      if (!TextUtils.isEmpty(time.room)) {
        var locationText = time.room

        if (!TextUtils.isEmpty(time.class_type)) {
          locationText = locationText + "  " + time.class_type
        }

        meetingTimeText.text = locationText
      }

      sectionTimesContainer.addView(timeLayout)
    }
  }

  private fun showSectionMetadata() {
    for (data in searchViewModel.section?.metadata.orEmpty()) {
      val metadata = LayoutInflater.from(parentActivity).inflate(R.layout.metadata, null) as ViewGroup
      val title = ButterKnife.findById<TextView>(metadata, R.id.metadata_title)
      val description = ButterKnife.findById<TextView>(metadata, R.id.metadata_text)
      description.movementMethod = LinkMovementMethod()
      title.text = data.title
      description.text = data.content
      sectionMetadata.addView(metadata)
    }
  }

  companion object {
    fun newInstance(): SectionInfoFragment {
      return SectionInfoFragment()
    }
  }
}