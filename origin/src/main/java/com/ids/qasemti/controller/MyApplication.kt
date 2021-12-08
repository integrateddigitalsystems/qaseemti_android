@file:Suppress("NAME_SHADOWING")
package com.ids.qasemti.controller

import android.app.Activity
import com.ids.qasemti.BuildConfig

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.location.Address
import android.location.Location
import android.os.Build
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.firebase.firestore.FirebaseFirestore
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome

import com.ids.qasemti.controller.Fragments.FragmentHomeSP
import com.ids.qasemti.controller.Fragments.FragmentHomeClient
import com.ids.qasemti.controller.MyApplication.Companion.isClient
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.LocationForeService
import com.ids.qasemti.utils.toText
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MyApplication : Application() {
    companion object {
        var upcoming: Boolean ?=false
        internal lateinit var instance: MyApplication
        var isDebug: Boolean = true
        var renewing  = false
        var addNew : Boolean = true
        var selectedFragmentTag : String ?=""
        var currency : String ?=""
        var fromOrderPlaced  =false
        var webLinks : FirebaseWebData ?=null
        var kuwaitCoordinates : Coordinates ?=null
        var salt : String ?=""
        var payparams : GatewayRespone ?=null
        var enableCountryCodes : Boolean ?=false
        var countryNameCodes : String ?=""
        var typeSelected = 0
        var relatedOrders : ArrayList<RelatedOrder> = arrayListOf()
        var renewed = false
        var completed = false
        var isBroadcast = false
        var serviceContext : Activity ?=null
        var fromAccount = false
        var fromAdd : Boolean ?= false
        var submitted : Boolean ?=false
        var categories : ArrayList<ResponseCategories> = arrayListOf()
        var selectedTitle : String ?=""
        var bannedActs : ArrayList<Activity> = arrayListOf()
        var rental : Boolean ?=false
        var finish : Boolean ?=false
        var latSelected : Double ?=0.0
        var longSelected : Double ?=0.0
        //var userStatus : ResponseUserStatus ?=null
        var fromLogout = false
        var register = false
        var selectedOrderTrack : ResponseOrders ?=null
        var selectedItemDialog="+965"
        var addNewAddress = true

        var settlementTabSelected =0
        var defaultIcon : ImageView ?=null
        var selectedFragment  : Fragment ?=null
        var selectedPos : Int = 2
        var fromProfile : Boolean ?= false
        var position : Int =0
        var db : FirebaseFirestore?=null
        var foregroundOnlyLocationService: LocationForeService? = null
        var tintColor : Int = R.color.white
        var selectedImage:String ?=""
        var selectedVideo :String ?=""
        var selectedPhone : String ?=""
        var clickable : Boolean ?= true
        var isSignedIn : Boolean
            get() = sharedPreferences.getBoolean(AppConstants.SIGNED_IN,false)!!
            set(value) { sharedPreferencesEditor.putBoolean(AppConstants.SIGNED_IN, value).apply() }

        var selectedUser : User ?=null
        var temporaryProfile : User ?=null
        var tempProfilePic : File ?=null
        var tempCivilId : File?=null
        var firstImage : Boolean ?=false
        var selectedService : ResponseService ?=null
        var rentalId : Int ?=0
        var purchaseId : Int ?=0
        var selectedOrder : ResponseOrders?=null
        var selectedPlaceOrder : RequestPlaceOrder ?=null
        var selectedAddress : ResponseAddress ?=null
        var selectedCurrentAddress : ResponseAddress ?=null
        var selectedSize : Int ?=0
        var selectedLatLngCall : LatLng ?=null
        var selectedPrice = ""
        var enableForeTracking = false
        var selectedVariationType : Int ?=0
        var selectedNotSignedInType : Int ?= -1
        var selectedNotSignedInSize : Int ?= -1
        var placesList : ArrayList<String> = arrayListOf()
        var trackingActivity : Activity ?=null
        var fromOrderDetails : Boolean = false
        var showLogs: Boolean = true
        var isClient : Boolean = false
        var isEditService : Boolean = false
        var fromSplash : Boolean = false
        var fromFooterOrder : Boolean = true
        var localizeArray: FirebaseLocalizeArray ?= null
        var kuwaitGovs : ArrayList<ResponseGovernant> = arrayListOf()
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
        var BASE_URL : String
            get() = sharedPreferences.getString(AppConstants.BASE_URL,"")!!
            set(value) { sharedPreferencesEditor.putString(AppConstants.BASE_URL, value).apply() }
        var firstTime : Boolean
            get() = sharedPreferences.getBoolean(AppConstants.FIRST_TIME,true)
            set(value) { sharedPreferencesEditor.putBoolean(AppConstants.FIRST_TIME, value).apply() }
        var userId : Int
            get() = sharedPreferences.getInt(AppConstants.USER_ID,0)
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
        var saveLocationTracking  : Boolean?
            get() = sharedPreferences.getBoolean(AppConstants.MAPPING,false)
            set(value) { sharedPreferencesEditor.putBoolean(AppConstants.MAPPING, value!!).apply() }
        var permissionAllow11  : Int?
            get() = sharedPreferences.getInt(AppConstants.PERMISSION,0)
            set(value) { sharedPreferencesEditor.putInt(AppConstants.PERMISSION, value!!).apply() }
        var termsCondition  : Boolean?
            get() = sharedPreferences.getBoolean(AppConstants.TERMS_CONDITIONS,false)
            set(value) { sharedPreferencesEditor.putBoolean(AppConstants.TERMS_CONDITIONS, value!!).apply() }
        var trackOrderId : Int ?
        get() = sharedPreferences.getInt(AppConstants.TRACKING_ORDER_ID,0)
        set(value) { sharedPreferencesEditor.putInt(AppConstants.TRACKING_ORDER_ID,value!!).apply()}
        var generalNotificaiton : Int ?
        get() = sharedPreferences.getInt(AppConstants.GENERAL_NOTF,1)
        set(value) { sharedPreferencesEditor.putInt(AppConstants.GENERAL_NOTF,value!!)}


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
