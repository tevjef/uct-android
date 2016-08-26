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
import com.tevinjeffrey.rutgersct.data.rutgersapi.RetroRutgers;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.SystemMessage;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.ui.base.View;
import com.tevinjeffrey.rutgersct.utils.TestConts;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import rx.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChooserPresenterImplTest {
    ChooserPresenterImpl chooserPresenterImpl;
    @Inject
    ChooserView chooserView;
    @Inject
    RetroRutgers retroRutgers;
    @Inject
    DatabaseHandler databaseHandler;

    @Before
    public void setUp() throws Exception {
        ObjectGraph og = ObjectGraph.create(new TestModule(), new MockModule());
        og.inject(this);
        chooserPresenterImpl = new ChooserPresenterImpl();
        og.inject(chooserPresenterImpl);
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

    @Test
    public void GetView_NotNull() throws Exception {
        chooserPresenterImpl.attachView(chooserView);
        View expected = chooserView;
        assertEquals(expected, chooserPresenterImpl.getView());
    }

    @Test
    public void LoadChooser_CompletesWithEmptyList() throws Exception {
        chooserPresenterImpl.attachView(chooserView);
        databaseHandler.addSectionToDb(TestConts.getPrimarySemesterRequest());
        when(retroRutgers.getSystemMessage()).thenReturn(Observable.<SystemMessage>empty());
        chooserPresenterImpl.loadSystemMessage();

        verify(chooserView, never()).showMessage(any(SystemMessage.class));

    }


    @Test
    public void LoadChooser_CompletesWithData() throws Exception {
        SystemMessage systemMessage = mock(SystemMessage.class);
        chooserPresenterImpl.attachView(chooserView);
        databaseHandler.addSectionToDb(TestConts.getPrimarySemesterRequest());
        when(retroRutgers.getSystemMessage()).thenReturn(Observable.just(systemMessage));
        chooserPresenterImpl.loadSystemMessage();

        verify(chooserView).showMessage(any(SystemMessage.class));
    }

    @Test
    public void LoadChooser_CompletesWithErrors() throws Exception {
        chooserPresenterImpl.attachView(chooserView);
        databaseHandler.addSectionToDb(TestConts.getPrimarySemesterRequest());
        when(retroRutgers.getSystemMessage()).thenReturn(Observable.<SystemMessage>error(new AssertionError()));
        chooserPresenterImpl.loadSystemMessage();

        verify(chooserView, never()).showMessage(any(SystemMessage.class));
    }

    @Test
    public void IsLoading_True_WhenObservableHasNotTerminated() throws Exception {
        chooserPresenterImpl.attachView(chooserView);
        databaseHandler.addSectionToDb(TestConts.getPrimarySemesterRequest());
        when(retroRutgers.getSystemMessage()).thenReturn(Observable.<SystemMessage>never());
        chooserPresenterImpl.loadSystemMessage();

        assertTrue(chooserPresenterImpl.isLoading());
    }

    @Test
    public void IsLoading_False_WhenObservableHasTerminated() throws Exception {
        chooserPresenterImpl.attachView(chooserView);
        databaseHandler.addSectionToDb(TestConts.getPrimarySemesterRequest());
        when(retroRutgers.getSystemMessage()).thenReturn(Observable.<SystemMessage>empty());
        chooserPresenterImpl.loadSystemMessage();

        assertFalse(chooserPresenterImpl.isLoading());
    }


    @Module(injects = {
            ChooserPresenterImplTest.class
    }

            , overrides = true
            , library = true
            , complete = false)
    public class MockModule {
        @Mock
        ChooserView mockChooserView;

        public MockModule() {
            MockitoAnnotations.initMocks(this);
        }

        @Provides
        @Singleton
        public RetroRutgers providesRetroRutgers() {
            //Inline mock
            return Mockito.mock(RetroRutgers.class);
        }
        @Provides
        @Singleton
        public ChooserView providesChooserView() {
            //Injected mock
            return mockChooserView;
        }
    }
}