package com.tevinjeffrey.rutgersct.data.database.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.squareup.moshi.Json
import com.tevinjeffrey.rutgersct.data.model.University

@Entity(tableName = "default_university")
data class DefaultUniversity(
    @Json(name = "university")
    var university: University,
    @PrimaryKey var key: String = "default_university_key"
)
