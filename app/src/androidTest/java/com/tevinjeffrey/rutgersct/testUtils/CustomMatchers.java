package com.tevinjeffrey.rutgersct.testUtils;

import android.support.test.espresso.matcher.BoundedMatcher;

import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionVH;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Subject;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.internal.util.Checks.checkNotNull;


public class CustomMatchers {

    public static Matcher<Object> withSubjectNumber(final Matcher<String> itemTextMatcher){
        checkNotNull(itemTextMatcher);
        return new BoundedMatcher<Object, Subject>(Subject.class) {
            @Override
            public boolean matchesSafely(Subject subject) {
                return itemTextMatcher.matches(String.valueOf(subject.getCode()));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with with subject number: ");
                itemTextMatcher.describeTo(description);
            }
        };
    }

    public static Matcher<Object> withCourseNumber(final Matcher<String> itemTextMatcher){
        checkNotNull(itemTextMatcher);
        return new BoundedMatcher<Object, Course>(Course.class) {
            @Override
            public boolean matchesSafely(Course course) {
                return itemTextMatcher.matches(String.valueOf(course.getCourseNumber()));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with with subject number: ");
                itemTextMatcher.describeTo(description);
            }
        };
    }

    public static Matcher<TrackedSectionVH> withIndexNumber(final Matcher<String> indexNumberMatcher) {
        checkNotNull(indexNumberMatcher);
        return new TypeSafeMatcher<TrackedSectionVH>() {
            @Override
            protected boolean matchesSafely(TrackedSectionVH item) {
                return indexNumberMatcher.matches(item.getIndexNumber());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with with index number: ");
                indexNumberMatcher.describeTo(description);
            }
        };
    }
}
