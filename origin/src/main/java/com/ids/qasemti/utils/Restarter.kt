package com.ids.qasemti.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.ids.qasemti.controller.MyApplication

class Restarter : BroadcastReceiver() {
   override fun onReceive(context: Context, intent: Intent?) {
        Log.i("Broadcast Listened", "Service tried to stop")
     //  Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, LocationForeService::class.java))
          //  MyApplication.foregroundOnlyLocationService!!.subscribeToLocationUpdates()
        } else {
            context.startService(Intent(context, LocationForeService::class.java))
        //    MyApplication.foregroundOnlyLocationService!!.subscribeToLocationUpdates()
        }
    }
}