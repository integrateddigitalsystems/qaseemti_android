package com.ids.qasemti.controller.Activities

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterGeneralSpinner
import com.ids.qasemti.controller.Adapters.AdapterOrderData
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.controller.MyApplication.Companion.foregroundOnlyLocationService
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import com.ids.qasemti.utils.AppHelper.Companion.createDialog
import com.ids.qasemti.utils.AppHelper.Companion.toEditable
import com.ids.sampleapp.model.ItemSpinner
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.layout_border_data.*
import kotlinx.android.synthetic.main.layout_home_orders.*
import kotlinx.android.synthetic.main.layout_order_contact_tab.*
import kotlinx.android.synthetic.main.layout_order_information.*
import kotlinx.android.synthetic.main.layout_order_switch.*
import kotlinx.android.synthetic.main.layout_profile.*
import kotlinx.android.synthetic.main.layout_request_new_time.*
import kotlinx.android.synthetic.main.layout_request_new_time.tvDateExpected
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ActivityOrderDetails : ActivityBase(), RVOnItemClickListener, ApiListener {

    var dialog: Dialog? = null
    var cancelReasons: ArrayList<BankItem> = arrayListOf()
    var arrayCancelSpinner: ArrayList<ItemSpinner> = arrayListOf()
    var orderId = 1
    var selectedCancelReason: Int? = 0
    var onTrack: Int? = 0
    var typeSelected: String? = ""
    var delivered: Int? = 0
    var paid: Int? = 0
    lateinit var shake: Animation

    private var foregroundOnlyLocationServiceBound = false

    private val TAG = "ActivityOrderDetails"
    private val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
    // Provides location updates for while-in-use feature.


    // Listens for location broadcasts from ForegroundOnlyLocationService.
    private lateinit var foregroundOnlyBroadcastReceiver: ForegroundOnlyBroadcastReceiver

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var foregroundOnlyLocationButton: Button

    private lateinit var outputTextView: TextView


    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val location = intent.getParcelableExtra<Location>(
                LocationForeService.EXTRA_LOCATION
            )

            if (location != null) {
                Log.wtf("FORE", "Foreground location: ${location.toText()}")
            }
        }
    }


    override fun onStart() {
        super.onStart()
        /*   updateButtonState(
               sharedPreferences.getBoolean(SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false)
           )*/
        //   sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        MyApplication.serviceContext = this
        val serviceIntent = Intent(this, LocationForeService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)

    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            foregroundOnlyBroadcastReceiver,
            IntentFilter(
                LocationForeService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST
            )
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
            foregroundOnlyBroadcastReceiver
        )
        super.onPause()
    }

    override fun onStop() {
        if (foregroundOnlyLocationServiceBound) {
            unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }
        //  sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)

        super.onStop()
    }

    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocationForeService.LocalBinder
            foregroundOnlyLocationService = binder.foreService
            foregroundOnlyLocationServiceBound = true
            if (MyApplication.saveLocationTracking!!)
                changeState(true)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundOnlyLocationService = null
            foregroundOnlyLocationServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        setListeners()


        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver()
        //  startServicing()
        init()
    }

    fun startServicing() {
        var enabled = MyApplication.saveLocationTracking

        if (!enabled!!) {
            foregroundOnlyLocationService?.unsubscribeToLocationUpdates()
        } else {
            // TODO: Step 1.0, Review Permissions: Checks and requests if needed.
            if (foregroundPermissionApproved()) {
                foregroundOnlyLocationService?.subscribeToLocationUpdates()
                    ?: Log.d(TAG, "Service Not Bound")
            } else {
                requestForegroundPermissions()
            }
        }
    }

    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }


    fun changeState(track: Boolean) {


        if (!track) {
            MyApplication.saveLocationTracking = false
            try {
                foregroundOnlyLocationService?.unsubscribeToLocationUpdates()
                val intent = Intent()
                intent.setClass(this, LocationForeService::class.java)
                stopService(intent)
            } catch (ex: Exception) {
            }
        } else {
            try {
                // TODO: Step 1.0, Review Permissions: Checks and requests if needed.
                if (foregroundPermissionApproved()) {
                    MyApplication.saveLocationTracking = true
                    foregroundOnlyLocationService?.subscribeToLocationUpdates()
                        ?: Log.d(TAG, "Service Not Bound")
                } else {
                    AppHelper.createYesNoDialog(
                        this,
                        AppHelper.getRemoteString("ok", this),
                        AppHelper.getRemoteString("cancel", this),
                        AppHelper.getRemoteString("permission_background_android", this)
                    ) {
                        requestForegroundPermissions()
                    }
                }
            } catch (ex: Exception) {
            }
        }
    }

    // TODO: Step 1.0, Review Permissions: Method requests permissions.
    private fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            Snackbar.make(
                findViewById(R.id.rootLayoutOrderDetails),
                "Location permission needed for core functionality",
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.ok) {
                    // Request permission
                    ActivityCompat.requestPermissions(
                        this@ActivityOrderDetails,
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        ),
                        REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                    )
                }
                .show()
        } else {
            Log.d(TAG, "Request foreground only permission")
            ActivityCompat.requestPermissions(
                this@ActivityOrderDetails,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    // TODO: Step 1.0, Review Permissions: Handles permission result.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "onRequestPermissionResult")

        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    // If user interaction was interrupted, the permission request
                    // is cancelled and you receive empty arrays.
                    Log.d(TAG, "User interaction was cancelled.")
                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    // Permission was granted.
                    foregroundOnlyLocationService?.subscribeToLocationUpdates()
                else -> {
                    // Permission denied.
                    // updateButtonState(false)

                    Snackbar.make(
                        findViewById(R.id.rootLayoutOrderDetails),
                        R.string.permission_denied,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.settings) {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                "",
                                null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        .show()
                }
            }
        }
    }

    fun init() {

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        btDrawer.hide()
        btBackTool.show()
        shake = AnimationUtils.loadAnimation(this, R.anim.shake)
        orderId = intent.getIntExtra("orderId", 1)

        btBackTool.onOneClick {
            super.onBackPressed()
        }
        if (MyApplication.isBroadcast) {
            MyApplication.isBroadcast = false
            btAcceptOrder.show()
            llEditOrderTime.hide()
        }

        try {
            var type = intent.getIntExtra("type", 1)
        }catch (ex:Exception){}

        tvLocationOrderDeatils.setColorTypeface(this, R.color.primary, "", false)


        loading.show()
        CallAPIs.getOrderByOrderId(orderId, this)
        //setOrderData()
    }


    private fun setOrderData() {
        try {
            typeSelected = MyApplication.selectedOrder!!.orderStatus
        } catch (e: Exception) {
        }
        if (typeSelected.equals(AppConstants.ORDER_TYPE_ACTIVE) || typeSelected.equals(AppConstants.ORDER_TYPE_CANCELED)) {
            llDetailsCallMessage.show()
        } else {
            llDetailsCallMessage.hide()
        }
        AppHelper.setAllTexts(rootLayoutOrderDetails, this)
        tvPageTitle.show()

        if (MyApplication.languageCode == AppConstants.LANG_ENGLISH) {
            try {
                tvPageTitle.setColorTypeface(
                    this,
                    R.color.white,
                    AppHelper.getRemoteString(
                        "status_" + MyApplication.selectedOrder!!.orderStatus,
                        this
                    ) + " " + AppHelper.getRemoteString("order_details", this),
                    true
                )
            } catch (e: Exception) {
            }
        } else {
            try {
                tvPageTitle.setColorTypeface(
                    this,
                    R.color.white,
                    AppHelper.getRemoteString(
                        "order_details",
                        this
                    ) + " " + AppHelper.getRemoteString(
                        "status_" + MyApplication.selectedOrder!!.orderStatus,
                        this
                    ),
                    true
                )
            } catch (e: Exception) {
            }
        }
        if (typeSelected.equals(AppConstants.ORDER_TYPE_ACTIVE)) {
            if (!MyApplication.isClient) {

                if (MyApplication.selectedOrder!!.vendor == null || MyApplication.selectedOrder!!.vendor!!.userId == null) {

                    llOrderSwitches.hide()
                    btCancelOrder.hide()
                    llDetailsCallMessage.hide()

                } else {
                    llEditOrderTime.show()
                    btCancelOrder.show()
                    llDetailsCallMessage.show()
                    llOrderSwitches.show()
                }
            } else {
                if (!MyApplication.selectedOrder!!.newDeliveryDate.isNullOrEmpty()) {
                    llSuggestedDate.show()
                    try {
                        tvSuggestedDate.text = MyApplication.selectedOrder!!.newDeliveryDate
                    }catch (ex:Exception){

                    }
                } else {
                    llSuggestedDate.hide()
                }
                llEditOrderTime.hide()
                llOrderSwitches.hide()
            }
            llRatingOrder.hide()
            llActualDelivery.hide()
        } else if (typeSelected.equals(AppConstants.ORDER_TYPE_COMPLETED)) {
            llRatingOrder.show()
            llOrderSwitches.show()
            btCancelOrder.hide()
            llActualDelivery.show()
            llOrderSwitches.hide()
        } else if (typeSelected.equals(AppConstants.ORDER_TYPE_UPCOMING)) {
            btCancelOrder.show()
            llRatingOrder.hide()
            llOrderSwitches.hide()
        } else {
            llRatingOrder.hide()
            /* if(MyApplication.languageCode==AppConstants.LANG_ARABIC) AppHelper.getRemoteString("order_details",this) + " " +  AppHelper.getRemoteString("cancelled",this).capitalized() else
                 AppHelper.getRemoteString("cancelled",this).capitalized() + " " + AppHelper.getRemoteString("order_details",this)*/
            llEditOrderTime.hide()
            llActualDelivery.show()
            llOrderSwitches.hide()
            btCancelOrder.hide()

        }

        try {
            if (MyApplication.isClient) {
                if (typeSelected.equals(AppConstants.ORDER_TYPE_COMPLETED)) {
                    llRatingOrder.show()
                    if (MyApplication.isClient && MyApplication.selectedOrder!!.type!!.lowercase() == "rental") {
                        btRenewOrder.show()
                    } else {
                        btRenewOrder.hide()
                    }
                }
            } else {
                btRenewOrder.hide()
            }
        } catch (e: Exception) {
            btRenewOrder.hide()
        }

        tvDateExpected.setColorTypeface(this, R.color.gray_font_title, "", true)
        var array: ArrayList<OrderData> = arrayListOf()
        var langType = ""
        if (MyApplication.languageCode == AppConstants.LANG_ENGLISH) {
            langType =
                MyApplication.categories!!.find { it.id!!.toInt() == MyApplication.selectedOrder!!.typeId }!!.valEn!!
        } else
            langType =
                MyApplication.categories!!.find { it.id!!.toInt() == MyApplication.selectedOrder!!.typeId }!!.valAr!!
        array.add(OrderData(AppHelper.getRemoteString("category", this), langType))
        array.add(
            OrderData(
                AppHelper.getRemoteString("service", this),
                MyApplication.selectedOrder!!.product!!.name
            )
        )
        array.add(
            OrderData(
                AppHelper.getRemoteString("type", this), try {
                    if (!MyApplication.selectedOrder!!.product!!.types!!.isEmpty()) MyApplication.selectedOrder!!.product!!.types else AppHelper.getRemoteString(
                        "no_data",
                        this
                    )
                } catch (ex: Exception) {
                    AppHelper.getRemoteString("no_data", this)
                }
            )
        )
        array.add(
            OrderData(
                AppHelper.getRemoteString("SizeCapacity", this), try {
                    if (!MyApplication.selectedOrder!!.product!!.sizeCapacity!!.isEmpty()) MyApplication.selectedOrder!!.product!!.sizeCapacity else AppHelper.getRemoteString(
                        "no_data",
                        this
                    )
                } catch (ex: Exception) {
                    AppHelper.getRemoteString("no_data", this)
                }
            )
        )
        array.add(
            OrderData(
                AppHelper.getRemoteString("Quantity", this),
                MyApplication.selectedOrder!!.product!!.qty
            )
        )
        if (MyApplication.selectedOrder!!.typeId != 345) {
            array.add(
                OrderData(
                    "Period",
                    try {
                        if (!MyApplication.selectedOrder!!.product!!.booking_start_date!!.isNullOrEmpty() && !MyApplication.selectedOrder!!.product!!.booking_end_date!!.isNullOrEmpty()) MyApplication.selectedOrder!!.product!!.booking_start_date!! + "\n -" + MyApplication.selectedOrder!!.product!!.booking_end_date!! else AppHelper.getRemoteString(
                            "no_data",
                            this
                        )
                    } catch (ex: Exception) {
                        AppHelper.getRemoteString(
                            "no_data",
                            this
                        )
                    }
                )
            )
        }


        rvDataBorder.layoutManager = LinearLayoutManager(this)
        rvDataBorder.adapter = AdapterOrderData(array, this, this)

        try {
            tvLocationOrderDeatils.text =
                AppHelper.addressFromOrder(MyApplication.selectedOrder!!, 1, this)
        } catch (e: Exception) {
            tvLocationOrderDeatils.text = AppHelper.getRemoteString("no_data", this)
        }
        try {
            if (!MyApplication.isClient)
                tvOrderCustomerName.text =
                    MyApplication.selectedOrder!!.customer!!.first_name + " " + MyApplication.selectedOrder!!.customer!!.last_name
            else {
                 if(MyApplication.selectedOrder!!.vendor!=null){
                   try{
                      tvOrderCustomerName.text = MyApplication.selectedOrder!!.vendor!!.firstName!! + " " + MyApplication.selectedOrder!!.vendor!!.lastName}catch (e:Exception){
                       tvOrderCustomerName.text = AppHelper.getRemoteString("pending_service_provider",this)
                   }
                }else{
                   tvOrderCustomerName.text = AppHelper.getRemoteString("pending_service_provider",this)
                }
            }
        } catch (e: Exception) {
        }
        try {
            tvOrderDeetId.text = MyApplication.selectedOrder!!.orderId.toString()
        } catch (e: Exception) {
        }
        try {
            tvOrderDateDeet.text = AppHelper.formatDate(
                MyApplication.selectedOrder!!.date!!,
                "yyyy-MM-dd hh:mm:ss",
                "dd MMMM yyyy hh:mm"
            )
        } catch (e: Exception) {
        }
        try {
            tvExpectedOrderDateDeet.text =
                if (!MyApplication.selectedOrder!!.deliveryDate!!.isEmpty()) MyApplication.selectedOrder!!.deliveryDate else AppHelper.getRemoteString(
                    "no_data",
                    this
                )
        } catch (e: Exception) {
            AppHelper.getRemoteString("no_data", this)
        }
        try {
            tvActualDeliveryTime.text = MyApplication.selectedOrder!!.deliveryDate
        } catch (e: Exception) {
        }
        try {
            tvOrderAmountDeet.text =
                MyApplication.selectedOrder!!.total!!.toString() + " " + MyApplication.selectedOrder!!.currency
        } catch (e: Exception) {
        }
        try {
            tvOrderAmountAdditional.text =
                MyApplication.selectedOrder!!.additional!!.toString() + " " + MyApplication.selectedOrder!!.currency
        } catch (e: Exception) {
        }
        try {
            tvAmountOrderTotal.text =
                MyApplication.selectedOrder!!.grand_total!!.toString() + " " + MyApplication.selectedOrder!!.currency
        } catch (e: Exception) {
        }
        try {
            if (MyApplication.selectedOrder!!.paymentMethod != null && MyApplication.selectedOrder!!.paymentMethod!!.isNotEmpty())
                tvPaymentMethod.text = MyApplication.selectedOrder!!.paymentMethod
        } catch (e: Exception) {
        }
        try {
            swDelivered.isChecked = MyApplication.selectedOrder!!.delivered!!
            if (swDelivered.isChecked) {
                swDelivered.isEnabled = false
            }
            AppHelper.setSwitchColor(swDelivered, this)
        } catch (ex: Exception) {
        }
        try {
            swPaid.isChecked = MyApplication.selectedOrder!!.paid!!
            if (swPaid.isChecked) {
                swPaid.isEnabled = false
            }
            AppHelper.setSwitchColor(swPaid, this)
        } catch (ex: java.lang.Exception) {
        }
        try {
            swOnTrack.isChecked = MyApplication.selectedOrder!!.onTrack!!
            AppHelper.setSwitchColor(swOnTrack, this)
        } catch (ex: java.lang.Exception) {
        }
        loading.hide()
    }


    fun updatePayment(orderId: Int) {
        try {
            loading.show()
        } catch (ex: java.lang.Exception) {

        }
        var req = RequestUpdatePayment(
            orderId,
            "",
            "",
            "selectedPayment",
            "12",
            "captured",
            MyApplication.selectedPlaceOrder!!.deliveryDate,
            "23",
            "test ref",
            "12",
            "abc123"
        )
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.updatePayment(req)?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(
                    call: Call<ResponseMessage>,
                    response: Response<ResponseMessage>
                ) {
                    try {

                    } catch (E: java.lang.Exception) {

                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    loading.hide()
                }
            })
    }

    fun nextStep(res: Int) {
        if (res == 1) {
            createDialog(this, "Renewal Successful")
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
                MyApplication.renewed = true
            }, 1000)
        } else {
            createDialog(this, "Failed to renew")
        }
    }

    fun renewOrder() {
        try {
            loading.show()
        } catch (ex: java.lang.Exception) {

        }
        var vendorId = 0
        try {
            vendorId = MyApplication.selectedOrder!!.product!!.vendorId!!.toInt()
        } catch (ex: java.lang.Exception) {

        }
        var storeName = ""
        try {
            storeName = MyApplication.selectedOrder!!.vendor!!.storeName!!
        } catch (ex: java.lang.Exception) {

        }
        var cal = Calendar.getInstance()
        var date = AppHelper.formatDate(cal.time, "dd/mm/yy hh:mm:ssss")
        var req = RequestRenewOrder(
            MyApplication.selectedOrder!!.customer!!.user_id!!.toInt(),
            MyApplication.selectedOrder!!.orderId!!.toInt(),
            vendorId,
            MyApplication.selectedOrder!!.type,
            MyApplication.selectedOrder!!.product!!.productId!!.toInt(),
            MyApplication.selectedOrder!!.product!!.types.toInt(),
            MyApplication.selectedOrder!!.product!!.sizeCapacity!!.toInt(),
            MyApplication.selectedOrder!!.deliveryDate,
            date,
            date,
            MyApplication.selectedOrder!!.shipping_address_name,
            MyApplication.selectedOrder!!.shipping_address_latitude!!.toDouble(),
            MyApplication.selectedOrder!!.shipping_address_longitude!!.toDouble(),
            MyApplication.selectedOrder!!.shipping_address_street,
            MyApplication.selectedOrder!!.shipping_address_building,
            MyApplication.selectedOrder!!.shipping_address_floor,
            MyApplication.selectedOrder!!.shipping_address_description,
            MyApplication.languageCode
        )
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.renewOrder(req)?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(
                    call: Call<ResponseMessage>,
                    response: Response<ResponseMessage>
                ) {
                    try {
                        loading.hide()
                        nextStep(response.body()!!.result!!)
                    } catch (E: java.lang.Exception) {

                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    loading.hide()
                }
            })
    }

    fun acceptOrder(orderId: Int, additional: Int) {
        loading.show()

        var newReq = RequestAcceptBroadccast(MyApplication.userId, orderId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.acceptBroadcast(newReq)?.enqueue(object : Callback<ResponseUser> {
                override fun onResponse(
                    call: Call<ResponseUser>,
                    response: Response<ResponseUser>
                ) {
                    try {
                        accepted(response.body()!!.result!!)
                    } catch (E: java.lang.Exception) {
                        accepted(0)
                    }
                }

                override fun onFailure(call: Call<ResponseUser>, throwable: Throwable) {
                    accepted(0)
                }
            })
    }

    fun setUpSpinner() {
        arrayCancelSpinner.clear()
        for (item in cancelReasons) {
            arrayCancelSpinner.add(ItemSpinner(item.id!!.toInt(), item.value, ""))
        }
        arrayCancelSpinner.add(
            0,
            ItemSpinner(0, AppHelper.getRemoteString("please__select", this), "")
        )
        selectedCancelReason = 0
        val adapterServices =
            AdapterGeneralSpinner(this, R.layout.spinner_layout, arrayCancelSpinner, 0)
        spCancelReason.adapter = adapterServices
        adapterServices.setDropDownViewResource(R.layout.item_spinner_drop_down)
        spCancelReason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedCancelReason = arrayCancelSpinner.get(position).id
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        /* spBanks.setSelection(arrayCancelSpinner.indexOf(arrayCancelSpinner.find {
             it.id.toString() == MyApplication.selectedUser!!.bankName!!
         }))*/

        loading.hide()
    }

    fun getCancelReason() {
        loading.show()
        var req = RequestLanguage(MyApplication.languageCode)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getCancelReason(req)?.enqueue(object : Callback<ResponseMainCancel> {
                override fun onResponse(
                    call: Call<ResponseMainCancel>,
                    response: Response<ResponseMainCancel>
                ) {
                    try {
                        cancelReasons.clear()
                        cancelReasons.addAll(response.body()!!.banks)
                        setUpSpinner()
                    } catch (E: java.lang.Exception) {
                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponseMainCancel>, throwable: Throwable) {
                    loading.hide()

                }
            })

    }

    fun showAcceptOrderPopup(context: Activity) {
        AppHelper.createYesNoDialog(
            context,
            AppHelper.getRemoteString("yes", context),
            AppHelper.getRemoteString("cancel", context),
            AppHelper.getRemoteString("sure_accept", context)
        ) {
            if (AppHelper.isOnline(this)) {
                acceptOrder(
                    MyApplication.selectedOrder!!.orderId!!.toInt(),
                    MyApplication.selectedOrder!!.grand_total!!.toInt()
                )
            } else {
                AppHelper.createDialog(this, AppHelper.getRemoteString("no_internet", this))
            }

        }
    }

    fun setListeners() {
        btBackTool.onOneClick {
            super.onBackPressed()
        }
        btAcceptOrder.onOneClick {
            if (MyApplication.selectedUser!!.active == 1)
                showAcceptOrderPopup(this)
            else
                createDialog(
                    this,
                    AppHelper.getRemoteString("inactive_user_msg", this)
                )
        }
        tvLocationOrderDeatils.onOneClick {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("geo:0,0?q=" + MyApplication.selectedOrder!!.shipping_latitude!! + "," + MyApplication.selectedOrder!!.shipping_longitude!! + "(" + MyApplication.selectedOrder!!.addressname + ")")
            startActivity(intent)
        }
        btRenewOrder.onOneClick {

            MyApplication.renewing = true
            startActivity(
                Intent(
                    this,
                    ActivityCheckout::class.java
                )
            )
            /*try {
                renewOrder()
            } catch (e: Exception) {
            }*/
        }
        llCall.onOneClick {
            try {
                if (!MyApplication.isClient) {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.setData(Uri.parse("tel:" + MyApplication.selectedOrder!!.customer!!.mobile_number));
                    startActivity(intent)
                } else {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.setData(Uri.parse("tel:" + MyApplication.selectedOrder!!.vendor!!.mobileNum));
                    startActivity(intent)
                }
            } catch (ex: Exception) {
                val intent = Intent(Intent.ACTION_DIAL)
                startActivity(intent)
            }
        }
        llMessage.onOneClick {
            startActivity(Intent(this, ActivityChat::class.java))
        }
        rlCheckoutDate.onOneClick {
            var mcurrentDate = Calendar.getInstance()
            var mYear = 0
            var mMonth = 0
            var mDay = 0
            mYear = mcurrentDate!![Calendar.YEAR]
            mMonth = mcurrentDate!![Calendar.MONTH]
            mDay = mcurrentDate!![Calendar.DAY_OF_MONTH]

            mcurrentDate.set(mYear, mMonth, mDay)
            val mDatePicker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { datepicker, selectedyear, selectedmonth, selectedday ->
                    val myCalendar = Calendar.getInstance()
                    myCalendar[Calendar.YEAR] = selectedyear
                    myCalendar[Calendar.MONTH] = selectedmonth
                    myCalendar[Calendar.DAY_OF_MONTH] = selectedday
                    val myFormat = "yyyy-MM-dd" //Change as you need
                    var sdf =
                        SimpleDateFormat(myFormat, Locale.ENGLISH)
                    var date = sdf.format(myCalendar.time)
                    etOrderDetailDate.text = date.toEditable()
                }, mYear, mMonth, mDay
            )
            mDatePicker.show()
        }

        btSendRequest.onOneClick {
            if (etOrderDetailDate.text.toString().isEmpty())
                createDialog(this, "Please enter suggested date")
            else {
                if (AppHelper.isOnline(this)) {
                    sendSuggestedDate()
                } else {
                    AppHelper.createDialog(this,AppHelper.getRemoteString("no_internet",this))
                }
            }

        }

        rlCheckoutTime.onOneClick {
            // TODO Auto-generated method stub
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val myFormat = "dd/MM/yyyy" //Change as you need
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
            val now = mcurrentTime.time
            val nowDate = sdf.format(now)
            val timePickerDialog = TimePickerDialog(
                this, R.style.DatePickerDialog,
                { timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int ->

                    var time = String.format("%02d", selectedHour) + ":" + String.format(
                        "%02d",
                        selectedMinute
                    ) + ":00"
                    etOrderDetailTime.text = time.toEditable()
                }, hour, minute, false
            ) //Yes 24 hour time
            timePickerDialog.show()
        }

        btSubmit.onOneClick {


            if (AppHelper.isOnline(this)) {


                if (!MyApplication.isClient && MyApplication.selectedUser!!.active == 0) {
                    createDialog(this, AppHelper.getRemoteString("inactive_user_msg", this))
                } else if (selectedCancelReason == 0) {
                    createDialog(this, AppHelper.getRemoteString("fill_all_field", this))
                } else {
                    AppHelper.createYesNoDialog(
                        this,
                        AppHelper.getRemoteString("yes", this),
                        AppHelper.getRemoteString("cancel", this),
                        AppHelper.getRemoteString("are_you_sure_cancel", this)
                    ) {

                        loading.show()

                        var cal = Calendar.getInstance()
                        var dateNow = AppHelper.formatDate(cal.time!!, "dd-MM-yyyy")
                        var newReq =
                            RequestCancelOrder(
                                MyApplication.selectedOrder!!.orderId!!.toInt(),
                                MyApplication.userId,
                                dateNow,
                                selectedCancelReason
                            )
                        RetrofitClient.client?.create(RetrofitInterface::class.java)
                            ?.cancelOrder(
                                newReq
                            )?.enqueue(object : Callback<ResponseCancel> {
                                override fun onResponse(
                                    call: Call<ResponseCancel>,
                                    response: Response<ResponseCancel>
                                ) {
                                    try {
                                        resultCancel(response.body()!!.result!!)
                                    } catch (E: java.lang.Exception) {
                                        resultCancel("0")
                                    }
                                }

                                override fun onFailure(
                                    call: Call<ResponseCancel>,
                                    throwable: Throwable
                                ) {
                                    resultCancel("0")
                                }
                            })
                    }
                }
            } else {
                AppHelper.createDialog(this,AppHelper.getRemoteString("no_internet",this))
            }
        }
        btDontCancel.onOneClick {
            btCancelOrder.show()
            rlCancellationReason.hide()
            llCancelButtons.hide()
        }
        btCancelOrder.onOneClick {
            btCancelOrder.hide()
            rlCancellationReason.show()
            getCancelReason()
            llCancelButtons.show()

        }

        swOnTrack.setOnClickListener {
            if(AppHelper.isOnline(this)) {
                AppHelper.createSwitchDialog(
                    this,
                    AppHelper.getRemoteString("ok", this),
                    AppHelper.getRemoteString("cancel", this),
                    getString(
                        R.string.are_you_sure_change_status
                    ),
                    swOnTrack
                ) {
                    if (swOnTrack.isChecked) {
                        MyApplication.saveLocationTracking = true
                        changeState(true)
                        AppHelper.setSwitchColor(swOnTrack, this)
                        onTrack = 1
                    } else {
                        AppHelper.setSwitchColor(swOnTrack, this)
                        onTrack = 0
                    }
                    setStatus()

                    // MyApplication.trackingActivity = this
                    //  AppHelper.setUpDoc(MyApplication.selectedOrder!!, this)
                }
            }else{
                swOnTrack.isChecked = !swOnTrack.isChecked
                AppHelper.createDialog(this, AppHelper.getRemoteString("no_internet", this))
            }

        }
        swPaid.setOnClickListener {
            if(AppHelper.isOnline(this)) {
                AppHelper.createSwitchDialog(
                    this,
                    AppHelper.getRemoteString("ok", this),
                    AppHelper.getRemoteString("cancel", this),
                    getString(
                        R.string.are_you_sure_change_status
                    ),
                    swPaid
                ) {
                    if (swPaid.isChecked) {

                        if (swDelivered.isChecked) {
                            createDialog(this, "This order is now completed") {
                                MyApplication.renewed = false
                                MyApplication.completed = true
                                super.onBackPressed()
                            }
                        }
                        swPaid.isEnabled = false
                        AppHelper.setSwitchColor(swPaid, this)
                        paid = 1
                    } else {
                        AppHelper.setSwitchColor(swPaid, this)
                        paid = 0
                    }
                    setStatus()
                }
            }else{
                swPaid.isChecked = !swPaid.isChecked
                AppHelper.createDialog(this, AppHelper.getRemoteString("no_internet", this))
            }
        }
        swDelivered.setOnClickListener {

            if (AppHelper.isOnline(this)) {
                AppHelper.createSwitchDialog(
                    this,
                    AppHelper.getRemoteString("ok", this),
                    AppHelper.getRemoteString("cancel", this),
                    getString(
                        R.string.are_you_sure_change_status
                    ),
                    swDelivered
                ) {
                    if (swDelivered.isChecked) {
                        if (swPaid.isChecked) {
                            createDialog(this, "This order is now completed") {
                                MyApplication.renewed = false
                                MyApplication.completed = true
                                super.onBackPressed()
                            }
                        }
                        swDelivered.isEnabled = false
                        MyApplication.saveLocationTracking = false
                        changeState(false)
                        AppHelper.setSwitchColor(swDelivered, this)
                        delivered = 1
                    } else {
                        AppHelper.setSwitchColor(swDelivered, this)
                        delivered = 0
                    }
                    setStatus()
                }
            } else {
                swDelivered.isChecked = !swDelivered.isChecked
                AppHelper.createDialog(this, AppHelper.getRemoteString("no_internet", this))

            }

        }


        llRatingOrder.setOnClickListener {
            showRatingDialog()
        }

        btAcceptNewdate.onOneClick {
            if (AppHelper.isOnline(this)) {
                respondDate(1)
            } else {
                AppHelper.createDialog(this, AppHelper.getRemoteString("no_internet", this))
            }

        }

        btRejectNewdate.onOneClick {
            if (AppHelper.isOnline(this)) {
                respondDate(0)
            } else {
                AppHelper.createDialog(this, AppHelper.getRemoteString("no_internet", this))
            }

        }
    }

    fun accepted(res: Int) {
        if (res == 1) {
            super.onBackPressed()
            toast(AppHelper.getRemoteString("order_accept_succ", this))
            loading.hide()
        } else {
            toast(AppHelper.getRemoteString("error_acc_order", this))
        }
    }

    fun accepted(res: ResponseMessage) {
        createDialog(this, res.message!!)
        llSuggestedDate.hide()
        loading.hide()
    }

    fun respondDate(accept: Int) {
        loading.show()
        var newReq = RequestAcceptDate(
            MyApplication.selectedOrder!!.orderId!!.toInt(),
            accept,
            MyApplication.languageCode
        )
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.clAcceptNewDT(newReq)?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(
                    call: Call<ResponseMessage>,
                    response: Response<ResponseMessage>
                ) {
                    try {
                        accepted(response.body()!!)
                    } catch (E: java.lang.Exception) {
                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    loading.hide()
                }
            })
    }

    fun setStatus() {
        var newReq = RequestUpdateOrder(
            MyApplication.selectedOrder!!.orderId!!.toInt(),
            swOnTrack.isChecked,
            swDelivered.isChecked,
            swPaid.isChecked
        )
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.updateOrderCustomStatus(newReq)?.enqueue(object : Callback<ResponseUpdate> {
                override fun onResponse(
                    call: Call<ResponseUpdate>,
                    response: Response<ResponseUpdate>
                ) {
                    try {
                    } catch (E: java.lang.Exception) {
                    }
                }

                override fun onFailure(call: Call<ResponseUpdate>, throwable: Throwable) {
                }
            })
    }


    fun sendSuggestedDate() {
        loading.show()
        var newReq = RequestNewDeliveryDate(
            MyApplication.selectedOrder!!.orderId!!.toInt(),
            etOrderDetailDate.text.toString() + " " + etOrderDetailTime.text.toString(),
            MyApplication.languageCode
        )
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.sp_send_new_dt(newReq)?.enqueue(object : Callback<ResponseDeliveryDate> {
                override fun onResponse(
                    call: Call<ResponseDeliveryDate>,
                    response: Response<ResponseDeliveryDate>
                ) {
                    try {
                        loading.hide()
                        if (response.body()!!.result == "1") {
                            createDialog(
                                this@ActivityOrderDetails,
                                AppHelper.getRemoteString(
                                    "sugg_date_sent",
                                    this@ActivityOrderDetails
                                )
                            )
                        }
                    } catch (E: java.lang.Exception) {
                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponseDeliveryDate>, throwable: Throwable) {
                    loading.hide()
                }
            })
    }

    fun resultCancel(req: String) {
        if (req == "1") {

            createDialog(this, AppHelper.getRemoteString("success", this)) {
                finish()
                MyApplication.renewed = true
            }
        } else {
            AppHelper.createDialog(this, AppHelper.getRemoteString("failure", this))
        }
        try {
            loading.hide()
        } catch (ex: Exception) {

        }
    }

    override fun onItemClicked(view: View, position: Int) {


    }


    private fun showRatingDialog() {
        dialog = Dialog(this, R.style.Base_ThemeOverlay_AppCompat_Dialog)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.setContentView(R.layout.dialog_rating)
        dialog!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialog!!.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        dialog!!.setCancelable(true)
        var close = dialog!!.findViewById<ImageView>(R.id.btClose)
        var tvTitleRate = dialog!!.findViewById<TextView>(R.id.tvTitleRate)
        var loading = dialog!!.findViewById<LinearLayout>(R.id.loading)
        var etRatingText = dialog!!.findViewById<EditText>(R.id.etRatingText)
        var rbOrder = dialog!!.findViewById<RatingBar>(R.id.rbOrder)
        var btSubmit = dialog!!.findViewById<Button>(R.id.btSubmit)
        var vendorName = ""
        try {
            if (MyApplication.selectedOrder != null) {
                vendorName =
                    MyApplication.selectedOrder!!.vendor!!.firstName!! + " " + MyApplication.selectedOrder!!.vendor!!.lastName!!
            }
        } catch (e: Exception) {
        }
        tvTitleRate.text = AppHelper.getRemoteString("rate", this) + " " + vendorName


        btSubmit.setOnClickListener {
            /* if (etRatingText.text.toString().isEmpty()) {
                 etRatingText.startAnimation(shake)
             } else*/ if (rbOrder.rating == 0f)
            rbOrder.startAnimation(shake)
        else {
            if (MyApplication.isClient) {
                setClientRating(loading, etRatingText.text.toString(), rbOrder.rating.toInt())
            } else {
                setSerRating(loading, etRatingText.text.toString(), rbOrder.rating.toInt())
            }
        }
        }

        close.onOneClick {
            dialog!!.cancel()
        }
        dialog!!.show()

    }

    fun setSerRating(loading: LinearLayout, description: String, rating: Int) {
        loading.show()
        var newReq = RequestClientReviews(
            MyApplication.selectedOrder!!.vendor!!.userId!!.toInt(),
            MyApplication.userId,
            "",
            description,
            rating

        )
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.setRatingSer(newReq)?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(
                    call: Call<ResponseMessage>,
                    response: Response<ResponseMessage>
                ) {
                    try {
                        loading.hide()
                        if (response.body()!!.result == 1)
                            dialog!!.dismiss()
                        else {
                            createDialog(this@ActivityOrderDetails, response.body()!!.message!!)
                        }
                    } catch (E: java.lang.Exception) {
                    }
                }

                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    loading.hide()
                }
            })
    }


    fun setClientRating(loading: LinearLayout, description: String, rating: Int) {
        loading.show()
        var newReq = RequestRating(
            MyApplication.selectedOrder!!.vendor!!.userId!!.toInt(),
            MyApplication.userId,
            MyApplication.selectedUser!!.firstName,//get from user object
            MyApplication.selectedUser!!.email,//get from user object
            "",//get from user object
            description,
            rating
        )
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.setRating(newReq)?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(
                    call: Call<ResponseMessage>,
                    response: Response<ResponseMessage>
                ) {
                    try {
                        loading.hide()
                        if (response.body()!!.result == 1)
                            dialog!!.dismiss()
                        else {
                            createDialog(this@ActivityOrderDetails, response.body()!!.message!!)
                        }
                    } catch (E: java.lang.Exception) {
                    }
                }

                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    loading.hide()
                }
            })
    }

    override fun onDataRetrieved(success: Boolean, response: Any, apiId: Int) {
        try {
            MyApplication.selectedOrder = response as ResponseOrders
            setOrderData()
        }catch (ex:Exception){
            logw("OrderError",ex.toString())
            loading.hide()
        }
    }
}