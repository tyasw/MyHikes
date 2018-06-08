package com.example.tyasw.myhikes

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.*

abstract class StepActivity: AppCompatActivity(),
    GestureDetector.OnGestureListener
{

    protected var accountId = -1
    protected var receivedHike: Hike? = null
    protected var supplies: ArrayList<Supply>? = null
    protected var contacts: ArrayList<Contact>? = null
    protected var isNewHike: Boolean = true

    var gDetector: GestureDetectorCompat? = null

    private var pixelDensity: Float = 0.toFloat()
    protected var verticalDimensionsSet: Boolean = false
    protected var horizontalDimensionsSet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.gDetector = GestureDetectorCompat(this, this)

        val res = resources
        val metrics = res.displayMetrics
        pixelDensity = metrics.density
        val config = resources.configuration
        checkDimensions(config)
    }

    override fun onFling(event1: MotionEvent, event2: MotionEvent,
                         velocityX: Float, velocityY: Float): Boolean {
        if (velocityX < 0f) {
            nextStep()
        } else {
            previousStep()
        }

        return true
    }

    override fun onDown(event: MotionEvent): Boolean {
        return true
    }

    override fun onLongPress(event: MotionEvent) {
    }

    override fun onScroll(e1: MotionEvent, e2: MotionEvent,
                          distanceX: Float, distanceY: Float): Boolean {
        return true
    }

    override fun onShowPress(event: MotionEvent) {
    }

    override fun onSingleTapUp(event: MotionEvent): Boolean {
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        this.gDetector?.onTouchEvent(event)

        // Be sure to call the superclass implementation
        return super.onTouchEvent(event)
    }

    open protected fun cancel(context: Context) {
        val i = Intent(context, MainActivity::class.java)
        i.putExtra("accountId", accountId)
        startActivity(i)
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

    protected fun setTableLayout(left: Int, top: Int, right: Int, bottom: Int): TableRow.LayoutParams {
        val params = TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(pxToDP(left), pxToDP(top), pxToDP(right), pxToDP(bottom))
        return params
    }

    protected fun pxToDP(dp: Int): Int {
        return dp * Resources.getSystem().displayMetrics.density.toInt()
    }

    protected fun dpToPx(px: Int): Int {
        return px / Resources.getSystem().displayMetrics.density.toInt()
    }

    open fun previousStep() {

    }

    open fun nextStep() {

    }

    protected fun displayHelpBox(context: Context) {
        AlertDialog.Builder(context)
                .setTitle("Help")
                .setMessage("MyHikes is an app that lets you keep track of " +
                        "hiking plans. First, create an account or log into one " +
                        "that already exists. Then, enter hiking information. To " +
                        "go to the next or previous step, either tap the appropriate " +
                        "buttons at the bottom of the screen, or swipe left or right. " +
                        "At any time, tap 'Cancel', and all changes you made will not " +
                        "be saved.")
                .show()
    }
}