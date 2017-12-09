package com.tevinjeffrey.rutgersct.ui.chooser

import com.tevinjeffrey.rutgersct.data.model.University

data class ChooserUniversityModel(
    val data: List<University> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null)