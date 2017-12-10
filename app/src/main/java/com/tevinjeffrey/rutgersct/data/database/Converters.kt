package com.tevinjeffrey.rutgersct.data.database

import android.arch.persistence.room.TypeConverter
import android.support.annotation.Nullable
import com.tevinjeffrey.rutgersct.dagger.RutgersAppModule
import com.tevinjeffrey.rutgersct.data.model.Semester
import com.tevinjeffrey.rutgersct.data.model.University

class Converters {

  companion object {
    private val semesterAdapter = RutgersAppModule.moshi.adapter(Semester::class.java)
    private val universityAdapter = RutgersAppModule.moshi.adapter(University::class.java)
  }

  @TypeConverter
  @Nullable
  fun semesterFromString(value: String): Semester? {
    return semesterAdapter.fromJson(value)
  }

  @TypeConverter
  fun stringFromSemester(semester: Semester): String {
    return semesterAdapter.toJson(semester)
  }

  @TypeConverter
  fun stringFromUniversity(university: University): String {
    return universityAdapter.toJson(university)
  }

  @TypeConverter
  @Nullable
  fun universityFromString(value: String): University? {
    return universityAdapter.fromJson(value)
  }
}