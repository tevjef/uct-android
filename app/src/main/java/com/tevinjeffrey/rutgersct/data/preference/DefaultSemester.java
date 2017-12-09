package com.tevinjeffrey.rutgersct.data.preference;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.tevinjeffrey.rutgersct.data.model.Semester;

@Entity(tableName = "default_semester")
public class DefaultSemester {

  @PrimaryKey
  @NonNull
  private Semester semester;

  public DefaultSemester(final Semester semester) {
    this.semester = semester;
  }

  public Semester getSemester() {
    return semester;
  }

  public void setSemester(final Semester semester) {
    this.semester = semester;
  }
}
