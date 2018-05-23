package com.example.tyasw.myhikes

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TableRow
import kotlinx.android.synthetic.main.activity_basic_info.*

class BasicInfoActivity : AppCompatActivity() {

    private var pixelDensity: Float = 0.toFloat()
    private var verticalDimensionsSet: Boolean = false
    private var horizontalDimensionsSet: Boolean = false

    private val LOCATION_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_info)

        val res = resources
        val metrics = res.displayMetrics
        pixelDensity = metrics.density
        val config = resources.configuration
        checkDimensions(config)

        suppliesPreviousButton.setOnClickListener {
            previousStep()
        }

        suppliesNextButton.setOnClickListener {
            nextStep()
        }
    }

    private fun previousStep() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    private fun nextStep() {
        val i = Intent(this, SuppliesActivity::class.java)
        startActivity(i)
    }

    fun checkDimensions(config: Configuration) {
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            horizontalDimensionsSet = true
        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            verticalDimensionsSet = true
        }
    }

    // Make sure that the button row at the bottom of the activity is large enough
    fun setLayoutMargins(buttonRow: TableRow) {
        val buttonRowWeight = when (verticalDimensionsSet) {
            true -> 1f
            false -> 2f
        }

        buttonRow.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                buttonRowWeight
        )
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (!verticalDimensionsSet || !horizontalDimensionsSet)
            checkDimensions(newConfig)
    }

    private fun pxToDP(dp: Int): Int {
        return dp * Resources.getSystem().displayMetrics.density.toInt()
    }
}
