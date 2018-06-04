package com.example.tyasw.myhikes

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.widget.TextViewCompat
import android.widget.LinearLayout
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

        setUpSuppliesList()

        suppliesPreviousButton.setOnClickListener {
            previousStep()
        }

        suppliesNextButton.setOnClickListener {
            nextStep()
        }

        addSupplyButton.setOnClickListener {
            addSupply()
        }

        setButtonRowParameters(buttonRow)
    }

    // Populate the list of supplies if items already exist, or create a new one
    private fun setUpSuppliesList() {
        dbTable.removeAllViews()

        val columnTitles = TableRow(this)

        if (!names.isEmpty()) {
            val nameColumn = TextView(this)
            val quantityColumn = TextView(this)

            nameColumn.text = "Name"
            quantityColumn.text = "Quantity"

            val config = resources.configuration
            val screenWidth = config.screenWidthDp

            val colWidth = (pxToDP(screenWidth) / 2.5).toInt()

            nameColumn.width = colWidth
            nameColumn.maxWidth = colWidth
            quantityColumn.width = colWidth
            quantityColumn.maxWidth = colWidth

            nameColumn.layoutParams = setTableLayout(10, 10, 10, 0)
            quantityColumn.layoutParams = setTableLayout(10, 10, 10, 0)

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

        for (i in names.indices) {
            val row = TableRow(this)
            val name = TextView(this)
            val quantity = TextView(this)

            name.text = names[i].text
            quantity.text = quantities[i].text

            setLayoutMargins(name, quantity)

            name.textSize = 18f
            quantity.textSize = 18f

            name.id = names.lastIndex
            quantity.id = quantities.lastIndex

            row.addView(name)
            row.addView(quantity)

            dbTable.addView(row)
        }
    }

    private fun addSupply() {
        val name = TextView(this)
        val quantity = TextView(this)

        name.text = supplyName.text.toString()
        quantity.text = supplyQuantity.text.toString()

        setLayoutMargins(name, quantity)

        name.textSize = 18f
        quantity.textSize = 18f

        names.add(name)
        quantities.add(quantity)

        supplyName.text.clear()
        supplyQuantity.text.clear()

        setUpSuppliesList()
    }

    override fun setLayoutMargins(vararg elements: TextView) {
        //super.setLayoutMargins(elements)

        if (verticalDimensionsSet) {
            elements[0].layoutParams = setTableLayout(10, 10, 10, 10)
            elements[1].layoutParams = setTableLayout(10, 10, 10, 10)
        } else {
            elements[0].layoutParams = setTableLayout(10, 10, 10, 10)
            elements[1].layoutParams = setTableLayout(10, 10, 10, 10)
        }
    }

    private fun previousStep() {
        supplies?.clear()

        for (i in names.indices) {
            val name = names[i].text.toString()
            val quantity = quantities[i].text.toString().toDouble()

            var supply: Supply? = null
            if (isNewHike) {
                supply = Supply(name, quantity)
            } else {
                // set hikeId
                //supply = Supply(id, name, quantity)
            }
            if (supply != null) {
                supplies?.add(supply)
            }
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
            val name = names[i].text.toString()
            val quantity = quantities[i].text.toString().toDouble()

            var supply: Supply? = null
            if (isNewHike) {
                supply = Supply(name, quantity)
            } else {
                // set hikeId
                //supply = Supply(id, name, quantity)
            }
            if (supply != null) {
                supplies?.add(supply)
            }
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
