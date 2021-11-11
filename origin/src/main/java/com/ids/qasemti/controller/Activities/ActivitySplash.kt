package com.ids.qasemti.controller.Activities


import android.app.ActionBar
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.ids.qasemti.BuildConfig
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterCountryCodes
import com.ids.qasemti.controller.Adapters.AdapterServerLinks
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.Fragments.FragmentHomeClient
import com.ids.qasemti.controller.Fragments.FragmentHomeSP
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import com.ids.qasemti.utils.AppConstants.FIREBASE_COUNTRY_NAME_CODE
import com.ids.qasemti.utils.AppConstants.FIREBASE_ENABLE
import com.ids.qasemti.utils.AppConstants.FIREBASE_LINKS
import com.ids.qasemti.utils.AppConstants.FIREBASE_LOCALIZE
import com.ids.qasemti.utils.AppConstants.FIREBASE_PARAMS
import com.ids.qasemti.utils.AppConstants.FIREBASE_SALT
import com.upayments.track.UpaymentGateway
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.dialog_links.*
import kotlinx.android.synthetic.main.loading.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class ActivitySplash : ActivityBase(), ApiListener, RVOnItemClickListener {
    var selectedURL: Boolean? = false
    var URLs: ArrayList<ServerLink> = arrayListOf()
    var dialog: Dialog? = null
    var mFirebaseRemoteConfig: FirebaseRemoteConfig? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        // updateConfig(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        //  openDialog()
        getFirebasePrefs()
    }


    private fun showDialogUpdate(activity: Activity) {

        val builder = AlertDialog.Builder(activity)
        val textView: TextView
        val inflater = activity.layoutInflater
        val textEntryView = inflater.inflate(R.layout.item_dialog, null)
        textView = textEntryView.findViewById(R.id.dialogMsg)
        textView.gravity = Gravity.CENTER
        textView.text = AppHelper.getRemoteString("popup_version_message", this)
        builder.setTitle(AppHelper.getRemoteString("popup_version_title", this))

        builder.setView(textEntryView)
            .setPositiveButton(AppHelper.getRemoteString("popup_version_done", this)) { dialog, _ ->
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
            .setNegativeButton(
                AppHelper.getRemoteString(
                    "popup_version_cancel",
                    this
                )
            ) { dialog, _ ->
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

        textView.text = AppHelper.getRemoteString("popup_version_message", this)
        builder.setTitle(AppHelper.getRemoteString("popup_version_title", this))



        builder.setView(textEntryView)
            .setNegativeButton(AppHelper.getRemoteString("popup_version_done", this)) { dialog, _ ->
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

        var arrayMobileConfiguration = Gson().fromJson(
            mFirebaseRemoteConfig!!.getString(AppConstants.FIREBASE_MOBILE_CONFIGURATION),
            FirebaseConfArray::class.java
        )
        var version =
            arrayMobileConfiguration.android!!.find { it.isClient == BuildConfig.isClient }!!.version!!
        var force =
            arrayMobileConfiguration.android!!.find { it.isClient == BuildConfig.isClient }!!.isForceUpdate!!
        try {
            if (BuildConfig.VERSION_NAME.toDouble() < version) {
                if (force) {
                    showDialogForceUpdate(this)
                } else {
                    showDialogUpdate(this)
                }
            } else {
                nextStep()
            }
        } catch (ex: Exception) {
            nextStep()
        }

    }

    fun getAddress() {
        RetroFitMap.client?.create(RetrofitInterface::class.java)
            ?.getLocationLatLng("32.879087766 , 12.1231233", getString(R.string.googleKey))
            ?.enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    try {
                    } catch (E: java.lang.Exception) {
                    }
                }

                override fun onFailure(call: Call<Any>, throwable: Throwable) {

                }
            })
    }

    fun getMobileConfig() {
        var newReq = RequestNotifications(
            MyApplication.languageCode,
            MyApplication.userId,
            MyApplication.deviceId,
            0,
            10,
            1
        )
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getMobileConfiguration(
            )?.enqueue(object : Callback<ArrayList<ResponseConfiguration>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseConfiguration>>,
                    response: Response<ArrayList<ResponseConfiguration>>
                ) {
                    try {
                    } catch (E: java.lang.Exception) {
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseConfiguration>>,
                    throwable: Throwable
                ) {
                }
            })
    }

    override fun onItemClicked(view: View, position: Int) {
        if (!URLs.get(position).password.isNullOrEmpty()) {
            dialog!!.rlPassword.show()
            dialog!!.btTestPassword.onOneClick {
                if (dialog!!.etLinkPassword.text.toString().equals(URLs.get(position).password)) {
                    MyApplication.BASE_URL = URLs.get(position).urlLink!!
                    dialog!!.dismiss()
                    selectedURL = true
                    setUpRestFirebase()
                } else {
                    AppHelper.createDialog(
                        this,
                        AppHelper.getRemoteString("incorrect_password", this)
                    )
                }
            }
        } else {
            MyApplication.BASE_URL = URLs.get(position).urlLink!!
            dialog!!.dismiss()
            selectedURL = true
            setUpRestFirebase()
        }
    }

    fun showDialog() {

        dialog = Dialog(this, R.style.Base_ThemeOverlay_AppCompat_Dialog)
    //    dialog!!.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE)
        dialog!!.setTitle(AppHelper.getRemoteString("please_choose_link",this))
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.dialog_links)
        dialog!!.setCancelable(false)
        dialog!!.window!!.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        val rv: RecyclerView = dialog!!.findViewById(R.id.rvLinkServers)

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv.layoutManager = layoutManager
        var adapter = AdapterServerLinks(URLs, this)
        rv.adapter = adapter

        dialog!!.show()
    }


    fun nextStep() {
        // getMobileConfig()
        /*MyApplication.isSignedIn = true
        MyApplication.userId = 41*/
        Handler(Looper.getMainLooper()).postDelayed({
            if (MyApplication.firstTime) {
                AppHelper.updateDevice(this, "")
                MyApplication.firstTime = false
                startActivity(Intent(this, ActivityChooseLanguage::class.java))
                finish()
            } else {
                if (MyApplication.isSignedIn) {

                    if (MyApplication.isClient) {
                        UpaymentGateway.init(this, "", "", true)
                        MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_CLIENT
                        MyApplication.selectedFragment = FragmentHomeClient()
                    } else {
                        MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_SP
                        MyApplication.selectedFragment = FragmentHomeSP()
                    }
                    MyApplication.isSignedIn = true
                    /*if(MyApplication.userId==0){
                        if(MyApplication.isClient){
                            MyApplication.userId = 51
                        }else{
                            MyApplication.userId = 41
                        }
                    }*/

                    AppHelper.updateDevice(this, MyApplication.phoneNumber!!)
                    CallAPIs.getUserInfo(this, this)

                } else {
                    AppHelper.updateDevice(this, "")
                    if (MyApplication.isClient) {
                        UpaymentGateway.init(this, "", "", true)
                        /*MyApplication.isSignedIn = true
                        MyApplication.userId = 41*/
                        MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_CLIENT
                        MyApplication.selectedFragment = FragmentHomeClient()

                        startActivity(Intent(this, ActivityHome::class.java))
                        finish()
                        // CallAPIs.getUserInfo(this,this)
                    } else {
                        startActivity(Intent(this, ActivityMobileRegistration::class.java))
                        finish()
                    }
                }
            }
        }, 500)
    }

    fun setUpRestFirebase() {
        try {
            if (!selectedURL!!) {
                var BASE_URLS = Gson().fromJson(
                    mFirebaseRemoteConfig!!.getString(BuildConfig.urls),
                    FirebaseBaseUrlsArray::class.java
                )
                if (BASE_URLS != null && BASE_URLS!!.android!!.size > 0) {
                    var myUrl =
                        BASE_URLS!!.android!!.find { it.version == BuildConfig.VERSION_NAME.toDouble() }
                    if (myUrl != null) {
                        MyApplication.BASE_URL = myUrl.url!!
                    } else
                        MyApplication.BASE_URL =
                            BASE_URLS!!.android!!.maxByOrNull { it.version!! }!!.url!!
                }
            }
        } catch (e: Exception) {
        }
        MyApplication.localizeArray = Gson().fromJson(
            mFirebaseRemoteConfig!!.getString(FIREBASE_LOCALIZE),
            FirebaseLocalizeArray::class.java
        )
        MyApplication.webLinks = Gson().fromJson(
            mFirebaseRemoteConfig!!.getString(FIREBASE_LINKS),
            FirebaseWebData::class.java
        )
        MyApplication.payparams = Gson().fromJson(
            mFirebaseRemoteConfig!!.getString(FIREBASE_PARAMS),
            GatewayRespone::class.java
        )
        MyApplication.enableCountryCodes =
            mFirebaseRemoteConfig!!.getBoolean(FIREBASE_ENABLE)
        MyApplication.countryNameCodes =
            mFirebaseRemoteConfig!!.getString(FIREBASE_COUNTRY_NAME_CODE)
        MyApplication.salt = mFirebaseRemoteConfig!!.getString(FIREBASE_SALT)
        AppHelper.setAllTexts(rootLayout, this)
        checkForUpdate()
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
                    var URLS = Gson().fromJson(
                        mFirebaseRemoteConfig!!.getString(AppConstants.MULTI_LINKS),
                        MultiLink::class.java
                    )
                    if (URLS.enableAndroid!! && MyApplication.firstTime) {
                        URLs.addAll(URLS.serverLink)
                        showDialog()
                    } else {
                        if(MyApplication.BASE_URL.isEmpty()) {
                            selectedURL = false
                        }else{
                            selectedURL = true
                        }
                        setUpRestFirebase()
                    }


                } else {
                    nextStep()
                }
            }

    }

    override fun onDataRetrieved(success: Boolean, response: Any, apiId: Int) {
        var res = response as ResponseUser
        try {
            if (res.user!!.suspended == 1 && MyApplication.isClient) {
                AppHelper.createDialog(this, AppHelper.getRemoteString("suspended_user_msg", this))
            } else {
                startActivity(Intent(this, ActivityHome::class.java))
                finish()
            }
        } catch (ex: Exception) {
            Log.wtf("apiSplash", ex.toString())
            startActivity(Intent(this, ActivityMobileRegistration::class.java))
        }

    }
}
