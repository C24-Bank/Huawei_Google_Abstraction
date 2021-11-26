package de.c24.hg_abstraction.core_pushkit

import android.net.Uri

data class NotificationData(
    val titleLockKey: String?,
    val title: String?,
    val ticker: String?,
    val tag: String?,
    val sound: String?,
    val huaweiIntentUri: String? = null,
    val huaweiNotifiyId: Int? = null,
    val imageUrl: Uri? = null,
    val link: Uri?,
    val icon: String?,
    val color: String?,
    val clickAction: String?,
    val channelId: String?,
    val notificationPriority:Int?,
    val bodyLocalizationKey: String?,
    val body: String?,
    val visibility: Int?,
    val notificationCount: Int?,
    val eventTime: Long?,
    val defaultVibrate: Boolean?,
    val defaultSound: Boolean?,
    val defaultLight: Boolean?,
    val sticky: Boolean?,

    val googleVibrateTiming: LongArray? = null,
    val lightSettings: IntArray?,
    val titleLockArgs: Array<String>?,
    val bodyLockArgs: Array<String>?,
    val huaweiVibrateConfig: LongArray? = null
)
