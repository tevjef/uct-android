package com.tevinjeffrey.rutgersct.data.database;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tevinjeffrey.rutgersct.data.model.Semester;
import com.tevinjeffrey.rutgersct.data.model.University;

public class Converters {

  private static Gson GSON_INSTANCE = new GsonBuilder().serializeNulls().create();

  @TypeConverter
  public static Semester semesterFromString(String value) {
    return GSON_INSTANCE.fromJson(value, Semester.class);
  }

  @TypeConverter
  public static String stringFromSemester(Semester semester) {
    return GSON_INSTANCE.toJson(semester);
  }

  @TypeConverter
  public static String stringFromUniversity(University university) {
    return GSON_INSTANCE.toJson(university);
  }

  @TypeConverter
  public static University universityFromString(String value) {
    return GSON_INSTANCE.fromJson(value, University.class);
  }
}