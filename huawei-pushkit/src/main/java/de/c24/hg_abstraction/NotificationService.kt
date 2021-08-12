package de.c24.hg_abstraction

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.huawei.hms.push.BaseException
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import com.huawei.hms.push.SendException

class NotificationService: HmsMessageService() {

    private lateinit var activity: Activity

    private lateinit var NOTIFICATION_CHANNEL_ID: String

    private lateinit var NOTIFICATION_ID: String

    val TAG = "PushDemoLog"
    val CODELABS_ACTION= "com.huawei.codelabpush.action"

    override fun onNewToken(token: String?, bundle: Bundle?) {
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

    override fun onMessageReceived(message: RemoteMessage?) {
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
        Log.i(TAG, "onMessageSent called, Message id:$msgId")
        val intent = Intent()
        intent.action = CODELABS_ACTION
        intent.putExtra("method", "onMessageSent")
        intent.putExtra("msg", "onMessageSent called, Message id:$msgId")
        sendBroadcast(intent)
    }

    override fun onSendError(msgId: String?, exception: Exception?) {
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
    }

    override fun onMessageDelivered(msgId: String?, exception: Exception?) {
        // Obtain the error code and description.
        val errCode = (exception as BaseException).errorCode
        val errInfo = exception.message
        if (errCode != 0) {
            // Process specific service requirements.
        }
    }

}