package de.c24.hg_abstraction.core_scan

import android.app.Activity

interface ScanViewCore {

    /***
     * resultListener is a result callback that sends back the originalvalue of the
     *  barcode result as a String
     */
    var resultListener: ((String) -> Unit)?

    /**
     * starts the camera and analyzes the barcode in the preview
     */
    fun startCamera(activity: Activity)

    /***
     * handles the situation what should happen if the View is destroyed
     */
    fun destroyView()
}