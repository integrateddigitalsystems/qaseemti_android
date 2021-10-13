package com.ids.qasemti.controller.Activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.RequestNotificationUpdate
import com.ids.qasemti.model.ResponseCancel
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivityMapAddress : ActivityBase(), OnMapReadyCallback{


    var latLng : LatLng ?=null
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
        var title :String ?=""
        AppHelper.setLogoTint(btBackTool,this,R.color.redPrimary)
        try {
            title = intent.getStringExtra("mapTitle")
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
            val intent = Intent()
            intent.putExtra("lat",latLng!!.latitude)
            intent.putExtra("long",latLng!!.longitude)
            intent.putExtra(
                "address",
                AppHelper.getAddress(latLng!!.latitude, latLng!!.longitude, this)
            )
            setResult(RESULT_OK, intent)
            finish()
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

    }
}