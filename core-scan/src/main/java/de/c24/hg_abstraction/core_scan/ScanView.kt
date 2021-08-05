package de.c24.hg_abstraction.core_scan

import android.app.Activity

interface ScanView {
    fun startCamera(activity: Activity)

    fun destroyView()
}