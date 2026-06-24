package com.sisco.tabpigs

import android.os.SystemClock
import android.view.View

fun View.setOnSingleClickListener(onClick: (View?) -> Unit) {
    val minClickInterval = 500L
    var mLastClickTime = 0L
    this.setOnClickListener {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - mLastClickTime >= minClickInterval) {
            mLastClickTime = currentTime
            onClick.invoke(this)
        }
    }
}