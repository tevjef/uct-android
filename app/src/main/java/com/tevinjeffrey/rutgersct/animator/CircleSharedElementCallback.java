package com.tevinjeffrey.rutgersct.animator;

import android.app.SharedElementCallback;
import android.transition.Transition;
import android.view.View;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.customviews.CircleView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

public class CircleSharedElementCallback extends SharedElementCallback {

    private CircleView mCircleViewSnapshot;
    private boolean isEnter = true;
    private WeakReference<View> tempView;

    private final SharedElementsEnterTransitionCallback mCallback = new SharedElementsEnterTransitionCallback();

    @Override //capture
    public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
        super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
        //get desired view by tranisition name...guarenteed to be unique ;)

        if (isEnter) {
            for (View v : sharedElements) {
                if (v.getTransitionName().equals(RutgersCTApp.getInstance().getString(R.string.transition_name_circle_view)) && v instanceof CircleView) {
                    mCircleViewSnapshot = (CircleView) v;
                    mCircleViewSnapshot.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override //set
    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
        //It's possible the the framework was unable to map the view in the appering activity/fragment.

        //It's possible for the fragmment to not be attach to the activity. Calls to getResources will crash.
        if (isEnter) {
            if (mCircleViewSnapshot != null) {
                final View mappedFrameLayout = sharedElements.get(mCircleViewSnapshot.getTransitionName());

                CircleView hiddenCircleView = ButterKnife.findById(mappedFrameLayout, R.id.hidden_circle_view);

                hiddenCircleView.setVisibility(View.VISIBLE);
                hiddenCircleView.setBackgroundColor(mCircleViewSnapshot.getBackgroundColor());
                hiddenCircleView.setTitleText(mCircleViewSnapshot.getTitleText());

                tempView = new WeakReference<>(mappedFrameLayout);
            }
        }
        isEnter = false;
    }

    public SharedElementsEnterTransitionCallback getTransitionCallback() {
        return mCallback;
    }

    public class SharedElementsEnterTransitionCallback implements Transition.TransitionListener {

        @Override
        public void onTransitionStart(Transition transition) {

        }

        @Override
        public void onTransitionEnd(Transition transition) {
            if (transition != null) {
                tempView.get().setAlpha(0);
                transition.removeListener(this);
            }
        }

        @Override
        public void onTransitionCancel(Transition transition) {
            transition.removeListener(this);
        }

        @Override
        public void onTransitionPause(Transition transition) {

        }

        @Override
        public void onTransitionResume(Transition transition) {

        }
    }
}

