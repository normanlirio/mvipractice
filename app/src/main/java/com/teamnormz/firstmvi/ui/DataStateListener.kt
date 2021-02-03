package com.teamnormz.firstmvi.ui

import com.teamnormz.firstmvi.util.DataState

interface DataStateListener {
  fun onDataStateChanged(dataState: DataState<*>?)
}