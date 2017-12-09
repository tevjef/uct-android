package com.tevinjeffrey.rutgersct.ui.chooser

import com.tevinjeffrey.rutgersct.data.model.Semester

data class ChooserSemesterModel(
    val data: List<Semester> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null)