package com.tevinjeffrey.rutgersct.data.search;

import com.tevinjeffrey.rutgersct.dagger.PerApp;
import javax.inject.Inject;

@PerApp
public class SearchManager {
  SearchFlow searchFlow;
  UCTSubscription currentSubscription;

  @Inject
  public SearchManager() {
  }

  @Override
  public String toString() {
    return searchFlow.toString();
  }

  public SearchFlow getSearchFlow() {
    return searchFlow;
  }

  public void setSearchFlow(SearchFlow searchFlow) {
    this.searchFlow = searchFlow;
  }

  public SearchFlow newSearch() {
    searchFlow = new SearchFlow();
    return searchFlow;
  }
}
