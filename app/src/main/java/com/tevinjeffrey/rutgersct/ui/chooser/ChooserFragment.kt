package com.tevinjeffrey.rutgersct.ui.chooser

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.data.model.Semester
import com.tevinjeffrey.rutgersct.data.model.University
import com.tevinjeffrey.rutgersct.data.model.extensions.Utils.SemesterUtils.readableString
import com.tevinjeffrey.rutgersct.ui.SearchViewModel
import com.tevinjeffrey.rutgersct.ui.base.BaseFragment
import com.tevinjeffrey.rutgersct.ui.subject.SubjectFragment
import com.tevinjeffrey.rutgersct.utils.Utils
import kotlinx.android.synthetic.main.fragment_chooser.searchBtn
import kotlinx.android.synthetic.main.fragment_chooser.semesterRadioGroup
import kotlinx.android.synthetic.main.fragment_chooser.toolbar
import kotlinx.android.synthetic.main.fragment_chooser.universitySpinner
import timber.log.Timber
import javax.inject.Inject

class ChooserFragment : BaseFragment() {

  @Inject lateinit var subcomponent: ChooserSubcomponent

  private lateinit var searchViewModel: SearchViewModel
  private lateinit var viewModel: ChooserViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    searchViewModel = ViewModelProviders.of(activity).get(SearchViewModel::class.java)
    viewModel = ViewModelProviders.of(activity).get(ChooserViewModel::class.java)
    super.onCreate(savedInstanceState)
    retainInstance = true

    viewModel.chooserSemesterLiveData.observe(this, Observer { model ->
      if (model == null) {
        return@Observer
      }

      if (model.error != null) {
        showError(model.error)
      }

      setAvailableSemesters(model.data)
    })


    viewModel.chooserUniversityLiveData.observe(this, Observer { model ->
      if (model == null) {
        return@Observer
      }

      if (model.error != null) {
        showError(model.error)
      }

      setUniversities(model.data)

      universitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
          viewModel.updateDefaultUniversity(model.data[position])
          viewModel.loadAvailableSemesters(model.data[position].topic_name!!)
        }

        override fun onNothingSelected(parent: AdapterView<*>) {}
      }

    })
  }

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {

    val themedInflater = inflater.cloneInContext(Utils.wrapContextTheme(activity, R.style.RutgersCT))
    return themedInflater.inflate(R.layout.fragment_chooser, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    setToolbar(toolbar)

    viewModel.loadAvailableSemesters(searchViewModel.university?.topic_name.orEmpty())

    searchBtn.setOnClickListener {
      val radioButton = semesterRadioGroup.findViewById<RadioButton>(semesterRadioGroup.checkedRadioButtonId)
      if (radioButton == null) {
        Toast.makeText(
            parentActivity,
            parentActivity.getString(R.string.select_a_semester),
            Toast.LENGTH_SHORT
        )
            .show()
        return@setOnClickListener
      }

      val tag = radioButton.tag
      if (tag == null) {
        Toast
            .makeText(
                parentActivity,
                parentActivity.getString(R.string.select_a_semester),
                Toast.LENGTH_SHORT
            )
            .show()
        return@setOnClickListener
      }

      val semester = tag as Semester
      Timber.d("%d %s", semesterRadioGroup.checkedRadioButtonId, semester)

      searchViewModel.newSearch()
      searchViewModel.university = viewModel.defaultUniversity
      searchViewModel.semester = viewModel.defaultSemester

      startSubjectFragment(Bundle())
    }
  }

  override fun injectTargets() {
    subcomponent.inject(viewModel)
  }

  private fun setAvailableSemesters(semesters: List<Semester>) {
    searchBtn.isEnabled = true
    semesterRadioGroup.removeAllViews()
    val defaultSemester = viewModel.defaultSemester

    for (semester in semesters) {
      val radioButton = LayoutInflater
          .from(this.parentActivity)
          .inflate(R.layout.radio_button, null) as RadioButton
      radioButton.text = readableString(semester)
      val layoutParams = ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.WRAP_CONTENT
      )
      radioButton.tag = semester
      semesterRadioGroup!!.addView(radioButton, layoutParams)

      radioButton.isChecked = semester == defaultSemester
    }

    semesterRadioGroup!!.setOnCheckedChangeListener { group, checkedId ->
      if (checkedId != -1) {
        val semester = group.findViewById<View>(checkedId).tag as Semester
        viewModel.updateSemester(semester)
      }
    }
  }

  fun setUniversities(universities: List<University>) {
    val universityList = ArrayList<String>()
    val defaultUni = viewModel.defaultUniversity

    var select = universities.indexOfFirst { it.topic_name == defaultUni?.topic_name }
    if (select == -1) {
      select = 0
    }

    universities.forEachIndexed { index, university ->
      universityList.add(university.name.orEmpty())
    }

    val arrayAdapter = ArrayAdapter(parentActivity, R.layout.spinner_item, universityList)
    arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
    universitySpinner.adapter = arrayAdapter
    universitySpinner.setSelection(select)
  }

  private fun startSubjectFragment(b: Bundle) {
    val sf = SubjectFragment()
    sf.arguments = b
    val ft = fragmentManager.beginTransaction()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      sf.exitTransition = Fade(Fade.OUT)
          .excludeTarget(ImageView::class.java, true)
          .setDuration(100)
      sf.returnTransition = Fade(Fade.OUT)
          .excludeTarget(ImageView::class.java, true)
          .setDuration(100)
      sf.allowReturnTransitionOverlap = false
      sf.allowEnterTransitionOverlap = false
      sf.sharedElementEnterTransition = ChangeBounds().setInterpolator(DecelerateInterpolator())
      sf.sharedElementReturnTransition = ChangeBounds().setInterpolator(DecelerateInterpolator())
      ft.addSharedElement(toolbar, getString(R.string.transition_name_tool_background))
    } else {
      ft.setCustomAnimations(
          R.animator.enter,
          R.animator.exit,
          R.animator.pop_enter,
          R.animator.pop_exit
      )
    }
    startFragment(this, sf, ft)
  }

  companion object {

    fun newInstance(): ChooserFragment {
      val newInstance = ChooserFragment()

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        newInstance.exitTransition = Fade(Fade.OUT)
            .setDuration(50)
            .excludeTarget(ImageView::class.java, true)

        newInstance.enterTransition = Fade(Fade.IN)
        newInstance.returnTransition = Fade(Fade.OUT)
            .excludeTarget(ImageView::class.java, true)
            .setDuration(50)

        newInstance.sharedElementEnterTransition = ChangeBounds().setInterpolator(DecelerateInterpolator())
        newInstance.sharedElementReturnTransition = ChangeBounds().setInterpolator(DecelerateInterpolator())
      }

      return newInstance
    }
  }
}