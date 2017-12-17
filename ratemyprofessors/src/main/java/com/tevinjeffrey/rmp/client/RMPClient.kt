package com.tevinjeffrey.rmp.client

import com.tevinjeffrey.rmp.common.Parameter
import com.tevinjeffrey.rmp.common.Professor

import java.util.HashMap

import io.reactivex.Observable

class RMPClient(private val mClientService: ClientService) {

  fun findProfessor(parameter: Parameter): Observable<Professor> {
    return mClientService.findProfessor(makeParameters(parameter))
  }

  companion object {

    fun makeParameters(params: Parameter): Map<String, String> {
      val map = HashMap<String, String>()
      map.put("first", params.firstName.orEmpty())
      map.put("last", params.lastName.orEmpty())
      map.put("subject", params.department.orEmpty())
      map.put("course", params.courseNumber.orEmpty())
      map.put("city", params.location.orEmpty())
      return map
    }
  }
}
