package de.c24.hg_abstraction

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.huawei.hms.push.BaseException
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import com.huawei.hms.push.SendException

abstract class NotificationService: HmsMessageService() {

    private lateinit var activity: Activity

    private lateinit var NOTIFICATION_CHANNEL_ID: String

    private lateinit var NOTIFICATION_ID: String

    abstract override fun onNewToken(token: String?, bundle: Bundle?)

    abstract override fun onMessageReceived(message: RemoteMessage?)

    abstract override fun onMessageSent(msgId: String?)

    abstract override fun onSendError(msgId: String?, exception: Exception?)

    abstract override fun onTokenError(e: Exception)

    abstract override fun onMessageDelivered(msgId: String?, exception: Exception?)

    abstract override fun onDeletedMessages()

}