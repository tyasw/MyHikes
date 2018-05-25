package com.example.tyasw.myhikes

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : StepActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up button onclick handlers
        mainAddButton.setOnClickListener {
            addNew()
        }

        mainNextButton.setOnClickListener {
            nextStep()
        }

        setLayoutMargins(buttonRow)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Toast.makeText(this, "Configuration changed", Toast.LENGTH_LONG).show()
    }

    private fun addNew() {
        val i = Intent(this, BasicInfoActivity::class.java)
        startActivity(i)
    }

    // Load from DB
    private fun nextStep() {
        val i = Intent(this, BasicInfoActivity::class.java)
        startActivity(i)
    }
}
