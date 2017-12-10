package com.tevinjeffrey.rutgersct.data.database

import android.arch.persistence.room.TypeConverter

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tevinjeffrey.rutgersct.data.model.Semester
import com.tevinjeffrey.rutgersct.data.model.University

class Converters {

  private val GSON_INSTANCE = GsonBuilder().serializeNulls().create()

  @TypeConverter
  fun semesterFromString(value: String): Semester {
    return GSON_INSTANCE.fromJson(value, Semester::class.java)
  }

  @TypeConverter
  fun stringFromSemester(semester: Semester): String {
    return GSON_INSTANCE.toJson(semester)
  }

  @TypeConverter
  fun stringFromUniversity(university: University): String {
    return GSON_INSTANCE.toJson(university)
  }

  @TypeConverter
  fun universityFromString(value: String): University {
    return GSON_INSTANCE.fromJson(value, University::class.java)
  }
}