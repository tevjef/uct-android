package com.tevinjeffrey.rutgersct.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.tevinjeffrey.rutgersct.R;

import jonathanfinerty.once.Once;

public class IntroActivity extends AppIntro2 {


    @Override
    public void init(Bundle bundle) {
        addSlide(AppIntroFragment.newInstance(
                "View course info!",
                "Get an comprehensive view of all sections and view information about course requirements.",
                R.drawable.slide_see_course,
                ContextCompat.getColor(this, R.color.accent)));

        addSlide(AppIntroFragment.newInstance(
                "Track or view section info!",
                "View basic information about a section or track when it opens",
                R.drawable.slide_add_section,
                ContextCompat.getColor(this, R.color.red)));

        addSlide(AppIntroFragment.newInstance(
                "Choose wisely!",
                "Get professor ratings and info to make better decisions when choosing your classes.",
                R.drawable.slide_see_ratings,
                ContextCompat.getColor(this, R.color.green)));

    }


    @Override
    public void onDonePressed() {
        Once.markDone(MainActivity.SHOW_TOUR);
        startMainActivity();
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onSlideChanged() {

    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
