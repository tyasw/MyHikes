package com.example.tyasw.myhikes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_basic_info.*

/**
 * Program: MyHikes
 * Description: Organize hiking plans.
 * Author: William Tyas
 * Notes: Currently, the SMS feature has not been tested, so it is unknown
 *      whether it works or not. A data plan is required to send SMS messages,
 *      but the device this app was tested on did not have one.
 * Last Modified: 6/8/18
 */
class BasicInfoActivity : StepActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_info)

        val actionBar = supportActionBar
        actionBar?.setLogo(R.mipmap.ic_launcher)

        val extras = intent.extras

        accountId = extras.getInt("accountId")
        isNewHike = extras.getBoolean("isNewHike")

        receivedHike = intent.getParcelableExtra<Hike>("hike") ?: null
        supplies = intent.getParcelableArrayListExtra<Supply>("supplies") ?: null
        contacts = intent.getParcelableArrayListExtra<Contact>("contacts") ?: null

        populateInfo(receivedHike)

        basicPreviousButton.setOnClickListener {
            previousStep()
        }

        basicCancelButton.setOnClickListener {
            cancel(this)
        }

        basicNextButton.setOnClickListener {
            nextStep()
        }

        setButtonRowParameters(buttonRow)
    }

    fun populateInfo(hike: Hike?) {
        if (hike != null) {
            hikeName.setText(hike.name.toString())
            length.setText(hike.length.toString())
            difficulty.setText(hike.difficulty.toString())
        }
    }

    override fun previousStep() {
        // Save all information user entered
        receivedHike?.let {
            it.name = hikeName.text.toString()
            it.length = length.text.toString().toDouble()
            it.difficulty = difficulty.text.toString()
        }

        val i = Intent(this, MainActivity::class.java)

        i.putExtra("accountId", accountId)

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

    override fun nextStep() {
        // Save all information user entered
        if (length.text.toString() == "") {
            Toast.makeText(this, "Must enter a valid length", Toast.LENGTH_LONG).show()
        } else {
            receivedHike?.let {
                it.name = hikeName.text.toString()
                it.length = length.text.toString().toDouble()
                it.difficulty = difficulty.text.toString()
            }

            val i = Intent(this, SuppliesActivity::class.java)

            i.putExtra("accountId", accountId)

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
