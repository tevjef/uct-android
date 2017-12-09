package com.tevinjeffrey.rutgersct.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import com.tevinjeffrey.rutgersct.R;

import jonathanfinerty.once.Once;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.tevinjeffrey.rutgersct.testUtils.OrientationChangeAction.orientationLandscape;
import static com.tevinjeffrey.rutgersct.testUtils.OrientationChangeAction.orientationPortrait;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TrackedSectionsFragmentTest {

  @Rule
  public ActivityTestRule<MainActivity> mActivityRule =
      new ActivityTestRule<MainActivity>(MainActivity.class);

  Activity mActivity;

  @Before
  public void setUp() throws Exception {
    // Espresso does not start the Activity for you we need to do this manually here.
    mActivity = mActivityRule.getActivity();
    assertThat(mActivity, notNullValue());
    //ObjectGraph.create(new RutgersCTTestModule()).inject(this);
    Once.markDone(MainActivity.SHOW_TOUR);
  }

  @Test
  public void testAddCoursesButtonInteractivity() {
    viewInteractivity(R.id.fab);
  }

  @Test
  public void testClickAddCoursesFab() {
    onView(withId(R.id.fab)).perform(click());
    pressBack();
  }

  @Test
  public void testClickRefresh() {
    onView(withId(R.id.action_refresh)).perform(click());
  }

  @Test
  public void testOrientationChange() {
    if (mActivity.getResources().getConfiguration().orientation
        == Configuration.ORIENTATION_PORTRAIT) {
      onView(isRoot()).perform(orientationLandscape());
      testAddCoursesButtonInteractivity();
      testClickRefresh();
      testSwipeToRefresh();
    } else if (mActivity.getResources().getConfiguration().orientation
        == Configuration.ORIENTATION_LANDSCAPE) {
      onView(isRoot()).perform(orientationPortrait());
    }
  }

  @Test
  public void testSwipeToRefresh() {
    onView(withId(R.id.swipeRefreshLayout)).perform(swipeDown());
  }

  public void viewInteractivity(int viewId) {
    onView(withId(viewId)).check(matches(isCompletelyDisplayed()));
    onView(withId(viewId)).check(matches(isEnabled()));
    onView(withId(viewId)).check(matches(isClickable()));
  }
}