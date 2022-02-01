@file:Suppress("NAME_SHADOWING")
package com.ids.qasemti.controller.Base

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Activities.ActivitySettlements
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.LocaleUtils
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*
import kotlin.collections.ArrayList

open class ActivityBase : Activity() , LifecycleObserver {
    private var decorView: View? = null
    init {
        AppHelper.setLocal(this)
        LocaleUtils.updateConfig(this)
    }


    override fun onPause() {
        super.onPause()
        MyApplication.backgrounded = true
        MyApplication.foregrounded = false
        if(MyApplication.destroyed)
            MyApplication.appAlive = false
        else
            MyApplication.appAlive = true
    }

    override fun onResume() {
        super.onResume()
        MyApplication.backgrounded = false
        MyApplication.foregrounded = true
        MyApplication.appAlive = true
    }

   /* @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onAppBackgrounded() {
        MyApplication.backgrounded = true
        MyApplication.foregrounded = false
        MyApplication.appAlive = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onAppForegrounded() {
        MyApplication.backgrounded = false
        MyApplication.foregrounded = true
        MyApplication.appAlive = true
    }*/

    override fun onDestroy() {
        super.onDestroy()
        if(MyApplication.backgrounded){
            MyApplication.backgrounded = false
            MyApplication.appAlive = false
            MyApplication.destroyed = true
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {

        AppHelper.handleCrashes(this)
        super.onCreate(savedInstanceState)
        AppHelper.setLocal(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        /*if(this is ActivitySettlements){
            AppHelper.createDialog(this,"Banned"){
                super.onBackPressed()
            }
        }*/
      //  AppHelper.startService(this)

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
