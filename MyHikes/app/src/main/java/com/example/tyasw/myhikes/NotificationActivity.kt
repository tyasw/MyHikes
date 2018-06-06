package com.example.tyasw.myhikes

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : StepActivity() {
    private var receivedHike: Hike? = null
    private var supplies: ArrayList<Supply>? = null
    private var contacts: ArrayList<Contact>? = null
    private var isNewHike: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val extras = intent.extras

        isNewHike = extras.getBoolean("isNewHike")

        receivedHike = intent.getParcelableExtra<Hike>("hike") ?: null
        supplies = intent.getParcelableArrayListExtra<Supply>("supplies") ?: null
        contacts = intent.getParcelableArrayListExtra<Contact>("contacts") ?: null

        populateInfo(receivedHike)

        notificationPreviousButton.setOnClickListener {
            previousStep()
        }

        notificationFinishButton.setOnClickListener {
            finishStep()
        }

        setButtonRowParameters(buttonRow)
    }

    private fun populateInfo(hike: Hike?) {
        if (hike != null) {

        }
    }

    private fun previousStep() {
        val i = Intent(this, ContactsActivity::class.java)

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

    private fun finishStep() {
        updateDatabase()

        // Send text

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

    private fun updateDatabase() {
        val dbHandler = MyDBHandler(this, null, null, 1)
        if (isNewHike) {        // Add new entries
            dbHandler.addHike(receivedHike!!)

            val hikeEntry = dbHandler.findHike(1, receivedHike!!.name)

            val hikeId = hikeEntry?.id

            for (i in supplies!!.indices) {
                supplies!![i].hikeId = hikeId!!.toInt()
                dbHandler.addSupply(supplies!![i])
            }

            for (i in contacts!!.indices) {
                contacts!![i].hikeId = hikeId!!.toInt()
                dbHandler.addContact(contacts!![i])
            }
        } else {    // update old entries and create new ones as necessary
            dbHandler.modifyHike(receivedHike!!)
            val hikeEntry = dbHandler.findHike(1, receivedHike!!.name)

            // Just delete old entries for now, much easier than updating
            val hikeId = hikeEntry?.id
            if (hikeEntry != null) {
                dbHandler.deleteAllSupplies(hikeId!!)
                dbHandler.deleteAllContacts(hikeId!!)
            }

            for (i in supplies!!.indices) {
                supplies!![i].hikeId = hikeId!!.toInt()
                dbHandler.addSupply(supplies!![i])
            }

            for (i in contacts!!.indices) {
                contacts!![i].hikeId = hikeId!!.toInt()
                dbHandler.addContact(contacts!![i])
            }
        }
    }
}
