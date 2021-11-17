package com.ids.qasemti.utils

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import com.ids.qasemti.R
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentReference
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Activities.ActivityOrderDetails
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.OrderLocation
import kotlinx.android.synthetic.main.activity_code_verification.*
import java.util.concurrent.TimeUnit

class LocationForeService : Service() {
    /*
     * Checks whether the bound activity has really gone away (foreground service with notification
     * created) or simply orientation change (no-op).
     */
    private var configurationChange = false
    private var serviceRunningInForeground = false
    var locationListenerGps : LocationListener ?=null
    var mLocationManager : LocationManager ?=null
    var doc: DocumentReference? = null
    private val localBinder = LocalBinder()
    var LOCATION_REFRESH_DISTANCE = 5
    var LOCATION_REFRESH_TIME = 5000
    var timer : CountDownTimer?=null

    private lateinit var notificationManager: NotificationManager

    // TODO: Step 1.1, Review variables (no changes).
    // FusedLocationProviderClient - Main class for receiving location updates.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // LocationRequest - Requirements for the location updates, i.e., how often you should receive
    // updates, the priority, etc.
    private lateinit var locationRequest: LocationRequest

    // LocationCallback - Called when FusedLocationProviderClient has a new Location.
    private lateinit var locationCallback: LocationCallback

    // Used only for local storage of the last known location. Usually, this would be saved to your
    // database, but because this is a simplified sample without a full database, we only need the
    // last location to create a Notification if the user navigates away from the app.
    private var currentLocation: Location? = null

    override fun onCreate() {
        Log.d(TAG, "onCreate()")

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // TODO: Step 1.2, Review the FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // TODO: Step 1.3, Create a LocationRequest.
        locationRequest = LocationRequest.create().apply {
            // Sets the desired interval for active location updates. This interval is inexact. You
            // may not receive updates at all if no location sources are available, or you may
            // receive them less frequently than requested. You may also receive updates more
            // frequently than requested if other applications are requesting location at a more
            // frequent interval.
            //
            // IMPORTANT NOTE: Apps running on Android 8.0 and higher devices (regardless of
            // targetSdkVersion) may receive updates less frequently than this interval when the app
            // is no longer in the foreground.
            interval = TimeUnit.SECONDS.toMillis(60)

            // Sets the fastest rate for active location updates. This interval is exact, and your
            // application will never receive updates more frequently than this value.
            fastestInterval = TimeUnit.SECONDS.toMillis(30)

            // Sets the maximum time when batched location updates are delivered. Updates may be
            // delivered sooner than this interval.
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        // TODO: Step 1.4, Initialize the LocationCallback.




        locationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                try {
                    locationResult.lastLocation
                 /*   doc!!.update("order_laltitude", locationResult.lastLocation.latitude.toString())
                    doc!!.update("order_longitude", locationResult.lastLocation.longitude.toString())*/

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
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand()")

        val cancelLocationTrackingFromNotification =
            intent.getBooleanExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, false)

        if (cancelLocationTrackingFromNotification) {
            unsubscribeToLocationUpdates()
            stopSelf()
        }
        // Tells the system not to recreate the service after it's been killed.
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind()")

        // MainActivity (client) comes into foreground and binds to service, so the service can
        // become a background services.
        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        return localBinder
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

    override fun onUnbind(intent: Intent): Boolean {
        Log.d(TAG, "onUnbind()")

        // MainActivity (client) leaves foreground, so service needs to become a foreground service
        // to maintain the 'while-in-use' label.
        // NOTE: If this method is called due to a configuration change in MainActivity,
        // we do nothing.
        if (!configurationChange && MyApplication.saveLocationTracking!!) {
            Log.d(TAG, "Start foreground service")
            val notification = generateNotification(currentLocation)
            startForeground(NOTIFICATION_ID, notification)
            serviceRunningInForeground = true
        }

        // Ensures onRebind() is called if MainActivity (client) rebinds.
        return true
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configurationChange = true
    }

    fun setUpLocations() {
        val locationResult = object : MyLocation.LocationResult() {
            override fun gotLocation(location: Location?) {
                if (location != null) {
                    var lat = location.latitude
                   /* doc!!.update("order_laltitude", lat.toString())
                    doc!!.update("order_longitude", location.longitude.toString())*/
                } else {
                    Toast.makeText(applicationContext, "cannot detect location", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }

        val myLocation = MyLocation()
        myLocation.getLocation(this, locationResult)


        locationListenerGps = object : LocationListener {


            override fun onLocationChanged(location: Location) {

                doc!!.update("order_laltitude", location.latitude.toString())
                doc!!.update("order_longitude", location.longitude.toString())

                val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
                intent.putExtra(EXTRA_LOCATION, currentLocation)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)



                startForeground(NOTIFICATION_ID,generateNotification(location))
                if (serviceRunningInForeground) {

                    notificationManager.notify(
                        NOTIFICATION_ID,
                        generateNotification(location))
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
    fun subscribeToLocationUpdates() {
        Log.d(TAG, "subscribeToLocationUpdates()")



        AppHelper.setUpDoc(MyApplication.selectedOrder!!)
        doc = MyApplication.db!!.collection("table_order")
            .document(MyApplication.selectedOrder!!.orderId!!)
        try {
            MyApplication.saveLocationTracking = true
            setUpLocations()
        }catch (ex:Exception){
            var x = ex
        }

        // Binding to this service doesn't actually trigger onStartCommand(). That is needed to
        // ensure this Service can be promoted to a foreground service, i.e., the service needs to
        // be officially started (which we do here).
        startService(Intent(applicationContext, LocationForeService::class.java))


        /*try {
            // TODO: Step 1.5, Subscribe to location changes.
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper())
        } catch (unlikely: SecurityException) {
            MyApplication.saveLocationTracking = false
            Log.e(TAG, "Lost location permissions. Couldn't remove updates. $unlikely")
        }*/
    }

    fun unsubscribeToLocationUpdates() {
        Log.d(TAG, "unsubscribeToLocationUpdates()")

        try {
            // TODO: Step 1.6, Unsubscribe to location changes.
            val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            mLocationManager!!.removeUpdates(locationListenerGps!!)
            removeTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Location Callback removed.")
                    stopSelf()
                } else {
                    Log.d(TAG, "Failed to remove Location Callback.")
                }
            }
            MyApplication.saveLocationTracking = false
        } catch (unlikely: SecurityException) {
           MyApplication.saveLocationTracking = true
            Log.e(TAG, "Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }

    /*
     * Generates a BIG_TEXT_STYLE Notification that represent latest location.
     */
    private fun generateNotification(location: Location?): Notification {
        Log.d(TAG, "generateNotification()")

        // Main steps for building a BIG_TEXT_STYLE notification:
        //      0. Get data
        //      1. Create Notification Channel for O+
        //      2. Build the BIG_TEXT_STYLE
        //      3. Set up Intent / Pending Intent for notification
        //      4. Build and issue the notification

        // 0. Get data
        val mainNotificationText = location?.toText() ?: AppHelper.getRemoteString("collecting_loc",this)
        val titleText = getString(R.string.app_name)

        // 1. Create Notification Channel for O+ and beyond devices (26+).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, titleText, NotificationManager.IMPORTANCE_DEFAULT)

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
        var launchActivityIntent:Intent ?=null
        if(MyApplication.fromOrderDetails!!) {
            launchActivityIntent = Intent(this, ActivityOrderDetails::class.java)
        }else{
            launchActivityIntent = Intent(this, ActivityHome::class.java)
        }

        val cancelIntent = Intent(this, LocationForeService::class.java)
        cancelIntent.putExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, true)

        val servicePendingIntent = PendingIntent.getService(
            this, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val activityPendingIntent = PendingIntent.getActivity(
            this, 0, launchActivityIntent, 0)

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
                R.drawable.logo, getString(R.string.available),
                activityPendingIntent
            )
            .addAction(
                R.drawable.close_x,
                getString(R.string.no_data),
                servicePendingIntent
            )
            .build()
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        internal val foreService: LocationForeService
            get() = this@LocationForeService
    }

    companion object {
        private const val TAG = "LocationForeService"

        private const val PACKAGE_NAME = "com.example.android.whileinuselocation"

        internal const val ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST =
            "$PACKAGE_NAME.action.FOREGROUND_ONLY_LOCATION_BROADCAST"

        internal const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"

        private const val EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION =
            "$PACKAGE_NAME.extra.CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION"

        private const val NOTIFICATION_ID = 12345678

        private const val NOTIFICATION_CHANNEL_ID = "while_in_use_channel_01"
    }
}