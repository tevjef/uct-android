package com.tevinjeffrey.rutgersct.ui.utils

import android.annotation.TargetApi
import android.os.Build
import android.support.transition.Transition
import android.support.v4.app.SharedElementCallback
import android.view.View

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class CircleSharedElementCallback(val transitionName: String) : SharedElementCallback() {

  val transitionCallback = object : Transition.TransitionListener {

    override fun onTransitionCancel(transition: Transition) {
      transition.removeListener(this)
    }

    override fun onTransitionEnd(transition: Transition) {
    }

    override fun onTransitionPause(transition: Transition) {
    }

    override fun onTransitionResume(transition: Transition) {
    }

    override fun onTransitionStart(transition: Transition) {
    }
  }

  private var isEnter = true

  override//set
  fun onMapSharedElements(names: List<String>?, sharedElements: Map<String, View>?) {
    //It's possible the the framework was unable to map the view in the appering activity/fragment.
    //It's possible for the fragmment to not be attach to the activity. Calls to getResources will crash.
    if (isEnter) {
      val hiddenCircleView = sharedElements.orEmpty()[transitionName] as? CircleView
      hiddenCircleView?.setBackgroundColor(circleColor)
      hiddenCircleView?.titleText = circleText
      hiddenCircleView?.invalidate()
    }
    isEnter = false
  }

  private var circleColor: Int = 0
  private var circleText: String = ""

  override fun onSharedElementStart(
      sharedElementNames: List<String>?,
      sharedElements: List<View>?,
      sharedElementSnapshots: List<View>?) {
    super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots)
    //get desired view by tranisition name...guarenteed to be unique ;)

    if (isEnter) {
      (sharedElements?.firstOrNull {
        it.transitionName == transitionName
      } as? CircleView)?.apply {
        circleColor = getBackgroundColor()
        circleText = this.text
      }
    }
  }
}

