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

package com.tevinjeffrey.rutgersct.ui.chooser;

import com.tevinjeffrey.rutgersct.TestModule;
import com.tevinjeffrey.rutgersct.ui.base.View;
import dagger.Module;
import dagger.Provides;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ChooserPresenterImplTest {
  ChooserPresenterImpl chooserPresenterImpl;
  @Inject
  ChooserView chooserView;

  @Test
  public void GetView_NotNull() throws Exception {
    chooserPresenterImpl.attachView(chooserView);
    View expected = chooserView;
    assertEquals(expected, chooserPresenterImpl.getView());
  }

  @Test
  public void GetView_NotNull_AfterAttach() throws Exception {
    chooserPresenterImpl.attachView(chooserView);
    assertNotNull(chooserPresenterImpl.getView());
  }

  @Test
  public void GetView_Null_AfterDetach() throws Exception {
    chooserPresenterImpl.attachView(chooserView);
    chooserPresenterImpl.detachView();
    assertNull(chooserPresenterImpl.getView());
  }

  @Before
  public void setUp() throws Exception {

  }

  @Module
  public class MockModule {
    @Mock
    ChooserView mockChooserView;

    public MockModule() {
      MockitoAnnotations.initMocks(this);
    }

    @Provides
    @Singleton
    public ChooserView providesChooserView() {
      //Injected mock
      return mockChooserView;
    }
  }
}