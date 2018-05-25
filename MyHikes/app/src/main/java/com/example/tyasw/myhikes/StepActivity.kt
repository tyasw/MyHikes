package com.example.tyasw.myhikes

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.TableRow

abstract class StepActivity: AppCompatActivity() {

    private var pixelDensity: Float = 0.toFloat()
    private var verticalDimensionsSet: Boolean = false
    private var horizontalDimensionsSet: Boolean = false

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
    protected fun setLayoutMargins(buttonRow: TableRow) {
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
        if (!verticalDimensionsSet || !horizontalDimensionsSet)
            checkDimensions(newConfig)
    }

    // Set the margins of an item, converting pixels to dp in the process
    protected fun setLayout(left: Int, top: Int, right: Int, bottom: Int): TableRow.LayoutParams {
        val params = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
        params.setMargins(pxToDP(left), pxToDP(top), pxToDP(right), pxToDP(bottom))
        return params
    }

    private fun pxToDP(dp: Int): Int {
        return dp * Resources.getSystem().displayMetrics.density.toInt()
    }
}