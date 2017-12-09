package com.tevinjeffrey.rutgersct.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tevinjeffrey.rutgersct.data.model.Course
import com.tevinjeffrey.rutgersct.data.model.Section
import com.tevinjeffrey.rutgersct.data.model.Semester
import com.tevinjeffrey.rutgersct.data.model.Subject
import com.tevinjeffrey.rutgersct.data.model.University
import com.tevinjeffrey.rutgersct.data.search.SearchFlow

class SearchViewModel : ViewModel() {
  var university: University? = null
  var semester: Semester? = null
  var subject: Subject? = null
  var course: Course? = null
  var section: Section? = null

  private val searchFlowLiveData = MutableLiveData<SearchFlow>()

  fun getSearchFlow(): LiveData<SearchFlow> {
    if (searchFlowLiveData.value != null) {
      searchFlowLiveData.postValue(SearchFlow())
    }

    return searchFlowLiveData
  }

  fun setSearchFlow(searchFlow: SearchFlow) {
    this.searchFlowLiveData.postValue(searchFlow)
  }
}
