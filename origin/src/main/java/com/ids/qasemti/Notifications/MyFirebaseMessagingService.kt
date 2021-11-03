package com.ids.qasemti.Notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import android.util.Log
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivitySplash
import com.ids.qasemti.controller.MyApplication


class MyFirebaseMessagingService: FirebaseMessagingService() {

    private val TAG = "MyFirebaseMessagingService"

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener {
            sendRegistrationToServer(it.result!!.token)
        }
    }

    private fun sendRegistrationToServer(token: String) {
       // Actions.addDevice(this, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

       // wtf("RECEIVED NOTIFICATION")

        Log.wtf(TAG, "From: " + remoteMessage.getFrom()!!)

        if (remoteMessage.data.size > 0) {
            Log.wtf(TAG, "Message data payload: " + remoteMessage.getData())
        }

        if (remoteMessage.notification != null) {
            Log.wtf(TAG, "Message Notification Body: " + remoteMessage.getNotification()!!.body!!)
        }

        var typeId :Int
        try {
            typeId = remoteMessage.data["typeId"]!!.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            typeId = -1
        }

        var recordId :Int
        try {
            recordId = remoteMessage.data["recordId"]!!.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            recordId = -1
        }

        var id :Int
        try {
            id = remoteMessage.data["Id"]!!.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            id = -1
        }

        var message : String
        try {
            message = remoteMessage.data["message"] as String
        } catch (e: Exception) {
            e.printStackTrace()
            message = ""
        }

       // if (MyApplication.showNotifications){

            sendNotification(typeId, recordId, id, message)
      // }
    }

    private fun sendNotification(typeId: Int, recordId: Int, id: Int, messageBody: String) {

        lateinit var intent: Intent


        var notificationId = 0
        try {
            notificationId = id
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

        intent = Intent(this, ActivitySplash::class.java)
        intent.putExtra("notificationId", notificationId)
        intent.putExtra("recordId", recordId)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, MyApplication.UNIQUE_REQUEST_CODE++, intent, PendingIntent.FLAG_ONE_SHOT)

        /*Intent backIntent = new Intent(this, SplashActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, UNIQUE_REQUEST_CODE++, new Intent[] {backIntent, intent}, PendingIntent.FLAG_ONE_SHOT);*/


        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, "CCIAZ_0001")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(resources.getString(R.string.app_name))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(messageBody))
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("QASEMTI_0001", "QASEMTI", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build())
    }


}