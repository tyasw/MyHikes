package com.example.tyasw.myhikes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.tyasw.myhikes.R.layout
import java.util.*
import javax.crypto.Cipher.DECRYPT_MODE
import javax.crypto.Cipher.ENCRYPT_MODE
import javax.crypto.spec.SecretKeySpec

class LoginActivity : StepActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    // Look up account in the database
    private fun login() {

    }

    // Create new entry in the database
    private fun createAccount() {

    }
}
