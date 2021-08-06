@file:Suppress("NAME_SHADOWING")
package com.ids.qasemti.controller.Base

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.LocaleUtils
import java.util.*

open class ActivityBase : Activity() {
    private var decorView: View? = null
    init {
        LocaleUtils.updateConfig(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         AppHelper.setLocal(this)
         AppHelper.handleCrashes(this)
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
