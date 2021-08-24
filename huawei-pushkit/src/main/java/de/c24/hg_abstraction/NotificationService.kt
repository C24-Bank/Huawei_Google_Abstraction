package de.c24.hg_abstraction

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.CallSuper
import com.huawei.hms.push.BaseException
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import com.huawei.hms.push.SendException

abstract class NotificationService: HmsMessageService() {

    private lateinit var activity: Activity

    private lateinit var NOTIFICATION_CHANNEL_ID: String

    private lateinit var NOTIFICATION_ID: String

    @CallSuper
    override fun onNewToken(token: String?){
        super.onNewToken(token)
    }

    @CallSuper
    override fun onMessageReceived(message: RemoteMessage?){
        super.onMessageReceived(message)
    }

    @CallSuper
    override fun onMessageSent(msgId: String?){
        super.onMessageSent(msgId)
    }

    @CallSuper
    override fun onSendError(msgId: String?, exception: Exception?){
        super.onSendError(msgId,exception)
    }

    @CallSuper
    override fun onTokenError(e: Exception){
        super.onTokenError(e)
    }

    @CallSuper
    override fun onMessageDelivered(msgId: String?, exception: Exception?){
        super.onMessageDelivered(msgId,exception)
    }

    @CallSuper
    override fun onDeletedMessages(){
        super.onDeletedMessages()
    }

}