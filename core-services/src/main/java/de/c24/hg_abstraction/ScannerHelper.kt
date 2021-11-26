package de.c24.hg_abstraction

import android.app.Activity
import android.content.Intent
import de.c24.hg_abstraction.scan.ScanActivity


class ScannerHelper {

    companion object {
        fun startScanActivity(activity:Activity,requestCode: Int){
            activity.startActivityForResult(
                Intent(activity, ScanActivity::class.java), requestCode
            )
        }
    }
}