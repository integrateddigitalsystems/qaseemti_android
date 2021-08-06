@file:Suppress("NAME_SHADOWING")
package com.ids.qasemti.controller

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import androidx.preference.PreferenceManager
import com.ids.qasemti.model.FirebaseLocalizeArray
import com.ids.qasemti.model.FirebaseLocalizeItem
import com.ids.qasemti.utils.AppConstants
import java.util.*
import kotlin.collections.ArrayList

class MyApplication : Application() {
    companion object {
        internal lateinit var instance: MyApplication
        var isDebug: Boolean = true
        var showLogs: Boolean = true
        var localizeArray: FirebaseLocalizeArray ?= null
        var BASE_URL = ""
        lateinit var sharedPreferences : SharedPreferences
        lateinit var sharedPreferencesEditor : SharedPreferences.Editor

        var languageCode : String
            get() = sharedPreferences.getString(AppConstants.SELECTED_LANGUAGE, AppConstants.LANG_ENGLISH)!!
            set(value) { sharedPreferencesEditor.putString(AppConstants.SELECTED_LANGUAGE, value).apply() }
        var UNIQUE_REQUEST_CODE = 0

    }


    override fun onCreate() {
        super.onCreate()

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        sharedPreferencesEditor = sharedPreferences.edit()
        instance=this
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

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
