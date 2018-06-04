package com.example.tyasw.myhikes

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : StepActivity() {
    val hikesList: ArrayList<TextView> = ArrayList<TextView>()
    val SAMPLE_USER_ID = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar

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

        setButtonRowParameters(buttonRow)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Toast.makeText(this, "Configuration changed", Toast.LENGTH_LONG).show()
    }

    private fun populateHikesList() {
        hikesList.clear()
        hikesTable.removeAllViews()

//        val dbHandler = MyDBHandler(this, null, null, 1)
//        val hikesList = dbHandler.findAllHikes(SAMPLE_USER_ID)

        val hikesList = ArrayList<Hike>()
        val hike1 = Hike(SAMPLE_USER_ID, "Skyline Divide", 10.0, "Hard", 0.0, 0.0)
        val hike2 = Hike(SAMPLE_USER_ID, "Ptarmigan Ridge", 11.5, "Medium", 0.0, 0.0)
        val hike3 = Hike(SAMPLE_USER_ID, "Chain Lakes", 6.3, "Medium", 0.0, 0.0)
        val hike4 = Hike(SAMPLE_USER_ID, "Excelsior Ridge", 8.0, "Hard", 0.0, 0.0)
        val hike5 = Hike(SAMPLE_USER_ID, "Church Mountain", 8.5, "Hard", 0.0, 0.0)

        hikesList.add(hike1)
        hikesList.add(hike2)
        hikesList.add(hike3)
        hikesList.add(hike4)
        hikesList.add(hike5)

        if (hikesList.isEmpty()) {
            val row = TableRow(this)
            val noResults = TextView(this)
            noResults.text = "There are currently no hikes"

            noResults.layoutParams = setTableLayout(20, 20, 0, 0)
            //row.layoutParams = setTableLayout(20, 20, 0, 0)

            noResults.textSize = 18f

            row.addView(noResults)
            hikesTable.addView(row)
        }

        val radioHikesList = RadioGroup(this)
        for (hike in hikesList) {
            //val row = TableRow(this)
            val radioButton = RadioButton(this)
            radioButton.id = hike.id
            radioButton.text = hike.name
            radioButton.textSize = 18f
            radioButton.layoutParams = setRadioButtonLayout(20, 20, 0, 0)

            radioHikesList.addView(radioButton)
        }
        hikesTable.addView(radioHikesList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            5 -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addNew() {
        val newHike = Hike(SAMPLE_USER_ID)
        val supplies = ArrayList<Supply>()
        val contacts = ArrayList<Contact>()

        val i = Intent(this, BasicInfoActivity::class.java)
        i.putExtra("isNewHike", true)
        i.putExtra("hike", newHike)
        i.putExtra("supplies", supplies)
        i.putExtra("contacts", contacts)
        startActivity(i)
    }

    // Load from DB
    private fun nextStep() {
        val dbHandler = MyDBHandler(this, null, null, 1)

        // Get hike name from the radio button that is selected
        val oldHike = dbHandler.findHike(SAMPLE_USER_ID, "enter hike name here")
        val supplies = dbHandler.findAllSupplies(oldHike!!.id)
        val contacts = dbHandler.findAllContacts(oldHike!!.id)

        val i = Intent(this, BasicInfoActivity::class.java)
        i.putExtra("isNewHike", false)
        i.putExtra("hike", oldHike)
        i.putExtra("supplies", supplies)
        i.putExtra("contacts", contacts)
        startActivity(i)
    }
}
