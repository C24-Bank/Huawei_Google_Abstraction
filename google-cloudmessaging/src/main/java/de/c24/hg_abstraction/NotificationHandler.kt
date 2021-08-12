package de.c24.hg_abstraction

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.Constants
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.messaging.ktx.remoteMessage

class NotificationHandler {

    companion object {

        private const val TAG = "NotificationHandler"
    }

    fun createChannel(notificationChannelID:String, channelName:String,context: Context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(notificationChannelID,
                    channelName, NotificationManager.IMPORTANCE_LOW)
            )
        }
    }

    fun subscribeToTopic(topic: String, context: Context){
        Log.d(TAG, "Subscribing to $topic topic")
        // [START subscribe_topics]
        Firebase.messaging.subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                var msg = "Subscribing successful"
                if (!task.isSuccessful) {
                    msg = "Subscribing failed! Try again later"
                }
                Log.d(TAG, msg)
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
    }

    fun getToken(context:Context){
        // Get token
        // [START log_reg_token]
        Firebase.messaging.getToken().addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = "Token created: $token"
            Log.d(TAG, msg)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        })
        // [END log_reg_token]
    }

    private fun sendingUpstreamMessage(){
        val fm = Firebase.messaging
        val messageId = 0 // Increment for each
        fm.send(remoteMessage("${Constants.MessagePayloadKeys.SENDER_ID}@fcm.googleapis.com") {
            setMessageId(messageId.toString())
            addData("my_message", "Hello World")
            addData("my_action", "SAY_HELLO")
        })
    }
}