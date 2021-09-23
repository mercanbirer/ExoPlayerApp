package com.example.ExoPlayerApp.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class ExoUtilHandler(private val exoUtil: ExoUtil): LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onStart() {
        exoUtil.onStart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        exoUtil.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onPause() {
        exoUtil.onPause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() {
        exoUtil.onStop()
    }
}
