package com.tevinjeffrey.rutgersct.data.preference;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import com.tevinjeffrey.rutgersct.data.model.University;

@Entity(tableName = "default_university")
public class DefaultUniversity {

  @PrimaryKey
  private University university;

  public DefaultUniversity(final University university) {
    this.university = university;
  }

  public University getUniversity() {
    return university;
  }

  public void setUniversity(final University university) {
    this.university = university;
  }
}
