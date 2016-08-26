package com.tevinjeffrey.rutgersct.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.view.Menu;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsFragment;
import com.tevinjeffrey.rutgersct.utils.PreferenceUtils;

import javax.inject.Inject;

import icepick.Icepick;
import icepick.State;
import jonathanfinerty.once.Once;

public class MainActivity extends AppCompatActivity {

    public final static String SHOW_TOUR = "showTour";

    @Inject
    Context context;

    @Inject
    PreferenceUtils mPreferenceUtils;

    @State
    public int mBackstackCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Once.beenDone(Once.THIS_APP_INSTALL, SHOW_TOUR)) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
            finish();
        }

        RutgersCTApp.getObjectGraph(this).inject(this);

        setContentView(R.layout.activity_main);

        Icepick.restoreInstanceState(this, savedInstanceState);

        if (savedInstanceState == null) {
            TrackedSectionsFragment tsf = new TrackedSectionsFragment();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tsf.setEnterTransition(new AutoTransition().excludeTarget(ImageView.class, true));
                tsf.setExitTransition(new Fade(Fade.OUT).excludeTarget(ImageView.class, true));
                tsf.setReenterTransition(new AutoTransition().excludeTarget(ImageView.class, true));
                tsf.setReturnTransition(new Fade(Fade.IN).excludeTarget(ImageView.class, true));
                tsf.setAllowReturnTransitionOverlap(false);
                tsf.setAllowEnterTransitionOverlap(false);
                tsf.setSharedElementEnterTransition(new ChangeBounds().setInterpolator(new DecelerateInterpolator()));
                tsf.setSharedElementReturnTransition(new ChangeBounds().setInterpolator(new DecelerateInterpolator()));
            }
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, tsf)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onBackPressed() {
        if (getBackstackCount() > 0) {
            decrementBackstackCount();
            getFragmentManager().popBackStackImmediate();
        } else {
            finish();
        }
    }

    //Helper methods to manage the back stack count. The count return from
    // getFragmentManager().getbackstackCount() is unreliable when using transitions
    public int getBackstackCount() {
        return mBackstackCount;
    }

    public void incrementBackstackCount() {
        ++mBackstackCount;
    }

    public void decrementBackstackCount() {
        --mBackstackCount;
    }
}
