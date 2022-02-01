package com.ids.qasemti.utils

import android.Manifest
import android.app.*
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentReference
import com.ids.qasemti.R
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.OrderLocation

class CurrentLocationService : Service() {

    inner class LocalBinder : Binder() {
        internal val service: CurrentLocationService
            get() = this@CurrentLocationService
    }
    private val PACKAGE_NAME = "com.ids.qasemti"
    private val EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION =
        "$PACKAGE_NAME.extra.CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION"
    var LOCATION_REFRESH_TIME = 5000
    internal val ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST =
        "$PACKAGE_NAME.action.FOREGROUND_ONLY_LOCATION_BROADCAST"
    val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"
    var locReq = LocationRequest.create()
    private lateinit var notificationManager: NotificationManager
    private val NOTIFICATION_ID = 12345678
    private var serviceRunningInForeground = true
    private var currentLocation: Location? = null
    private val NOTIFICATION_CHANNEL_ID = "while_in_use_channel_01"
    var LOCATION_REFRESH_DISTANCE = 5
    private var configurationChange = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var firstTime = false
    var locCall: LocationCallback? = null
    var doc: DocumentReference? = null
    var LatLngCurr: LatLng? = null


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configurationChange = true
    }
    private fun generateNotification(location: Location?): Notification {
        Log.d(TAG, "generateNotification()")

        // Main steps for building a BIG_TEXT_STYLE notification:
        //      0. Get data
        //      1. Create Notification Channel for O+
        //      2. Build the BIG_TEXT_STYLE
        //      3. Set up Intent / Pending Intent for notification
        //      4. Build and issue the notification

        // 0. Get data
        val mainNotificationText = location?.toText() ?: getString(R.string.no_data)
        val titleText = getString(R.string.app_name)

        // 1. Create Notification Channel for O+ and beyond devices (26+).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, titleText, NotificationManager.IMPORTANCE_DEFAULT
            )

            // Adds NotificationChannel to system. Attempting to create an
            // existing notification channel with its original values performs
            // no operation, so it's safe to perform the below sequence.
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // 2. Build the BIG_TEXT_STYLE.
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(mainNotificationText)
            .setBigContentTitle(titleText)

        // 3. Set up main Intent/Pending Intents for notification.
        val launchActivityIntent = Intent(this,MyApplication.trackingActivity!!::class.java)

        val cancelIntent = Intent(this, CurrentLocationService::class.java)
        cancelIntent.putExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, true)

        val servicePendingIntent = PendingIntent.getService(
            this, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val activityPendingIntent = PendingIntent.getActivity(
            this, 0, launchActivityIntent, 0
        )

        // 4. Build and issue the notification.
        // Notification Channel Id is ignored for Android pre O (26).
        val notificationCompatBuilder =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)

        return notificationCompatBuilder
            .setStyle(bigTextStyle)
            .setContentTitle(titleText)
            .setContentText(mainNotificationText)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(
                R.drawable.logo, getString(R.string.accept),//
                activityPendingIntent
            )
            .addAction(
                R.drawable.close_x,
                getString(R.string.permission_denied),//
                servicePendingIntent
            )
            .build()
    }

    override fun onUnbind(intent: Intent?): Boolean {


        if (!configurationChange && MyApplication.saveLocationTracking!!) {
            Log.d(TAG, "Start foreground service")
            val notification = generateNotification(currentLocation!!)
            startForeground(NOTIFICATION_ID, notification)
            serviceRunningInForeground = true
        }

        // Ensures onRebind() is called if MainActivity (client) rebinds.
        return true
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onRebind(intent: Intent) {
        Log.d(TAG, "onRebind()")

        // MainActivity (client) returns to the foreground and rebinds to service, so the service
        // can become a background services.
        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        super.onRebind(intent)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
    }
    fun subscribeToLocationUpdates() {
        Log.d(TAG, "subscribeToLocationUpdates()")

        MyApplication.saveLocationTracking = true

        // Binding to this service doesn't actually trigger onStartCommand(). That is needed to
        // ensure this Service can be promoted to a foreground service, i.e., the service needs to
        // be officially started (which we do here).
        startService(Intent(applicationContext, CurrentLocationService::class.java))



        try {
            // TODO: Step 1.5, Subscribe to location changes.
            fusedLocationClient.requestLocationUpdates(
                locReq, locCall!!, Looper.getMainLooper())
        } catch (unlikely: SecurityException) {
            MyApplication.saveLocationTracking = false
            Log.e(TAG, "Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }

    fun unsubscribeToLocationUpdates() {
        Log.d(TAG, "unsubscribeToLocationUpdates()")

        try {
            // TODO: Step 1.6, Unsubscribe to location changes.
            MyApplication.saveLocationTracking = false

        } catch (unlikely: SecurityException) {
            MyApplication.saveLocationTracking = true
            Log.e(TAG, "Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand()")


        val cancelLocationTrackingFromNotification =
            intent.getBooleanExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, false)

        if (cancelLocationTrackingFromNotification) {
            unsubscribeToLocationUpdates()
            stopSelf()
        }else{
            subscribeToLocationUpdates()
        }
        // Tells the system not to recreate the service after it's been killed.
        return START_NOT_STICKY
    }

    fun setUpLocations() {
        val locationResult = object : MyLocation.LocationResult() {
            override fun gotLocation(location: Location?) {
                if (location != null) {
                    var lat = location.latitude
                    doc!!.update("order_laltitude", lat.toString())
                    doc!!.update("order_longitude", location.longitude.toString())
                } else {
                    Toast.makeText(applicationContext, "cannot detect location", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }

        val myLocation = MyLocation()
        myLocation.getLocation(this, locationResult)


        var locationListenerGps = object : LocationListener {


            override fun onLocationChanged(location: Location) {
                if (!firstTime) {
                    //  AppHelper.createDialog(this@ActivityTrackOrder,"CHANGE")
                    var order: OrderLocation
                    doc!!.update("order_laltitude", location.latitude.toString())
                    doc!!.update("order_longitude", location.longitude.toString())
                    val ny = LatLng(
                        location!!.latitude!!.toDouble(),
                        location!!.longitude!!.toDouble()
                    )
                } else {
                    firstTime = false
                }
            }

            override fun onProviderDisabled(provider: String) {
                Log.wtf("test", provider)
            }

            override fun onProviderEnabled(provider: String) {
                Log.wtf("test", provider)
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
                Log.wtf("test", provider)
            }
        }

        var mLocationManager =
            getSystemService(LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                applicationContext as Activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 111
            )
            return
        }
        mLocationManager!!.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME.toLong(),
            LOCATION_REFRESH_DISTANCE.toFloat(), locationListenerGps!!
        )

    }

    private fun startLocationUpdates() { if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {

        return
    }

        fusedLocationClient.requestLocationUpdates(
            locReq, locCall!!, Looper.getMainLooper())

    }

    override fun onCreate() {

      /*  var doc = MyApplication.db!!.collection("table_order")
            .document("2718")*/

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        locReq.interval = 1
        locReq.fastestInterval = 1
        locReq.smallestDisplacement = 5f // 170 m = 0.1 mile
        locReq.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

       /* locCall = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                try {
                    locationResult.lastLocation
                    doc!!.update("order_laltitude", locationResult.lastLocation.latitude.toString())
                    doc!!.update("order_longitude", locationResult.lastLocation.longitude.toString())

                    val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
                    intent.putExtra(EXTRA_LOCATION, currentLocation)
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

                    startForeground(NOTIFICATION_ID,generateNotification(locationResult.lastLocation))

                    if (serviceRunningInForeground) {
                        notificationManager.notify(
                            NOTIFICATION_ID,
                            generateNotification(locationResult.lastLocation))
                    }
                } catch (ex: Exception) {
                    Log.wtf("", ex.toString())
                }
            }
        }*/



    }



    fun stopServiceFinished() {
        AppHelper.stopService(applicationContext)
    }


}