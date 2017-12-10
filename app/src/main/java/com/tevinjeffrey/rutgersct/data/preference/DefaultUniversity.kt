package com.tevinjeffrey.rutgersct.data.preference

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.tevinjeffrey.rutgersct.data.model.University

@Entity(tableName = "default_university")
data class DefaultUniversity(
    var university: University,
    @PrimaryKey var key: String = "default_university_key"
)
