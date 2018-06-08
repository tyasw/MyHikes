package com.example.tyasw.myhikes

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintSet
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import javax.crypto.Cipher.ENCRYPT_MODE
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * Program: MyHikes
 * Description: Organize hiking plans.
 * Author: William Tyas
 * Notes: Currently, the SMS feature has not been tested, so it is unknown
 *      whether it works or not. A data plan is required to send SMS messages,
 *      but the device this app was tested on did not have one.
 * Last Modified: 6/8/18
 */
class LoginActivity : StepActivity() {
    private var aes: AdvEncryptionStand = AdvEncryptionStand()
    private var MASTER_KEY: SecretKey? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val actionBar = supportActionBar
        actionBar?.setLogo(R.mipmap.ic_launcher)

        MASTER_KEY = getEncryptionKey()

        aes = AdvEncryptionStand()

        createAccountButton.setOnClickListener {
            createAccount()
        }

        loginButton.setOnClickListener {
            login()
        }

        // Play animation after 1 second
        val handler: Handler = Handler()
        handler.postDelayed(object: Runnable {
            override fun run() {
                playAnimation()
            }
        }, 1000)
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

    // Get the key used for encryption from the database
    // If it doesn't exist, create a new one
    private fun getEncryptionKey(): SecretKey {
        var secretKey: SecretKey? = null

        val dbHandler = MyDBHandler(this, null, null, 1)
        val key = dbHandler.getKey()
        if (key != null) {
            val decodedKey: ByteArray = Base64.decode(key, Base64.DEFAULT)
            secretKey = SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")
        } else {
            aes = AdvEncryptionStand()
            secretKey = aes.getKey()
            val encodedKey: String = Base64.encodeToString(aes.getKey().encoded, Base64.DEFAULT)
            dbHandler.addKey(encodedKey)
        }

        return secretKey
    }

    // Play animation of app logo increasing in size
    private fun playAnimation() {
        val transition = ChangeBounds()
        transition.setDuration(1000L)

        TransitionManager.beginDelayedTransition(imageViewLayout, transition)

        logoImageView.minimumWidth = 500
        logoImageView.minimumHeight = 500

        val set = ConstraintSet()
        set.clone(imageViewLayout)
        set.applyTo(imageViewLayout)
    }

    // Look up account in the database
    private fun login() {
        val encryptedPassword = encryptPassword(password.text.toString())

        val dbHandler = MyDBHandler(this, null, null, 1)
        val id = dbHandler.findAccount(userName.text.toString(), encryptedPassword!!)

        if (id >= 0) {
            // Pass account_id to main activity
            val i = Intent(this, MainActivity::class.java)
            i.putExtra("accountId", id)
            startActivity(i)
        } else {
            Toast.makeText(this, "Username or password is incorrect.", Toast.LENGTH_LONG).show()
        }
    }

    // Create new entry in the database
    private fun createAccount() {
        val encryptedPassword = encryptPassword(password.text.toString())

        val dbHandler = MyDBHandler(this, null, null, 1)
        val doesAccountAlreadyExist = dbHandler.doesAccountNameExist(userName.text.toString())

        if (!doesAccountAlreadyExist) {
            val newAccount = Account(userName.text.toString(), encryptedPassword!!)
            dbHandler.addAccount(newAccount)
            val id = dbHandler.findAccount(userName.text.toString(), encryptedPassword!!)
            val i = Intent(this, MainActivity::class.java)
            i.putExtra("accountId", id)
            startActivity(i)
        } else {
            Toast.makeText(this, "Username already exists. Please choose another.", Toast.LENGTH_LONG).show()
        }
    }

    private fun encryptPassword(plaintext: String): String? {
        return aes.crypt(ENCRYPT_MODE, plaintext, MASTER_KEY!!)
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
