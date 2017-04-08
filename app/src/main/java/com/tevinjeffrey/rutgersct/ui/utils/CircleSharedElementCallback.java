package com.tevinjeffrey.rutgersct.ui.utils;

import android.annotation.TargetApi;
import android.app.SharedElementCallback;
import android.os.Build;
import android.transition.Transition;
import android.view.View;
import butterknife.ButterKnife;
import com.tevinjeffrey.rutgersct.R;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CircleSharedElementCallback extends SharedElementCallback {

  private final SharedElementsEnterTransitionCallback mCallback =
      new SharedElementsEnterTransitionCallback();
  private CircleView mCircleViewSnapshot;
  private boolean isEnter = true;
  private WeakReference<View> tempView;

  @Override //set
  public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
    //It's possible the the framework was unable to map the view in the appering activity/fragment.

    //It's possible for the fragmment to not be attach to the activity. Calls to getResources will crash.
    if (isEnter) {
      if (mCircleViewSnapshot != null) {
        final View mappedFrameLayout = sharedElements.get(mCircleViewSnapshot.getTransitionName());

        CircleView hiddenCircleView =
            ButterKnife.findById(mappedFrameLayout, R.id.hidden_circle_view);

        hiddenCircleView.setVisibility(View.VISIBLE);
        hiddenCircleView.setBackgroundColor(mCircleViewSnapshot.getBackgroundColor());
        hiddenCircleView.setTitleText(mCircleViewSnapshot.getTitleText());

        tempView = new WeakReference<>(mappedFrameLayout);
      }
    }
    isEnter = false;
  }

  @Override //capture
  public void onSharedElementStart(
      List<String> sharedElementNames,
      List<View> sharedElements,
      List<View> sharedElementSnapshots) {
    super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
    //get desired view by tranisition name...guarenteed to be unique ;)

    if (isEnter) {
      for (View v : sharedElements) {
        if (v
            .getTransitionName()
            .equals(v.getResources().getString(R.string.transition_name_circle_view))
            && v instanceof CircleView) {
          mCircleViewSnapshot = (CircleView) v;
          mCircleViewSnapshot.setVisibility(View.INVISIBLE);
        }
      }
    }
  }

  public SharedElementsEnterTransitionCallback getTransitionCallback() {
    return mCallback;
  }

  public class SharedElementsEnterTransitionCallback implements Transition.TransitionListener {

    @Override
    public void onTransitionCancel(Transition transition) {
      transition.removeListener(this);
    }

    @Override
    public void onTransitionEnd(Transition transition) {
      if (transition != null) {
        tempView.get().setAlpha(0);
        transition.removeListener(this);
      }
    }

    @Override
    public void onTransitionPause(Transition transition) {

    }

    @Override
    public void onTransitionResume(Transition transition) {

    }

    @Override
    public void onTransitionStart(Transition transition) {

    }
  }
}

