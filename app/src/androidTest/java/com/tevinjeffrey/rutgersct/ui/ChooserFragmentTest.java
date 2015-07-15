package com.tevinjeffrey.rutgersct.ui;

import android.content.res.Configuration;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.tevinjeffrey.rutgersct.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.tevinjeffrey.rutgersct.testUtils.OrientationChangeAction.orientationLandscape;
import static com.tevinjeffrey.rutgersct.testUtils.OrientationChangeAction.orientationPortrait;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChooserFragmentTest {

    private MainActivity mActivity;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class);

    @Before
    public void setUp() throws Exception {
        // Espresso does not start the Activity for you we need to do this manually here.
        mActivity = mActivityRule.getActivity();

        assertThat(mActivity, notNullValue());

        navigateToFragment();
    }

    public void navigateToFragment() {
        onView(withId(R.id.add_courses_fab)).perform(click());
    }

    @Test
    public void testChooserFragmentInteractivity() {
        viewInteractivity(R.id.primarySemester);
        onView(withId(R.id.primarySemester)).perform(click()).check(matches(isChecked()));

        viewInteractivity(R.id.otherSemester);
        onView(withId(R.id.otherSemester)).perform(click());
        pressBack();

        viewInteractivity(R.id.secondarySemester);
        onView(withId(R.id.secondarySemester)).perform(click()).check(matches(isChecked()));

        viewInteractivity(R.id.location1);
        onView(withId(R.id.location1)).perform(click()).check(matches(isChecked()));

        viewInteractivity(R.id.location2);
        onView(withId(R.id.location2)).perform(click()).check(matches(isChecked()));

        viewInteractivity(R.id.location3);
        onView(withId(R.id.location3)).perform(click()).check(matches(isChecked()));

        viewInteractivity(R.id.level1);
        onView(withId(R.id.level1)).perform(click()).check(matches(isChecked()));

        viewInteractivity(R.id.level2);
        onView(withId(R.id.level2)).perform(click()).check(matches(isChecked()));

        onView(withText(R.string.next_text)).check(matches(isCompletelyDisplayed()));
        onView(withText(R.string.next_text)).check(matches(isEnabled()));
        onView(withText(R.string.next_text)).check(matches(isClickable()));
        onView(withText(R.string.next_text)).perform(click());

    }

    @Test
    public void testClickNextInChooserFragment() {
        onView(withId(R.id.primarySemester)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.location1)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.level1)).perform(click()).check(matches(isChecked()));

        onView(withText(R.string.next_text)).perform(click());

        pressBack();
    }

    public void viewInteractivity(int viewId) {
        onView(withId(viewId)).check(matches(isCompletelyDisplayed()));
        onView(withId(viewId)).check(matches(isEnabled()));
        onView(withId(viewId)).check(matches(isClickable()));
    }

    @Test
    public void testOrientationChange() {
        if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            onView(isRoot()).perform(orientationLandscape());
        } else if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            onView(isRoot()).perform(orientationPortrait());
        }
    }

}