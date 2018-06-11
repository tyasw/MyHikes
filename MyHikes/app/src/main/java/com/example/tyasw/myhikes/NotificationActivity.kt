package com.example.tyasw.myhikes

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.SmsManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_notification.*

/**
 * Program: MyHikes
 * Description: Organize hiking plans.
 * Author: William Tyas
 * Notes: Currently, the SMS feature has not been tested, so it is unknown
 *      whether it works or not. A data plan is required to send SMS messages,
 *      but the device this app was tested on did not have one.
 * Last Modified: 6/8/18
 */

// Code sending sms messages came from https://android.jlelse.eu/detecting-sending-sms-on-android-8a154562597f,
// https://www.ssaurel.com/blog/how-to-send-and-receive-sms-in-android/,
// and http://xadkile.blogspot.com/2017/09/this-post-gives-examples-of-sending-sms.html
class NotificationActivity : StepActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val actionBar = supportActionBar
        actionBar?.setLogo(R.mipmap.ic_launcher)

        val extras = intent.extras

        accountId = extras.getInt("accountId")
        isNewHike = extras.getBoolean("isNewHike")

        receivedHike = intent.getParcelableExtra<Hike>("hike") ?: null
        supplies = intent.getParcelableArrayListExtra<Supply>("supplies") ?: null
        contacts = intent.getParcelableArrayListExtra<Contact>("contacts") ?: null

        sendTextButton.setOnClickListener {
            send()
        }

        notificationPreviousButton.setOnClickListener {
            previousStep()
        }

        notificationCancelButton.setOnClickListener {
            cancel(this)
        }

        notificationFinishButton.setOnClickListener {
            finishStep()
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
    }

    override fun previousStep() {
        val i = Intent(this, ContactsActivity::class.java)

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

    private fun finishStep() {
        updateDatabase()

        val i = Intent(this, MainActivity::class.java)

        i.putExtra("accountId", accountId)

        startActivity(i)
    }

    private fun updateDatabase() {
        val dbHandler = MyDBHandler(this, null, null, 1)
        if (isNewHike) {        // Add new entries
            dbHandler.addHike(receivedHike!!)

            val hikeEntry = dbHandler.findHike(accountId, receivedHike!!.name)

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

            val hikeEntry = dbHandler.findHikeById(receivedHike!!.id)

            //val hikeEntry = dbHandler.findHike(accountId, receivedHike!!.name)
            Log.d("NotificationActivity", "Hike id is " + hikeEntry?.id)


            // Just delete old entries for now, much easier than updating
            //val hikeId = hikeEntry?.id
            if (hikeEntry != null) {
                dbHandler.deleteAllSupplies(hikeEntry.id)
                dbHandler.deleteAllContacts(hikeEntry.id)
            }

//            if (supplies == null) {
//                Log.d("NotificationActivity", "Supplies is null")
//            } else {
//                Log.d("NotificationActivity", "Supplies has size " + supplies?.size)
//            }

            for (i in supplies!!.indices) {
                supplies!![i].hikeId = hikeEntry!!.id
                dbHandler.addSupply(supplies!![i])
            }

            if (contacts == null) {
                Log.d("NotificationActivity", "Contacts is null")
            } else {
                Log.d("NotificationActivity", "Contacts has size " + contacts?.size)
            }

            for (i in contacts!!.indices) {
                contacts!![i].hikeId = hikeEntry!!.id
                dbHandler.addContact(contacts!![i])
            }
        }
    }

    private fun sendGroupText() {
        // Create list of phone numbers to send text to
        if (contacts != null) {
            val newContacts = contacts!!
            val messageString = message.text.toString()
            for (i in newContacts.indices) {
                sendText(newContacts[i], messageString)
            }
        }
    }

    private fun sendText(contact: Contact, messageString: String) {
        val smsBroadcastReceiver = SMSBroadcastReceiver(this)
        val sentIntentFilter = IntentFilter(SENT)
        sentIntentFilter.addAction(DELIVERED)
        registerReceiver(smsBroadcastReceiver, sentIntentFilter)

        val pendingIntentSent = PendingIntent.getBroadcast(applicationContext, 0, Intent(SENT), 0)
        val pendingIntentDelivered = PendingIntent.getBroadcast(applicationContext, 0, Intent(DELIVERED), 0)

        val smsManager = SmsManager.getDefault()
        val length = message.text.toString().length

        if (length > MAX_SMS_MESSAGE_LENGTH) {
            val messagelist: ArrayList<String> = smsManager.divideMessage(messageString)
            smsManager.sendMultipartTextMessage(contact.phone, null, messagelist, null, null)
        } else {
            smsManager.sendTextMessage(contact.phone, null, messageString, pendingIntentSent, pendingIntentDelivered)
        }
    }

    private fun send() {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)

        if (permission == PackageManager.PERMISSION_GRANTED) {
            sendGroupText()
        } else {
            requestPermission(Manifest.permission.SEND_SMS)
        }
    }

    private fun requestPermission(permissionType: String) {
        ActivityCompat.requestPermissions(this, arrayOf(permissionType), SMS_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            SMS_PERMISSION_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendGroupText()
                } else {
                    Toast.makeText(this, "Unable to send text - permission required",
                            Toast.LENGTH_LONG).show()
                }
            }
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

    companion object {
        private val SENT = "SMS_SENT"
        private val DELIVERED = "SMS_DELIVERED"
        private val MAX_SMS_MESSAGE_LENGTH = 160
        private val SMS_PERMISSION_CODE = 101
    }
}
