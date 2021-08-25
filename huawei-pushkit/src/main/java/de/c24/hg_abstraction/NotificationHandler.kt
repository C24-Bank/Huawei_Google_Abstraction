package de.c24.hg_abstraction

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.common.ApiException
import com.huawei.hms.push.HmsMessaging
import com.huawei.hms.push.RemoteMessage
import de.c24.hg_abstraction.core_pushkit.NotificationHandlerCore

class NotificationHandler: NotificationHandlerCore {

    companion object {

        private const val TAG = "NotificationHandler"
    }

    override var tokenResult: ((String) -> Unit)? = null

     override fun getToken(context: Context, appID: String?) {
        // Create a thread.
        object : Thread() {
            override fun run() {
                try {
                    // Obtain the app ID from the agconnect-service.json file.
                    val appId = "your_APPId"

                    // Set tokenScope to HCM.
                    val tokenScope = "HCM"
                    val token = HmsInstanceId.getInstance(context).getToken(appID, tokenScope)
                    Log.i(TAG, "get token:$token")

                    // Check whether the token is empty.
                    if (!TextUtils.isEmpty(token)) {
                        tokenResult?.invoke(token)
                    }
                } catch (e: ApiException) {
                    Log.e(TAG, "get token failed, $e")
                }
            }
        }.start()
    }

     override fun deleteToken(context: Context, appID: String?) {
        // Create a thread.
        object : Thread() {
            override fun run() {
                try {
                    // Obtain the app ID from the agconnect-service.json file.
                    val appId = "your_APPId"

                    // Set tokenScope to HCM.
                    val tokenScope = "HCM"

                    // Delete the token.
                    HmsInstanceId.getInstance(context).deleteToken(appID, tokenScope)
                    Log.i(TAG, "token deleted successfully")
                } catch (e: ApiException) {
                    Log.e(TAG, "delete token failed, $e")
                }
            }
        }.start()
    }

     override fun subscribeToTopic(topic: String, context: Context) {
        try {
            // Subscribe to a topic.
            HmsMessaging.getInstance(context)
                .subscribe(topic)
                .addOnCompleteListener { task ->
                    // Obtain the topic subscription result.
                    if (task.isSuccessful) {
                        Log.i(TAG, "subscribe topic successfully")
                    } else {
                        Log.e(TAG, "subscribe topic failed, the return value is " + task.exception.message)
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, "subscribe failed, catch exception : $e")
        }
    }

     override fun unsubscribeToTopic(topic: String,context: Context) {
        try {
            // Unsubscribe from a topic.
            HmsMessaging.getInstance(context)
                .unsubscribe(topic)
                .addOnCompleteListener { task ->
                    // Obtain the topic unsubscription result.
                    if (task.isSuccessful) {
                        Log.i(TAG, "unsubscribe topic successfully")
                    } else {
                        Log.e(TAG, "unsubscribe topic failed, the return value is " + task.exception.message)
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, "unsubscribe failed, catch exception : $e")
        }
    }

    override fun sendUplinkMessage(context: Context, messageId: String, dataList: List<Pair<String,String>>){

// The input parameter of the RemoteMessage.Builder method is push.hcm.upstream, which cannot be changed.
        val remoteMessage = RemoteMessage.Builder("push.hcm.upstream")
            .setMessageId(messageId)
            .apply {
                dataList.forEach { (key,data)->
                    addData(key,data)
                }
            }
            .build()
        try {
            // Send an uplink message.
            HmsMessaging.getInstance(context).send(remoteMessage)
            Log.i(TAG, "send message successfully")
        } catch (e: Exception) {
            Log.e(TAG, "send message catch exception: $e")
        }
    }
}