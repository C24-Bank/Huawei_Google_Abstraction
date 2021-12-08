package de.c24.hg_abstraction.notification

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.Constants
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.messaging.ktx.remoteMessage
import de.c24.hg_abstraction.core_pushkit.NotificationHandlerCore

class NotificationHandler: NotificationHandlerCore {

    companion object {

        private const val TAG = "NotificationHandler"
    }

    override fun getToken(context:Context,  appID: String?, tokenResult: ((String?) -> Unit)) {
        // Get token
        // [START log_reg_token]
        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                tokenResult(null)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Check whether the token is empty.
            if (!TextUtils.isEmpty(token) && token != null) {
                tokenResult(token)
            }else{
                tokenResult(null)
            }

            // Log
            Log.d(TAG, "Token created")
        })
        // [END log_reg_token]
    }

    override fun subscribeToTopic(topic: String, context: Context){
        Log.d(TAG, "Subscribing to $topic topic")
        // [START subscribe_topic]
        Firebase.messaging.subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                var msg = "Subscribing successful"
                if (!task.isSuccessful) {
                    msg = "Subscribing failed! Try again later"
                }
                Log.d(TAG, msg)
            }
    }

    override fun unsubscribeToTopic(topic: String, context: Context) {
        Log.d(TAG, "Unsubscribing to $topic topic")
        // [START Unsubscribe_topic]
        Firebase.messaging.unsubscribeFromTopic(topic)
                .addOnCompleteListener { task ->
                    var msg = "Unsubscribing successful"
                    if (!task.isSuccessful) {
                        msg = "Unsubscribing failed! Try again later"
                    }
                    Log.d(TAG, msg)
                }

    }

     override fun sendUplinkMessage(
         context: Context,
         messageId: String,
         dataList: List<Pair<String,String>>
     ){
        val fm = Firebase.messaging
        fm.send(remoteMessage("${Constants.MessagePayloadKeys.SENDER_ID}@fcm.googleapis.com") {
            setMessageId(messageId)

            for((key,data) in dataList){
                addData(key,data)
            }

        })
    }

    override fun deleteToken(context: Context, appID: String?) {
        // Delete token
        // [START log_delete_token]
        Firebase.messaging.deleteToken()
    }
}
