package com.example.tyasw.myhikes

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

// Code came from http://xadkile.blogspot.com/2017/09/this-post-gives-examples-of-sending-sms.html
class SMSBroadcastReceiver: BroadcastReceiver {
    private var context: Context? = null

    constructor(context: Context?) {
        this.context = context
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            SENT -> {
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(context!!, "SMS was sent successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context!!, "SMS was not sent successfully", Toast.LENGTH_SHORT).show()
                }
            }

            DELIVERED -> {
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(context!!, "SMS was delivered successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context!!, "SMS was not delivered successfully", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        val SENT = "SMS_SENT"
        val DELIVERED = "SMS_DELIVERED"
    }
}