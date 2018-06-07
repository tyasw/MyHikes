package com.example.tyasw.myhikes

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.tyasw.myhikes.R.layout
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import javax.crypto.Cipher.DECRYPT_MODE
import javax.crypto.Cipher.ENCRYPT_MODE
import javax.crypto.spec.SecretKeySpec

class LoginActivity : StepActivity() {
    private var aes: AdvEncryptionStand = AdvEncryptionStand()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //deleteEverythingFromDatabase()

        aes = AdvEncryptionStand()

        createAccountButton.setOnClickListener {
            createAccount()
        }

        loginButton.setOnClickListener {
            login()
        }
    }

    private fun deleteEverythingFromDatabase() {
        val dbHandler = MyDBHandler(this, null, null, 1)
        dbHandler.deleteAccount(0)
        dbHandler.deleteAllHikes(0)
        dbHandler.deleteEveryContact()
        dbHandler.deleteEverySupply()
    }

    // Look up account in the database
    private fun login() {
        val encryptedPassword = encryptPassword(password.text.toString())
        Log.d("LoginActivity", "encryptedPassword" + encryptedPassword!!)

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
        Log.d("LoginActivity", "encryptedPassword" + encryptedPassword!!)

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
        return aes.crypt(ENCRYPT_MODE, plaintext, aes.getKey())
    }
}
