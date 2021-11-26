package com.example.sample_app

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import de.c24.hg_abstraction.scan.ScanActivity
import de.c24.hg_abstraction.ScannerHelper
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        private val DEFINED_CODE = 222
        private val REQUEST_CODE_SCAN = 0X01
        private val CODE_NOTIFICATION = 111
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button?.setOnClickListener()
        {
            navigateToActivityScan()
        }
        servicebutton?.setOnClickListener {
            val list = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, list, DEFINED_CODE)
        }
        notificationbutton?.setOnClickListener {
            this.startActivityForResult(
                    Intent(this, NotificationActivity::class.java), CODE_NOTIFICATION)
        }
        notificationservicebutton?.setOnClickListener {
            this.startActivityForResult(
                Intent(this, PushNotificationActivity::class.java), CODE_NOTIFICATION)
        }
    }


    private fun navigateToActivityScan() {
        this.startActivityForResult(
            Intent(this, ScaniActivity::class.java), REQUEST_CODE_SCAN)
    }

    private fun allPermissionsGranted(permissions: Array<out String>) = permissions.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == DEFINED_CODE) {
            if (allPermissionsGranted(permissions)) {
                //start your activity for scanning barcode
                ScannerHelper.startScanActivity(this,REQUEST_CODE_SCAN)
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
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

