package com.tevinjeffrey.rutgersct.ui.trackedsections;

import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Course;
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
public class TrackedSectionsViewTest {

  @Mock //Injected via @RunWith(MockitoJUnitRunner.class)
      TrackedSectionsView mockTrackedSectionsView;

  TrackedSectionsViewState viewState;

  @Before
  public void setUp() throws Exception {
    viewState = new TrackedSectionsViewState();
  }

  @Test
  public void TestEssentialViewInit() throws Exception {
    viewState.apply(mockTrackedSectionsView, true);
    verify(mockTrackedSectionsView).initRecyclerView();
    verify(mockTrackedSectionsView).initSwipeLayout();
    verify(mockTrackedSectionsView).initToolbar();
  }

  @Test
  public void ShowLoading_GivenIsLoading() throws Exception {
    viewState.isRefreshing = true;
    viewState.apply(mockTrackedSectionsView, true);
    verify(mockTrackedSectionsView).showLoading(true);
  }

  @Test
  public void ShowData_GivenSomeData() throws Exception {
    viewState.data = mock(List.class);
    viewState.apply(mockTrackedSectionsView, true);
    verify(mockTrackedSectionsView).setData(anyListOf(Course.Section.class));
  }

  @Test
  public void ShowLayout_Empty() throws Exception {
    viewState.layoutType = View.LayoutType.EMPTY;
    viewState.apply(mockTrackedSectionsView, true);
    verify(mockTrackedSectionsView).showLayout(View.LayoutType.EMPTY);
  }

  @Test
  public void ShowLayout_List() throws Exception {
    viewState.layoutType = View.LayoutType.LIST;
    viewState.apply(mockTrackedSectionsView, true);
    verify(mockTrackedSectionsView).showLayout(View.LayoutType.LIST);
  }

  @Test
  public void ShowLayout_Error() throws Exception {
    viewState.layoutType = View.LayoutType.ERROR;
    viewState.apply(mockTrackedSectionsView, true);
    verify(mockTrackedSectionsView).showLayout(View.LayoutType.ERROR);
  }
}