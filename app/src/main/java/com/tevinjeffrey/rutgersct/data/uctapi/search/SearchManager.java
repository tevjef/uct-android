package com.tevinjeffrey.rutgersct.data.uctapi.search;


public class SearchManager {
    SearchFlow searchFlow;

    public SearchFlow newSearch() {
        searchFlow = new SearchFlow();
        return searchFlow;
    }

    public SearchFlow getSearchFlow() {
        return searchFlow;
    }

    @Override
    public String toString() {
        return searchFlow.toString();
    }
}
