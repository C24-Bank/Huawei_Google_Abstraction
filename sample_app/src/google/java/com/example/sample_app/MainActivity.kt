package com.example.sample_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import de.c24.hg_abstraction.ScanActivity
import de.c24.hg_abstraction.ScannerHelper
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : Activity() {

    companion object {
        private val TAG = "MainActivity"
        private val DEFINED_CODE = 222
        private val REQUEST_CODE_SCAN = 0X01
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button?.setOnClickListener()
        {
            navigateToActivityScan()
        }
        servicebutton?.setOnClickListener {
            ScannerHelper.startScanActivity(this,REQUEST_CODE_SCAN)
        }
    }


    private fun navigateToActivityScan() {
        this.startActivityForResult(
            Intent(this, DefinedActivity::class.java), REQUEST_CODE_SCAN)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }
        else if (requestCode == REQUEST_CODE_SCAN) {
            val qrToken = data.getCharSequenceExtra(ScanActivity.SCAN_RESULT)
            if (!TextUtils.isEmpty(qrToken))
                Toast.makeText(this, qrToken, Toast.LENGTH_SHORT).show()
        }
    }

}

