package com.ids.qasemti.controller.Activities

import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.show
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.toolbar.*


class ActivityMap : ActivityBase(), OnMapReadyCallback{


    var gmap : GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(getString(R.string.google_api_key))
        }

        mvLocation.onCreate(mapViewBundle);

        init()


    }

    fun init(){
        mvLocation.getMapAsync(this);

        btDrawer.hide()
        btBackTool.show()
        btBackTool.setOnClickListener {
            onBackPressed()
        }
        tvPageTitle.text = getString(R.string.view_address)

    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(getString(R.string.google_api_key))
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(getString(R.string.google_api_key), mapViewBundle)
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
        val ny = LatLng(33.8658486, 35.5483189)
        gmap!!.moveCamera(CameraUpdateFactory.newLatLng(ny))
        val markerOptions = MarkerOptions()
        markerOptions.position(ny)
        gmap!!.addMarker(markerOptions)
    }
}