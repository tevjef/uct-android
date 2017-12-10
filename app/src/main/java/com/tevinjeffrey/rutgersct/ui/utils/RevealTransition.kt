package com.tevinjeffrey.rutgersct.ui.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.transition.TransitionValues
import android.support.transition.Visibility
import android.util.ArrayMap
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup

class RevealTransition constructor(context: Context, attrs: AttributeSet)
  : Visibility(context, attrs) {

  override fun onAppear(
      sceneRoot: ViewGroup,
      view: View,
      startValues: TransitionValues?,
      endValues: TransitionValues?): Animator {
    val radius = calculateMaxRadius(view)
    val originalAlpha = view.alpha
    view.alpha = 0f

    val startRadius = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        40f,
        view.context.resources.displayMetrics
    )

    val reveal = createAnimator(view, startRadius, radius)
    reveal.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationStart(animation: Animator) {
        view.alpha = originalAlpha
      }
    })
    return reveal
  }

  override fun onDisappear(
      sceneRoot: ViewGroup, view: View, startValues: TransitionValues,
      endValues: TransitionValues): Animator {
    val radius = calculateMaxRadius(view)
    return createAnimator(view, radius, 0f)
  }

  private fun createAnimator(view: View, startRadius: Float, endRadius: Float): Animator {
    val centerX = view.width / 2
    val centerY = view.height / 2

    val reveal = ViewAnimationUtils.createCircularReveal(view, centerX, centerY,
        startRadius, endRadius
    )
    return NoPauseAnimator(reveal)
  }

  private class AnimatorListenerWrapper(private val mAnimator: Animator, private val mListener: Animator.AnimatorListener) : Animator.AnimatorListener {

    override fun onAnimationCancel(animator: Animator) {
      mListener.onAnimationCancel(mAnimator)
    }

    override fun onAnimationEnd(animator: Animator) {
      mListener.onAnimationEnd(mAnimator)
    }

    override fun onAnimationRepeat(animator: Animator) {
      mListener.onAnimationRepeat(mAnimator)
    }

    override fun onAnimationStart(animator: Animator) {
      mListener.onAnimationStart(mAnimator)
    }
  }

  private class NoPauseAnimator(private val mAnimator: Animator) : Animator() {
    private val mListeners = ArrayMap<Animator.AnimatorListener, Animator.AnimatorListener>()

    override fun addListener(listener: Animator.AnimatorListener) {
      val wrapper = AnimatorListenerWrapper(this, listener)
      if (!mListeners.containsKey(listener)) {
        mListeners.put(listener, wrapper)
        mAnimator.addListener(wrapper)
      }
    }

    override fun cancel() {
      mAnimator.cancel()
    }

    override fun end() {
      mAnimator.end()
    }

    override fun getDuration(): Long {
      return mAnimator.duration
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun getInterpolator(): TimeInterpolator {
      return mAnimator.interpolator
    }

    override fun setInterpolator(timeInterpolator: TimeInterpolator) {
      mAnimator.interpolator = timeInterpolator
    }

    override fun getListeners(): ArrayList<Animator.AnimatorListener> {
      return ArrayList(mListeners.keys)
    }

    override fun getStartDelay(): Long {
      return mAnimator.startDelay
    }

    override fun setStartDelay(delayMS: Long) {
      mAnimator.startDelay = delayMS
    }

    override fun isPaused(): Boolean {
      return mAnimator.isPaused
    }

    override fun isRunning(): Boolean {
      return mAnimator.isRunning
    }

    override fun isStarted(): Boolean {
      return mAnimator.isStarted
    }


    /* We don't want to override pause or resume methods because we don't want them
         * to affect mAnimator.
        public void pause();

        public void resume();

        public void addPauseListener(AnimatorPauseListener mListener);

        public void removePauseListener(AnimatorPauseListener mListener);
        */

    override fun removeAllListeners() {
      mListeners.clear()
      mAnimator.removeAllListeners()
    }

    override fun removeListener(listener: Animator.AnimatorListener) {
      val wrapper = mListeners[listener]
      if (wrapper != null) {
        mListeners.remove(listener)
        mAnimator.removeListener(wrapper)
      }
    }

    override fun setDuration(durationMS: Long): Animator {
      mAnimator.duration = durationMS
      return this
    }

    override fun setTarget(target: Any?) {
      mAnimator.setTarget(target)
    }

    override fun setupEndValues() {
      mAnimator.setupEndValues()
    }

    override fun setupStartValues() {
      mAnimator.setupStartValues()
    }

    override fun start() {
      mAnimator.start()
    }
  }

  companion object {

    internal fun calculateMaxRadius(view: View): Float {
      val widthSquared = (view.width * view.width).toFloat()
      val heightSquared = (view.height * view.height).toFloat()
      return (Math.sqrt((widthSquared + heightSquared).toDouble()) / 2).toFloat()
    }
  }
}

