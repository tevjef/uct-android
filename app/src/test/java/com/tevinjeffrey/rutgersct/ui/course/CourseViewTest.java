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

package com.tevinjeffrey.rutgersct.ui.course;

import com.tevinjeffrey.rutgersct.data.model.Course;
import com.tevinjeffrey.rutgersct.ui.base.View;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CourseViewTest {

  @Mock //Injected via @RunWith(MockitoJUnitRunner.class)
      CourseView courseView;

  CourseModel viewState;

  @Test
  public void ShowData_GivenSomeData() throws Exception {
    viewState.setData(mock(List.class));
    viewState.apply(courseView, true);
    verify(courseView).setData(anyListOf(Course.class));
  }

  @Test
  public void ShowLayout_Empty() throws Exception {
    viewState.setLayoutType(View.LayoutType.EMPTY);
    viewState.apply(courseView, true);
    verify(courseView).showLayout(View.LayoutType.EMPTY);
  }

  @Test
  public void ShowLayout_Error() throws Exception {
    viewState.setLayoutType(View.LayoutType.ERROR);
    viewState.apply(courseView, true);
    verify(courseView).showLayout(View.LayoutType.ERROR);
  }

  @Test
  public void ShowLayout_List() throws Exception {
    viewState.setLayoutType(View.LayoutType.LIST);
    viewState.apply(courseView, true);
    verify(courseView).showLayout(View.LayoutType.LIST);
  }

  @Test
  public void ShowLoading_GivenIsLoading() throws Exception {
    viewState.setIsRefreshing(true);
    viewState.apply(courseView, true);
    verify(courseView).showLoading(true);
  }

  @Test
  public void TestEssentialViewInit() throws Exception {
    viewState.apply(courseView, true);
    verify(courseView).initRecyclerView();
    verify(courseView).initSwipeLayout();
    verify(courseView).initToolbar();
  }

  @Before
  public void setUp() throws Exception {
    viewState = new CourseModel();
  }
}