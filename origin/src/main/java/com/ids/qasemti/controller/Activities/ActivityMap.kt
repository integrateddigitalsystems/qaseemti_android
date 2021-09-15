package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.toolbar.*


class ActivityMap : ActivityBase(), OnMapReadyCallback{


    var gmap : GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        AppHelper.setAllTexts(rootLayout,this)


        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(getString(R.string.googleKey))
        }

        mvLocation.onCreate(mapViewBundle)

        init()


    }

    fun init(){
        mvLocation.getMapAsync(this);

        btDrawer.hide()
        btBackTool.show()
        btBackTool.onOneClick {
            onBackPressed()
        }

        var title = intent.getStringExtra("mapTitle")
        AppHelper.setLogoTint(btBackTool,this,R.color.redPrimary)
        tvPageTitle.setColorTypeface(this,R.color.redPrimary,title!!,true)


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
        val ny = LatLng(33.8658486, 35.5483189)
        gmap!!.moveCamera(CameraUpdateFactory.newLatLng(ny))
        val markerOptions = MarkerOptions()
        markerOptions.position(ny)
        gmap!!.addMarker(markerOptions)
    }
}