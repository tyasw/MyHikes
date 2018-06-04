package com.example.tyasw.myhikes

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_contacts.*

class ContactsActivity : StepActivity() {
    private var receivedHike: Hike? = null
    private var supplies: ArrayList<Supply>? = null
    private var contacts: ArrayList<Contact>? = null
    private var isNewHike: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        val extras = intent.extras

        isNewHike = extras.getBoolean("isNewHike")

        receivedHike = intent.getParcelableExtra<Hike>("hike") ?: null
        supplies = intent.getParcelableArrayListExtra<Supply>("supplies") ?: null
        contacts = intent.getParcelableArrayListExtra<Contact>("contacts") ?: null

        setUpContactsList()

        contactsPreviousButton.setOnClickListener {
            previousStep()
        }

        contactsNextButton.setOnClickListener {
            nextStep()
        }

        setButtonRowParameters(buttonRow)
    }

    private fun setUpContactsList() {
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Toast.makeText(this, "Configuration changed", Toast.LENGTH_LONG).show()
    }

    private fun previousStep() {
        contacts?.clear()

//        for (i in 0..names.size) {
//            val name = names.get(i).text.toString()
//            val quantity = quantities.get(i).text.toString().toDouble()
//
//            val supply = Supply(name, quantity)
//            supplies?.add(supply)
//        }

        val i = Intent(this, MapsActivity::class.java)

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
        contacts?.clear()

//        for (i in 0..names.size) {
//            val name = names.get(i).text.toString()
//            val quantity = quantities.get(i).text.toString().toDouble()
//
//            val supply = Supply(name, quantity)
//            supplies?.add(supply)
//        }

        val i = Intent(this, NotificationActivity::class.java)

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
