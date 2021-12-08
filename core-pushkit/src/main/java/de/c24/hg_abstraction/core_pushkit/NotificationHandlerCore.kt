
package de.c24.hg_abstraction.core_pushkit

import android.content.Context

interface NotificationHandlerCore {

    /**
     * returns the devicetoken as String through a callback listener
     */
    fun getToken(context: Context, appID: String? = null, tokenResult: ((String?) -> Unit))

    /**
     * subscribes the device to a specific topic
     */
    fun subscribeToTopic(topic:String, context: Context)

    /**
     * unsubscribes the device to a specific topic
     */
    fun unsubscribeToTopic(topic:String, context: Context)

    /**
     * sends Messages to the Server
     */
    fun sendUplinkMessage(context: Context, messageID:String, dataList: List<Pair<String,String>>)

    /**
     * deletes the deviceToken
     */
    fun deleteToken(context: Context, appID: String? = null)
}