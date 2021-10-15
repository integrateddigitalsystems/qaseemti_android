package com.ids.qasemti.controller.Activities

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.toObject
import com.ids.qasemti.R

import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.OrderLocation
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*


class ActivityMap : AppCompactBase(), OnMapReadyCallback, LocationListener {


    var gmap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_CHECK_SETTINGS = 2
    var doc : DocumentReference ?=null

    // in onCreate() initialize FusedLocationProviderClient


    // globally declare LocationRequest
    private lateinit var locationRequest: LocationRequest
    var locationUpdateState = false
    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        AppHelper.setAllTexts(rootLayout, this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)




        doc = MyApplication.db!!.collection("table_order").document("125")
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(getString(R.string.googleKey))
        }

        mvLocation.onCreate(mapViewBundle)

        init()
       //initGooglePlacesApi()


    }



    fun init() {
        mvLocation.getMapAsync(this);

        btDrawer.hide()
        btBackTool.show()
        btBackTool.onOneClick {
            onBackPressed()
        }

        var title = intent.getStringExtra("mapTitle")
        AppHelper.setLogoTint(btBackTool, this, R.color.redPrimary)
        tvPageTitle.setColorTypeface(this, R.color.redPrimary, title!!, true)


    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(getString(R.string.googleKey))
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(getString(R.string.googleKey), mapViewBundle)
        }

        mvLocation!!.onSaveInstanceState(mapViewBundle)
    }


    override fun onStart() {
        super.onStart()
        mvLocation!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        mvLocation!!.onStop()
    }


    override fun onDestroy() {
        mvLocation!!.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mvLocation!!.onLowMemory()
    }




    // stop receiving location update when activity not visible/foreground
    override fun onPause() {
        super.onPause()
        mvLocation!!.onPause()
    }

    // start receiving location update when activity  visible/foreground
    override fun onResume() {
        super.onResume()
        mvLocation!!.onResume()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gmap = googleMap
        gmap!!.setMinZoomPreference(12f)

        var lan: Int = 0
        var long: Int = 1


        doc!!.get().addOnSuccessListener { documentSnapshot ->
            val orderLoc = documentSnapshot.toObject<OrderLocation>()

            val ny = LatLng(
                orderLoc!!.order_laltitude!!.toDouble(),
                orderLoc!!.order_longitude!!.toDouble()
            )
            gmap!!.moveCamera(CameraUpdateFactory.newLatLng(ny))
            val markerOptions = MarkerOptions()
            markerOptions.position(ny)
            gmap!!.addMarker(markerOptions)
        }


    }

    override fun onLocationChanged(loc: Location) {
        AppHelper.createDialog(this, loc.latitude.toString() + "LONG" + loc.longitude.toString())

    }



}