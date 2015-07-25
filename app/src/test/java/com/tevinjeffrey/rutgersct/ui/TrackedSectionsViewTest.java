package com.tevinjeffrey.rutgersct.ui;

import com.tevinjeffrey.rutgersct.ui.base.View;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsView;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsViewState;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TrackedSectionsViewTest {

    TrackedSectionsView mockTrackedSectionsView;
    TrackedSectionsViewState viewState;

    @Before
    public void setUp() throws Exception {
        mockTrackedSectionsView = mock(TrackedSectionsView.class);
        viewState = new TrackedSectionsViewState();

    }

    @Test
    public void testShowLoading() throws Exception {
        viewState.isRefreshing = true;
        viewState.apply(mockTrackedSectionsView, true);
        verify(mockTrackedSectionsView).showLoading(true);
    }

    @Test
    public void testSetData() throws Exception {
        viewState.data = mock(List.class);
        viewState.apply(mockTrackedSectionsView, true);
        verify(mockTrackedSectionsView).setData(anyList());
    }

    @Test
    public void testShowEmptyLayout() throws Exception {
        viewState.layoutType = View.LayoutType.EMPTY;
        viewState.apply(mockTrackedSectionsView, true);
        verify(mockTrackedSectionsView).showLayout(View.LayoutType.EMPTY);
    }

    @Test
    public void testShowListLayout() throws Exception {
        viewState.layoutType = View.LayoutType.LIST;
        viewState.apply(mockTrackedSectionsView, true);
        verify(mockTrackedSectionsView).showLayout(View.LayoutType.LIST);
    }

    @Test
    public void testShowErrorLayout() throws Exception {
        viewState.layoutType = View.LayoutType.ERROR;
        viewState.apply(mockTrackedSectionsView, true);
        verify(mockTrackedSectionsView).showLayout(View.LayoutType.ERROR);
    }

}