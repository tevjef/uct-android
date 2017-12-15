package com.tevinjeffrey.rutgersct.ui.trackedsections

import com.tevinjeffrey.rutgersct.data.search.UCTSubscription
import java.util.*

class TrackedSectionsModel(
    var isLoading: Boolean = false,
    var data: List<UCTSubscription>? = null,
    var error: Throwable? = null)