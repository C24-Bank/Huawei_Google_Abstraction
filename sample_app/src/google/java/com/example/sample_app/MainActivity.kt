package com.example.sample_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
    }


    private fun navigateToActivityScan() {
        this.startActivityForResult(
            Intent(this, ScanActivity::class.java), REQUEST_CODE_SCAN)
    }

}

