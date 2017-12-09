package com.tevinjeffrey.rutgersct.ui.trackedsections;

import com.tevinjeffrey.rutgersct.data.search.UCTSubscription;
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

  TrackedSectionsModel viewState;

  @Test
  public void ShowData_GivenSomeData() throws Exception {
    viewState.setData(mock(List.class));
    viewState.apply(mockTrackedSectionsView, true);
    verify(mockTrackedSectionsView).setData(anyListOf(UCTSubscription.class));
  }

  @Test
  public void ShowLayout_Empty() throws Exception {
    viewState.setLayoutType(View.LayoutType.EMPTY);
    viewState.apply(mockTrackedSectionsView, true);
    verify(mockTrackedSectionsView).showLayout(View.LayoutType.EMPTY);
  }

  @Test
  public void ShowLayout_Error() throws Exception {
    viewState.setLayoutType(View.LayoutType.ERROR);
    viewState.apply(mockTrackedSectionsView, true);
    verify(mockTrackedSectionsView).showLayout(View.LayoutType.ERROR);
  }

  @Test
  public void ShowLayout_List() throws Exception {
    viewState.setLayoutType(View.LayoutType.LIST);
    viewState.apply(mockTrackedSectionsView, true);
    verify(mockTrackedSectionsView).showLayout(View.LayoutType.LIST);
  }

  @Test
  public void ShowLoading_GivenIsLoading() throws Exception {
    viewState.setIsRefreshing(true);
    viewState.apply(mockTrackedSectionsView, true);
    verify(mockTrackedSectionsView).showLoading(true);
  }

  @Test
  public void TestEssentialViewInit() throws Exception {
    viewState.apply(mockTrackedSectionsView, true);
    verify(mockTrackedSectionsView).initRecyclerView();
    verify(mockTrackedSectionsView).initSwipeLayout();
    verify(mockTrackedSectionsView).initToolbar();
  }

  @Before
  public void setUp() throws Exception {
    viewState = new TrackedSectionsModel();
  }
}