
package de.c24.hg_abstraction.core_pushkit

import android.content.Context

interface NotificationHandlerCore {

    fun getToken(context: Context)

    fun subscribeToTopic(topic:String, context: Context)

    fun unsubscribeToTopic(topic:String, context: Context)

    fun sendUplinkMessage(context: Context)

    fun deleteToken(context: Context)
}