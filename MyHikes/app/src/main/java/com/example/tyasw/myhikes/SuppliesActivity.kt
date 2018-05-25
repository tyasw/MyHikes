package com.example.tyasw.myhikes

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_supplies.*

class SuppliesActivity : StepActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplies)

        // Check DB to see if this is a preexisting hike, populate list if it is
        // else have blank list of supplies

        setUpSuppliesList()

        suppliesPreviousButton.setOnClickListener {
            previousStep()
        }

        suppliesNextButton.setOnClickListener {
            nextStep()
        }

        setLayoutMargins(buttonRow)
    }

    // Populate the list of supplies if items already exist, or create a new one
    private fun setUpSuppliesList() {
        val suppliesList = ArrayList<Int>()

        val columnTitles = TableRow(this)

        if (!suppliesList.isEmpty()) {  // Create column titles
            dbTable.addView(columnTitles)
        } else {    // Display "no supplies" message
            val noResults = TextView(this)
            noResults.text = "No entries"
            noResults.layoutParams = setLayout(10, 0, 10, 0)
            noResults.textSize = 18f
            columnTitles.addView(noResults)

            dbTable.addView(columnTitles)
        }


        // Populate supplies list
        for (item: Int in suppliesList) {
            val row = TableRow(this)
            val suppliesId = TextView(this)
            // And so on...

            // Set margins

            // Add views to row
            row.addView(suppliesId)

            dbTable.addView(row)
        }
    }

    private fun previousStep() {
        val i = Intent(this, BasicInfoActivity::class.java)
        startActivity(i)
    }

    private fun nextStep() {
        val i = Intent(this, MapsActivity::class.java)
        startActivity(i)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Toast.makeText(this, "Configuration changed", Toast.LENGTH_LONG).show()
    }
}
