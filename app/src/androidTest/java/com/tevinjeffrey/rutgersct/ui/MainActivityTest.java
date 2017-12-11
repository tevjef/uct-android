package com.tevinjeffrey.rutgersct.ui;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.tevinjeffrey.rutgersct.R;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule =
      new ActivityTestRule<>(MainActivity.class);

  @Test
  public void mainActivityTest() {
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(60000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction n = onView(
        allOf(
            withId(R.id.next),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom),
                    1
                ),
                1
            ),
            isDisplayed()
        ));
    n.perform(click());

    ViewInteraction n2 = onView(
        allOf(
            withId(R.id.next),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom),
                    1
                ),
                1
            ),
            isDisplayed()
        ));
    n2.perform(click());

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(60000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction n3 = onView(
        allOf(
            withId(R.id.done),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom),
                    1
                ),
                2
            ),
            isDisplayed()
        ));
    n3.perform(click());

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(3586459);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction actionMenuItemView = onView(
        allOf(withId(R.id.action_refresh), withContentDescription("Refresh"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.toolbar),
                    2
                ),
                0
            ),
            isDisplayed()
        ));
    actionMenuItemView.perform(click());

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction d = onView(
        allOf(
            withContentDescription("More options"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.toolbar),
                    2
                ),
                1
            ),
            isDisplayed()
        ));
    d.perform(click());

    DataInteraction listMenuItemView = onData(anything())
        .inAdapterView(childAtPosition(
            withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
            0
        ))
        .atPosition(1);
    listMenuItemView.perform(click());

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(3587548);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    DataInteraction linearLayout = onData(anything())
        .inAdapterView(allOf(
            withId(android.R.id.list),
            childAtPosition(
                withId(android.R.id.list_container),
                0
            )
        ))
        .atPosition(1);
    linearLayout.perform(click());

    DataInteraction linearLayout2 = onData(anything())
        .inAdapterView(allOf(
            withId(android.R.id.list),
            childAtPosition(
                withId(android.R.id.list_container),
                0
            )
        ))
        .atPosition(1);
    linearLayout2.perform(click());

    DataInteraction linearLayout3 = onData(anything())
        .inAdapterView(allOf(
            withId(android.R.id.list),
            childAtPosition(
                withId(android.R.id.list_container),
                0
            )
        ))
        .atPosition(2);
    linearLayout3.perform(click());

    DataInteraction linearLayout4 = onData(anything())
        .inAdapterView(allOf(
            withId(android.R.id.list),
            childAtPosition(
                withId(android.R.id.list_container),
                0
            )
        ))
        .atPosition(1);
    linearLayout4.perform(click());

    DataInteraction linearLayout5 = onData(anything())
        .inAdapterView(allOf(
            withId(android.R.id.list),
            childAtPosition(
                withId(android.R.id.list_container),
                0
            )
        ))
        .atPosition(1);
    linearLayout5.perform(click());

    DataInteraction linearLayout6 = onData(anything())
        .inAdapterView(allOf(
            withId(android.R.id.list),
            childAtPosition(
                withId(android.R.id.list_container),
                0
            )
        ))
        .atPosition(4);
    linearLayout6.perform(click());

    ViewInteraction mDButton = onView(
        allOf(withId(R.id.md_buttonDefaultPositive), withText("Ok"),
            childAtPosition(
                allOf(
                    withId(R.id.md_root),
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    )
                ),
                4
            ),
            isDisplayed()
        ));
    mDButton.perform(click());

    DataInteraction linearLayout7 = onData(anything())
        .inAdapterView(allOf(
            withId(android.R.id.list),
            childAtPosition(
                withId(android.R.id.list_container),
                0
            )
        ))
        .atPosition(5);
    linearLayout7.perform(click());

    ViewInteraction mDButton2 = onView(
        allOf(withId(R.id.md_buttonDefaultPositive), withText("OK"),
            childAtPosition(
                allOf(
                    withId(R.id.md_root),
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    )
                ),
                4
            ),
            isDisplayed()
        ));
    mDButton2.perform(click());

    pressBack();

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(3558566);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction floatingActionButton = onView(
        allOf(
            withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.container),
                    0
                ),
                3
            ),
            isDisplayed()
        ));
    floatingActionButton.perform(click());

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(3595787);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction appCompatRadioButton = onView(
        allOf(
            withText("Spring 2018"),
            childAtPosition(
                allOf(
                    withId(R.id.semesterRadioGroup),
                    childAtPosition(
                        withClassName(is("android.widget.RelativeLayout")),
                        2
                    )
                ),
                0
            )
        ));
    appCompatRadioButton.perform(scrollTo(), click());

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction i = onView(
        allOf(withId(R.id.searchBtn), withText("SEARCH"),
            childAtPosition(
                allOf(
                    withId(R.id.relativeLayout2),
                    childAtPosition(
                        withClassName(is("android.widget.RelativeLayout")),
                        2
                    )
                ),
                0
            ),
            isDisplayed()
        ));
    i.perform(click());

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(3590818);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction frameLayout = onView(
        allOf(
            childAtPosition(
                allOf(
                    withId(R.id.list),
                    childAtPosition(
                        withId(R.id.swipeRefreshLayout),
                        0
                    )
                ),
                4
            ),
            isDisplayed()
        ));
    frameLayout.perform(click());

    ViewInteraction actionMenuItemView2 = onView(
        allOf(withId(R.id.action_refresh), withContentDescription("Refresh"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.toolbar),
                    2
                ),
                0
            ),
            isDisplayed()
        ));
    actionMenuItemView2.perform(click());

    ViewInteraction relativeLayout = onView(
        allOf(
            childAtPosition(
                allOf(
                    withId(R.id.list),
                    childAtPosition(
                        withId(R.id.swipeRefreshLayout),
                        0
                    )
                ),
                1
            ),
            isDisplayed()
        ));
    relativeLayout.perform(click());

    ViewInteraction linearLayout8 = onView(
        allOf(
            withId(R.id.section_details),
            childAtPosition(
                allOf(
                    withId(R.id.list),
                    childAtPosition(
                        withId(R.id.coordinator),
                        0
                    )
                ),
                1
            ),
            isDisplayed()
        ));
    linearLayout8.perform(click());

    ViewInteraction floatingActionButton2 = onView(
        allOf(
            withId(R.id.fab),
            childAtPosition(
                allOf(
                    withId(R.id.coordinator),
                    childAtPosition(
                        withId(R.id.container),
                        0
                    )
                ),
                2
            ),
            isDisplayed()
        ));
    floatingActionButton2.perform(click());

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(60000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    pressBack();

    ViewInteraction linearLayout9 = onView(
        allOf(
            withId(R.id.section_details),
            childAtPosition(
                allOf(
                    withId(R.id.list),
                    childAtPosition(
                        withId(R.id.coordinator),
                        0
                    )
                ),
                1
            ),
            isDisplayed()
        ));
    linearLayout9.perform(click());

    ViewInteraction floatingActionButton3 = onView(
        allOf(
            withId(R.id.fab),
            childAtPosition(
                allOf(
                    withId(R.id.coordinator),
                    childAtPosition(
                        withId(R.id.container),
                        0
                    )
                ),
                2
            ),
            isDisplayed()
        ));
    floatingActionButton3.perform(click());

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(60000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    pressBack();

    ViewInteraction linearLayout10 = onView(
        allOf(
            withId(R.id.section_details),
            childAtPosition(
                allOf(
                    withId(R.id.list),
                    childAtPosition(
                        withId(R.id.coordinator),
                        0
                    )
                ),
                1
            ),
            isDisplayed()
        ));
    linearLayout10.perform(click());

    ViewInteraction floatingActionButton4 = onView(
        allOf(
            withId(R.id.fab),
            childAtPosition(
                allOf(
                    withId(R.id.coordinator),
                    childAtPosition(
                        withId(R.id.container),
                        0
                    )
                ),
                2
            ),
            isDisplayed()
        ));
    floatingActionButton4.perform(click());

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(60000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction actionMenuItemView3 = onView(
        allOf(withId(R.id.action_track), withContentDescription("Track Sections"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.toolbar),
                    1
                ),
                0
            ),
            isDisplayed()
        ));
    actionMenuItemView3.perform(click());

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(3555206);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction linearLayout11 = onView(
        allOf(
            withId(R.id.section_details),
            childAtPosition(
                allOf(
                    withId(R.id.list),
                    childAtPosition(
                        withId(R.id.swipeRefreshLayout),
                        0
                    )
                ),
                0
            ),
            isDisplayed()
        ));
    linearLayout11.perform(click());

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(3594920);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction floatingActionButton5 = onView(
        allOf(
            withId(R.id.fab),
            childAtPosition(
                allOf(
                    withId(R.id.coordinator),
                    childAtPosition(
                        withId(R.id.container),
                        0
                    )
                ),
                2
            ),
            isDisplayed()
        ));
    floatingActionButton5.perform(click());

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction n4 = onView(
        allOf(
            withContentDescription("Navigate up"),
            childAtPosition(
                allOf(
                    withId(R.id.toolbar),
                    childAtPosition(
                        withId(R.id.collapsingToolbar),
                        0
                    )
                ),
                0
            ),
            isDisplayed()
        ));
    n4.perform(click());
  }

  private static Matcher<View> childAtPosition(
      final Matcher<View> parentMatcher, final int position) {

    return new TypeSafeMatcher<View>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("Child at position " + position + " in parent ");
        parentMatcher.describeTo(description);
      }

      @Override
      public boolean matchesSafely(View view) {
        ViewParent parent = view.getParent();
        return parent instanceof ViewGroup && parentMatcher.matches(parent)
            && view.equals(((ViewGroup) parent).getChildAt(position));
      }
    };
  }
}
