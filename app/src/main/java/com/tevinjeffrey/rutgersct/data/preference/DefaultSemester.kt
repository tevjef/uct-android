package com.tevinjeffrey.rutgersct.data.preference

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import com.tevinjeffrey.rutgersct.data.model.Semester

@Entity(tableName = "default_semester")
data class DefaultSemester(
    var semester: Semester,
    @PrimaryKey var key: String = "default_semester_key"
)
