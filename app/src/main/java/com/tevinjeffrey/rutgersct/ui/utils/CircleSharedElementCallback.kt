package com.tevinjeffrey.rutgersct.ui.utils

import android.annotation.TargetApi
import android.os.Build
import android.support.transition.Transition
import android.support.v4.app.SharedElementCallback
import android.view.View

import com.tevinjeffrey.rutgersct.R

import java.lang.ref.WeakReference

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class CircleSharedElementCallback : SharedElementCallback() {

  val transitionCallback = object : Transition.TransitionListener {

    override fun onTransitionCancel(transition: Transition) {
      transition.removeListener(this)
    }

    override fun onTransitionEnd(transition: Transition) {
      if (tempView != null) {
        tempView!!.get()?.alpha = 0f
        transition.removeListener(this)
      }
    }

    override fun onTransitionPause(transition: Transition) {

    }

    override fun onTransitionResume(transition: Transition) {

    }

    override fun onTransitionStart(transition: Transition) {

    }
  }

  private var mCircleViewSnapshot: CircleView? = null
  private var isEnter = true
  private var tempView: WeakReference<View>? = null

  override//set
  fun onMapSharedElements(names: List<String>?, sharedElements: Map<String, View>?) {
    //It's possible the the framework was unable to map the view in the appering activity/fragment.

    //It's possible for the fragmment to not be attach to the activity. Calls to getResources will crash.
    if (isEnter) {
      if (mCircleViewSnapshot != null) {
        val mappedFrameLayout = sharedElements!![mCircleViewSnapshot!!.transitionName]

        val hiddenCircleView = mappedFrameLayout!!.findViewById<CircleView>(R.id.hidden_circle_view)

        hiddenCircleView.visibility = View.VISIBLE
        hiddenCircleView.backgroundColor = mCircleViewSnapshot!!.backgroundColor
        hiddenCircleView.text = mCircleViewSnapshot!!.text

        tempView = WeakReference<View>(mappedFrameLayout)
      }
    }
    isEnter = false
  }

  override//capture
  fun onSharedElementStart(
      sharedElementNames: List<String>?,
      sharedElements: List<View>?,
      sharedElementSnapshots: List<View>?) {
    super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots)
    //get desired view by tranisition name...guarenteed to be unique ;)

    if (isEnter) {
      for (v in sharedElements!!) {
        if (v
            .transitionName == v.resources.getString(R.string.transition_name_circle_view) && v is CircleView) {
          mCircleViewSnapshot = v
          mCircleViewSnapshot!!.visibility = View.INVISIBLE
        }
      }
    }
  }
}

