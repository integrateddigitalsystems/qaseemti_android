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
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.ids.qasemti.BuildConfig
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterServerLinks
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.Fragments.*
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import com.ids.qasemti.utils.AppConstants.API_USER_STATUS
import com.ids.qasemti.utils.AppConstants.BANNER_TIME
import com.ids.qasemti.utils.AppConstants.COORDINATES
import com.ids.qasemti.utils.AppConstants.CURRENCY
import com.ids.qasemti.utils.AppConstants.FIREBASE_COUNTRY_NAME_CODE
import com.ids.qasemti.utils.AppConstants.FIREBASE_DISTANCE
import com.ids.qasemti.utils.AppConstants.FIREBASE_ENABLE
import com.ids.qasemti.utils.AppConstants.FIREBASE_GOVS
import com.ids.qasemti.utils.AppConstants.FIREBASE_LINKS
import com.ids.qasemti.utils.AppConstants.FIREBASE_LOCALIZE
import com.ids.qasemti.utils.AppConstants.FIREBASE_PARAMS
import com.ids.qasemti.utils.AppConstants.FIREBASE_SALT
import com.upayments.track.UpaymentGateway
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.dialog_links.*
import kotlinx.android.synthetic.main.footer.*
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
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        // updateConfig(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        //  openDialog()
        //CallAPIs.getAddressName("33.59608186012923,35.39359968155622",this,this)


        getFirebasePrefs()


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        checkForUpdate()


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

    fun setUpActivities() {
        MyApplication.bannedActs.add(ActivitySettlements())
    }

    fun getMobileConfig() {
        var newReq = RequestNotifications(
            MyApplication.languageCode,
            MyApplication.selectedUser!!.mobileNumber,
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
        dialog!!.setTitle(AppHelper.getRemoteString("please_choose_link", this))
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





        CallAPIs.getCategories(this, this)
        Handler(Looper.getMainLooper()).postDelayed({
            if (MyApplication.firstTime || !MyApplication.termsCondition!!) {
                if(MyApplication.isClient)
                    UpaymentGateway.init(this, "", "", true)
                MyApplication.selectedPhone = ""
                CallAPIs.updateDevice(this, this)
                // AppHelper.updateDevice(this, "")
                MyApplication.firstTime = false
                finish()
                startActivity(Intent(this, ActivityChooseLanguage::class.java))

            } else {

                if (MyApplication.isSignedIn) {

                    var from = 0
                    try {
                        from = intent.getIntExtra("fromNotf", 0)
                    } catch (ex: Exception) {
                        from = 0
                    }
                    var type = 0
                    try {
                        type = intent.getIntExtra("Type", 0)
                    } catch (ex: Exception) {
                        type = -1
                    }

                    var orderId = -1
                    try{
                        orderId = intent.getIntExtra("orderId",-1)
                    }catch (ex:Exception){

                    }

                    var crash = false
                    try{
                        crash = intent.getBooleanExtra("crash",false)
                    }catch (ex:Exception){

                    }

                  /*  orderId = 7851
                    type = AppConstants.NOTF_TYPE_ACCEPT_ORDER*/

                    if(!MyApplication.trackOrderIdList.isNullOrEmpty())
                    {
                        try{
                            AppHelper.GsontoArrString()
                        }catch (ex:Exception){}
                    }

                    try{
                        AppHelper.GsontoArrDone()
                    }catch (ex:Exception){

                    }

                    if (MyApplication.isClient) {

                        try {
                            UpaymentGateway.init(this, "", "", true)
                        }catch (ex:Exception){
                            logw("UpaymError",ex.toString())
                        }
                        if(from == 1 && !crash){
                        if (type == AppConstants.NOTF_TYPE_ACCOUNT_ACTIVATE_DEACTIVATE) {
                            MyApplication.selectedPos = 3
                            MyApplication.defaultIcon = ivFooterNotifications
                            MyApplication.selectedFragment = FragmentNotifications()
                            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_NOTFICATIONS
                            MyApplication.tintColor = R.color.primary
                        } else if (type == AppConstants.NOTF_TYPE_BROADCAST_ORDERS) {
                            MyApplication.selectedPos = 2
                            MyApplication.defaultIcon = ivFooterHome
                            MyApplication.tintColor = R.color.primary
                            MyApplication.selectedFragment = FragmentHomeClient()
                            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_CLIENT
                        } else if (type == AppConstants.NOTF_TYPE_SERVICE) {
                            MyApplication.selectedPos = 2
                            MyApplication.defaultIcon = ivFooterHome
                            MyApplication.tintColor = R.color.primary
                            MyApplication.selectedFragment = FragmentHomeClient()
                            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_CLIENT
                        } else if (type == AppConstants.NOTF_TYPE_NORMAL) {
                            MyApplication.selectedPos = 3
                            MyApplication.defaultIcon = ivFooterNotifications
                            MyApplication.selectedFragment = FragmentNotifications()
                            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_NOTFICATIONS
                            MyApplication.tintColor = R.color.primary
                        } else if (type == AppConstants.NOTF_TYPE_ACCEPT_ORDER){
                            MyApplication.selectedPos = 0
                            MyApplication.defaultIcon = ivCartFooter
                            MyApplication.tintColor = R.color.primary
                            MyApplication.selectedFragment = FragmentCart()
                            if(orderId!=-1){
                                MyApplication.selectedOrderId = orderId
                                MyApplication.toDetails = true
                            }else{
                                MyApplication.toDetails = false
                            }
                            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_CART
                        }else if (type == AppConstants.NOTF_TYPE_SUGGEST_NEW_DATE  || type == AppConstants.NOTF_PAYMENT_ADDED){
                            MyApplication.selectedPos = 1
                            MyApplication.defaultIcon = ivFooterOrder
                            MyApplication.tintColor = R.color.primary
                            MyApplication.selectedFragment = FragmentOrders()
                            if(orderId!=-1){
                                MyApplication.selectedOrderId = orderId
                                MyApplication.toDetails = true
                            }else{
                                MyApplication.toDetails = false
                            }
                            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_ORDER
                        } else {
                            MyApplication.selectedPos = 3
                            MyApplication.defaultIcon = ivFooterNotifications
                            MyApplication.selectedFragment = FragmentNotifications()
                            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_NOTFICATIONS
                            MyApplication.tintColor = R.color.primary
                        }
                        }else{
                            MyApplication.selectedPos = 2
                            MyApplication.defaultIcon = ivFooterHome
                            MyApplication.tintColor = R.color.primary
                            MyApplication.selectedFragment = FragmentHomeClient()
                            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_CLIENT
                        }


                    } else {

                        if(from == 1 && !crash){

                        if (type == AppConstants.NOTF_TYPE_ACCOUNT_ACTIVATE_DEACTIVATE) {
                            MyApplication.selectedPos = 3
                            MyApplication.defaultIcon = ivFooterNotifications
                            MyApplication.selectedFragment = FragmentNotifications()
                            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_NOTFICATIONS
                            MyApplication.tintColor = R.color.primary
                        } else if (type == AppConstants.NOTF_TYPE_BROADCAST_ORDERS) {
                            MyApplication.selectedPos = 2
                            MyApplication.defaultIcon = ivFooterHome
                            MyApplication.selectedFragment = FragmentHomeSP()
                            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_SP
                            MyApplication.tintColor = R.color.primary
                            if(orderId!=-1){
                                MyApplication.isBroadcast = true
                                MyApplication.selectedOrderId = orderId
                                MyApplication.toDetails = true
                            }else{
                                MyApplication.toDetails = false
                            }
                        } else if (type == AppConstants.NOTF_TYPE_SERVICE) {
                            MyApplication.selectedPos = 0
                            MyApplication.defaultIcon = ivProductFooter
                            MyApplication.tintColor = R.color.primary
                            MyApplication.selectedFragment = FragmentMyServices()
                            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_MY_SERVICES

                        } else if (type == AppConstants.NOTF_TYPE_NORMAL) {
                            MyApplication.selectedPos = 3
                            MyApplication.defaultIcon = ivFooterNotifications
                            MyApplication.selectedFragment = FragmentNotifications()
                            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_NOTFICATIONS
                            MyApplication.tintColor = R.color.primary
                        }else if (type == AppConstants.NOTF_TYPE_ACCEPT_ORDER){
                            MyApplication.selectedPos = 1
                            MyApplication.defaultIcon = ivFooterOrder
                            MyApplication.tintColor = R.color.primary
                            MyApplication.selectedFragment = FragmentOrders()
                            if(orderId!=-1){
                                MyApplication.selectedOrderId = orderId
                                MyApplication.toDetails = true
                            }else{
                                MyApplication.toDetails = false
                            }
                            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_ORDER
                        } else if (type == AppConstants.NOTF_TYPE_SUGGEST_NEW_DATE || type == AppConstants.NOTF_PAYMENT_ADDED){
                            MyApplication.selectedPos = 1
                            MyApplication.defaultIcon = ivFooterOrder
                            MyApplication.tintColor = R.color.primary
                            if(orderId!=-1){
                                MyApplication.selectedOrderId = orderId
                                MyApplication.toDetails = true
                            }else{
                                MyApplication.toDetails = false
                            }
                            MyApplication.selectedFragment = FragmentOrders()
                            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_ORDER
                        }else  {
                            MyApplication.selectedPos = 3
                            MyApplication.defaultIcon = ivFooterNotifications
                            MyApplication.selectedFragment = FragmentNotifications()
                            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_NOTFICATIONS
                            MyApplication.tintColor = R.color.primary
                        } }else {
                            MyApplication.selectedPos = 2
                            MyApplication.defaultIcon = ivFooterHome
                            MyApplication.selectedFragment = FragmentHomeSP()
                            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_SP
                            MyApplication.tintColor = R.color.primary
                        }
                    }
                    MyApplication.isSignedIn = true
                    /*if(MyApplication.userId==0){
                        if(MyApplication.isClient){
                            MyApplication.userId = 51
                        }else{
                            MyApplication.userId = 41
                        }
                    }*/

                    MyApplication.selectedPhone = MyApplication.phoneNumber
                    if(MyApplication.toDetails){
                        CallAPIs.getOrderByOrderId(orderId,this)
                    }else {
                        CallAPIs.updateDevice(this, this)
                        //AppHelper.updateDevice(this, MyApplication.phoneNumber!!)
                        CallAPIs.getUserInfo(this)
                    }

                } else {
                    MyApplication.selectedPhone = ""
                    CallAPIs.updateDevice(this, this)
                    //AppHelper.updateDevice(this, "")
                    if (MyApplication.isClient) {
                        UpaymentGateway.init(this, "", "", true)
                        /*MyApplication.isSignedIn = true
                        MyApplication.userId = 41*/
                        MyApplication.selectedPos = 2
                        MyApplication.defaultIcon = ivFooterHome
                        MyApplication.tintColor = R.color.primary
                        MyApplication.selectedFragment = FragmentHomeClient()
                        MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_CLIENT

                        finish()
                        startActivity(Intent(this, ActivityHome::class.java))

                        // CallAPIs.getUserInfo(this,this)
                    } else {
                        MyApplication.selectedPos = 2
                        MyApplication.defaultIcon = ivFooterHome
                        MyApplication.tintColor = R.color.primary
                        MyApplication.selectedFragment = FragmentHomeSP()
                        MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_SP
                        finish()
                        startActivity(Intent(this, ActivityMobileRegistration::class.java))

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
        var list = Gson().fromJson(
            mFirebaseRemoteConfig!!.getString(FIREBASE_GOVS),
            KuwaitGovs::class.java
        )
        MyApplication.adTimer = mFirebaseRemoteConfig!!.getString(BANNER_TIME).toInt()
        MyApplication.kuwaitGovs.clear()
        MyApplication.kuwaitGovs.addAll(list.list)
        MyApplication.localizeArray = Gson().fromJson(
            mFirebaseRemoteConfig!!.getString(FIREBASE_LOCALIZE),
            FirebaseLocalizeArray::class.java
        )
        MyApplication.currency = mFirebaseRemoteConfig!!.getString(CURRENCY)
        MyApplication.webLinks = Gson().fromJson(
            mFirebaseRemoteConfig!!.getString(FIREBASE_LINKS),
            FirebaseWebData::class.java
        )

        var typePrefix = MyApplication.BASE_URL.split("wp-json").get(0)


        for(item in MyApplication.webLinks!!.links){
            item.urlAr = typePrefix+item.urlAr
            item.urlEn= typePrefix+item.urlEn
        }

        val wv= WebView(this)
        wv.settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wv.loadUrl(MyApplication.webLinks!!.links.find { it.idNo == 2  }!!.urlEn!!)

        val wv2= WebView(this)
        wv2.settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wv2.loadUrl(MyApplication.webLinks!!.links.find { it.idNo == 2  }!!.urlAr!!)
        MyApplication.kuwaitCoordinates = Gson().fromJson(
            mFirebaseRemoteConfig!!.getString(COORDINATES),
            Coordinates::class.java
        )

        MyApplication.notifyDistance = mFirebaseRemoteConfig!!.getString(FIREBASE_DISTANCE).toInt()
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


        //testing should be removed........................................................
       // MyApplication.BASE_URL = BuildConfig.BASE_URL
        checkForUpdate()

        /*     if(MyApplication.termsCondition!!)
                 checkForUpdate()
             else {
                 MyApplication.fromSplash = true
                 startActivityForResult(Intent(this, ActivityWeb::class.java)
                     .putExtra("webTitle", AppHelper.getRemoteString("TermsAndConditions",this))
                     .putExtra("webId",2), 1000)
             }*/

        //1D$q@semt!salT


        /* var str = AppHelper.getSha256Hash("590c6f2246d9e51e07e0d937e59a65b2f978964a15cf6c045c24f9991ba2a2fa1D")!!
         Log.wtf("testFirst", str)
         Log.wtf("testFirst",str+MyApplication.salt)
         Log.wtf("testFirst", AppHelper.getSha256Hash(str+MyApplication.salt))*/

    }

    override fun onStart() {
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
        signInAnonymously()
        super.onStart()
    }

    private fun linkAccount() {
        // Create EmailAuthCredential with email and password
        val credential = EmailAuthProvider.getCredential("", "")
        // [START link_credential]
        auth.currentUser!!.linkWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("firebase_auth", "linkWithCredential:success")
                    val user = task.result?.user
                    updateUI(user)
                } else {
                    Log.w("firebase_auth", "linkWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
        // [END link_credential]
    }

    private fun updateUI(user: FirebaseUser?) {

    }

    private fun signInAnonymously() {
        // [START signin_anonymously]
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    logw("auth", "signInAnonymously:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    logw("auth", "signInAnonymously:failure+" + task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
        // [END signin_anonymously]
    }


    private fun getFirebasePrefs() {
        auth = FirebaseAuth.getInstance()
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
                       // selectedURL = MyApplication.BASE_URL.isNotEmpty()
                        setUpRestFirebase()
                    }


                } else {
                    MyApplication.BASE_URL = BuildConfig.BASE_URL
                    //nextStep()
                    AppHelper.createDialog(
                        this,
                        AppHelper.getRemoteString("error_getting_data", this)
                    ) {
                        getFirebasePrefs()
                    }
                }
            }

    }

    override fun onDataRetrieved(success: Boolean, response: Any, apiId: Int) {

        if(apiId == AppConstants.ORDER_BY_ID){

            var order = response as ResponseOrders
            MyApplication.selectedOrderId = order.orderId!!.toInt()
            MyApplication.selectedPlaceOrder = RequestPlaceOrder(
                MyApplication.userId,
                order.typeId,//MAKESURE
                order.product!!.id,
                order.typesId,//MAKESURE
                order.sizeCapacityId,//MAKESURE
                order.deliveryDate,
                if (order.addressname != null && order.addressname!!.isNotEmpty()) order.addressname else "",
                order.addressLat,
                order.addressLong,
                order.addressStreet,
                order.addressBuilding,
                order.addressFloor,
                order.addressDescription,
                if (order.addresses.size > 0)order.addresses.get(0).addressId!!.toInt() else 0,
                order.product!!.booking_start_date,
                order.product!!.booking_end_date,
                MyApplication.languageCode


            )
            CallAPIs.updateDevice(this, this)
            //AppHelper.updateDevice(this, MyApplication.phoneNumber!!)
            CallAPIs.getUserInfo(this)
        }else if (apiId == AppConstants.ADDRESS_GEO || apiId == AppConstants.UPDATE_DEVICE) {

        } else if (apiId == API_USER_STATUS) {
            var res = response as ResponseUser
            try {
                if (res.user!!.suspended.equals("1") && MyApplication.isClient) {
                    AppHelper.createDialog(
                        this,
                        AppHelper.getRemoteString("suspended_user_msg", this)
                    )
                } else {
                    finish()
                    startActivity(Intent(this, ActivityHome::class.java))

                }
            } catch (ex: Exception) {
                Log.wtf("apiSplash", ex.toString())
                finish()
                startActivity(Intent(this, ActivityMobileRegistration::class.java))

            }
        } else {
            try {
                var res = response as ResponseMainCategories
                MyApplication.categories.clear()
                MyApplication.categories.addAll(res.categories)
                MyApplication.purchaseId =
                    MyApplication.categories.find { it.valEn.equals("purchase") }!!.id!!.toInt()
                MyApplication.rentalId =
                    MyApplication.categories.find { it.valEn.equals("rental") }!!.id!!.toInt()
            } catch (ex: Exception) {
            }
        }

    }
}
