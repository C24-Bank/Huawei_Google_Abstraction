package de.c24.hg_abstraction

import androidx.annotation.CallSuper
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage

abstract class NotificationService: HmsMessageService() {

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