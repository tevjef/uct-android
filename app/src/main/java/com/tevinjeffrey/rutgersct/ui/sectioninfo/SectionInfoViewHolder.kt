package com.tevinjeffrey.rutgersct.ui.sectioninfo

import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.ButterKnife
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.data.model.Meeting
import com.tevinjeffrey.rutgersct.data.model.Section
import com.tevinjeffrey.rutgersct.data.model.extensions.Utils
import com.tevinjeffrey.rutgersct.ui.utils.CircleView

open class SectionInfoViewHolder(
    private val mParent: View,
    val instructors: TextView,
    val sectionNumberBackground: CircleView,
    val sectionTimeContainer: ViewGroup) : RecyclerView.ViewHolder(mParent) {

  val indexNumber: String
    get() = sectionNumberBackground.tag as String

  fun setInstructors(section: Section) {
    instructors.text = Utils.InstructorUtils.toString(section.instructors)
  }

  fun setOnClickListener(listener: View.OnClickListener) {
    mParent.setOnClickListener(listener)
  }

  fun setOpenStatus(section: Section) {
    if (section.status == "Open") {
      sectionNumberBackground.backgroundColor = ContextCompat.getColor(
          mParent.context,
          R.color.green
      )
    } else {
      sectionNumberBackground.backgroundColor = ContextCompat.getColor(
          mParent.context,
          R.color.red
      )
    }
  }

  fun setSectionNumber(section: Section) {
    sectionNumberBackground.titleText = section.number
    sectionNumberBackground.tag = section.topic_name

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      sectionNumberBackground.transitionName = section.topic_name
    }
  }

  fun setTimes(section: Section) {
    var mDayText: TextView
    var mSectionLocationText: TextView
    var mTimeText: TextView

    val meetingTimes = section.meetings
    //sort times so that Monday > Tuesday and Lecture > Recitation

    for (i in 0 until sectionTimeContainer.childCount) {
      val timeLayout = sectionTimeContainer.getChildAt(i)

      var time: Meeting? = null
      if (meetingTimes.size > 0 && meetingTimes.size - 1 >= i) {
        time = meetingTimes[i]
      }

      if (time == null) {
        timeLayout.visibility = View.GONE
      } else {

        mDayText = timeLayout.findViewById<View>(R.id.section_item_time_info_day_text) as TextView
        mSectionLocationText = timeLayout.findViewById<View>(R.id.section_item_time_info_location_text) as TextView
        mTimeText = timeLayout.findViewById<View>(R.id.section_item_time_info_meeting_time_text) as TextView

        timeLayout.visibility = View.VISIBLE
        //This is a reused layout that contains inforation not to be shown at this time.
        // The class location is not to be shown in the Tracked Section Fragment
        //timeLayout.findViewById(R.id.section_item_time_info_meeting_type).setVisibility(View.GONE);

        if (TextUtils.isEmpty(time.day)) {
          mDayText.text = time.class_type
        } else {
          mDayText.text = time.day
        }

        if (!TextUtils.isEmpty(time.start_time)) {
          mTimeText.text = time.start_time + " - " + time.end_time
        }

        if (!TextUtils.isEmpty(time.room)) {
          var locationText = time.room

          if (!TextUtils.isEmpty(time.class_type)) {
            locationText = locationText + "  " + time.class_type
          }

          mSectionLocationText.text = locationText
        }
      }
    }
  }

  companion object {

    fun newInstance(parent: View): SectionInfoViewHolder {
      val instructors = ButterKnife.findById<TextView>(parent, R.id.prof_text)
      val sectionNumberBackground = ButterKnife.findById<CircleView>(parent, R.id.section_number_background)
      val sectionTimeContainer = ButterKnife.findById<ViewGroup>(parent, R.id.section_item_time_container)

      return SectionInfoViewHolder(parent, instructors, sectionNumberBackground, sectionTimeContainer)
    }
  }
}