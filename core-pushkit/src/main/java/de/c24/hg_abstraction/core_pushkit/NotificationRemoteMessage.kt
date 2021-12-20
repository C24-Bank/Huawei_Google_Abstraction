package de.c24.hg_abstraction.core_pushkit

/**
 * functions as a wrapper class / facade for the Object RemoteMessage from
 * huawei.push and firebase.messaging
 */
data class NotificationRemoteMessage (
    val collapseKey :String?,
    val huaweiData: String? = null,
    val googleSenderId: String? = null,
    val huaweiToken: String? = null,
    val ttl: Int,
    val data: Map<String, String>,
    val from: String?,
    val messageId: String?,
    val messageType: String?,
    val notification: NotificationData?,
    val originalPriority: Int,
    val priority: Int,
    val sentTime: Long,
    val to: String?,
)

