package com.example.tyasw.myhikes

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up button onclick handlers
        addButton.setOnClickListener {
            addNew()
        }

        mapsNextButton.setOnClickListener {
            nextStep()
        }
    }

    private fun addNew() {
        val i = Intent(this, BasicInfoActivity::class.java)
        startActivity(i)
    }

    // Load from DB
    private fun nextStep() {
        val i = Intent(this, BasicInfoActivity::class.java)
        startActivity(i)
    }
}
