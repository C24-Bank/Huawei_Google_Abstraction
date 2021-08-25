package com.example.sample_app

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import de.c24.hg_abstraction.NotificationService
import java.lang.Exception

class PushNotificationService :NotificationService(){

    override var notificationChanelId = "de.c24.hg_abstraction.notification"
    override var notificationId = "100"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.e("message","Message Received ...");

        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]
            showNotification(applicationContext, title, body)
        } else {
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body
            showNotification(applicationContext, title, body)
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.e("token","New Token")
    }

    fun showNotification(
        context: Context,
        title: String?,
        message: String?
    ) {
        val ii: Intent = Intent(context, MainActivity::class.java)
        ii.data = Uri.parse("custom://" + System.currentTimeMillis())
        ii.action = "actionstring" + System.currentTimeMillis()
        ii.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pi =
            PendingIntent.getActivity(context, 0, ii, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification: Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Log.e("Notification", "Created in up to orio OS device");
            notification = NotificationCompat.Builder(context, notificationChanelId)
                .setColor(getNotificationColor())
                .setOngoing(true)
                .setSmallIcon(getNotificationIcon())
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setWhen(System.currentTimeMillis())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title).build()
            val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            val notificationChannel = NotificationChannel(
                    notificationChanelId,
                title,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.notify(notificationId.toInt(), notification)
        } else {
            notification = NotificationCompat.Builder(context)
                .setSmallIcon(getNotificationIcon())
                .setAutoCancel(true)
                .setContentText(message)
                .setContentIntent(pi)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title).build()
            val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.notify(notificationId.toInt(), notification)
        }
    }

    private fun getNotificationIcon(): Int {
        val useWhiteIcon =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        return if (useWhiteIcon) R.drawable.mountain_icon else R.drawable.mountain_icon
    }

    private fun getNotificationColor(): Int {
        val useColor =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        return if (useColor) R.color.notification_color else R.color.notification_color
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
        TODO("Not yet implemented")
    }

    override fun onMessageSent(p0: String) {
        super.onMessageSent(p0)
        TODO("Not yet implemented")
    }

    override fun onSendError(p0: String, p1: Exception) {
        super.onSendError(p0, p1)
        TODO("Not yet implemented")
    }

}
