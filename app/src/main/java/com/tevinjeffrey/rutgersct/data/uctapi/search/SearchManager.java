package com.tevinjeffrey.rutgersct.data.uctapi.search;

public class SearchManager {
  SearchFlow searchFlow;
  UCTSubscription currentSubscription;

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
