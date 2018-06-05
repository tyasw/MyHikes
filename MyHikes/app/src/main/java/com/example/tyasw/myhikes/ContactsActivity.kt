package com.example.tyasw.myhikes

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_contacts.*

class ContactsActivity : StepActivity() {
    private var receivedHike: Hike? = null
    private var supplies: ArrayList<Supply>? = null
    private var contacts: ArrayList<Contact>? = null
    private var isNewHike: Boolean = true

    private var names = ArrayList<TextView>()
    private var phoneNumbers = ArrayList<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        val extras = intent.extras

        isNewHike = extras.getBoolean("isNewHike")

        receivedHike = intent.getParcelableExtra<Hike>("hike") ?: null
        supplies = intent.getParcelableArrayListExtra<Supply>("supplies") ?: null
        contacts = intent.getParcelableArrayListExtra<Contact>("contacts") ?: null

        setUpContactsList()

        chooseContactsButton.setOnClickListener {
            chooseContacts()
        }

        contactsPreviousButton.setOnClickListener {
            previousStep()
        }

        contactsNextButton.setOnClickListener {
            nextStep()
        }

        setButtonRowParameters(buttonRow)
    }

    private fun setUpContactsList() {
        dbTable.removeAllViews()

        val columnTitles = TableRow(this)

        val newContacts = contacts

        if (newContacts == null || newContacts.isEmpty()) {
            val noResults = TextView(this)
            noResults.text = "No entries"
            noResults.layoutParams = setTableLayout(10, 10, 10, 10)
            noResults.textSize = 18f

            columnTitles.addView(noResults)
            dbTable.addView(columnTitles)
        }

        if (newContacts != null) {
            for (i in newContacts.indices) {
                val row = TableRow(this)
                val name = TextView(this)
                val phone = TextView(this)

                name.text = newContacts[i].name.toString()
                phone.text = newContacts[i].phone.toString()

                setLayoutMargins(name, phone)

                name.textSize = 18f
                phone.textSize = 18f

                name.id = names.lastIndex
                phone.id = phoneNumbers.lastIndex

                names.add(name)
                phoneNumbers.add(phone)

                row.addView(name)
                row.addView(phone)
                dbTable.addView(row)
            }
            newContacts.clear()
        }
    }

    private fun chooseContacts() {
        val i = Intent(this, ChooseContactsActivity::class.java)
        startActivityForResult(i, CHOOSE_CONTACTS_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if ((requestCode == CHOOSE_CONTACTS_CODE) && (resultCode == RESULT_OK)) {
            if (data.hasExtra("returnedContacts")) {
                contacts = data.extras.getParcelableArrayList("returnedContacts")
                setUpContactsList()
            }
        }
    }

    private fun setLayout() {
        if (verticalDimensionsSet) {
            val chooseContactsRowParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0, 1f)
            chooseContactsRow.layoutParams = chooseContactsRowParams

            val mainContentRowParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0, 14f)
            mainContentRow.layoutParams = mainContentRowParams

            val buttonRowParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0, 1f)
            buttonRow.layoutParams = buttonRowParams
        } else {
            val chooseContactsRowParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0, 2f)
            chooseContactsRow.layoutParams = chooseContactsRowParams

            val mainContentRowParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0, 10f)
            mainContentRow.layoutParams = mainContentRowParams

            val buttonRowParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0, 1f)
            buttonRow.layoutParams = buttonRowParams
        }
    }

    // Elements are name
    override fun setLayoutMargins(vararg elements: TextView) {
        //super.setLayoutMargins(*elements)
        setLayout()

        if (verticalDimensionsSet) {
            elements[0].layoutParams = setTableLayout(10, 10, 10, 10)
        } else {
            elements[0].layoutParams = setTableLayout(10, 10, 10, 10)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Toast.makeText(this, "Configuration changed", Toast.LENGTH_LONG).show()
    }

    private fun previousStep() {
        contacts?.clear()

        for (i in names.indices) {
            val name = names[i].text.toString()
            val phone = phoneNumbers[i].text.toString()

            val contact = Contact(name, phone)
            contacts?.add(contact)
        }

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

        for (i in names.indices) {
            val name = names[i].text.toString()
            val phone = phoneNumbers[i].text.toString()

            val contact = Contact(name, phone)
            contacts?.add(contact)
        }

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

    companion object {
        private val CHOOSE_CONTACTS_CODE = 1
    }
}
