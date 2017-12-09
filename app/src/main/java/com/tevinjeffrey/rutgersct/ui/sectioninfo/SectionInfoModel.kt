package com.tevinjeffrey.rutgersct.ui.sectioninfo

data class SectionInfoModel(
    val isSectionAdded: Boolean = false,
    val shouldAnimateFabIn: Boolean = true,
    val error: Throwable? = null
)
