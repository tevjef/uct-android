package com.tevinjeffrey.rutgersct.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.tevinjeffrey.rutgersct.data.preference.DefaultSemester;
import com.tevinjeffrey.rutgersct.data.preference.DefaultUniversity;

@Dao
public interface PreferenceDao {
  @Query("SELECT * FROM default_semester LIMIT 1")
  DefaultSemester getDefaultSemester();

  @Query("SELECT * FROM default_university LIMIT 1")
  DefaultUniversity getDefaultUniversity();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void updateDefaultSemester(DefaultSemester semester);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void updateDefaultUniversity(DefaultUniversity university);
}
