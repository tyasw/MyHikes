package com.example.tyasw.myhikes

import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var pixelDensity: Float = 0.toFloat()
    private var verticalDimensionsSet: Boolean = false
    private var horizontalDimensionsSet: Boolean = false

    private val LOCATION_REQUEST_CODE = 101
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val res = resources
        val metrics = res.displayMetrics
        pixelDensity = metrics.density
        val config = resources.configuration
        checkDimensions(config)

        // Set up button onclick handlers
        previousButton.setOnClickListener {
            previousStep()
        }

        nextButton.setOnClickListener {
            nextStep()
        }

        setLayoutMargins(buttonRow)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                    LOCATION_REQUEST_CODE)
        }

        val mapSettings = mMap.uiSettings
        mapSettings?.isZoomControlsEnabled = true
    }

    // This method was created by Jianna Zhang.
    private fun requestPermission(permissionType: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permissionType), requestCode)
    }

    // This method was created by Jianna Zhang. It is called when a user
    // responds to a permission request.
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                            "Unable to show location - permission required",
                            Toast.LENGTH_LONG).show()
                } else {
                    val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                    mapFragment.getMapAsync(this)
                }
            }
        }
    }

    private fun previousStep() {
        Toast.makeText(this, "Previous step", Toast.LENGTH_LONG).show()
    }

    private fun nextStep() {
        Toast.makeText(this, "Next step", Toast.LENGTH_LONG).show()
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
