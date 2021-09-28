package de.c24.hg_abstraction.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.CallSuper
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import de.c24.hg_abstraction.core_pushkit.NotificationCore
import de.c24.hg_abstraction.core_pushkit.NotificationRemoteMessage
import java.lang.Exception

abstract class NotificationService: FirebaseMessagingService() {

    abstract fun onMessageReceived(message: NotificationRemoteMessage)

    @CallSuper
    override fun onMessageReceived(remoteMessage: RemoteMessage){
        super.onMessageReceived(remoteMessage)
        remoteMessage.notification?.let { x ->
            val notification = NotificationCore(
                titleLockKey = x.titleLocalizationKey,
                title = x.title,
                ticker = x.ticker,
                tag = x.tag,
                sound = x.sound,
                imageUrl = x.imageUrl,
                link = x.link,
                icon = x.icon,
                color = x.color,
                clickAction = x.clickAction,
                channelId = x.channelId,
                notificationPriority = x.notificationPriority,
                bodyLocalizationKey = x.bodyLocalizationKey,
                body = x.body,
                visibility = x.visibility,
                notificationCount = x.notificationCount,
                eventTime = x.eventTime,
                sticky = x.sticky,
                defaultVibrate = x.defaultVibrateSettings,
                defaultLight = x.defaultLightSettings,
                defaultSound = x.defaultSound,
                lightSettings = x.lightSettings,
                titleLockArgs = x.titleLocalizationArgs,
                bodyLockArgs = x.bodyLocalizationArgs,
                googleVibrateTiming = x.vibrateTimings

            )

            val notificationMessage =
                NotificationRemoteMessage(
                    collapseKey = remoteMessage.collapseKey,
                    data = remoteMessage.data,
                    ttl = remoteMessage.ttl,
                    from = remoteMessage.from,
                    messageId = remoteMessage.messageId,
                    messageType = remoteMessage.messageType,
                    notification = notification,
                    originalPriority = remoteMessage.originalPriority,
                    priority = remoteMessage.priority,
                    sentTime = remoteMessage.sentTime,
                    to = remoteMessage.to
                )
            this.onMessageReceived(notificationMessage)
        }
    }

    @CallSuper
    override fun onNewToken(p0: String){
        super.onNewToken(p0)
    }

    @CallSuper
    override fun onDeletedMessages(){
        super.onDeletedMessages()
    }

    @CallSuper
    override fun onMessageSent(p0: String){
        super.onMessageSent(p0)
    }

    @CallSuper
    override fun onSendError(p0: String, p1: Exception){
        super.onSendError(p0, p1)
    }

}
