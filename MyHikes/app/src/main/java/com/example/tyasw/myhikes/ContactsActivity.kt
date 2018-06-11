package com.example.tyasw.myhikes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TableRow
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_contacts.*

/**
 * Program: MyHikes
 * Description: Organize hiking plans.
 * Author: William Tyas
 * Notes: Currently, the SMS feature has not been tested, so it is unknown
 *      whether it works or not. A data plan is required to send SMS messages,
 *      but the device this app was tested on did not have one.
 * Last Modified: 6/8/18
 */
class ContactsActivity : StepActivity() {
    private var names = ArrayList<TextView>()
    private var phoneNumbers = ArrayList<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        val actionBar = supportActionBar
        actionBar?.setLogo(R.mipmap.ic_launcher)

        val extras = intent.extras

        accountId = extras.getInt("accountId")
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

        contactsCancelButton.setOnClickListener {
            cancel(this)
        }

        contactsNextButton.setOnClickListener {
            nextStep()
        }

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
        setUpContactsList()
    }

    // Want to create from a list of contacts, not a list of names and phone numbers
    private fun setUpContactsList() {
        dbTable.removeAllViews()
        names.clear()
        phoneNumbers.clear()

        val columnTitles = TableRow(this)

        val newContacts = contacts

        if ((newContacts != null && !newContacts.isEmpty()) || !names.isEmpty()) {
            val nameColumn = TextView(this)
            val phoneColumn = TextView(this)

            nameColumn.text = "Name"
            phoneColumn.text = "Phone"

            val config = resources.configuration
            val screenWidth = config.screenWidthDp

            val colWidth = (pxToDP(screenWidth) / 2.5).toInt()

            nameColumn.width = colWidth
            nameColumn.maxWidth = colWidth
            phoneColumn.width = colWidth
            phoneColumn.maxWidth = colWidth

            nameColumn.layoutParams = setTableLayout(10, 10, 10, 0)
            phoneColumn.layoutParams = setTableLayout(10, 10, 10, 0)

            nameColumn.textSize = 22f
            phoneColumn.textSize = 22f

            columnTitles.addView(nameColumn)
            columnTitles.addView(phoneColumn)

            dbTable.addView(columnTitles)
        } else {
            val noResults = TextView(this)
            noResults.text = "No entries"
            noResults.layoutParams = setTableLayout(10, 10, 10, 10)
            noResults.textSize = 22f

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

                name.textSize = 22f
                phone.textSize = 22f

                name.id = names.lastIndex
                phone.id = phoneNumbers.lastIndex

                names.add(name)
                phoneNumbers.add(phone)

                row.addView(name)
                row.addView(phone)

                dbTable.addView(row)
            }
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

    // Elements are name, phone
    override fun setLayoutMargins(vararg elements: TextView) {
        //super.setLayoutMargins(*elements)
        setLayout()

        if (verticalDimensionsSet) {
            elements[0].layoutParams = setTableLayout(10, 10, 10, 10)
            elements[1].layoutParams = setTableLayout(10, 10, 10, 10)
        } else {
            elements[0].layoutParams = setTableLayout(10, 10, 10, 10)
            elements[1].layoutParams = setTableLayout(10, 10, 10, 10)
        }
    }

    override fun previousStep() {
        val i = Intent(this, MapsActivity::class.java)

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
        val i = Intent(this, NotificationActivity::class.java)

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

    companion object {
        private val CHOOSE_CONTACTS_CODE = 1
    }
}
