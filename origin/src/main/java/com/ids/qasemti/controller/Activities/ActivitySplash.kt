package com.ids.qasemti.controller.Activities


import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.ids.qasemti.BuildConfig
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.Fragments.FragmentHomeClient
import com.ids.qasemti.controller.Fragments.FragmentHomeSP
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*

import com.ids.qasemti.utils.AppConstants.FIREBASE_FORCE_UPDATE
import com.ids.qasemti.utils.AppConstants.FIREBASE_LOCALIZE
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.loading.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivitySplash : ActivityBase() {
    var mFirebaseRemoteConfig: FirebaseRemoteConfig? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //  MyApplication.isLoggedIn = true

        getFirebasePrefs()
      //  getMobileConfig()
       //getAddress()
    }

    private fun showDialogUpdate(activity: Activity) {

        val builder = AlertDialog.Builder(activity)
        val textView: TextView
        val inflater = activity.layoutInflater
        val textEntryView = inflater.inflate(R.layout.item_dialog, null)
        textView = textEntryView.findViewById(R.id.dialogMsg)
        textView.gravity = Gravity.CENTER
        textView.text = AppHelper.getRemoteString("popup_version_message",this)
        builder.setTitle(AppHelper.getRemoteString("popup_version_title",this))

        builder.setView(textEntryView)
            .setPositiveButton(AppHelper.getRemoteString("popup_version_done",this)) { dialog, _ ->
                dialog.dismiss()
                val appPackageName = activity.packageName
                try {
                    activity.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$appPackageName")
                        )
                    )

                    activity.finish()

                } catch (anfe: android.content.ActivityNotFoundException) {
                    activity.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                        )
                    )
                    activity.finish()

                }
            }
            .setNegativeButton(AppHelper.getRemoteString("popup_version_cancel",this)) { dialog, _ ->
                nextStep()

            }

        val d = builder.create()
        d.setOnShowListener {

            d.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            d.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            d.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).transformationMethod = null
            d.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).isAllCaps = false

            d.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            d.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            d.getButton(android.app.AlertDialog.BUTTON_POSITIVE).transformationMethod = null
            d.getButton(android.app.AlertDialog.BUTTON_POSITIVE).isAllCaps = false
        }
        d.setCancelable(false)

        d.show()

    }

    private fun showDialogForceUpdate(activity: Activity) {


        val builder = AlertDialog.Builder(activity)
        val textView: TextView
        val inflater = activity.layoutInflater
        val textEntryView = inflater.inflate(R.layout.item_dialog, null)
        textView = textEntryView.findViewById(R.id.dialogMsg)
        textView.gravity = Gravity.START

        textView.text = AppHelper.getRemoteString("popup_version_message",this)
        builder.setTitle(AppHelper.getRemoteString("popup_version_title",this))



        builder.setView(textEntryView)
            .setNegativeButton(AppHelper.getRemoteString("popup_version_done",this)) { dialog, _ ->
                dialog.dismiss()
                val appPackageName = activity.packageName
                try {

                    activity.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + appPackageName)
                        )
                    )
                    activity.finish()

                } catch (anfe: ActivityNotFoundException) {

                    activity.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)
                        )
                    )
                    activity.finish()

                }
            }
        val d = builder.create()
        d.setOnShowListener {
            d.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this@ActivitySplash, R.color.colorPrimary))
            d.getButton(AlertDialog.BUTTON_NEGATIVE).transformationMethod = null
            d.getButton(AlertDialog.BUTTON_NEGATIVE).isAllCaps = false
        }



        d.show()
    }

    fun checkForUpdate() {

        var arrayMobileConfiguration = Gson().fromJson(mFirebaseRemoteConfig!!.getString(AppConstants.FIREBASE_MOBILE_CONFIGURATION), FirebaseConfArray::class.java)
        var version=arrayMobileConfiguration.android!!.find { it.isClient == BuildConfig.isClient }!!.version!!
        var force=arrayMobileConfiguration.android!!.find { it.isClient == BuildConfig.isClient }!!.isForceUpdate!!
        try {
            if (BuildConfig.VERSION_NAME.toDouble() < version) {
                if (force) {
                    showDialogForceUpdate(this)
                } else {
                    showDialogUpdate(this)
                }
            }else{
                nextStep()
           }
        } catch (ex: Exception) {

        }

    }

    fun getAddress(){
        RetroFitMap.client?.create(RetrofitInterface::class.java)
            ?.getLocationLatLng("32.879087766 , 12.1231233",getString(R.string.googleKey))?.enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    try{
                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<Any>, throwable: Throwable) {

                }
            })
    }

    fun getMobileConfig(){
        var newReq = RequestNotifications(MyApplication.languageCode,MyApplication.userId,MyApplication.deviceId,0,10,1)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getMobileConfiguration(
            )?.enqueue(object : Callback<ArrayList<ResponseConfiguration>> {
                override fun onResponse(call: Call<ArrayList<ResponseConfiguration>>, response: Response<ArrayList<ResponseConfiguration>>) {
                    try{
                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseConfiguration>>, throwable: Throwable) {
                }
            })
    }



    fun nextStep() {
       // getMobileConfig()
      /*  MyApplication.isSignedIn = true
        MyApplication.userId = 41*/
        Handler(Looper.getMainLooper()).postDelayed({
            if(MyApplication.firstTime) {
                AppHelper.updateDevice(this,"")
                MyApplication.firstTime = false
                startActivity(Intent(this, ActivityChooseLanguage::class.java))
                finish()
            }else{
                if(MyApplication.isSignedIn) {
                    AppHelper.updateDevice(this,MyApplication.phoneNumber!!)
                    AppHelper.getUserInfo()
                    if (MyApplication.isClient) {
                        MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_CLIENT
                        MyApplication.selectedFragment = FragmentHomeClient()
                    } else {
                        MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_SP
                        MyApplication.selectedFragment = FragmentHomeSP()
                    }
                    MyApplication.isSignedIn = true
                    startActivity(Intent(this, ActivityHome::class.java))
                    finish()
                }else{
                    AppHelper.updateDevice(this,"")
                    if(MyApplication.isClient){
                        /*MyApplication.isSignedIn = true
                        MyApplication.userId = 41*/
                        AppHelper.getUserInfo()
                        MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_CLIENT
                        MyApplication.selectedFragment = FragmentHomeClient()
                        startActivity(Intent(this, ActivityHome::class.java))
                    }
                    else {
                        startActivity(Intent(this, ActivityMobileRegistration::class.java))
                        finish()
                    }
                }
            }
        }, 500)
    }

    private fun getFirebasePrefs() {
        MyApplication.db = Firebase.firestore
        mFirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        mFirebaseRemoteConfig!!.setConfigSettingsAsync(configSettings)
        mFirebaseRemoteConfig!!.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    try{
                        var BASE_URLS = Gson().fromJson(mFirebaseRemoteConfig!!.getString(BuildConfig.urls), FirebaseBaseUrlsArray::class.java)
                        if(BASE_URLS!=null && BASE_URLS!!.android!!.size>0){
                            var myUrl=BASE_URLS!!.android!!.find { it.version == BuildConfig.VERSION_NAME.toDouble() }
                            if(myUrl!=null){
                                MyApplication.BASE_URL=myUrl.url!!
                            }else
                                MyApplication.BASE_URL=BASE_URLS!!.android!!.maxByOrNull { it.version!! }!!.url!!
                        }}catch (e:Exception){}
                    MyApplication.localizeArray = Gson().fromJson(mFirebaseRemoteConfig!!.getString(FIREBASE_LOCALIZE), FirebaseLocalizeArray::class.java)
                    AppHelper.setAllTexts(rootLayout, this)
                    checkForUpdate()
                }


            }

    }
}
