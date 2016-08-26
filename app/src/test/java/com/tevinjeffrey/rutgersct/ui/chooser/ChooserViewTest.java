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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ChooserViewTest {

    @Mock //Injected via @RunWith(MockitoJUnitRunner.class)
    ChooserView chooserView;
    ChooserViewState viewState;

    @Before
    public void setUp() throws Exception {
        viewState = new ChooserViewState();
    }

    @Test
    public void TestEssentialViewInit() throws Exception {
        viewState.apply(chooserView, true);
        verify(chooserView).initPicker();
        verify(chooserView).initToolbar();
    }
}