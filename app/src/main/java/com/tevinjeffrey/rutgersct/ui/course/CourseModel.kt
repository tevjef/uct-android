package com.tevinjeffrey.rutgersct.ui.course

import com.tevinjeffrey.rutgersct.data.model.Course

data class CourseModel(
    val data: List<Course> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null)
