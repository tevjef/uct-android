/*
 * Copyright 2015 Tevin Jeffrey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tevinjeffrey.rutgersct.ui.courseinfo;

import com.tevinjeffrey.rutgersct.ui.base.View;
import com.tevinjeffrey.rutgersct.ui.courseinfo.CourseInfoView;
import com.tevinjeffrey.rutgersct.ui.courseinfo.CourseInfoViewState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CourseInfoViewTest {

    @Mock //Injected via @RunWith(MockitoJUnitRunner.class)
    CourseInfoView courseinfoView;

    CourseInfoViewState viewState;

    @Before
    public void setUp() throws Exception {
        viewState = new CourseInfoViewState();
    }

    @Test
    public void TestEssentialViewInit() throws Exception {
        viewState.apply(courseinfoView, true);
        verify(courseinfoView).initRecyclerView();
        verify(courseinfoView).initToolbar();
        verify(courseinfoView).initHeaderView();
        verify(courseinfoView).initViews();
    }
}