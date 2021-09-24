package com.ids.qasemti.controller.Activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.toObject
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.OrderLocation
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_track_order.*
import kotlinx.android.synthetic.main.toolbar.*


class ActivityTrackOrder : ActivityBase(), OnMapReadyCallback {

    var gmap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_CHECK_SETTINGS = 2
    var firtTime: Boolean = true
    private val options = MarkerOptions()
    var  mLocationListener : LocationListener ?=null
    var doc: DocumentReference? = null
    var currLocation : Location ?=null
    var markers: ArrayList<Marker> = arrayListOf()

    // in onCreate() initialize FusedLocationProviderClient


    // globally declare LocationRequest
    private lateinit var locationRequest: LocationRequest
    var locationUpdateState = false
    var markerMoving: Marker? = null

    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback
    var LOCATION_REFRESH_TIME = 1000
    var LOCATION_REFRESH_DISTANCE =5

    fun LocationChanged() {
        if (!MyApplication.isClient) {
            mLocationListener = LocationListener {
                var order: OrderLocation
                doc!!.update("order_laltitude", it.latitude.toString())
                doc!!.update("order_longitude", it.longitude.toString())
                if (!firtTime) {
                    val ny = LatLng(
                        it!!.latitude!!.toDouble(),
                        it!!.longitude!!.toDouble()
                    )
                    gmap!!.moveCamera(CameraUpdateFactory.newLatLng(ny))
                    markers.get(1).position = ny
                } else {
                    firtTime = false
                }
            }
            var mLocationManager: LocationManager =
                getSystemService(LOCATION_SERVICE) as LocationManager

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            currLocation = fusedLocationClient.lastLocation.result
            mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME.toLong(),
                LOCATION_REFRESH_DISTANCE.toFloat(), mLocationListener!!
            )
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                  AppHelper.createDialog(this,"lat:"+location!!.latitude +" , "+"long:"+location!!.longitude)
                }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_order)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)



        doc = MyApplication.db!!.collection("table_order").document("125")


        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(getString(R.string.googleKey))
        }

        mvTrackOrder.onCreate(mapViewBundle)

        init()
        LocationChanged()


    }


    fun init() {


        btDrawer.hide()
        btBackTool.show()
        btBackTool.onOneClick {
            onBackPressed()
        }
        AppHelper.setLogoTint(btBackTool, this, R.color.white)
        tvPageTitle.textRemote("track_order",this)
        tvPageTitle.setColorTypeface(this,R.color.white,"",true)



        mvTrackOrder.getMapAsync(this);

    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(getString(R.string.googleKey))
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(getString(R.string.googleKey), mapViewBundle)
        }

        mvTrackOrder!!.onSaveInstanceState(mapViewBundle)
    }


    override fun onStart() {
        super.onStart()
        mvTrackOrder!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        mvTrackOrder!!.onStop()
    }


    override fun onDestroy() {
        mvTrackOrder!!.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mvTrackOrder!!.onLowMemory()
    }


    // stop receiving location update when activity not visible/foreground
    override fun onPause() {
        super.onPause()
        mvTrackOrder!!.onPause()
    }

    // start receiving location update when activity  visible/foreground
    override fun onResume() {
        super.onResume()
        mvTrackOrder!!.onResume()
    }

    fun setClientListener() {
        doc!!.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
            val orderLoc = snapshot!!.toObject<OrderLocation>()
            val ny = LatLng(
                orderLoc!!.order_laltitude!!.toDouble(),
                orderLoc!!.order_longitude!!.toDouble()
            )

            gmap!!.moveCamera(CameraUpdateFactory.newLatLng(ny))
            markers.get(1).position = ny


            //  markerMoving!!.setPosition(ny);
            /*val markerOptions = MarkerOptions()
            markerOptions.position(ny)
            gmap!!.addMarker(markerOptions)*/
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gmap = googleMap
        gmap!!.setMinZoomPreference(12f)
        var latLngs: ArrayList<LatLng> = arrayListOf()

        var lan: Int = 0
        var long: Int = 1

        var deliverToPosition = LatLng(33.85918135530559, 35.492324839447576)
        latLngs.add(deliverToPosition)



        doc!!.get().addOnSuccessListener { documentSnapshot ->
            val orderLoc = documentSnapshot.toObject<OrderLocation>()

            val ny = LatLng(
                orderLoc!!.order_laltitude!!.toDouble(),
                orderLoc!!.order_longitude!!.toDouble()
            )
            latLngs.add(ny)
            for (item in latLngs) {
                options.position(item);
                options.title("someTitle");
                options.snippet("someDesc");
                markers.add(gmap!!.addMarker(options))
            }

            gmap!!.moveCamera(CameraUpdateFactory.newLatLng(ny))

            if (MyApplication.isClient) {
                setClientListener()
            }
        }


    }
}