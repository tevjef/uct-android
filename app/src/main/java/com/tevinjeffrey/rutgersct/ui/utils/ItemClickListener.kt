package com.tevinjeffrey.rutgersct.ui.utils

interface ItemClickListener<D, V> {
  fun onItemClicked(data: D, view: V)
}
