package com.example.tyasw.myhikes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Program: MyHikes
 * Description: Organize hiking plans.
 * Author: William Tyas
 * Notes: Currently, the SMS feature has not been tested, so it is unknown
 *      whether it works or not. A data plan is required to send SMS messages,
 *      but the device this app was tested on did not have one.
 * Last Modified: 6/8/18
 */
class MainActivity : StepActivity() {
    private var hikesList: ArrayList<Hike> = ArrayList<Hike>()
    private var oldHikeName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val extras = intent.extras

        accountId = extras.getInt("accountId")

        val actionBar = supportActionBar
        actionBar?.setLogo(R.mipmap.ic_launcher)

        mainAddButton.setOnClickListener {
            addNew()
        }

        mainNextButton.setOnClickListener {
            nextStep()
        }

        populateHikesList()

        setButtonRowParameters(buttonRow)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)

        savedInstanceState.putInt("accountId", accountId)

        if (isNewHike) {
            savedInstanceState.putBoolean("isNewHike", true)
        } else {
            savedInstanceState.putBoolean("isNewHike", false)
        }

        savedInstanceState.putParcelable("hike", receivedHike)
        savedInstanceState.putParcelableArrayList("supplies", supplies)
        savedInstanceState.putParcelableArrayList("contacts", contacts)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        accountId = savedInstanceState.getInt("accountId")
        isNewHike = savedInstanceState.getBoolean("isNewHike")
        receivedHike = savedInstanceState.getParcelable("hike")
        supplies = savedInstanceState.getParcelableArrayList("supplies")
        contacts = savedInstanceState.getParcelableArrayList("contacts")
    }

    private fun populateHikesList() {
        hikesList.clear()
        hikesTable.removeAllViews()

        val dbHandler = MyDBHandler(this, null, null, 1)
        val hikes = dbHandler.findAllHikes(accountId)

        if (hikes.isEmpty()) {
            val row = TableRow(this)
            val noResults = TextView(this)
            noResults.text = "There are currently no hikes"

            noResults.layoutParams = setTableLayout(20, 20, 0, 0)

            noResults.textSize = 18f

            row.addView(noResults)
            hikesTable.addView(row)
        }

        val radioHikesList = RadioGroup(this)
        for (hike in hikes) {
            val radioButton = RadioButton(this)
            radioButton.id = hike.id
            radioButton.text = hike.name
            radioButton.textSize = 18f
            radioButton.layoutParams = setRadioButtonLayout(20, 20, 0, 0)

            radioHikesList.addView(radioButton)
        }

        radioHikesList.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i -> getSelectedItem(radioGroup, i)})
        hikesTable.addView(radioHikesList)
    }

    private fun getSelectedItem(radioGroup: RadioGroup, i: Int) {
        val selectedId = radioGroup.checkedRadioButtonId
        val radio: RadioButton = findViewById(selectedId)
        oldHikeName = radio.text.toString()
    }

    private fun addNew() {
        val newHike = Hike(accountId)
        val supplies = ArrayList<Supply>()
        val contacts = ArrayList<Contact>()

        val i = Intent(this, BasicInfoActivity::class.java)
        i.putExtra("accountId", accountId)
        i.putExtra("isNewHike", true)
        i.putExtra("hike", newHike)
        i.putExtra("supplies", supplies)
        i.putExtra("contacts", contacts)
        startActivity(i)
    }

    // Load from DB
    override fun nextStep() {
        val dbHandler = MyDBHandler(this, null, null, 1)

        if (oldHikeName != "") {
            // Get hike name from the radio button that is selected
            val oldHike = dbHandler.findHike(accountId, oldHikeName)
            val supplies = dbHandler.findAllSupplies(oldHike!!.id)
            val contacts = dbHandler.findAllContacts(oldHike!!.id)

            val i = Intent(this, BasicInfoActivity::class.java)
            i.putExtra("accountId", accountId)
            i.putExtra("isNewHike", false)
            i.putExtra("hike", oldHike)
            i.putExtra("supplies", supplies)
            i.putExtra("contacts", contacts)
            startActivity(i)
        } else {
            Toast.makeText(this, "Select a hike first, or create a new one.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_help -> {
                displayHelpBox(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
