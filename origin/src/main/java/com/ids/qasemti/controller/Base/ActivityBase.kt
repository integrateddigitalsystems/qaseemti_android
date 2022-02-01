@file:Suppress("NAME_SHADOWING")
package com.ids.qasemti.controller.Base

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Activities.ActivitySettlements
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.LocaleUtils
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*
import kotlin.collections.ArrayList
import com.uxcam.UXCam;

open class ActivityBase : Activity() {
    private var decorView: View? = null
    init {
        AppHelper.setLocal(this)
        LocaleUtils.updateConfig(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        AppHelper.setLocal(this)
        AppHelper.handleCrashes(this)
        UXCam.startWithKey("4447rogmfdtw87p")
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
