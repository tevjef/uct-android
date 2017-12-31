package com.tevinjeffrey.rutgersct.data.database.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.squareup.moshi.Json

import com.tevinjeffrey.rutgersct.data.model.Semester

@Entity(tableName = "default_semester")
data class DefaultSemester(
    @Json(name = "semester")
    var semester: Semester,
    @PrimaryKey var key: String = "default_semester_key"
)
