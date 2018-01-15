package com.tevinjeffrey.rutgersct.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.transition.AutoTransition
import android.support.transition.ChangeBounds
import android.support.transition.Fade
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.firebase.iid.FirebaseInstanceId
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import icepick.Icepick
import icepick.State
import jonathanfinerty.once.Once
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

  //Helper methods to manage the back stack count. The count return from
  // getFragmentManager().getbackstackCount() is unreliable when using transitions
  @JvmField
  @field:[State]
  var backstackCount: Int = 0

  @Inject lateinit var context: Context
  @Inject lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)

    if (!Once.beenDone(Once.THIS_APP_INSTALL, SHOW_TOUR)) {
      val intent = Intent(this, IntroActivity::class.java)
      startActivity(intent)
      finish()
    }

    Timber.d("Token %s", FirebaseInstanceId.getInstance().token)
    setContentView(R.layout.activity_main)
    Icepick.restoreInstanceState(this, savedInstanceState)

    if (savedInstanceState == null) {
      val tsf = TrackedSectionsFragment().apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          enterTransition = AutoTransition().excludeTarget(ImageView::class.java, true)
          exitTransition = Fade(Fade.OUT).excludeTarget(ImageView::class.java, true)
          reenterTransition = AutoTransition().excludeTarget(ImageView::class.java, true)
          returnTransition = Fade(Fade.IN).excludeTarget(ImageView::class.java, true)
          allowReturnTransitionOverlap = false
          allowEnterTransitionOverlap = false
          sharedElementEnterTransition = ChangeBounds().setInterpolator(DecelerateInterpolator())
          sharedElementReturnTransition = ChangeBounds().setInterpolator(DecelerateInterpolator())
        }
      }

      supportFragmentManager.beginTransaction()
          .replace(R.id.container, tsf)
          .commit()
    }
    // checkGooglePlayVersion()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    return true
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    Icepick.saveInstanceState(this, outState)
  }

  override fun onBackPressed() {
    if (backstackCount > 0) {
      decrementBackstackCount()
      supportFragmentManager.popBackStackImmediate()
    } else {
      finish()
    }
  }

  private fun decrementBackstackCount() {
    --backstackCount
  }

  fun incrementBackstackCount() {
    ++backstackCount
  }

  override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
    return fragmentInjector
  }

  companion object {
    val SHOW_TOUR = "showTour"
  }

  private fun checkGooglePlayVersion(): Boolean {
    val apiAvailability = GoogleApiAvailability.getInstance()
    val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
    if (resultCode != ConnectionResult.SUCCESS) {
      if (apiAvailability.isUserResolvableError(resultCode)) {
        apiAvailability.getErrorDialog(this, resultCode, 42).show()
      } else {
        finish()
      }
      return false
    }
    return true
  }
}
