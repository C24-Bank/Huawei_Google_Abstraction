package com.example.sample_app

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.huawei.hms.ml.scan.HmsScan
import de.c24.hg_abstraction.ScannerHelper
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = "MainActivity"
        private val DEFINED_CODE = 222
        private val REQUEST_CODE_SCAN = 0X01
        private val GET_NOTIFICATION_CODE = 111
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button?.setOnClickListener()
        {
            newViewBtnClick()
        }
        servicebutton?.setOnClickListener {
            serviceBtnClick()
        }
        notificationbutton?.setOnClickListener {
            this.startActivityForResult(
                Intent(this, NotificationActivity::class.java), REQUEST_CODE_SCAN)
        }
        notificationservicebutton?.setOnClickListener {
            this.startActivityForResult(
                Intent(this, PushNotificationActivity::class.java), REQUEST_CODE_SCAN)
        }
    }

    private fun newViewBtnClick() {
        // Initialize a list of required permissions to request runtime
        val list = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, list, DEFINED_CODE)
    }

    private fun serviceBtnClick() {
        ScannerHelper.startScanActivity(this,REQUEST_CODE_SCAN)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size < 2 || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            return
        }
        else if (requestCode == DEFINED_CODE) {
            //start your activity for scanning barcode
            this.startActivityForResult(
                Intent(this, ScaniActivity::class.java), REQUEST_CODE_SCAN)
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }
        else if (requestCode == REQUEST_CODE_SCAN) {
            // Obtain the return value of HmsScan from the value returned by the onActivityResult method by using ScanUtil.RESULT as the key value.
            val result = data.getCharSequenceExtra(DefinedActivity.SCAN_RESULT)
            if (!TextUtils.isEmpty(result))
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        }
    }

}

