package com.tevinjeffrey.rutgersct.ui.subject

import com.tevinjeffrey.rutgersct.data.model.Subject

data class SubjectModel(
    val data: List<Subject> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null)
