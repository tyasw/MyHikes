package com.example.tyasw.myhikes

import android.support.v4.content.ContextCompat
import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.support.v4.app.ActivityCompat
import android.widget.Toast

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : StepActivity(), OnMapReadyCallback {
    private var receivedHike: Hike? = null
    private var supplies: ArrayList<Supply>? = null
    private var contacts: ArrayList<Contact>? = null
    private var isNewHike: Boolean = true

    private val LOCATION_REQUEST_CODE = 101
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val extras = intent.extras

        isNewHike = extras.getBoolean("isNewHike")

        receivedHike = intent.getParcelableExtra<Hike>("hike") ?: null
        supplies = intent.getParcelableArrayListExtra<Supply>("supplies") ?: null
        contacts = intent.getParcelableArrayListExtra<Contact>("contacts") ?: null

        populateInfo(receivedHike)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Set up button onclick handlers
        mapsPreviousButton.setOnClickListener {
            previousStep()
        }

        mapsNextButton.setOnClickListener {
            nextStep()
        }

        setButtonRowParameters(buttonRow)
    }

    private fun populateInfo(hike: Hike?) {
        if (hike != null) {

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
        val i = Intent(this, SuppliesActivity::class.java)

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

    private fun nextStep() {
        val i = Intent(this, ContactsActivity::class.java)

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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Toast.makeText(this, "Configuration changed", Toast.LENGTH_LONG).show()
    }
}
