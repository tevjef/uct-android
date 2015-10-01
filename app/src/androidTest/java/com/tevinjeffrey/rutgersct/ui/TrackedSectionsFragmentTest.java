package com.tevinjeffrey.rutgersct.ui;

import android.app.Activity;
import android.content.res.Configuration;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.RutgersCTTestModule;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.testUtils.CustomMatchers;
import com.tevinjeffrey.rutgersct.testUtils.RutgersApiConts;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import dagger.ObjectGraph;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.contrib.RecyclerViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.Espresso.*;
import static com.tevinjeffrey.rutgersct.testUtils.OrientationChangeAction.orientationLandscape;
import static com.tevinjeffrey.rutgersct.testUtils.OrientationChangeAction.orientationPortrait;
import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.assertThat;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class TrackedSectionsFragmentTest {

    @Inject
    DatabaseHandler databaseHandler;

    Activity mActivity;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        // Espresso does not start the Activity for you we need to do this manually here.
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
        ObjectGraph.create(new RutgersCTTestModule()).inject(this);
    }

    @Test
    public void testAddCoursesButtonInteractivity() {
        viewInteractivity(R.id.add_courses_fab);
    }

    @Test
    public void testClickAddCoursesFab() {
        onView(withId(R.id.add_courses_fab)).perform(click());
        pressBack();
    }

    @Test
    public void testOrientationChange() {
        if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            onView(isRoot()).perform(orientationLandscape());
            testAddCoursesButtonInteractivity();
            testClickRefresh();
            testSwipeToRefresh();
        } else if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            onView(isRoot()).perform(orientationPortrait());
        }
    }

    @Test
    public void testClickRefresh() {
        onView(withId(R.id.action_refresh)).perform(click());
    }


    @Test
    public void testRecyclerItemClickAndRemoveSection() {
        Request expected = RutgersApiConts.getPrimarySemesterRequest();
        databaseHandler.addSectionToDb(expected);

        onView(withId(R.id.tsf_list)).perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.add_courses_fab)).perform(click());

        databaseHandler.removeSectionFromDb(expected);
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