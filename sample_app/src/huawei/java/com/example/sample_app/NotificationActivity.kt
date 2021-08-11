package com.example.sample_app

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.sample_app.databinding.ActivityNotificationBinding
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.common.ApiException

class NotificationActivity: AppCompatActivity() {
    val TAG = "PushDemoLog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getToken()
    }

    private fun getToken() {
        // Create a thread.
        object : Thread() {
            override fun run() {
                try {
                    // Obtain the app ID from the agconnect-service.json file.
                    val appId = "your_APPId"

                    // Set tokenScope to HCM.
                    val tokenScope = "HCM"
                    val token = HmsInstanceId.getInstance(this@NotificationActivity).getToken(appId, tokenScope)
                    Log.i(TAG, "get token:$token")

                    // Check whether the token is empty.
                    if (!TextUtils.isEmpty(token)) {
                        sendRegTokenToServer(token)
                    }
                } catch (e: ApiException) {
                    Log.e(TAG, "get token failed, $e")
                }
            }
        }.start()
    }

    private fun sendRegTokenToServer(token: String?) {
        Log.i(TAG, "sending token to server. token:$token")
    }

    private fun deleteToken() {
        // Create a thread.
        object : Thread() {
            override fun run() {
                try {
                    // Obtain the app ID from the agconnect-service.json file.
                    val appId = "your_APPId"

                    // Set tokenScope to HCM.
                    val tokenScope = "HCM"

                    // Delete the token.
                    HmsInstanceId.getInstance(this@NotificationActivity).deleteToken(appId, tokenScope)
                    Log.i(TAG, "token deleted successfully")
                } catch (e: ApiException) {
                    Log.e(TAG, "delete token failed, $e")
                }
            }
        }.start()
    }
}