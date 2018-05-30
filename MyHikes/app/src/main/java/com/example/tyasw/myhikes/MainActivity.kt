package com.example.tyasw.myhikes

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : StepActivity() {
    val hikesList: ArrayList<TextView> = ArrayList<TextView>()
    val SAMPLE_USER_ID = 1

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

        //val dbHandler = MyDBHandler(this, null, null, 1)
        //dbHandler.deleteAllHikes(SAMPLE_USER_ID)
        populateHikesList()

        setLayoutMargins(buttonRow)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Toast.makeText(this, "Configuration changed", Toast.LENGTH_LONG).show()
    }

    private fun populateHikesList() {
        hikesList.clear()
        hikesTable.removeAllViews()

        val dbHandler = MyDBHandler(this, null, null, 1)
        val hikesList = dbHandler.findAllHikes(SAMPLE_USER_ID)

        if (hikesList.isEmpty()) {
            val row = TableRow(this)
            val noResults = TextView(this)
            noResults.text = "There are currently no hikes"
            noResults.layoutParams = setLayout(10, 0, 10, 0)
            noResults.textSize = 18f

            row.addView(noResults)
            hikesTable.addView(row)
        }

        for (hike in hikesList) {
            val row = TableRow(this)
            val hikeName = TextView(this)

            hikeName.text = hike.name
            hikeName.textSize = 18f
            hikeName.layoutParams = setLayout(10, 0, 10, 0)

            row.addView(hikeName)
            hikesTable.addView(row)
        }
    }

    private fun addNew() {
        val newHike = Hike(SAMPLE_USER_ID)

        val i = Intent(this, BasicInfoActivity::class.java)
        i.putExtra("isNewHike", true)
        i.putExtra("newHike", newHike)
        startActivity(i)
    }

    // Load from DB
    private fun nextStep() {
        val i = Intent(this, BasicInfoActivity::class.java)
        startActivity(i)
    }
}
