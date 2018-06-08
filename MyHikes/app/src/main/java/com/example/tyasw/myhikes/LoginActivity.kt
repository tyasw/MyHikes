package com.example.tyasw.myhikes

import android.util.Base64

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintSet
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.tyasw.myhikes.R.layout
import kotlinx.android.synthetic.main.activity_login.*
import java.io.*
import java.nio.Buffer
import java.util.*
import javax.crypto.Cipher.DECRYPT_MODE
import javax.crypto.Cipher.ENCRYPT_MODE
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class LoginActivity : StepActivity() {
    private var aes: AdvEncryptionStand = AdvEncryptionStand()
    private var MASTER_KEY: SecretKey? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
}
