package com.tevinjeffrey.rutgersct.ui.trackedsections

import com.tevinjeffrey.rutgersct.data.search.UCTSubscription
import java.util.*

class TrackedSectionsModel(
    var isLoading: Boolean = false,
    var data: List<UCTSubscription> = ArrayList(10),
    var error: Throwable? = null)