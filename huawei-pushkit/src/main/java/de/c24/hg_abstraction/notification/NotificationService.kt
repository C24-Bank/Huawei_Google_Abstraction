package de.c24.hg_abstraction.notification

import androidx.annotation.CallSuper
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import de.c24.hg_abstraction.core_pushkit.NotificationCore
import de.c24.hg_abstraction.core_pushkit.NotificationRemoteMessage

abstract class NotificationService : HmsMessageService() {

    @CallSuper
    override fun onNewToken(token: String?) {
        super.onNewToken(token)
    }


    abstract fun onMessageReceived(message: NotificationRemoteMessage)

    @CallSuper
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val x = message.notification
        val notification = NotificationCore(
            titleLockKey = x.titleLocalizationKey,
            title = x.title,
            ticker = x.ticker,
            tag = x.tag,
            sound = x.sound,
            huaweiIntentUri = x.intentUri,
            huaweiNotifiyId = x.notifyId,
            huaweiImageUrl = x.imageUrl,
            link = x.link,
            icon = x.icon,
            color = x.color,
            clickAction = x.clickAction,
            channelId = x.channelId,
            notificationPriority = x.importance,
            bodyLocalizationKey = x.bodyLocalizationKey,
            body = x.body,
            visibility = x.visibility,
            notificationCount = x.badgeNumber,
            eventTime = x.`when`,
            sticky = x.isAutoCancel,
            defaultVibrate = x.isDefaultVibrate,
            defaultLight = x.isDefaultLight,
            defaultSound = x.isDefaultSound,
            huaweiVibrateConfig = x.vibrateConfig,
            lightSettings = x.lightSettings,
            titleLockArgs = x.titleLocalizationArgs,
            bodyLockArgs = x.bodyLocalizationArgs

        )

        val notificationMessage =
            NotificationRemoteMessage(
                collapseKey = message.collapseKey,
                huaweiData = message.data,
                data = message.dataOfMap,
                ttl = message.ttl,
                from = message.from,
                messageId = message.messageId,
                messageType = message.messageType,
                notification = notification,
                originalPriority = message.originalUrgency,
                priority = message.urgency,
                sentTime = message.sentTime,
                to = message.to,
                huaweiToken = message.token
            )
        this.onMessageReceived(notificationMessage)

    }

    @CallSuper
    override fun onMessageSent(msgId: String?) {
        super.onMessageSent(msgId)
    }

    @CallSuper
    override fun onSendError(msgId: String?, exception: Exception?) {
        super.onSendError(msgId, exception)
    }

    @CallSuper
    override fun onTokenError(e: Exception) {
        super.onTokenError(e)
    }

    @CallSuper
    override fun onMessageDelivered(msgId: String?, exception: Exception?) {
        super.onMessageDelivered(msgId, exception)
    }

    @CallSuper
    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

}