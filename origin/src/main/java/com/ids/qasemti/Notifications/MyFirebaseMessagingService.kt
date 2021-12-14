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
import com.ids.qasemti.BuildConfig
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Activities.ActivitySplash
import com.ids.qasemti.controller.Fragments.*
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.AppConstants.NOTF_TYPE_ACCOUNT_ACTIVATE_DEACTIVATE
import com.ids.qasemti.utils.AppConstants.NOTF_TYPE_NORMAL
import com.ids.qasemti.utils.AppConstants.NOTF_TYPE_ORDERS
import com.ids.qasemti.utils.AppConstants.NOTF_TYPE_SERVICE
import com.ids.qasemti.utils.CallAPIs
import com.ids.qasemti.utils.LocaleUtils
import com.ids.qasemti.utils.logw

import java.util.*


class MyFirebaseMessagingService: FirebaseMessagingService() {

    private val TAG = "MyFirebaseMessagingService"

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)


        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener {
            sendRegistrationToServer(it.result!!.token)
        }


    }

    private fun sendRegistrationToServer(token: String) {

        //Actions.addDevice(this, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {


        // Log.wtf(TAG, "From: " + remoteMessage?.getFrom()!!)

       // ActivityHome().getNotf()


        if (remoteMessage.getData().size > 0) {
            Log.wtf(TAG, "Message data payload: " + remoteMessage.data)
        }

        try{
            if (remoteMessage.notification != null) {
                Log.wtf(TAG, "Message Notification Body: " + remoteMessage.notification!!.body!!)
            }}catch (e:java.lang.Exception){}


        try{Log.wtf("notification_data",remoteMessage.data.toString())}catch (e:java.lang.Exception ){
            Log.wtf("crashNotf",e.toString())
        }

        var typeId = -1
        try {
            typeId = remoteMessage.data["type"]!!.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            typeId = -1
        }
        logw("ACTIVATE_NOW","NOTIFICATION ACT")
        try {
            CallAPIs.getUserInfo()
        }catch (ex:java.lang.Exception){
            logw("ACTIVATE_NOW","CATCH NOTIFICATION ACT DONE")
        }
        logw("ACTIVATE_NOW","NOTIFICATION ACT DONE")

        if(typeId == NOTF_TYPE_ACCOUNT_ACTIVATE_DEACTIVATE){

        }

        var recordId = -1
        try {
            recordId = remoteMessage.data["recordId"]!!.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            recordId = -1
        }

        var id = -1
        try {
            id = remoteMessage.data["id"]!!.toInt()
            Log.wtf("notification_id",id.toString()+"aaa")
        } catch (e: Exception) {
            e.printStackTrace()
            id = -1
        }

        var message = ""
        try {
            message = remoteMessage.data["body"] as String
        } catch (e: Exception) {
            e.printStackTrace()
            message = ""
        }

        var title = ""
        try {
            title = remoteMessage.data["title"] as String
        } catch (e: Exception) {
            e.printStackTrace()
            title = ""
        }

        // setBadge(applicationContext, 0);
        if (MyApplication.generalNotificaiton==1){

            sendNotification(title, typeId, id, message)
       }
    }

    private fun sendNotification(title: String, typeId: Int, id: Int, messageBody: String) {

        lateinit var intent: Intent


        var notificationId = 0
        try {
            notificationId = id
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

        Log.wtf("notificationId",notificationId.toString())
       // MyApplication.isNotficOpened = true
        if (MyApplication.languageCode == AppConstants.LANG_ENGLISH) {
            LocaleUtils.setLocale(Locale("en"))
        } else if (MyApplication.languageCode == AppConstants.LANG_ARABIC) {
            LocaleUtils.setLocale(Locale("ar", "LB"))
        }
        intent = Intent(this, ActivitySplash::class.java)
        intent.putExtra("order_id", id)
        intent.putExtra("typeId", typeId)
        intent.putExtra("text", messageBody)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, MyApplication.UNIQUE_REQUEST_CODE++, intent, PendingIntent.FLAG_ONE_SHOT)

        /*Intent backIntent = new Intent(this, SplashActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, UNIQUE_REQUEST_CODE++, new Intent[] {backIntent, intent}, PendingIntent.FLAG_ONE_SHOT);*/



        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, BuildConfig.APPLICATION_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(messageBody))
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(BuildConfig.APPLICATION_ID, "QASEEMTI", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build())
    }





}