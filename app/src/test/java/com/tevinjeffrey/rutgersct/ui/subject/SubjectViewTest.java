package com.tevinjeffrey.rutgersct.ui.subject;

import com.tevinjeffrey.rutgersct.data.uctapi.model.Subject;
import com.tevinjeffrey.rutgersct.ui.base.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SubjectViewTest {

    @Mock //Injected via @RunWith(MockitoJUnitRunner.class)
    SubjectView subjectView;

    SubjectViewState viewState;

    @Before
    public void setUp() throws Exception {
        viewState = new SubjectViewState();
    }

    @Test
    public void TestEssentialViewInit() throws Exception {
        viewState.apply(subjectView, true);
        verify(subjectView).initRecyclerView();
        verify(subjectView).initSwipeLayout();
        verify(subjectView).initToolbar();
    }

    @Test
    public void ShowLoading_GivenIsLoading() throws Exception {
        viewState.isRefreshing = true;
        viewState.apply(subjectView, true);
        verify(subjectView).showLoading(true);
    }

    @Test
    public void ShowData_GivenSomeData() throws Exception {
        viewState.data = mock(List.class);
        viewState.apply(subjectView, true);
        verify(subjectView).setData(anyListOf(Subject.class));
    }

    @Test
    public void ShowLayout_Empty() throws Exception {
        viewState.layoutType = View.LayoutType.EMPTY;
        viewState.apply(subjectView, true);
        verify(subjectView).showLayout(View.LayoutType.EMPTY);
    }

    @Test
    public void ShowLayout_List() throws Exception {
        viewState.layoutType = View.LayoutType.LIST;
        viewState.apply(subjectView, true);
        verify(subjectView).showLayout(View.LayoutType.LIST);
    }

    @Test
    public void ShowLayout_Error() throws Exception {
        viewState.layoutType = View.LayoutType.ERROR;
        viewState.apply(subjectView, true);
        verify(subjectView).showLayout(View.LayoutType.ERROR);
    }

}