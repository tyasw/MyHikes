package com.example.tyasw.myhikes

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_basic_info.*

class BasicInfoActivity : StepActivity() {
    private var receivedHike: Hike? = null
    private var supplies: ArrayList<Supply>? = null
    private var contacts: ArrayList<Contact>? = null
    private var isNewHike: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_info)

        val extras = intent.extras

        isNewHike = extras.getBoolean("isNewHike")

        receivedHike = intent.getParcelableExtra<Hike>("hike") ?: null
        supplies = intent.getParcelableArrayListExtra<Supply>("supplies") ?: null
        contacts = intent.getParcelableArrayListExtra<Contact>("contacts") ?: null

        populateInfo(receivedHike)

        basicPreviousButton.setOnClickListener {
            previousStep()
        }

        basicNextButton.setOnClickListener {
            nextStep()
        }

        setButtonRowParameters(buttonRow)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Toast.makeText(this, "Configuration changed", Toast.LENGTH_LONG).show()
    }

    fun populateInfo(hike: Hike?) {
        if (hike != null) {
            hikeName.setText(hike.name.toString())
            length.setText(hike.length.toString())
            difficulty.setText(hike.difficulty.toString())
        }
    }

    private fun previousStep() {
        // Save all information user entered
        receivedHike?.let {
            it.name = hikeName.text.toString()
            it.length = length.text.toString().toDouble()
            it.difficulty = difficulty.text.toString()
        }

        val i = Intent(this, MainActivity::class.java)

        if (isNewHike) {
            i.putExtra("isNewHike", true)
        } else {
            i.putExtra("isNewHike", false)
        }

        i.putExtra("hike", receivedHike)
        i.putExtra("supplies", supplies)
        i.putExtra("contacts", contacts)

        startActivity(i)
    }

    private fun nextStep() {
        // Save all information user entered
        receivedHike?.let {
            it.name = hikeName.text.toString()
            it.length = length.text.toString().toDouble()
            it.difficulty = difficulty.text.toString()
        }

        val i = Intent(this, SuppliesActivity::class.java)

        if (isNewHike) {
            i.putExtra("isNewHike", true)
        } else {
            i.putExtra("isNewHike", false)
        }

        i.putExtra("hike", receivedHike)
        i.putExtra("supplies", supplies)
        i.putExtra("contacts", contacts)

        startActivity(i)
    }
}
