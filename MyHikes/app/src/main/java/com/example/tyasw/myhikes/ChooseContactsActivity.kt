package com.example.tyasw.myhikes

import android.support.v4.content.ContextCompat
import android.os.Bundle
import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.os.PersistableBundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.CheckBox
import android.widget.TableRow
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_choose_contacts.*

class ChooseContactsActivity : StepActivity() {
    private var contacts: ArrayList<Contact>? = ArrayList<Contact>()
    private var entries = ArrayList<CheckBox>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_contacts)

        chooseContactsDoneButton.setOnClickListener {
            done()
        }

        populateContactsList()
    }

    // Save list of checkboxes in list of contacts
    override fun onSaveInstanceState(outState: Bundle) {
        contacts?.clear()

        for (i in entries.indices) {
            val contact = parseContactEntry(entries[i].text.toString())
            contacts?.add(contact)
        }

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun populateContactsList() {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)

        if (permission == PackageManager.PERMISSION_GRANTED) {
            contacts = loadContacts()           // Only check the first time, when restoring data, just populateListOfCheckBoxes(contacts)
            populateListOfCheckBoxes(contacts)
        } else {
            requestPermission(Manifest.permission.READ_CONTACTS,
                    PERMISSIONS_REQUEST_READ_CONTACTS)
        }
    }

    // Populates the list of contacts
    // Source: https://medium.com/@manuaravindpta/fetching-contacts-from-device-using-kotlin-6c6d3e76574f
    // Accessed 6/5/18
    private fun loadContacts(): ArrayList<Contact>? {
        var contactsList: ArrayList<Contact>? = null

        val resolver: ContentResolver = contentResolver
        val cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null)

        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)).toInt()

                if (phoneNumber > 0) {
                    val cursorPhone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            arrayOf(id),
                            null)

                    if (cursorPhone.count > 0) {
                        contactsList = ArrayList<Contact>()
                        while (cursorPhone.moveToNext()) {
                            val phoneNumValue = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            //builder.append("Contact: ").append(name).append(", Phone Number: ").append(phoneNumValue).append("\n\n")
                            Log.d("Name ===>", phoneNumValue)

                            val contact = Contact(name, phoneNumValue)
                            contactsList?.add(contact)
                        }
                    }
                    cursorPhone.close()
                }
            }
        } else {
            Toast.makeText(this, "No contacts available", Toast.LENGTH_LONG).show()
        }
        cursor.close()
        return contactsList
    }

    private fun populateListOfCheckBoxes(contacts: ArrayList<Contact>?) {
        if (contacts != null) {
            for (i in contacts.indices) {
                val row = TableRow(this)
                val entry = CheckBox(this)
                entry.text = contacts[i].name + " " + contacts[i].phone

                //setLayoutMargins(entry)
                entry.layoutParams = setTableLayout(10, 10, 10, 10)

                entry.textSize = 18f

                entry.id = contacts.lastIndex

                entries.add(entry)

                row.addView(entry)

                dbTable.addView(row)
            }
        }
    }

    private fun requestPermission(permissionType: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permissionType), requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    contacts = loadContacts()
                    populateListOfCheckBoxes(contacts)
                } else {
                    Toast.makeText(this, "Unable to retrieve contacts - permission required",
                            Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Transform a string containing contact information into a Contact
    private fun parseContactEntry(entry: String): Contact {
        var name = ""
        var phone = ""

        // Split name on space, then create new string only containing contact name
        val entryStringArray = entry.split(" ")

        var i = 0
        var word = entryStringArray[i]
        while (!word.startsWith("(")) {
            name = name + " " + word
            i++
            word = entryStringArray[i]
        }

        while (i < entryStringArray.size) {
            word = entryStringArray[i]
            phone = phone + " " + word
            i++
        }
        Log.d("ABC", phone)

        return Contact(name, phone)
    }

    private fun done() {
        contacts?.clear()

        // Get all selected checkboxes and add them to contacts list
        for (i in entries.indices) {
            if (entries[i].isChecked()) {
                val contact = parseContactEntry(entries[i].text.toString())
                contacts?.add(contact)
            }
        }

        finish()
    }

    override fun finish() {
        val i = Intent()

        i.putExtra("returnedContacts", contacts)
        setResult(RESULT_OK, i)
        super.finish()
    }

    companion object {
        val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }
}
