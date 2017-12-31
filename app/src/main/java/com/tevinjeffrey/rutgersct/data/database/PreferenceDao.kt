package com.tevinjeffrey.rutgersct.data.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

import com.tevinjeffrey.rutgersct.data.database.entities.DefaultSemester
import com.tevinjeffrey.rutgersct.data.database.entities.DefaultUniversity
import org.intellij.lang.annotations.Language

@Dao
interface PreferenceDao {
  @Language("RoomSql")
  @Query("SELECT * FROM default_semester LIMIT 1")
  fun defaultSemester(): DefaultSemester?

  @Language("RoomSql")
  @Query("SELECT * FROM default_university LIMIT 1")
  fun defaultUniversity(): DefaultUniversity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun updateDefaultSemester(semester: DefaultSemester)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun updateDefaultUniversity(university: DefaultUniversity)
}
