@file:Suppress("NAME_SHADOWING")
package com.ids.qasemti.controller

import com.ids.qasemti.BuildConfig

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore
import com.ids.qasemti.R

import com.ids.qasemti.controller.Fragments.FragmentHomeSP
import com.ids.qasemti.controller.Fragments.FragmentHomeClient
import com.ids.qasemti.controller.MyApplication.Companion.isClient
import com.ids.qasemti.model.FirebaseLocalizeArray
import com.ids.qasemti.model.RequestUpdate
import com.ids.qasemti.model.ResponseUserStatus
import com.ids.qasemti.model.ServiceItem
import com.ids.qasemti.utils.AppConstants
import java.util.*

class MyApplication : Application() {
    companion object {
        var upcoming: Boolean ?=false
        internal lateinit var instance: MyApplication
        var isDebug: Boolean = true
        var selectedFragmentTag : String ?=""
        var typeSelected = 0
        var userStatus : ResponseUserStatus ?=null
        var fromLogout = false
        var settlementTabSelected =0
        var defaultIcon : ImageView ?=null
        var selectedFragment  : Fragment ?=null
        var selectedPos : Int = 2
        var position : Int =0
        var db : FirebaseFirestore?=null
        var tintColor : Int = R.color.white
        var BASE_URL = "http://dev.qasemti.com/wp-json/api/v1/"
        var selectedImage:String ?=""
        var selectedVideo :String ?=""
        var selectedPhone : String ?=""
        var clickable : Boolean ?= true
        var isSignedIn : Boolean = false
        var firstImage : Boolean ?=false
        var selectedService : ServiceItem ?=null
        var showLogs: Boolean = true
        var isClient : Boolean = false
        var fromFooterOrder : Boolean = true
        var localizeArray: FirebaseLocalizeArray ?= null
        lateinit var sharedPreferences : SharedPreferences
        lateinit var sharedPreferencesEditor : SharedPreferences.Editor
        var deviceId : Int
            get() = sharedPreferences.getInt(AppConstants.DEVICE_ID,0)!!
            set(value) { sharedPreferencesEditor.putInt(AppConstants.DEVICE_ID, value).apply() }
        var languageCode : String
            get() = sharedPreferences.getString(AppConstants.SELECTED_LANGUAGE, AppConstants.LANG_ENGLISH)!!
            set(value) { sharedPreferencesEditor.putString(AppConstants.SELECTED_LANGUAGE, value).apply() }
        var UNIQUE_REQUEST_CODE = 0
        var firstTime : Boolean
            get() = sharedPreferences.getBoolean(AppConstants.FIRST_TIME,true)!!
            set(value) { sharedPreferencesEditor.putBoolean(AppConstants.DEVICE_ID, value).apply() }

    }


    override fun onCreate() {
        super.onCreate()

        isClient= BuildConfig.isClient
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
