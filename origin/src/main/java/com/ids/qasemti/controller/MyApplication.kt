@file:Suppress("NAME_SHADOWING")
package com.ids.qasemti.controller

import com.ids.qasemti.BuildConfig

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.location.Address
import android.os.Build
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore
import com.ids.qasemti.R

import com.ids.qasemti.controller.Fragments.FragmentHomeSP
import com.ids.qasemti.controller.Fragments.FragmentHomeClient
import com.ids.qasemti.controller.MyApplication.Companion.isClient
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.AppConstants
import java.util.*
import kotlin.collections.ArrayList

class MyApplication : Application() {
    companion object {
        var upcoming: Boolean ?=false
        internal lateinit var instance: MyApplication
        var isDebug: Boolean = true
        var selectedFragmentTag : String ?=""
        var typeSelected = 0
        var renewed = false
        var fromAdd : Boolean ?= false
        var rental : Boolean ?=false
        var finish : Boolean ?=false
        var latSelected : Double ?=0.0
        var longSelected : Double ?=0.0
        var userStatus : ResponseUserStatus ?=null
        var fromLogout = false
        var register = false
        var addNewAddress = true 
        var settlementTabSelected =0
        var defaultIcon : ImageView ?=null
        var selectedFragment  : Fragment ?=null
        var selectedPos : Int = 2
        var fromProfile : Boolean ?= false
        var position : Int =0
        var db : FirebaseFirestore?=null
        var tintColor : Int = R.color.white
        var BASE_URL = "http://dev.qasemti.com/wp-json/api/v1/"
        var selectedImage:String ?=""
        var selectedVideo :String ?=""
        var selectedPhone : String ?="03/123123"
        var clickable : Boolean ?= true
        var isSignedIn : Boolean
            get() = sharedPreferences.getBoolean(AppConstants.SIGNED_IN,false)!!
            set(value) { sharedPreferencesEditor.putBoolean(AppConstants.SIGNED_IN, value).apply() }

        var selectedUser : User ?=null
        var firstImage : Boolean ?=false
        var selectedService : ResponseService ?=null
        var selectedOrder : ResponseOrders?=null
        var selectedPlaceOrder : RequestPlaceOrder ?=null
        var selectedAddress : ResponseAddress ?=null
        var selectedCurrentAddress : Address ?=null
        var selectedSize = ""
        var selectedPrice = ""
        var selectedVariationType = ""

        var showLogs: Boolean = true
        var isClient : Boolean = false
        var fromFooterOrder : Boolean = true
        var localizeArray: FirebaseLocalizeArray ?= null
        var arrayCart : ArrayList<RequestPlaceOrder> = arrayListOf()
        var seletedPosCart = 0
        lateinit var sharedPreferences : SharedPreferences
        lateinit var sharedPreferencesEditor : SharedPreferences.Editor
        var deviceId : Int
            get() = sharedPreferences.getInt(AppConstants.DEVICE_ID,0)
            set(value) { sharedPreferencesEditor.putInt(AppConstants.DEVICE_ID, value).apply() }
        var languageCode : String
            get() = sharedPreferences.getString(AppConstants.SELECTED_LANGUAGE, AppConstants.LANG_ENGLISH)!!
            set(value) { sharedPreferencesEditor.putString(AppConstants.SELECTED_LANGUAGE, value).apply() }
        var UNIQUE_REQUEST_CODE = 0
        var firstTime : Boolean
            get() = sharedPreferences.getBoolean(AppConstants.FIRST_TIME,true)
            set(value) { sharedPreferencesEditor.putBoolean(AppConstants.FIRST_TIME, value).apply() }
        var userId : Int
            get() = sharedPreferences.getInt(AppConstants.USER_ID,6)
            set(value) { sharedPreferencesEditor.putInt(AppConstants.USER_ID, value).apply() }
        var cartItems : String?
            get() = sharedPreferences.getString(AppConstants.ARRAY_CARTS,"")
            set(value) { sharedPreferencesEditor.putString(AppConstants.ARRAY_CARTS, value).apply() }
        var notfType : Int?
            get() = sharedPreferences.getInt(AppConstants.ARRAY_CARTS,1)
            set(value) { sharedPreferencesEditor.putInt(AppConstants.ARRAY_CARTS, value!!).apply() }
        var phoneNumber : String?
            get() = sharedPreferences.getString(AppConstants.PHONE_NUMBER,"")
            set(value) { sharedPreferencesEditor.putString(AppConstants.PHONE_NUMBER, value).apply() }

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
