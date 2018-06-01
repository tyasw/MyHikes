package com.example.tyasw.myhikes

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.widget.TextViewCompat
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_supplies.*

class SuppliesActivity : StepActivity() {
    private var receivedHike: Hike? = null
    private var contacts: ArrayList<Contact>? = null
    private var supplies: ArrayList<Supply>? = null
    private var isNewHike: Boolean = true

    private var names = ArrayList<TextView>()
    private var quantities = ArrayList<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplies)

        // Check DB to see if this is a preexisting hike, populate list if it is
        // else have blank list of supplies
        val extras = intent.extras

        isNewHike = extras.getBoolean("isNewHike")

        receivedHike = intent.getParcelableExtra<Hike>("newHike") ?: null
        supplies = intent.getParcelableArrayListExtra<Supply>("supplies") ?: null
        contacts = intent.getParcelableArrayListExtra<Contact>("contacts") ?: null

        if (isNewHike) {
            receivedHike?.let { receivedHike -> setUpSuppliesList(receivedHike) }
        }

        suppliesPreviousButton.setOnClickListener {
            previousStep()
        }

        suppliesNextButton.setOnClickListener {
            nextStep()
        }

        setButtonRowParameters(buttonRow)
    }

    // Populate the list of supplies if items already exist, or create a new one
    private fun setUpSuppliesList(hike: Hike?) {
        names.clear()
        quantities.clear()

        dbTable.removeAllViews()

        if (hike != null) {         // Unnecessary, but Kotlin demands it
            val dbHandler = MyDBHandler(this, null, null, 1)
            val suppliesList = dbHandler.findAllSupplies(hike.id)

            val columnTitles = TableRow(this)

            if (!suppliesList.isEmpty()) {  // Create column titles
                val nameColumn = TextView(this)
                val quantityColumn = TextView(this)

                nameColumn.text = "Name"
                quantityColumn.text = "Quantity"

                setLayoutMargins(nameColumn, quantityColumn)

                nameColumn.textSize = 18f
                quantityColumn.textSize = 18f

                columnTitles.addView(nameColumn)
                columnTitles.addView(quantityColumn)

                dbTable.addView(columnTitles)
            } else {
                val noResults = TextView(this)
                noResults.text = "No entries"
                noResults.layoutParams = setTableLayout(10, 0, 10, 0)
                noResults.textSize = 18f

                columnTitles.addView(noResults)
                dbTable.addView(columnTitles)
            }

            // Populate supplies list
            for (item: Supply in suppliesList) {
                val row = TableRow(this)
                val name = TextView(this)
                val quantity = TextView(this)

                name.text = item.name
                quantity.text = item.quantity.toString()

                name.textSize = 18f
                quantity.textSize = 18f

                setLayoutMargins(name, quantity)

                row.addView(name)
                row.addView(quantity)

                // Maybe set ids of items

                dbTable.addView(row)
            }
        }
    }

    override fun setLayoutMargins(vararg elements: TextView) {
        //super.setLayoutMargins(elements)

        if (verticalDimensionsSet) {
            elements[0].layoutParams =  setTableLayout(10, 0, 10, 0)
            elements[1].layoutParams = setTableLayout(10, 0, 10, 0)
        } else {
            elements[0].layoutParams = setTableLayout(20, 0, 10, 0)
            elements[1].layoutParams = setTableLayout(10, 0, 10, 0)
        }
    }

    private fun previousStep() {
        supplies?.clear()

        for (i in names.indices) {
            val name = names.get(i).text.toString()
            val quantity = quantities.get(i).text.toString().toDouble()

            val supply = Supply(name, quantity)
            supplies?.add(supply)
        }

        val i = Intent(this, BasicInfoActivity::class.java)

        if (isNewHike) {
            i.putExtra("isNewHike", true)
        } else {
            i.putExtra("isNewHike", false)
        }

        i.putExtra("newHike", receivedHike)
        i.putExtra("supplies", supplies)
        i.putExtra("contacts", contacts)

        startActivity(i)
    }

    private fun nextStep() {
        supplies?.clear()

        for (i in names.indices) {
            val name = names.get(i).text.toString()
            val quantity = quantities.get(i).text.toString().toDouble()

            val supply = Supply(name, quantity)
            supplies?.add(supply)
        }

        val i = Intent(this, MapsActivity::class.java)

        if (isNewHike) {
            i.putExtra("isNewHike", true)
        } else {
            i.putExtra("isNewHike", false)
        }

        i.putExtra("newHike", receivedHike)
        i.putExtra("supplies", supplies)
        i.putExtra("contacts", contacts)

        startActivity(i)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Toast.makeText(this, "Configuration changed", Toast.LENGTH_LONG).show()
    }
}
