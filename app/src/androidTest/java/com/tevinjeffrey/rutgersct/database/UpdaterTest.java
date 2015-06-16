package com.tevinjeffrey.rutgersct.database;

import com.tevinjeffrey.rutgersct.model.Course;

import org.junit.Before;
import org.junit.Test;

import rx.Observable;

import static org.junit.Assert.*;

public class UpdaterTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testGetTrackedSections() throws Exception {
        Observable<Course> courseObservable = Updater.getTrackedSections();
        assertNotNull(courseObservable);
    }
}