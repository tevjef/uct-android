package com.tevinjeffrey.rutgersct.ui.sectioninfo

import com.tevinjeffrey.rmp.common.Professor

data class RMPModel(
    val showRatingsLayout: Boolean = false,
    val ratingsLayoutLoading: Boolean = true,
    val professor: List<Professor> = emptyList(),
    val professorNotFound: List<String> = emptyList()
)
