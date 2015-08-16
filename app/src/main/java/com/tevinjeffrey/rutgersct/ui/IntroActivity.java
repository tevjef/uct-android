package com.tevinjeffrey.rutgersct.ui;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.utils.Utils;

import jonathanfinerty.once.Once;

public class IntroActivity extends AppIntro2 {


    @Override
    public void init(Bundle bundle) {
        addSlide(AppIntroFragment.newInstance(
                "View course info!",
                "Get an overal view of all sections and view information about the course's requirements.",
                R.drawable.slide_see_course,
                getResources().getColor(R.color.accent)));

        addSlide(AppIntroFragment.newInstance(
                "Track or view section info!",
                "View basic information about a section or track when it opens",
                R.drawable.slide_add_section,
                getResources().getColor(R.color.red)));

        addSlide(AppIntroFragment.newInstance(
                "Choose wisely!",
                "Get professor ratings and info to make better decisions when choosing your classes.",
                R.drawable.slide_see_ratings,
                getResources().getColor(R.color.green)));

    }


    @Override
    public void onDonePressed() {
        Once.markDone(MainActivity.SHOW_TOUR);
        startMainActivity();
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
