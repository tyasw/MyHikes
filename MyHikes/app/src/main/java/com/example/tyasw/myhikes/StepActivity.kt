package com.example.tyasw.myhikes

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*

abstract class StepActivity: AppCompatActivity() {
    private var pixelDensity: Float = 0.toFloat()
    protected var verticalDimensionsSet: Boolean = false
    protected var horizontalDimensionsSet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val res = resources
        val metrics = res.displayMetrics
        pixelDensity = metrics.density
        val config = resources.configuration
        checkDimensions(config)
    }

    private fun checkDimensions(config: Configuration) {
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            horizontalDimensionsSet = true
        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            verticalDimensionsSet = true
        }
    }

    // Make sure that the button row at the bottom of the activity is large enough
    protected fun setButtonRowParameters(buttonRow: TableRow) {
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

    open protected fun setLayoutMargins(vararg elements: TextView) {

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (!verticalDimensionsSet || !horizontalDimensionsSet)
            checkDimensions(newConfig)
    }

    // Set the margins of an item, converting pixels to dp in the process
    protected fun setRadioButtonLayout(left: Int, top: Int, right: Int, bottom: Int): RadioGroup.LayoutParams {
        val params = RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(pxToDP(left), pxToDP(top), pxToDP(right), pxToDP(bottom))
        return params
    }

    protected fun setTableLayout(left: Int, top: Int, right: Int, bottom: Int): TableLayout.LayoutParams {
        val params = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(pxToDP(left), pxToDP(top), pxToDP(right), pxToDP(bottom))
        return params
    }

    private fun pxToDP(dp: Int): Int {
        return dp * Resources.getSystem().displayMetrics.density.toInt()
    }
}