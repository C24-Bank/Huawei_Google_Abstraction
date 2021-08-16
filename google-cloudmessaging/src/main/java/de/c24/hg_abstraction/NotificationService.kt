package de.c24.hg_abstraction

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.lang.Exception

abstract class NotificationService: FirebaseMessagingService() {

    private lateinit var activity: Activity

    private lateinit var NOTIFICATION_CHANNEL_ID: String

    private lateinit var NOTIFICATION_ID: String

    abstract override fun onMessageReceived(remoteMessage: RemoteMessage)

    abstract override fun onNewToken(p0: String)

    abstract override fun onDeletedMessages()

    abstract override fun onMessageSent(p0: String)

    abstract override fun onSendError(p0: String, p1: Exception)

}
