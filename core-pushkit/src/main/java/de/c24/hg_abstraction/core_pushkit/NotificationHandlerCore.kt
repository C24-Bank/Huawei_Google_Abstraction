
package de.c24.hg_abstraction.core_pushkit

import android.content.Context

interface NotificationHandlerCore {

    var tokenResult : ((String) -> Unit)?

    fun getToken(context: Context, appID: String? = null)

    fun subscribeToTopic(topic:String, context: Context)

    fun unsubscribeToTopic(topic:String, context: Context)

    fun sendUplinkMessage(context: Context, messageID:String)

    fun deleteToken(context: Context, appID: String? = null)
}