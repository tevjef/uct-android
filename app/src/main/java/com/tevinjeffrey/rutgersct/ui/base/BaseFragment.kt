package com.tevinjeffrey.rutgersct.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.data.analytics.Analytics
import com.tevinjeffrey.rutgersct.ui.MainActivity
import com.tevinjeffrey.rutgersct.ui.settings.SettingsActivity
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsFragment
import dagger.android.support.AndroidSupportInjection
import icepick.Icepick
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

  @Inject lateinit var analytics: Analytics

  val parentActivity: MainActivity
    get() = activity as MainActivity

  internal var snackbar: Snackbar? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
    Icepick.restoreInstanceState(this, savedInstanceState)
  }

  override fun onAttach(context: Context) {
    AndroidSupportInjection.inject(this)
    injectTargets()
    super.onAttach(context)
  }

  private fun makeSnackBar(message: CharSequence) {
    snackbar = Snackbar.make(view!!, message, Snackbar.LENGTH_LONG).apply {
      setActionTextColor(ResourcesCompat.getColor(
          resources,
          android.R.color.white, null
      ))
      view.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.accent, null))
    }
  }

  fun showSnackBar(message: CharSequence, retry: (() -> Unit)? = null) {
    makeSnackBar(message)
    this.snackbar?.apply {
      setText(message)
      if (retry != null) {
        setAction(R.string.retry) { retry() }
        addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
          override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            removeCallback(this)
          }
        })
      }
      show()
    }
  }

  open fun showError(t: Throwable) {
    val message: String = when (t) {
      is UnknownHostException -> resources.getString(R.string.no_internet)
      is SocketTimeoutException -> resources.getString(R.string.timed_out)
      else -> t.message ?: ""
    }

    showSnackBar(message)
  }

  override fun onResume() {
    super.onResume()
    analytics.logScreenView(parentActivity, this.toString())
  }

  override fun onDestroyView() {
    super.onDestroyView()
    snackbar?.dismiss()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_track -> {

        parentActivity.backstackCount = 0
        fragmentManager!!.popBackStackImmediate(
            TrackedSectionsFragment.TAG,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        true
      }
      R.id.action_settings -> {
        parentActivity.startActivity(Intent(parentActivity, SettingsActivity::class.java))
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    Icepick.saveInstanceState(this, outState)
  }

  abstract fun injectTargets()

  fun dismissSnackbar() {
    snackbar?.dismiss()
  }

  fun setToolbar(toolbar: Toolbar) {
    toolbar.setTitleTextAppearance(parentActivity, R.style.ToolbarTitleStyle)
    toolbar.setSubtitleTextAppearance(
        parentActivity,
        R.style.ToolbarSubtitleStyle_TextApperance
    )

    parentActivity.setSupportActionBar(toolbar)
    toolbar.setNavigationOnClickListener { parentActivity.onBackPressed() }

    val actionBar = parentActivity.supportActionBar

    if (actionBar != null && parentActivity.backstackCount > 0) {
      actionBar.setDisplayHomeAsUpEnabled(true)
      actionBar.setDisplayShowHomeEnabled(true)
    }
  }

  fun setToolbarTitle(toolbar: Toolbar, title: CharSequence) {
    toolbar.title = title
  }

  fun startFragment(
      outgoingFragment: Fragment,
      incomingFragment: Fragment,
      ft: FragmentTransaction) {
    ft.addToBackStack(outgoingFragment.toString())
        .replace(R.id.container, incomingFragment)
        .commitAllowingStateLoss()
    parentActivity.incrementBackstackCount()
    fragmentManager!!.executePendingTransactions()
  }

  override fun toString(): String {
    return this.javaClass.simpleName
  }
}
