package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.*
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.controller.Fragments.*
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import com.upayments.UPaymentCallBack
import com.upayments.activity.PostUpayData
import com.upayments.track.UpaymentGateway
import com.upayments.track.UpaymentGatewayEvent.Builder
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.activity_place_order.*
import kotlinx.android.synthetic.main.activity_place_order.rootLayout
import kotlinx.android.synthetic.main.footer.*
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.android.synthetic.main.home_container.*
import kotlinx.android.synthetic.main.layout_border_data.*
import kotlinx.android.synthetic.main.layout_border_red.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.service_tab_1.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivityPlaceOrder : AppCompactBase(), RVOnItemClickListener, UPaymentCallBack, ApiListener {

    var fragMang: FragmentManager? = null
    var selectedPaymentId: Int? = 0
    var selectedSlug: String? = ""
    var firstTime = true
    var merchantId : String ?=""
    var username : String ?=""
    var apiKey : String ?=""
    var refNum : String ?=""
    var orderId = "0"

    var arrayPaymentMethods: ArrayList<PaymentMethod> = arrayListOf()
    var adapterPaymentMethods: AdapterPaymentMethods? = null
    override fun onItemClicked(view: View, position: Int) {
        if (view.id == R.id.linearPaymentMethod) {
            try {
                arrayPaymentMethods.forEach { it.selected = false }
                arrayPaymentMethods[position].selected = true
                selectedPaymentId = arrayPaymentMethods[position].id
                adapterPaymentMethods!!.notifyDataSetChanged()
            } catch (e: Exception) {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_order)
        init()
        AppHelper.setAllTexts(rootLayout, this)
        supportActionBar!!.hide()


    }

    fun setData(orders: ResponseOrders) {

        tvLocationPlaceOrder.text = ""
        if (!orders.addressDescription.equals("null") && !orders.shipping_address_description.isNullOrEmpty()) {
            tvLocationPlaceOrder.text = orders.addressDescription + ","
        }
        if (!orders.shipping_address_street.equals("null") && !orders.shipping_address_street.isNullOrEmpty()) {
            tvLocationPlaceOrder.text =
                tvLocationPlaceOrder.text.toString() + orders.shipping_address_street + ","
        }
        if (!orders.shipping_address_building.equals("null") && !orders.shipping_address_building.isNullOrEmpty()) {
            tvLocationPlaceOrder.text =
                tvLocationPlaceOrder.text.toString() + orders.shipping_address_building + ","
        }
        if (!orders.shipping_address_floor.equals("null") && !orders.shipping_address_floor.isNullOrEmpty()) {
            tvLocationPlaceOrder.text =
                tvLocationPlaceOrder.text.toString() + orders.shipping_address_floor + ","
        }
        if (tvLocationPlaceOrder.text.isNullOrEmpty())
            tvLocationPlaceOrder.text = AppHelper.getRemoteString("no_data", this)

        try {
            tvOrderDate.text =
                AppHelper.formatDate(orders.date!!, "yyyy-MM-dd hh:mm:ssss", "dd MMM yyyy hh:mm")
        } catch (e: Exception) {
        }
        try {
            tvExpectedDate.text = orders.deliveryDate
        } catch (ex: Exception) {
        }
        fragMang = supportFragmentManager
        var array: ArrayList<OrderData> = arrayListOf()
        array.add(
            OrderData(
                AppHelper.getRemoteString("category", this),
                if (orders.type != null && orders.type!!.isNotEmpty()) orders.type!! else ""
            )
        )
        array.add(
            OrderData(
                AppHelper.getRemoteString("service", this),
                if (orders.product!!.name != null && orders.product!!.name!!.isNotEmpty()) orders.product!!.name else ""
            )
        )
        array.add(
            OrderData(
                AppHelper.getRemoteString("type", this),
                if (orders.product!!.type != null && orders.product!!.types!!.isNotEmpty()) orders.product!!.types else ""
            )
        )
        array.add(
            OrderData(
                AppHelper.getRemoteString("SizeCapacity", this),
                if (orders.product!!.sizeCapacity != null && orders.product!!.sizeCapacity!!.isNotEmpty()) orders.product!!.sizeCapacity else ""
            )
        )
        array.add(OrderData(AppHelper.getRemoteString("Quantity", this), "1"))
        rvDataBorder.layoutManager = LinearLayoutManager(this)
        rvDataBorder.adapter = AdapterOrderData(array, this, this)

        var array2: ArrayList<OrderData> = arrayListOf()
        array2.add(
            OrderData(
                AppHelper.getRemoteString("Subtotal", this),
                if (orders.total != null && orders.total!!.isNotEmpty()) orders.total + " KWD" else ""
            )
        )
        array2.add(
            OrderData(
                AppHelper.getRemoteString("AdditionalFees", this),
                if (orders.shippingTotal != null && orders.shippingTotal!!.isNotEmpty()) orders.shippingTotal + " KWD" else ""
            )
        )
        array2.add(
            OrderData(
                AppHelper.getRemoteString("TotalAmount", this),
                if (orders.grand_total != null && orders.grand_total!!.isNotEmpty()) orders.grand_total + " KWD" else ""
            )
        )
        rvOtherData.layoutManager = LinearLayoutManager(this)
        rvOtherData.adapter = AdapterOtherOrderData(array2, this, this)

        loading.hide()

    }

    fun nextStep() {
        finishAffinity()
        MyApplication.selectedPos = 1
        MyApplication.fromOrderPlaced = true
        MyApplication.typeSelected = 0
        MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_ORDER
        MyApplication.selectedFragment = FragmentOrders()
        MyApplication.tintColor = R.color.primary
        startActivity(Intent(this@ActivityPlaceOrder, ActivityHome::class.java))
    }

    fun paymentMethodStep() {

        selectedSlug = arrayPaymentMethods.find { it.id == selectedPaymentId }!!.slug
        if (selectedSlug.equals("cod")) {
            nextStep()
        } else {
            paymentGateway()
        }
        // nextStep()
    }

    fun paymentGateway() {


        //
        /* var array : ArrayList<String> = arrayListOf()
         val analyticsEvent3 = Builder<Builder<Builder<*>>>(MyApplication.selectedPlaceOrder!!.userId!!.toString())
             .setMerchantId(MyApplication.selectedPlaceOrder!!.userId!!.toString())
             .setUsername(MyApplication.selectedPlaceOrder!!.firstName)
             .setPassword("password")
             .setApikey("apiKey")
             .setOrderId(MyApplication.selectedPlaceOrder!!.productId!!.toString())
             .setTotalPrice(MyApplication.selectedPlaceOrder!!.price)
             .setCurrencyCode("KWD")
             .setSuccessUrl("https://example.com/success.html")
             .setErrorUrl("https://example.com/success.html")
             .setTestMode("https://example.com/success.html")
             .setCustomerName("mCustomerName")
             .setCustomerEmail("mCustomerEmail")
             .setCustomerMobile("mCustomerMobile")
             .setPaymentGateway("mPaymentGateway")
             .setWhitelabled(true)
             .setProductTitle("mProductTitle")
             .setProductName(array)
             .setProductPrice(array)
             .setProductQty(array)
             .setReference("")
             .setNotifyUrl("https://example.com/success.html")
             .build()
 */
        loading.show()
        val listProductName: MutableList<String> = java.util.ArrayList()
        listProductName.add(MyApplication.selectedPlaceOrder!!.title!!)

        val listProductPrice: MutableList<String> = java.util.ArrayList()
        listProductPrice.add(MyApplication.selectedPlaceOrder!!.price!!)

        val listProductQuantity: MutableList<String> = java.util.ArrayList()
        listProductQuantity.add(MyApplication.selectedPlaceOrder!!.sizeCapacity!!)


        merchantId = MyApplication.payparams!!.params.find { it.key == "merchant_id" }!!.value
        username = MyApplication.payparams!!.params.find { it.key == "username" }!!.value
        var password = MyApplication.payparams!!.params.find { it.key == "password" }!!.value
        apiKey = MyApplication.payparams!!.params.find { it.key == "apiKey" }!!.value
        var succURL = MyApplication.payparams!!.params.find { it.key == "successURL" }!!.value
        var errorURL = MyApplication.payparams!!.params.find { it.key == "errorURL" }!!.value
        refNum = MyApplication.payparams!!.params.find { it.key == "reference" }!!.value
        var notifyURl = MyApplication.payparams!!.params.find { it.key == "notifyURL" }!!.value
        var testMode = MyApplication.payparams!!.params.find { it.key == "test_mode" }!!.value




        val analyticsEvent3 = Builder<Builder<Builder<*>>>(merchantId)
            .setMerchantId(merchantId)
            .setUsername(username)
            .setPassword(password)
            .setApikey(apiKey)
            .setOrderId(MyApplication.selectedPlaceOrder!!.productId!!.toString())
            .setTotalPrice(MyApplication.selectedPlaceOrder!!.price)
            .setCurrencyCode("KWD")
            .setSuccessUrl(succURL)
            .setErrorUrl(errorURL)
            .setTestMode(testMode)
            .setCustomerName(MyApplication.selectedPlaceOrder!!.firstName + " " + MyApplication.selectedPlaceOrder!!.lastName)
            .setCustomerEmail(MyApplication.selectedPlaceOrder!!.email)
            .setCustomerMobile(MyApplication.selectedPlaceOrder!!.phone)
            .setPaymentGateway(selectedSlug)
            .setWhitelabled(true)
            .setProductTitle(MyApplication.selectedPlaceOrder!!.title)
            .setProductName(listProductName)
            .setProductPrice(listProductPrice)
            .setProductQty(listProductQuantity)
            .setReference(refNum)
            .setNotifyUrl(notifyURl)
            .build();
        UpaymentGateway.getInstance().track(analyticsEvent3, this)
    }

    fun setListeners() {

        btPLaceOrder.setOnClickListener {
            if (arrayPaymentMethods.count { it.selected } == 0)
                AppHelper.createDialog(this, "Please choose payment method")
            else
                updatePayment()
        }
    }


    override fun onResume() {
        super.onResume()
        //  loading.hide()
    }

    fun updatePayment() {
        if (orderId == "")
            orderId = "0"
        loading.show()
        var request = RequestPaymentOrder(orderId.toInt(), selectedPaymentId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.updatePaymentOrder(request)?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(
                    call: Call<ResponseMessage>,
                    response: Response<ResponseMessage>
                ) {
                    try {
                        loading.hide()
                        if (response.body()!!.result == 1) {
                            paymentMethodStep()
                        }
                    } catch (E: java.lang.Exception) {

                    }
                }

                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    loading.hide()

                }
            })
    }


    fun init() {


        tvLocationPlaceOrder.setColorTypeface(this, R.color.primary, "", false)
        tvPageTitle.show()
        tvPageTitle.textRemote("PlaceOrder", this)
        tvPageTitle.setColorTypeface(this, R.color.primary, "", true)
        btBackTool.show()
        btBackTool.onOneClick {
            super.onBackPressed()
        }
        btPLaceOrder.typeface = AppHelper.getTypeFace(this)
        btClose.hide()
        AppHelper.setLogoTint(btBackTool, this, R.color.primary)
        setListeners()

        AppHelper.setLogoTint(btDrawer, this, R.color.primary)
        var spFound = true
        if (intent.hasExtra(AppConstants.SP_FOUND))
            spFound = intent.extras!!.getBoolean(AppConstants.SP_FOUND, true)
        if (intent.hasExtra(AppConstants.ORDER_ID))
            orderId = intent.extras!!.getString(AppConstants.ORDER_ID, "0")
        loading.show()
        try {
            CallAPIs.getOrderByOrderId(orderId.toInt(), this)
        } catch (ex: Exception) {
        }
        if (!spFound) {
            rlNotService.show()
            llMain.hide()
        } else {
            rlNotService.hide()
            llMain.show()
        }


        getPaymentMethods()

    }


    fun getPaymentMethods() {
        loading.show()
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getPaymentMethods()?.enqueue(object : Callback<ResponsePaymentMethod> {
                override fun onResponse(
                    call: Call<ResponsePaymentMethod>,
                    response: Response<ResponsePaymentMethod>
                ) {
                    try {
                        loading.hide()
                        arrayPaymentMethods.clear()
                        if (response.body()!!.result == 1) {
                            arrayPaymentMethods.addAll(response.body()!!.paymentMethods!!)
                            setPaymentMethods()
                        } else {
                            toast("Error in getting payment methods")
                        }
                    } catch (E: java.lang.Exception) {
                        toast("Error in getting payment methods")
                    }
                }

                override fun onFailure(call: Call<ResponsePaymentMethod>, throwable: Throwable) {
                    loading.hide()
                    toast("Error in getting payment methods")
                }
            })
    }


    private fun setPaymentMethods() {
        adapterPaymentMethods = AdapterPaymentMethods(arrayPaymentMethods, this, this)
        rvPaymentMethod.layoutManager = GridLayoutManager(this, 1)
        rvPaymentMethod.adapter = adapterPaymentMethods
        rvPaymentMethod.isNestedScrollingEnabled = false
    }

    override fun callBackUpayment(postUpayData: PostUpayData?) {
        loading.hide()
        if (postUpayData!!.result == AppConstants.PAYMENT_SUCCESS) {

            if (firstTime) {
                firstTime = false
                nextStep()
            }


           // postUpayData.
            var sha1 =
                AppHelper.sha256(merchantId + username + apiKey + "KWD" + MyApplication.selectedOrder!!.orderId + MyApplication.selectedOrder!!.product!!.qty +postUpayData.payMentId+ refNum + postUpayData.tranID + postUpayData.trackID + 100+postUpayData.cust_ref +postUpayData.auth)

            var sha15 = sha1 + MyApplication.salt
            var sha2 = AppHelper.sha256(sha15)
            var bytes = org.apache.commons.codec.digest.DigestUtils.sha256(sha15)
            var myJsonString = Gson().toJson(bytes)
        } else {
            runOnUiThread(Runnable {
                AppHelper.createDialog(this@ActivityPlaceOrder, postUpayData.result)
                loading.hide()
            })
        }
        Log.wtf("callBack", "data")
    }

    override fun errorPayUpayment(data: String?) {

        var message: String? = ""
        if (MyApplication.languageCode == AppConstants.LANG_ARABIC)
            message = MyApplication.payparams!!.errorCode.find { it.key == data }!!.codeAr
        else
            message = MyApplication.payparams!!.errorCode.find { it.key == data }!!.codeEn

        runOnUiThread(Runnable {
            AppHelper.createDialog(this@ActivityPlaceOrder, message!!)
            loading.hide()
        })


    }

    override fun onDataRetrieved(success: Boolean, response: Any, apiId: Int) {
        var orderData = response as ResponseOrders
        setData(orderData)
        loading.hide()
    }
}