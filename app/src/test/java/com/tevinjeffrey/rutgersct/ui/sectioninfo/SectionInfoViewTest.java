package com.tevinjeffrey.rutgersct.ui.sectioninfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SectionInfoViewTest {

  @Mock //Injected via @RunWith(MockitoJUnitRunner.class)
      SectionInfoView sectioninfoView;

  SectionInfoViewState viewState;

  @Before
  public void setUp() throws Exception {
    viewState = new SectionInfoViewState();
  }

  @Test
  public void TestEssentialViewInit() throws Exception {
    viewState.apply(sectioninfoView, true);
    verify(sectioninfoView).initViews();
    verify(sectioninfoView).initToolbar();
  }

  @Test
  public void ShowFabWithAnimation() throws Exception {
    viewState.shouldAnimateFabIn = true;
    viewState.apply(sectioninfoView, true);
    verify(sectioninfoView).showFab(true);
  }
}