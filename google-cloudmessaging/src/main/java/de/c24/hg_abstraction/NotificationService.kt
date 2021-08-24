package de.c24.hg_abstraction

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
import java.lang.Exception

abstract class NotificationService: FirebaseMessagingService() {

    private lateinit var activity: Activity

    private lateinit var NOTIFICATION_CHANNEL_ID: String

    private lateinit var NOTIFICATION_ID: String

    @CallSuper
    override fun onMessageReceived(remoteMessage: RemoteMessage){
        super.onMessageReceived(remoteMessage)
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
