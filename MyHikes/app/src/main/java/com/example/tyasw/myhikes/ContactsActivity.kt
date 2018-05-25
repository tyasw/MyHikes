package com.example.tyasw.myhikes

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_contacts.*

class ContactsActivity : StepActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        contactsPreviousButton.setOnClickListener {
            previousStep()
        }

        contactsNextButton.setOnClickListener {
            nextStep()
        }

        setLayoutMargins(buttonRow)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Toast.makeText(this, "Configuration changed", Toast.LENGTH_LONG).show()
    }

    private fun previousStep() {
        val i = Intent(this, MapsActivity::class.java)
        startActivity(i)
    }

    private fun nextStep() {
        val i = Intent(this, NotificationActivity::class.java)
        startActivity(i)
    }
}
