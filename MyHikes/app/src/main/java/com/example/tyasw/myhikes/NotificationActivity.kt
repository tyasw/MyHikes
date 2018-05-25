package com.example.tyasw.myhikes

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : StepActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        notificationPreviousButton.setOnClickListener {
            previousStep()
        }

        notificationFinishButton.setOnClickListener {
            finishStep()
        }

        setLayoutMargins(buttonRow)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Toast.makeText(this, "Configuration changed", Toast.LENGTH_LONG).show()
    }

    private fun previousStep() {
        val i = Intent(this, ContactsActivity::class.java)
        startActivity(i)
    }

    private fun finishStep() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }
}
