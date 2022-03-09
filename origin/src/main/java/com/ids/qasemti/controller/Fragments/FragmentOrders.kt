package com.ids.qasemti.controller.Fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.gms.auth.api.phone.SmsCodeAutofillClient.PermissionState
import com.google.android.material.snackbar.Snackbar
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.*
import com.ids.qasemti.controller.Adapters.AdapterOrderType
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.controller.MyApplication.Companion.typeSelected
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.layout_order_contact_tab.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import com.google.android.gms.auth.api.phone.SmsCodeAutofillClient.PermissionState.DENIED
import com.ids.qasemti.controller.MyApplication.Companion.allowedLocation
import com.ids.qasemti.controller.MyApplication.Companion.listOrderTrack
import com.ids.qasemti.controller.MyApplication.Companion.tempOrder
import com.ids.qasemti.controller.MyApplication.Companion.tempSwitch
import kotlinx.android.synthetic.main.layout_order_switch.*
import kotlin.math.log


class FragmentOrders : Fragment(), RVOnItemClickListener , ReloadData {

    var ordersArray: ArrayList<ResponseOrders> = arrayListOf()
    var adapter: AdapterOrderType? = null
    var denied : Boolean ?=false
    var activeType = 0
    var mPermissionResult: ActivityResultLauncher<Array<String>>? = null
    var mPermissionResult2 : ActivityResultLauncher<Array<String>>?=null
    var resultLauncher: ActivityResultLauncher<Intent>? = null
    val BLOCKED = -1
    var mainArray: ArrayList<ResponseOrders> = arrayListOf()
    private val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
    val GRANTED = 0
    val DENIED = 1
    val BLOCKED_OR_NEVER_ASKED = 2
    var orderType: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    fun setUp2() {
        mPermissionResult2 =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
            { result ->
                var permission = false
                for (item in result) {
                    permission = item.value
                }
                if (permission) {
                    allowedLocation = true
                    tempSwitch!!.isEnabled = false
                    MyApplication.selectedOrder = tempOrder!!
                    MyApplication.trackOrderId =
                        tempOrder!!.orderId!!.toInt()
                    if(!listOrderTrack.contains(MyApplication.trackOrderId.toString()))
                        listOrderTrack.add(MyApplication.trackOrderId.toString())
                    AppHelper.toGsonArrString()
                    if (!MyApplication.isClient)
                        (requireActivity() as ActivityHome).changeState(
                            true,
                            MyApplication.listOrderTrack.size - 1
                        )
                    AppHelper.setSwitchColor(tempSwitch!!, requireContext())
                   var indx = ordersArray.indexOf(tempOrder!!)
                    ordersArray.get(indx).onTrack = true
                    AppHelper.updateStatus(
                        tempOrder!!.orderId!!.toInt(),
                        tempSwitch!!.isChecked,
                        tempOrder!!.delivered!!,
                        tempOrder!!.paid!!,
                        this,
                        loading
                    )
                  //  adapter!!.notifyDataSetChanged()


                } else {
                    for(item in result ){
                        requireActivity().toast(AppHelper.getRemoteString("location_updates_disabled",requireContext()))
                        if(ContextCompat.checkSelfPermission(requireContext(),item.key) == BLOCKED){
                            denied = true
                            tempSwitch!!.isChecked = tempSwitch!!.isChecked

                            if(getPermissionStatus( Manifest.permission.ACCESS_FINE_LOCATION) == BLOCKED_OR_NEVER_ASKED) {
                                Toast.makeText(
                                    requireContext(),
                                    AppHelper.getRemoteString(
                                        "grant_settings_permission",
                                        requireContext()
                                    ),
                                    Toast.LENGTH_LONG
                                ).show()
                                break
                            }
                        }
                    }

                }
            }



    }

    fun getPermissionStatus(androidPermissionName: String?): Int {
        return if (ContextCompat.checkSelfPermission(
                requireActivity(),
                androidPermissionName!!
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    androidPermissionName
                )
            ) {
                BLOCKED_OR_NEVER_ASKED
            } else DENIED
        } else GRANTED
    }

    override fun onResume() {
        super.onResume()
        (activity as ActivityHome).showTitle(true)
        AppHelper.setTitle(requireActivity(), MyApplication.selectedTitle!!, "",R.color.primary)


        /*if(!MyApplication.fromFooterOrder) {
            (activity as ActivityHome).showBack(true)
            MyApplication.fromFooterOrder = true
        }else*/
            (activity as ActivityHome).showBack(false)

        if(denied!!){
            denied = false
            if(MyApplication.isClient){
                getClientOrders()
            }else {
                getOrders()
            }
        }else {
            if (MyApplication.toDetails) {
                MyApplication.toDetails = false
                startActivity(
                    Intent(requireActivity(), ActivityOrderDetails::class.java)
                        .putExtra("orderId", MyApplication.selectedOrderId)
                )
            } else if(MyApplication.toChat){
                MyApplication.toChat = false
                startActivity(Intent(requireActivity(),ActivityChat::class.java))
            }else {
                if (MyApplication.renewed == true) {
                    MyApplication.renewed = false
                    setTabLayout(0)
                } else if (MyApplication.completed) {
                    MyApplication.completed = false
                    setTabLayout(2)
                } else if(MyApplication.fromHome){
                    MyApplication.fromHome = false
                    setTabLayout(typeSelected)
                } else{
                    typeSelected =0
                    setTabLayout(typeSelected)
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_orders, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutOrderType, requireContext())
        requireActivity().getWindow().setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        setUp()
        setUp2()
        init()
        setTabs()
     //   setTabLayout(typeSelected)
        //  setData(true)

    }

    fun getClientOrders() {
        try {
            loading.show()
        } catch (ex: Exception) {

        }
        var newReq = RequestOrders(MyApplication.userId, MyApplication.languageCode, orderType)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getClientOrders(
                newReq
            )?.enqueue(object : Callback<ResponseMainOrder> {
                override fun onResponse(
                    call: Call<ResponseMainOrder>,
                    response: Response<ResponseMainOrder>
                ) {
                    try {
                        mainArray.clear()
                        mainArray.addAll(response.body()!!.orders)
                        ordersArray.clear()
                        ordersArray.addAll(mainArray)
                        setData(true)
                    } catch (E: java.lang.Exception) {
                        mainArray.clear()
                        ordersArray.clear()
                       try{ setData(true)}catch (e:Exception){}
                    }
                }

                override fun onFailure(call: Call<ResponseMainOrder>, throwable: Throwable) {
                    mainArray.clear()
                    ordersArray.clear()
                    try{setData(true)}catch (e:Exception){}
                }
            })
    }

    fun checkOrders(){
        var doneOnce = false
        for(item in ordersArray)
            if(item.onTrack!!){
                doneOnce = true
                retrieveOrders()
                break
            }
        if(!doneOnce)
            setData(true)

    }
    fun getOrders() {
        try {
            loading.show()
        } catch (ex: Exception) {

        }

        logw("OrerTypetag",orderType!!)
        var newReq = RequestOrders(MyApplication.userId, MyApplication.languageCode, orderType)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getOrders(
                newReq
            )?.enqueue(object : Callback<ResponseMainOrder> {
                override fun onResponse(
                    call: Call<ResponseMainOrder>,
                    response: Response<ResponseMainOrder>
                ) {
                    try {
                        mainArray.clear()
                        mainArray.addAll(response.body()!!.orders)
                        ordersArray.clear()
                        ordersArray.addAll(mainArray)
                        checkOrders()
                       // setData(true)
                    } catch (E: java.lang.Exception) {
                        mainArray.clear()
                        ordersArray.clear()
                        //try{setData(true)}catch (e:Exception){}
                    }
                }

                override fun onFailure(call: Call<ResponseMainOrder>, throwable: Throwable) {
                    mainArray.clear()
                    ordersArray.clear()
                    //try{setData(true)}catch (e:Exception){}
                }
            })
    }

    fun init() {
        /*ordersArray.clear()
        ordersArray.add("1")
        ordersArray.add("2")
        ordersArray.add("1")
        ordersArray.add("3")
        mainArray.addAll(ordersArray)*/
        (activity as ActivityHome?)!!.drawColor()
        (activity as ActivityHome?)!!.hideBack()
        /*(activity as ActivityHome?)!!.setTitleAc(
            AppHelper.getRemoteString(
                "order_type",
                requireContext()
            )
        )*/

        etSearchOrders.typeface = AppHelper.getTypeFace(requireContext())
        if(MyApplication.isClient){
            etSearchOrders.hint = AppHelper.getRemoteString("search_by_service",requireContext())
        }else{
            etSearchOrders.hint = AppHelper.getRemoteString("search_by",requireContext())
        }
        AppHelper.setTitle(requireActivity(), MyApplication.selectedTitle!!, "",R.color.primary)
        (activity as ActivityHome).showTitle(true)
        (activity as ActivityHome).showLogout(false)
        (activity as ActivityHome).setTintLogo(R.color.primary)
        if (!MyApplication.fromFooterOrder ) {
            (activity as ActivityHome).showBack(true)
        }
        if(MyApplication.fromOrderPlaced ){
            (activity as ActivityHome).showBack(false)
            MyApplication.fromOrderPlaced = false
        }

        etSearchOrders.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.length > 0) {
                    ordersArray.clear()
                    if(!MyApplication.isClient) {
                        ordersArray.addAll(mainArray.filter {
                            it.orderId!!.contains(s) ||  (it.customer!!.first_name!=null && it.customer!!.first_name!!.contains(
                                s
                            ) )|| (it.product!!.name!=null && it.product!!.name!!.contains(
                                s
                            ) )
                        })
                    }else{
                        ordersArray.addAll(mainArray.filter {
                            it.orderId!!.contains(s) || (it.vendor!=null && it.vendor!!.firstName!!.contains(
                                s
                            ) )||  (it.product!!.name!=null && it.product!!.name!!.contains(
                                s
                            ) )
                        })
                    }
                    try {
                        adapter!!.notifyDataSetChanged()
                    }catch (ex:Exception){}
                } else {
                    ordersArray.clear()
                    ordersArray.addAll(mainArray)
                    try {
                        adapter!!.notifyDataSetChanged()
                    } catch (ex: Exception) {

                    }
                }

            }
        })

        slRefresh.setOnRefreshListener(OnRefreshListener {
            if(MyApplication.isClient){
                getClientOrders()
            }else {
                getOrders()
            }
        })


    }

    private fun setTabs() {
        for (i in 0 until linearTabs.childCount) {
            linearTabs.getChildAt(i).onOneClick {
                if (typeSelected != i) {
                    var tv = linearTabs.getChildAt(i) as TextView
                    setTabLayout(i)
                    // setData(true)
                }
            }
            var tv = linearTabs.getChildAt(i) as TextView
            tv.setColorTypeface(requireContext(),R.color.primary,"",false)
        }
    }

    override fun onItemClicked(view: View, position: Int) {
        if (view.id == R.id.llLocation) {


            val intent=Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("geo:0,0?q="+ordersArray.get(position).shipping_latitude!!+","+ordersArray.get(position).shipping_longitude!!+"("+ordersArray.get(position).addressname+")")
            startActivity(intent)
               /* AppHelper.onOneClick {
                    MyApplication.selectedOrder = ordersArray.get(position)
                    if (!MyApplication.selectedOrder!!.customerLocation.isNullOrEmpty() && !MyApplication.selectedOrder!!.customerLocation.equals(
                            "null"
                        )
                    ) {
                        startActivity(
                            Intent(requireActivity(), ActivityMapAddress::class.java)
                                .putExtra(
                                    "mapTitle",
                                    AppHelper.getRemoteString("view_address", requireContext())
                                )
                                .putExtra("seeOnly", true)
                        )
                    }
                }*/

        }
        else if (view.id == R.id.llViewOrderDetails) {
            AppHelper.onOneClick {
                MyApplication.selectedOrder = ordersArray[position]
                MyApplication.rental = position == 1
                MyApplication.rental = position == 1
                mainArray
                var x = ordersArray.get(position).orderId
                startActivity(
                    Intent(requireActivity(), ActivityOrderDetails::class.java)
                        .putExtra("orderId", ordersArray.get(position).orderId!!.toInt())
                        .putExtra("type", typeSelected)
                )

            }
        }
        else if (view.id == R.id.ivOrderCall) {
            AppHelper.onOneClick {
                try {
                    if (!MyApplication.isClient) {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.setData(Uri.parse("tel:" + ordersArray.get(position).customer!!.mobile_number));
                        startActivity(intent)
                    } else {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.setData(Uri.parse("tel:" + ordersArray.get(position).vendor!!.mobileNum));
                        startActivity(intent)
                    }
                }catch (ex:Exception){
                    val intent = Intent(Intent.ACTION_DIAL)
                    startActivity(intent)
                }
            }

        }
        else if (view.id == R.id.ivOrderMessage) {
            AppHelper.onOneClick {
                /*val uri = Uri.parse("smsto:12346556")
                val it = Intent(Intent.ACTION_SENDTO, uri)
                it.putExtra("sms_body", "Here you can set the SMS text to be sent")
                startActivity(it)*/
                MyApplication.selectedOrderId = ordersArray[position].orderId!!.toInt()
                startActivity(Intent(requireContext(), ActivityChat::class.java))
            }
        }
        else if (view.id == R.id.llTrackOrder) {
            MyApplication.selectedOrder = ordersArray.get(position)
            startActivity(Intent(requireActivity(), ActivityTrackOrder::class.java))
        }
        else if (view.id==R.id.swDelivered || view.id==R.id.swPaid){
            if(ordersArray.get(position).done)
                if(MyApplication.isClient){
                    getClientOrders()
                }else {
                    getOrders()
                }
        }
    }

    fun setSelected(position: Int) {

        when (position) {
            0 -> {
                etSearchOrders.text.clear()
                llFailedTabs.hide()
                llActiveTabs.show()
                if(MyApplication.isClient) {
                    llActiveTabs.weightSum = 3f
                    tvPendingSP.show()
                }else {
                    llActiveTabs.weightSum = 2f
                    tvPendingSP.hide()
                }
                tvActive.setBackgroundResource(R.drawable.rounded_orders)
                AppHelper.setTextColor(requireContext(), tvActive, R.color.white)
                orderType = AppConstants.ORDER_TYPE_ACTIVE
                if(MyApplication.isClient){
                    getClientOrders()
                }else {
                    getOrders()
                }
            }
           /* 1 -> {
                *//*etSearchOrders.text.clear()
                tvUpcoming.setBackgroundResource(R.drawable.rounded_orders)
                llFailedTabs.hide()
                llActiveTabs.hide()
                AppHelper.setTextColor(requireContext(), tvUpcoming, R.color.white)
                orderType = AppConstants.ORDER_TYPE_UPCOMING
                if(MyApplication.isClient){
                    getClientOrders()
                }else {
                    getOrders()
                }*//*
            }*/
            1 -> {
                etSearchOrders.text.clear()
                llFailedTabs.hide()
                llActiveTabs.hide()
                tvCompleted.setBackgroundResource(R.drawable.rounded_orders)
                AppHelper.setTextColor(requireContext(), tvCompleted, R.color.white)
                orderType = AppConstants.ORDER_TYPE_COMPLETED
                if(MyApplication.isClient){
                    getClientOrders()
                }else {
                    getOrders()
                }
            }
            /*3 -> {
                etSearchOrders.text.clear()
                tvCancelled.setBackgroundResource(R.drawable.rounded_orders)
                AppHelper.setTextColor(requireContext(), tvCancelled, R.color.white)
                orderType = AppConstants.ORDER_TYPE_CANCELED
                if(MyApplication.isClient){
                getClientOrders()
            }else {
                getOrders()
            }
            }*/
            else -> {
                etSearchOrders.text.clear()
                tvFailed.setBackgroundResource(R.drawable.rounded_orders)
                AppHelper.setTextColor(requireContext(), tvFailed, R.color.white)
                llFailedTabs.show()
                llActiveTabs.hide()
                orderType = AppConstants.ORDER_TYPE_CANCELED
                tvCancelled.onOneClick {
                    if(orderType !=AppConstants.ORDER_TYPE_CANCELED ) {
                        tvCancelled.setBackgroundResource(R.drawable.rounded_orders)
                        AppHelper.setTextColor(requireContext(), tvCancelled, R.color.white)
                        tvUnsuccPayment.setBackgroundResource(R.color.transparent)
                        AppHelper.setTextColor(requireContext(), tvUnsuccPayment, R.color.primary)
                        orderType = AppConstants.ORDER_TYPE_CANCELED
                        if(MyApplication.isClient){
                            getClientOrders()
                        }else {
                            getOrders()
                        }
                    }
                }
                tvUnsuccPayment.onOneClick {
                    if(orderType !=AppConstants.ORDER_TYPE_FAILED ) {
                        tvUnsuccPayment.setBackgroundResource(R.drawable.rounded_orders)
                        AppHelper.setTextColor(requireContext(), tvUnsuccPayment, R.color.white)
                        tvCancelled.setBackgroundResource(R.color.transparent)
                        AppHelper.setTextColor(requireContext(), tvCancelled, R.color.primary)
                        orderType = AppConstants.ORDER_TYPE_FAILED
                        if(MyApplication.isClient){
                            getClientOrders()
                        }else {
                            getOrders()
                        }
                    }
                }
                if(MyApplication.isClient){
                    getClientOrders()
                }else {
                    getOrders()
                }
            }
        }
    }

    fun setTabLayout(position: Int) {
        typeSelected = position
        for (i in 0 until linearTabs.childCount) {

                if (linearTabs.getChildAt(i) is TextView) {
                    var tv = linearTabs.getChildAt(i) as TextView
                    tv.setBackgroundResource(R.color.transparent)
                    AppHelper.setTextColor(requireContext(), tv, R.color.primary)
                }

        }

        setSelected(position)

    }

    private fun setData(type: Boolean) {

        if(orderType == AppConstants.ORDER_TYPE_ACTIVE) {
            when (activeType) {
                0 -> {
                    ordersArray.clear()
                    ordersArray.addAll(mainArray.filter {
                        ( (it.vendor != null
                                && it.paymentMethod.equals("Cash On Delivery",true))
                                ||
                                (it.vendor!=null && it.paymentMethod.equals("knet",true) && it.paymentStatus!!.toInt() == 1))&&(it.isRenew!=1 || it.isRenew==1 && it.accepted==1) })
                }
                1 -> {
                    ordersArray.clear()
                    ordersArray.addAll(mainArray.filter { (it.paymentMethod.isNullOrEmpty() || (it.paymentMethod.equals("knet",true) && it.paymentStatus!!.toInt() == 0 ) )&& it.vendor!=null&&(it.isRenew!=1 || it.isRenew==1 && it.accepted==1) })
                }

                else -> {
                    ordersArray.clear()
                    ordersArray.addAll(mainArray.filter { it.vendor == null || it.isRenew==1&&it.accepted==0})
                }


            }
        }
        /*if(orderType == AppConstants.ORDER_TYPE_ACTIVE){
            ordersArray.clear()
            ordersArray.addAll(mainArray.filter { it.vendor != null && !it.paymentMethod.isNullOrEmpty() })
            tvRunning.setBackgroundResource(R.drawable.rounded_orders)
            AppHelper.setTextColor(requireContext(), tvRunning, R.color.white)
            tvPendingSP.setBackgroundResource(R.color.transparent)
            AppHelper.setTextColor(requireContext(), tvPendingSP, R.color.primary)
            tvPendingPayment.setBackgroundResource(R.color.transparent)
            AppHelper.setTextColor(requireContext(), tvPendingPayment, R.color.primary)
        }
*/
        try {
            if(ordersArray.size==0){
                tvNoData.show()
            }else{
                tvNoData.hide()
                slRefresh.isRefreshing = false
                var glm2 = LinearLayoutManager(requireContext())
                rvOrderDetails.layoutManager = glm2
                adapter = AdapterOrderType(ordersArray, this, this,requireActivity(),loading,this,mPermissionResult2!!)
                rvOrderDetails.adapter = adapter

            }
        } catch (ex: Exception) {

        }

        tvPendingPayment.onOneClick {
            activeType = 1
            loading.show()
            tvPendingPayment.setBackgroundResource(R.drawable.rounded_orders)
            AppHelper.setTextColor(requireContext(), tvPendingPayment, R.color.white)
            tvRunning.setBackgroundResource(R.color.transparent)
            AppHelper.setTextColor(requireContext(), tvRunning, R.color.primary)
            tvPendingSP.setBackgroundResource(R.color.transparent)
            AppHelper.setTextColor(requireContext(), tvPendingSP, R.color.primary)
            ordersArray.clear()
            ordersArray.addAll(mainArray.filter { (it.paymentMethod.isNullOrEmpty() || (it.paymentMethod.equals("knet",true) && it.paymentStatus!!.toInt() == 0 ) )&& it.vendor!=null&&(it.isRenew!=1 || it.isRenew==1 && it.accepted==1) })
           setData(true)
            loading.hide()

        }
        tvPendingSP.onOneClick {
            activeType = 2
            loading.show()
            tvPendingSP.setBackgroundResource(R.drawable.rounded_orders)
            AppHelper.setTextColor(requireContext(), tvPendingSP, R.color.white)
            tvRunning.setBackgroundResource(R.color.transparent)
            AppHelper.setTextColor(requireContext(), tvRunning, R.color.primary)
            tvPendingPayment.setBackgroundResource(R.color.transparent)
            AppHelper.setTextColor(requireContext(), tvPendingPayment, R.color.primary)
            ordersArray.clear()
            ordersArray.addAll(mainArray.filter { it.vendor == null || it.isRenew==1&&it.accepted==0})
            setData(true)
            loading.hide()
        }
        tvRunning.onOneClick {
            loading.show()
            activeType = 0
            tvRunning.setBackgroundResource(R.drawable.rounded_orders)
            AppHelper.setTextColor(requireContext(), tvRunning, R.color.white)
            tvPendingSP.setBackgroundResource(R.color.transparent)
            AppHelper.setTextColor(requireContext(), tvPendingSP, R.color.primary)
            tvPendingPayment.setBackgroundResource(R.color.transparent)
            AppHelper.setTextColor(requireContext(), tvPendingPayment, R.color.primary)
            ordersArray.clear()
            ordersArray.addAll(mainArray.filter {
                ( (it.vendor != null
                        && it.paymentMethod.equals("Cash On Delivery",true))
                        ||
                        (it.vendor!=null && it.paymentMethod.equals("knet",true) && it.paymentStatus!!.toInt() == 1))&&(it.isRenew!=1 || it.isRenew==1 && it.accepted==1) })
            setData(true)
            loading.hide()
        }
        /* if(adapter!=null){
             adapter!!.notifyDataSetChanged()
             adapter!!.notifyDataSetChanged()
         }else {
             try {
                 adapter = AdapterOrderType(ordersArray, this, requireActivity())
                 rvOrderDetails.adapter = adapter
                 var glm2 = GridLayoutManager(requireContext(), 1)
                 rvOrderDetails.layoutManager = glm2
             }catch (ex:Exception){

             }
         }*/
        try {
            loading.hide()
        } catch (ex: Exception) {

        }
    }


    private fun openChooser() {


        mPermissionResult!!.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
        /*mPermissionResult!!.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )*/


    }

    fun setUp() {
        mPermissionResult =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
            { result ->
                var permission = false
                for (item in result) {
                    permission = item.value
                }
                if (permission) {
                    allowedLocation = true
                    setData(true)
                    //getOrders()
                 //   selectImage(requireContext())
                  //  Log.e(TAG, "onActivityResult: PERMISSION GRANTED")
                  //  MyApplication.permissionAllow11 = 0
                } else {
                    requireActivity().toast(AppHelper.getRemoteString("location_updates_disabled",requireContext()))
                    for(item in result ){
                        if(ContextCompat.checkSelfPermission(requireContext(),item.key) == BLOCKED){
                            denied = true
                            setData(true)

                            if(getPermissionStatus( Manifest.permission.ACCESS_FINE_LOCATION) == BLOCKED_OR_NEVER_ASKED) {
                                Toast.makeText(
                                    requireContext(),
                                    AppHelper.getRemoteString(
                                        "grant_settings_permission",
                                        requireContext()
                                    ),
                                    Toast.LENGTH_LONG
                                ).show()
                                break
                            }
                        }
                    }

                }
            }



    }


    private fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            Snackbar.make(
                requireActivity().findViewById(R.id.rootLayoutOrderDetails),
                "Location permission needed for core functionality",
                Snackbar.LENGTH_LONG
            ).setAction(R.string.ok) {
                // Request permission
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE

                )
            }.show()
        } else {
            Log.d(TAG, "Request foreground only permission")
            ActivityCompat.requestPermissions(
                requireActivity(),
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

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionResult")

        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    // If user interaction was interrupted, the permission request
                    // is cancelled and you receive empty arrays.
                    Log.d(TAG, "User interaction was cancelled.")
                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    getOrders()
                    // Permission was granted.
                    //MyApplication.foregroundOnlyLocationService?.subscribeToLocationUpdates()
                else -> {
                    // Permission denied.
                    // updateButtonState(false)
                    denied = true
                    getOrders()
                    requireActivity().toast(AppHelper.getRemoteString("permission_denied",requireContext()))
                    /*Snackbar.make(
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
                        .show()*/
                }
            }
        }
    }

    fun retrieveOrders(){
        slRefresh.isRefreshing=false

        var gps_enabled = false
        var mLocationManager =
            requireActivity().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

        try {
            gps_enabled = mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }
        if (!MyApplication.isClient) {
            if(foregroundPermissionApproved() && gps_enabled) {
                setData(true)
            }else{
                if(gps_enabled) {
                    val builder = AlertDialog.Builder(requireContext())
                    builder
                        .setMessage(AppHelper.getRemoteString("permission_background_android", requireActivity()))
                        .setCancelable(true)
                        .setNegativeButton( AppHelper.getRemoteString("cancel", requireActivity())) { dialog, _ ->
                            requireActivity().toast(AppHelper.getRemoteString("location_updates_disabled",requireContext()))
                            setData(true)
                        }
                        .setPositiveButton(AppHelper.getRemoteString("ok", requireActivity())) { dialog, _ ->
                            openChooser()
                        }
                    val alert = builder.create()
                    alert.show()

                }else{
                    requireActivity().toast(AppHelper.getRemoteString("GpsDisabled", requireContext()))
                    setData(true)
                }
            }
        //    MyApplication.listOrderTrack.clear()
        }/*else
            getClientOrders()*/
    }

    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
    override fun reload() {
        getOrders()
    }
}