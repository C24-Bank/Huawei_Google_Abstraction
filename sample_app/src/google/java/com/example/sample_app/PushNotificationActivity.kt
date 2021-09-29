package com.example.sample_app


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import de.c24.hg_abstraction.notification.NotificationHandler
import kotlinx.android.synthetic.main.acitivity_pushnoti.*


class PushNotificationActivity:AppCompatActivity() {

    private val pushNotificationHandler = NotificationHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_pushnoti)

        getTokenbutton?.setOnClickListener {
            pushNotificationHandler.getToken(this){ tokenResult ->
                tokenResult?.let {
                    runOnUiThread(Runnable {
                        run {
                            Toast.makeText(this,it, Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }

        subscribebutton?.setOnClickListener {
            pushNotificationHandler.subscribeToTopic("weather",this)
        }
    }
}