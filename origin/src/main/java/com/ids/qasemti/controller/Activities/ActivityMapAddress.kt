package com.ids.qasemti.controller.Activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.toolbar.*


class ActivityMapAddress : AppCompactBase(), OnMapReadyCallback{


    var REQUEST_ADDRESS = 1005
    var latLng : LatLng ?=null
    var seeOnly = false
    var gmap : GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        AppHelper.setAllTexts(rootLayout,this)

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(getString(R.string.googleKey))
        }

        mvLocation.onCreate(mapViewBundle);

        init()
       // initGooglePlacesApi()


    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED, intent)
        super.onBackPressed()
    }

    fun init(){
        mvLocation.getMapAsync(this);

        btDrawer.hide()
        btBackTool.show()
        btBackTool.onOneClick {
            onBackPressed()
        }
        var title=""

        try{
            seeOnly = intent.getBooleanExtra("seeOnly",false)
        }catch (ex:Exception){
            seeOnly = false
        }

        if(seeOnly){
            btSavePosition.hide()
        }
        AppHelper.setLogoTint(btBackTool,this,R.color.redPrimary)
        try {
            title = intent.getStringExtra("mapTitle")!!
        }
        catch (ex:java.lang.Exception){
            title = AppHelper.getRemoteString("LocationOnMap",this)
        }
        AppHelper.setLogoTint(btBackTool, this, R.color.redPrimary)
        tvPageTitle.setColorTypeface(this, R.color.redPrimary, title!!, true)
        listeners()
    }

    fun listeners (){
        btSavePosition.setOnClickListener {

            var x = latLng
            if( MyApplication.finish!!){
                MyApplication.latSelected = latLng!!.latitude
                MyApplication.longSelected = latLng!!.longitude
                finish()
                startActivityForResult(Intent(this,ActivityAddNewAddress::class.java),REQUEST_ADDRESS)
            }else {
                val intent = Intent()
                intent.putExtra("lat", latLng!!.latitude)
                intent.putExtra("long", latLng!!.longitude)
                intent.putExtra(
                    "address",
                    AppHelper.getAddress(latLng!!.latitude, latLng!!.longitude, this)
                )
                setResult(RESULT_OK, intent)
                finish()
            }
        }
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

    override fun onResume() {
        super.onResume()
        mvLocation!!.onResume()
    }

    override fun onStart() {
        super.onStart()
        mvLocation!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        mvLocation!!.onStop()
    }

    override fun onPause() {
        mvLocation!!.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mvLocation!!.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mvLocation!!.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gmap = googleMap
        gmap!!.setMinZoomPreference(12f)

        if(!seeOnly) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

            }
            try {
                val locationManager =
                    getSystemService(Context.LOCATION_SERVICE) as LocationManager
                var gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                latLng = LatLng(gps_loc!!.latitude, gps_loc!!.longitude)
                //   addressName = AppHelper.getAddress(latLng!!.latitude, latLng!!.longitude, this)
            } catch (e: Exception) {
                latLng = LatLng(33.8658486, 35.5483189)
                e.printStackTrace()
            }

            gmap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            gmap!!.addMarker(markerOptions)

            gmap!!.setOnMapClickListener {
                gmap!!.moveCamera(CameraUpdateFactory.newLatLng(it))
                val marker = MarkerOptions()
                marker.position(it)
                gmap!!.clear()
                gmap!!.addMarker(marker)
                latLng = it
            }
        }else{
            var ll : LatLng ?=null
            try{
                ll= LatLng(MyApplication.selectedOrder!!.customerLat!!.toDouble(),MyApplication.selectedOrder!!.customerLong!!.toDouble())
            }catch (ex:Exception){

            }

            if(ll!=null){
                gmap!!.moveCamera(CameraUpdateFactory.newLatLng(ll))
                val markerOptions = MarkerOptions()
                markerOptions.position(ll)
                gmap!!.addMarker(markerOptions)
            }else{
                AppHelper.createDialog(this,"No location date connected to selected order")
                latLng = LatLng(33.8658486, 35.5483189)
                gmap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            }

        }

    }

/*    private fun initGooglePlacesApi() {
        Places.initialize(applicationContext, getString(R.string.google_api_key))
        val placesClient: PlacesClient = Places.createClient(applicationContext)
        val autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setHint(getString(R.string.please_select_location))
        //        autocompleteFragment.setLocationRestriction(RectangularBounds.newInstance(
//                new LatLng(34.7006096, 19.2477876),
//                new LatLng(41.7488862, 29.7296986))); //Greece bounds
        autocompleteFragment.setCountry("gr")

        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ADDRESS,
                Place.Field.ADDRESS_COMPONENTS
            )
        )
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS)


        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                if (place.addressComponents!!.asList()[0].types[0]
                        .equals("route")
                ) {

                    toast(place.address!!)
                    var location = place.address
                } else { //If user does not choose a specific place.

                    toast("choose address")
                }

            }

            override fun onError(status: Status) {
                toast( "An error occurred: $status")
            }
        })
    }*/
}