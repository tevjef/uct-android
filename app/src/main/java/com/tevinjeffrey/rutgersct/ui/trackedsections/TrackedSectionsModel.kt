package com.tevinjeffrey.rutgersct.ui.trackedsections

import com.tevinjeffrey.rutgersct.data.database.entities.UCTSubscription

class TrackedSectionsModel(
    var isLoading: Boolean = false,
    var data: List<UCTSubscription>? = null,
    var error: Throwable? = null)