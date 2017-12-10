package com.tevinjeffrey.rutgersct.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.annotation.MainThread
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.dlazaro66.wheelindicatorview.WheelIndicatorItem
import com.dlazaro66.wheelindicatorview.WheelIndicatorView
import com.tevinjeffrey.rmp.common.Professor
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.data.UrlUtils
import com.tevinjeffrey.rutgersct.data.model.Section

class RatingLayoutInflater(context: Activity, private val professor: Professor) {
  private val mContext: Context

  val professorLayout: ViewGroup
    @MainThread
    get() {
      val root = LayoutInflater.from(mContext).inflate(R.layout.section_info_rmp_rating, null) as ViewGroup
      setOpenInBrowser(root)
      setName(root)
      setSubtitle(root)
      setOverall(root)
      setEasiness(root)
      tagView(root)

      return root
    }

  init {
    this.mContext = context
  }

  private fun append(str: String): String {
    return " - " + str
  }

  private fun getItem(percentage: Double): WheelIndicatorItem {
    return getItem(1.0f, percentage)
  }

  private fun getItem(weight: Float, percentage: Double): WheelIndicatorItem {
    return WheelIndicatorItem(weight, getRatingColor(percentage))
  }

  private fun getRatingColor(rating: Double): Int {
    return if (rating < LOW_RATING_LIMIT) {
      ContextCompat.getColor(mContext, R.color.rating_low)
    } else if (rating < MEDIUM_RATING_LIMIT) {
      ContextCompat.getColor(mContext, R.color.rating_medium)
    } else {
      ContextCompat.getColor(mContext, R.color.rating_high)
    }
  }

  private fun setEasiness(root: ViewGroup) {
    val rating = Math.abs(professor.getRating().getEasiness() - 5) / 5
    val percentage = rating * 100
    val easinessWheel: WheelIndicatorView = root.findViewById(R.id.wheel_easiness_rating)
    val overallEasinessText: TextView = root.findViewById(R.id.rmp_easiness_rating_number)
    overallEasinessText.text = (Math.round(Math.abs(professor.getRating().getEasiness() - 5) * 100.0) / 100.0).toString()
    easinessWheel.filledPercent = percentage.toInt()
    easinessWheel.addWheelIndicatorItem(getItem(percentage))
    easinessWheel.startItemsAnimation()
  }

  private fun setName(root: ViewGroup) {
    val professorName = professor.getFirstName() + " " + professor.getLastName()
    val professorNameText: TextView = root.findViewById(R.id.rmp_prof_name)
    professorNameText.text = professorName
  }

  private fun setOpenInBrowser(root: ViewGroup) {
    val openInBrowser: ImageView = root.findViewById(R.id.open_in_browser)
    openInBrowser.setOnClickListener({
      val url = professor.getRating().fullRatingUrl
      val i = Intent(Intent.ACTION_VIEW)
      i.data = Uri.parse(url)
      mContext.startActivity(i)
    })
  }

  private fun setOverall(root: ViewGroup) {
    val rating = professor.getRating().getOverall() / 5
    val percentage = rating * 100
    val overallQualityWheel: WheelIndicatorView = root.findViewById(R.id.wheel_quality_rating)
    val overallQualityText: TextView = root.findViewById(R.id.rmp_overall_rating_number)
    overallQualityText.text = professor.getRating().getOverall().toString()
    overallQualityWheel.filledPercent = percentage.toInt()
    overallQualityWheel.addWheelIndicatorItem(getItem(percentage))
    overallQualityWheel.startItemsAnimation()
  }

  private fun setSubtitle(root: ViewGroup) {
    val professorDepartment = professor.getDepartment()
    val professorDepartmentText: TextView = root.findViewById(R.id.rmp_subtitle)
    var str = professorDepartment
    if (professor.getTitle() != null) {
      str += append(professor.getTitle())
    }
    if (professor.getLocation().getCity() != null) {
      str += append(professor.getLocation().getCity())
    }
    professorDepartmentText.text = str
  }

  private fun tagView(root: ViewGroup) {
    root.tag = "http://www.ratemyprofessors.com" + professor.getRating().getRatingUrl()
  }

  companion object {
    fun getErrorLayout(context: Context, professorName: String, s: Section): View {
      val message = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
          .inflate(R.layout.no_professor, null) as TextView
      val url = "http://www.google.com/#q=" + UrlUtils.getGoogleUrl(s)
      message.tag = url
      message.text = context.getString(R.string.could_not_find_professor) + professorName
      return message
    }

    val LOW_RATING_LIMIT = 40
    val MEDIUM_RATING_LIMIT = 60
  }
}
