@file:Suppress("NAME_SHADOWING")
package com.ids.qasemti.controller

import com.ids.qasemti.BuildConfig

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.ids.qasemti.controller.Fragments.FragmentHomeSP
import com.ids.qasemti.controller.Fragments.FragmentHomeClient
import com.ids.qasemti.model.FirebaseLocalizeArray
import com.ids.qasemti.model.ServiceItem
import com.ids.qasemti.utils.AppConstants
import java.util.*

class MyApplication : Application() {
    companion object {
        internal lateinit var instance: MyApplication
        var isDebug: Boolean = true
        var selectedFragmentTag : String ?=""
        var typeSelected = 1
        var selectedFragment  : Fragment ?=null
        var selectedPos : Int = 2
        var selectedImage:String ?=""
        var selectedVideo :String ?=""
        var isSignedIn : Boolean = false
        var firstImage : Boolean ?=false
        var selectedService : ServiceItem ?=null
        var showLogs: Boolean = true
        var isClient : Boolean = false
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

        isClient=BuildConfig.isClient
        if(isClient)
            selectedFragment=FragmentHomeClient()
        else
            selectedFragment= FragmentHomeSP()
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
