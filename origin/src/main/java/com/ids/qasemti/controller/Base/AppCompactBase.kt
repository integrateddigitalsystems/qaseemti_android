package com.ids.qasemti.controller.Base

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.LocaleUtils

import java.util.*
import com.ids.qasemti.utils.Restarter





open class AppCompactBase : AppCompatActivity() {
    private var decorView: View? = null

    init {
        LocaleUtils.updateConfig(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppHelper.setLocal(this)
        AppHelper.handleCrashes(this)
    }


    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()

        val restartServiceIntent = Intent(
            applicationContext, this.javaClass
        )
        restartServiceIntent.setPackage(packageName)

        val restartServicePendingIntent: PendingIntent = PendingIntent.getService(
            applicationContext,
            1,
            restartServiceIntent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val alarmService: AlarmManager =
            applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmService.set(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + 1,
            restartServicePendingIntent
        )
       /* if(MyApplication.saveLocationTracking!!) {
            val broadcastIntent = Intent()
            broadcastIntent.setAction("restartservice")
            broadcastIntent.setClass(this, Restarter::class.java)
            this.sendBroadcast(broadcastIntent)
        }*/

    }


    override fun attachBaseContext(newBase: Context) {
        var newBase = newBase
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val config = newBase.resources.configuration
            config.setLocale(Locale.getDefault())
            newBase = newBase.createConfigurationContext(config)
        }
        super.attachBaseContext(newBase)
    }
}
