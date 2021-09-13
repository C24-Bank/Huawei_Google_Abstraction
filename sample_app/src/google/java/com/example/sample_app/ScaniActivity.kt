package com.example.sample_app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_scani.*

class ScaniActivity :AppCompatActivity(){

    companion object {
        private val DEFINED_CODE = 222
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scani)
        val list = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, list, ScaniActivity.DEFINED_CODE)

    }

    private fun allPermissionsGranted(permissions: Array<out String>) = permissions.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == ScaniActivity.DEFINED_CODE) {
            if (allPermissionsGranted(permissions)) {
                //invoke the startCamera method of the view object for scanning barcode
                scanView.startCamera(this)
                scanView.resultListener = { result ->
                    Toast.makeText(this,result,Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scanView.destroyView()
    }

}