package com.tevinjeffrey.rutgersct.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.AppIntroFragment
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.data.UCTApi
import com.tevinjeffrey.rutgersct.data.UCTApi.Companion
import jonathanfinerty.once.Once

class IntroActivity : AppIntro2() {

  override fun init(bundle: Bundle?) {
    showStatusBar(false)
    addSlide(AppIntroFragment.newInstance(
        "View course info!",
        "Get an comprehensive view of all sections and view information about course requirements.",
        R.drawable.slide_see_course,
        ContextCompat.getColor(this, R.color.accent)
    ))

    addSlide(AppIntroFragment.newInstance(
        "Track or view section info!",
        "View basic information about a section or track when it opens",
        R.drawable.slide_add_section,
        ContextCompat.getColor(this, R.color.red)
    ))

    addSlide(AppIntroFragment.newInstance(
        "Choose wisely!",
        "Get professor ratings and info to make better decisions when choosing your classes.",
        R.drawable.slide_see_ratings,
        ContextCompat.getColor(this, R.color.green)
    ))
  }

  override fun onDonePressed() {
    Once.markDone(UCTApi.TRACKED_SECTIONS_MIGRATION)
    Once.markDone(MainActivity.SHOW_TOUR)
    Once.markDone(TOUR_DONE)
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
    finish()
  }

  override fun onNextPressed() {

  }

  override fun onSlideChanged() {

  }

  companion object {
    val TOUR_DONE = "tour_started"
  }
}
