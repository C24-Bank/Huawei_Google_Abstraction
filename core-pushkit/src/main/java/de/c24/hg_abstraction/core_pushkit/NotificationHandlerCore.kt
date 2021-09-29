
package de.c24.hg_abstraction.core_pushkit

import android.content.Context

interface NotificationHandlerCore {

    fun getToken(context: Context, appID: String? = null, tokenResult: ((String?) -> Unit))

    fun subscribeToTopic(topic:String, context: Context)

    fun unsubscribeToTopic(topic:String, context: Context)

    fun sendUplinkMessage(context: Context, messageID:String, dataList: List<Pair<String,String>>)

    fun deleteToken(context: Context, appID: String? = null)
}