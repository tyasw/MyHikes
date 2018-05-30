package com.example.tyasw.myhikes

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_basic_info.*

class BasicInfoActivity : StepActivity() {
    var receivedHike: Hike? = null
    var isNewHike: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_info)

        val extras = intent.extras

        isNewHike = extras.getBoolean("isNewHike")

        if (isNewHike) {
            receivedHike = intent.getParcelableExtra<Hike>("newHike") ?: null

            receivedHike?.let { receivedHike -> populateInfo(receivedHike) }
        } else {
            // Modifying a preexisting plan, receive the rest of the data
        }

        basicPreviousButton.setOnClickListener {
            previousStep()
        }

        basicNextButton.setOnClickListener {
            nextStep()
        }

        setLayoutMargins(buttonRow)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Toast.makeText(this, "Configuration changed", Toast.LENGTH_LONG).show()
    }

    fun populateInfo(hike: Hike) {
        hikeName.setText(hike.name.toString())
        length.setText(hike.length.toString())
        difficulty.setText(hike.difficulty.toString())
    }

    private fun previousStep() {
        val i = Intent(this, MainActivity::class.java)

        // Should we discard all changes made on this activity?
        startActivity(i)
    }

    private fun nextStep() {
        // Save all information user entered
        receivedHike?.let {
            it.name = hikeName.text.toString()
            it.length = length.text.toString().toDouble()
            it.difficulty = difficulty.toString()
        }

        val i = Intent(this, SuppliesActivity::class.java)

        if (isNewHike) {
            i.putExtra("isNewHike", true)
            i.putExtra("newHike", receivedHike)
        } else {
            i.putExtra("isNewHike", false)

            // Place the rest of the data
        }

        startActivity(i)
    }
}
