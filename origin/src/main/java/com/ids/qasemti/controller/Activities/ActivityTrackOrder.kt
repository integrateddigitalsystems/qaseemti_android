package com.ids.qasemti.controller.Activities

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.toObject
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.OrderLocation
import com.ids.qasemti.model.ResponseGeoAddress
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_track_order.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*


class ActivityTrackOrder : ActivityBase(), OnMapReadyCallback , ApiListener{

    var gmap: GoogleMap? = null
    var LatLngCurr: LatLng? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_CHECK_SETTINGS = 2
    var firtTime: Boolean = true
    var locationListenerGps: LocationListener? = null
    var db: FirebaseFirestore? = null
    private val options = MarkerOptions()
    var mLocationManager: LocationManager ?=null
    var mLocationListener: LocationListener? = null
    var doc: DocumentReference? = null
    var currLocation: Location? = null
    var markers: ArrayList<Marker> = arrayListOf()

    // in onCreate() initialize FusedLocationProviderClient


    // globally declare LocationRequest
    private lateinit var locationRequest: LocationRequest
    var locationUpdateState = false
    var markerMoving: Marker? = null

    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback
    var LOCATION_REFRESH_TIME = 5000
    var LOCATION_REFRESH_DISTANCE = 5

    fun LocationChanged() {
        getCurrentLocation()
        /*  if (!MyApplication.isClient) {

              getCurrentLocation()
              var mLocationManager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
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
                  }
              }


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
              try {
                  val locationManager =
                    getSystemService(Context.LOCATION_SERVICE) as LocationManager
                  var gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                  var network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
              } catch (e: Exception) {
                  e.printStackTrace()
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


              if(firtTime){
                  doc!!.update("order_laltitude", LatLngCurr!!.latitude.toString())
                  doc!!.update("order_longitude", LatLngCurr!!.longitude.toString())
                  firtTime = false
              }


          }*/

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_order)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)



        doc = MyApplication.db!!.collection("table_order")
            .document(MyApplication.selectedOrder!!.orderId!!)


        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(getString(R.string.googleKey))
        }

        mvTrackOrder.onCreate(mapViewBundle)

        init()
       // LocationChanged()


    }


    fun init() {


        btDrawer.hide()
        btBackTool.show()
        btBackTool.onOneClick {
            onBackPressed()
        }
        AppHelper.setLogoTint(btBackTool, this, R.color.white)
        tvPageTitle.textRemote("track_order", this)
        tvPageTitle.setColorTypeface(this, R.color.white, "", true)



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
//        mLocationManager!!.removeUpdates(locationListenerGps!!)
    }


    override fun onDestroy() {
        super.onDestroy()
        mvTrackOrder!!.onDestroy()
   //     mLocationManager!!.removeUpdates(locationListenerGps!!)
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
        try {
            doc!!.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
                val orderLoc = snapshot!!.toObject<OrderLocation>()
                if (orderLoc != null) {
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
                }else{
                    AppHelper.createFixedDialog(this,AppHelper.getRemoteString("no_curr_track",this)){
                        super.onBackPressed()
                    }
                }
            }
        }catch (ex:Exception){

        }
    }

    fun resizeMapIcons(drawabele: Int, width: Int, height: Int): Bitmap? {
        val imageBitmap = BitmapFactory.decodeResource(
            resources, drawabele
        )

        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gmap = googleMap
        gmap!!.setMinZoomPreference(1f)
        var latLngs: ArrayList<LatLng> = arrayListOf()

        var lan: Int = 0
        var long: Int = 1

        var deliverToPosition : LatLng ?=null
        try {
            deliverToPosition = LatLng(
                MyApplication.selectedOrder!!.shipping_latitude!!.toDouble(),
                MyApplication.selectedOrder!!.shipping_longitude!!.toDouble()
            )
        }catch (ex:Exception){
            deliverToPosition = LatLng(
                33.85916355598623,
                35.49256087672
            )
        }
        latLngs.add(deliverToPosition!!)



        doc!!.get().addOnFailureListener {  }
        doc!!.get().addOnFailureListener {
            var x= it
            logw("FAILED","map Failed")
            AppHelper.createDialog(this,AppHelper.getRemoteString("noTrackData",this)){
                super.onBackPressed()
            }
        }
        doc!!.get().addOnSuccessListener { documentSnapshot ->
            val orderLoc = documentSnapshot.toObject<OrderLocation>()
            if(orderLoc!=null) {
                try {
                    var ny: LatLng? = null
                    if (orderLoc != null) {
                        ny = LatLng(
                            orderLoc!!.order_laltitude!!.toDouble(),
                            orderLoc!!.order_longitude!!.toDouble()
                        )
                    } else {
                        val user: MutableMap<String, String> = HashMap()
                        user["oder_id"] = MyApplication.selectedOrder!!.orderId!!
                        user["order_laltitude"] = LatLngCurr!!.latitude.toString()
                        user["order_longitude"] = LatLngCurr!!.longitude.toString()
                        doc!!.set(user)
                        ny = LatLngCurr
                    }
                    latLngs.add(ny!!)
                    for (item in latLngs) {
                        options.position(item);
                        options.title("someTitle");
                        options.snippet("someDesc");
                        markers.add(gmap!!.addMarker(options)!!)
                    }

                    markers.get(1)
                        .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_vehicle))
                    //    markers.get(1).setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.icon_map_vehicle,125,125)!!))

                    gmap!!.moveCamera(CameraUpdateFactory.newLatLng(ny))

                    if (MyApplication.isClient) {
                        setClientListener()
                    }
                }catch (ex:Exception){
                    AppHelper.createFixedDialog(this,AppHelper.getRemoteString("no_curr_track",this)){
                        super.onBackPressed()
                    }
                }
            }else{
                AppHelper.createFixedDialog(this,AppHelper.getRemoteString("no_curr_track",this)){
                    super.onBackPressed()
                }
            }
        }


    }


    private fun getCurrentLocation() {
        val locationResult = object : MyLocation.LocationResult() {
            override fun gotLocation(location: Location?) {
                if (location != null) {
                    val lat = location!!.latitude
                    val lon = location.longitude
                    //toast("$lat --SLocRes-- $lon")
                    LatLngCurr = LatLng(lat, lon)
                    CallAPIs.getAddressName( LatLngCurr!!,this@ActivityTrackOrder,this@ActivityTrackOrder)
                    try {
                        doc!!.update("order_laltitude", location.latitude.toString())
                        doc!!.update("order_longitude", location.longitude.toString())
                    } catch (ex: Exception) {
                    }
                    /*startActivityForResult(
                        Intent(this@ActivitySelectAddress, ActivityAddNewAddress::class.java)
                            .putExtra("from","current")
                        ,
                        REQUEST_LOCATION
                    )*/

                } else {
                    toast("cannot detect location")
                }
            }

        }

        val myLocation = MyLocation()
        myLocation.getLocation(this, locationResult)


        locationListenerGps= object : LocationListener {


            override fun onLocationChanged(location: Location) {
                if (!firtTime) {
                    //  AppHelper.createDialog(this@ActivityTrackOrder,"CHANGE")
                    var order: OrderLocation
                    doc!!.update("order_laltitude", location.latitude.toString())
                    doc!!.update("order_longitude", location.longitude.toString())
                    val ny = LatLng(
                        location!!.latitude!!.toDouble(),
                        location!!.longitude!!.toDouble()
                    )
                    gmap!!.moveCamera(CameraUpdateFactory.newLatLng(ny))
                    markers.get(1).position = ny
                } else {
                    firtTime = false
                }
            }

            override fun onProviderDisabled(provider: String) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        }

        mLocationManager =
            getSystemService(LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mLocationManager!!.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME.toLong(),
            LOCATION_REFRESH_DISTANCE.toFloat(), locationListenerGps!!
        )


    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            toast("Please accept requested permission in order to detect your current location")
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    override fun onDataRetrieved(success: Boolean, response: Any, apiId: Int) {

        if(success){
            MyApplication.selectedCurrentAddress = AppHelper.getAddressNames(response as ResponseGeoAddress)
        }

    }
}