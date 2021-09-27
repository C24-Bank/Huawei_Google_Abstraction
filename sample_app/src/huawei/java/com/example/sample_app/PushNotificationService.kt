package com.example.sample_app

import android.content.Intent
import android.util.Log
import com.huawei.hms.push.BaseException
import com.huawei.hms.push.RemoteMessage
import com.huawei.hms.push.SendException
import de.c24.hg_abstraction.notification.NotificationService

class PushNotificationService: NotificationService() {
    val TAG = "PushDemoLog"
    val CODELABS_ACTION= "com.huawei.codelabpush.action"

    override fun onNewToken(token: String?) {
        super.onNewToken(token);
        // Obtain a token.
        Log.i(TAG, "have received refresh token:$token")

        // Check whether the token is empty.
        if (!token.isNullOrEmpty()) {
            refreshedTokenToServer(token)
        }
    }

    private fun refreshedTokenToServer(token: String) {
        Log.i(TAG, "sending token to server. token:$token")
    }

    override fun onMessageReceived(message: RemoteMessage?)  {
        super.onMessageReceived(message)
        Log.i(TAG, "onMessageReceived is called")

        // Check whether the message is empty.
        if (message == null)
        {
            Log.e(TAG, "Received message entity is null!")
            return
        }

        // Obtain the message content.
        Log.i(TAG, """getData: ${message.data}        
            getFrom: ${message.from}        
            getTo: ${message.to}        
            getMessageId: ${message.messageId}
            getSendTime: ${message.sentTime}           
            getDataMap: ${message.dataOfMap}
            getMessageType: ${message.messageType}   
            getTtl: ${message.ttl}        
            getToken: ${message.token}""".trimIndent())

        val judgeWhetherIn10s = false
        // If the message is not processed within 10 seconds, create a job to process it.
        if (judgeWhetherIn10s) {
            startWorkManagerJob(message)
        } else {
            // Process the message within 10 seconds.
            processWithin10s(message)
        }
    }

    private fun startWorkManagerJob(message: RemoteMessage?) {
        Log.d(TAG, "Start new Job processing.")
    }
    private fun processWithin10s(message: RemoteMessage?) {
        Log.d(TAG, "Processing now.")
    }

    override fun onMessageSent(msgId: String?) {
        super.onMessageSent(msgId)
        Log.i(TAG, "onMessageSent called, Message id:$msgId")
        val intent = Intent()
        intent.action = CODELABS_ACTION
        intent.putExtra("method", "onMessageSent")
        intent.putExtra("msg", "onMessageSent called, Message id:$msgId")
        sendBroadcast(intent)
    }

    override fun onSendError(msgId: String?, exception: Exception?) {
        super.onSendError(msgId, exception)
        Log.i(TAG, "onSendError called, message id:$msgId, ErrCode:${(exception as SendException).errorCode}, " +
                "description:${exception.message}")
        val intent = Intent()
        intent.action = CODELABS_ACTION
        intent.putExtra("method", "onSendError")
        intent.putExtra("msg", "onSendError called, message id:$msgId, ErrCode:${exception.errorCode}, " +
                "description:${exception.message}")
        sendBroadcast(intent)
    }

    override fun onTokenError(e: Exception) {
        super.onTokenError(e)
        TODO("Not yet implemented")
    }

    override fun onMessageDelivered(msgId: String?, exception: Exception?) {
        super.onMessageDelivered(msgId, exception)
        // Obtain the error code and description.
        val errCode = (exception as BaseException).errorCode
        val errInfo = exception.message
        if (errCode != 0) {
            // Process specific service requirements.
        }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }
}