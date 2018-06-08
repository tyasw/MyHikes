package com.example.tyasw.myhikes

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

/**
 * Program: MyHikes
 * Description: Organize hiking plans.
 * Author: William Tyas
 * Notes: Currently, the SMS feature has not been tested, so it is unknown
 *      whether it works or not. A data plan is required to send SMS messages,
 *      but the device this app was tested on did not have one.
 * Last Modified: 6/8/18
 */
class MapsActivity : StepActivity(), OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private val LOCATION_REQUEST_CODE = 101
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val actionBar = supportActionBar
        actionBar?.setLogo(R.mipmap.ic_launcher)

        val extras = intent.extras

        accountId = extras.getInt("accountId")
        isNewHike = extras.getBoolean("isNewHike")

        receivedHike = intent.getParcelableExtra<Hike>("hike") ?: null
        supplies = intent.getParcelableArrayListExtra<Supply>("supplies") ?: null
        contacts = intent.getParcelableArrayListExtra<Contact>("contacts") ?: null

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Set up button onclick handlers

        mapsPreviousButton.setOnClickListener {
            previousStep()
        }

        mapsCancelButton.setOnClickListener {
            cancel(this)
        }

        mapsNextButton.setOnClickListener {
            nextStep()
        }

        setButtonRowParameters(buttonRow)
    }

    override fun onResume() {
        super.onResume()
        try {
            populateMap()
        } catch (e : UninitializedPropertyAccessException) {
            Log.d("ABC", "Map uninitialized")
        }
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

    private fun populateMap() {
        try {
            mMap.clear()

            val position = LatLng(receivedHike?.latitude as Double, receivedHike?.longitude as Double)

            mMap.addMarker(MarkerOptions()
                    .position(position)
                    .title(receivedHike?.latitude.toString() + ", " + receivedHike?.longitude.toString()))

            mMap.animateCamera(CameraUpdateFactory.newLatLng(position))
        } catch (e: UninitializedPropertyAccessException) {
            Log.d("ABC", "Map uninitialized")
        }
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

        mMap.setOnMapClickListener {
            p -> onMapClick(p)
        }

        val mapSettings = mMap.uiSettings
        mapSettings?.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        populateMap()
    }

    override fun onMapClick(location: LatLng) {
        try {
            mMap.clear()
            mMap.addMarker(MarkerOptions()
                    .position(location)
                    .title(location.latitude.toString() + ", " + location.longitude.toString()))

            receivedHike?.latitude = location.latitude
            receivedHike?.longitude = location.longitude

            populateMap()
        } catch (e: UninitializedPropertyAccessException) {
            Log.d("ABC", "Map uninitialized")
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        return false
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

    override fun previousStep() {
        val i = Intent(this, SuppliesActivity::class.java)

        i.putExtra("accountId", accountId)

        if (isNewHike) {
            i.putExtra("isNewHike", true)
        } else {
            i.putExtra("isNewHike", false)
        }

        i.putExtra("hike", receivedHike)
        i.putExtra("supplies", supplies)
        i.putExtra("contacts", contacts)

        startActivity(i)
    }

    override fun nextStep() {
        val i = Intent(this, ContactsActivity::class.java)

        i.putExtra("accountId", accountId)

        if (isNewHike) {
            i.putExtra("isNewHike", true)
        } else {
            i.putExtra("isNewHike", false)
        }

        i.putExtra("hike", receivedHike)
        i.putExtra("supplies", supplies)
        i.putExtra("contacts", contacts)

        startActivity(i)
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
