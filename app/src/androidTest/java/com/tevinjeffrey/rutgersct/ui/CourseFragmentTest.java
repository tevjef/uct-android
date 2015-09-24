package com.tevinjeffrey.rutgersct.ui;

import android.content.res.Configuration;
import android.support.test.filters.FlakyTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Subject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
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
import static com.tevinjeffrey.rutgersct.testUtils.CustomMatchers.withCourseNumber;
import static com.tevinjeffrey.rutgersct.testUtils.CustomMatchers.withSubjectNumber;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CourseFragmentTest {

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

        onView(withId(R.id.primarySemester)).perform(click()).check(matches(isChecked()));

        onView(withId(R.id.location1)).perform(click()).check(matches(isChecked()));

        onView(withId(R.id.level1)).perform(click()).check(matches(isChecked()));

        onView(withText(R.string.next_text)).perform(click());

        onData(allOf(is(instanceOf(Subject.class)), withSubjectNumber(equalTo("640")))).perform(click());
    }

    @Test
    public void testCourseFragmentInteractivity() {
        onView(withId(R.id.swipeRefreshLayout)).perform(swipeUp());

        viewInteractivity(R.id.action_refresh);
        onView(withId(R.id.action_refresh)).perform(click());
    }

    @Test
    public void testClickNextInCourseFragment() {
        onData(allOf(is(instanceOf(Course.class)), withCourseNumber(equalTo("135")))).perform(click());
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

    @Test
    public void testClickRefresh() {
        onView(withId(R.id.action_refresh)).perform(click());
    }

    @Test
    public void testSwipeToRefresh() {
        onView(withId(R.id.swipeRefreshLayout)).perform(swipeDown());
    }

    @Test
    @FlakyTest
    public void testTrackedSectionBackNavigation() {
        viewInteractivity(R.id.action_track);
        onView(withId(R.id.action_track)).perform(click());
    }

}