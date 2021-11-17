package com.ids.qasemti.controller.Activities

import android.app.ActionBar
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ActivityPlaceOrder : AppCompactBase(), RVOnItemClickListener, UPaymentCallBack, ApiListener {

    var fragMang: FragmentManager? = null
    var selectedPaymentId: Int? = 0
    var selectedSlug: String? = ""
    var firstTime = true
    var merchantId: String? = ""
    var request: RequestPaymentOrder? = null
    var reviewed: Boolean? = false
    var username: String? = ""
    var arrayOrderData: ArrayList<OrderData> = arrayListOf()
    var arrayOrderCost: ArrayList<OrderData> = arrayListOf()

    var adapterOrderData: AdapterOrderData? = null
    var adapterOrderCost: AdapterOtherOrderData? = null
    var apiKey: String? = ""
    var refNum: String? = ""
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
        arrayOrderData.clear()
        arrayOrderData.add(
            OrderData(
                AppHelper.getRemoteString("category", this),
                if (orders.type != null && orders.type!!.isNotEmpty()) orders.type!! else ""
            )
        )
        arrayOrderData.add(
            OrderData(
                AppHelper.getRemoteString("service", this),
                if (orders.product!!.name != null && orders.product!!.name!!.isNotEmpty()) orders.product!!.name else ""
            )
        )
        arrayOrderData.add(
            OrderData(
                AppHelper.getRemoteString("type", this),
                if (orders.product!!.type != null && orders.product!!.types!!.isNotEmpty()) orders.product!!.types else ""
            )
        )
        arrayOrderData.add(
            OrderData(
                AppHelper.getRemoteString("SizeCapacity", this),
                if (orders.product!!.sizeCapacity != null && orders.product!!.sizeCapacity!!.isNotEmpty()) orders.product!!.sizeCapacity else ""
            )
        )
        arrayOrderData.add(OrderData(AppHelper.getRemoteString("Quantity", this), "1"))
        rvDataBorder.layoutManager = LinearLayoutManager(this)
        adapterOrderData = AdapterOrderData(arrayOrderData, this, this)
        rvDataBorder.adapter = adapterOrderData

        arrayOrderCost.clear()

        arrayOrderCost.add(
            OrderData(
                AppHelper.getRemoteString("Subtotal", this),
                if (orders.total != null && orders.total!!.isNotEmpty()) orders.total + " KWD" else ""
            )
        )
        arrayOrderCost.add(
            OrderData(
                AppHelper.getRemoteString("AdditionalFees", this),
                if (orders.shippingTotal != null && orders.shippingTotal!!.isNotEmpty()) orders.shippingTotal + " KWD" else ""
            )
        )
        arrayOrderCost.add(
            OrderData(
                AppHelper.getRemoteString("TotalAmount", this),
                if (orders.grand_total != null && orders.grand_total!!.isNotEmpty()) orders.grand_total + " KWD" else ""
            )
        )
        rvOtherData.layoutManager = LinearLayoutManager(this)
        adapterOrderCost = AdapterOtherOrderData(arrayOrderCost, this, this)
        rvOtherData.adapter = adapterOrderCost

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
            request = RequestPaymentOrder(orderId.toInt(), selectedPaymentId)
            finalStep()
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
            .setCurrencyCode(MyApplication.currency)
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

    fun setCouponData(res: ResponsePreviewCoupon) {

        arrayOrderCost.clear()
        arrayOrderCost.add(
            OrderData(
                AppHelper.getRemoteString("Subtotal", this),
                if (MyApplication.selectedOrder!!.total != null && MyApplication.selectedOrder!!.total!!.isNotEmpty()) MyApplication.selectedOrder!!.total + " KWD" else ""
            )
        )
        arrayOrderCost.add(
            OrderData(
                AppHelper.getRemoteString("AdditionalFees", this),
                if (MyApplication.selectedOrder!!.shippingTotal != null && MyApplication.selectedOrder!!.shippingTotal!!.isNotEmpty()) MyApplication.selectedOrder!!.shippingTotal + " KWD" else ""
            )
        )
        arrayOrderCost.add(
            OrderData(
                AppHelper.getRemoteString("OldAmount", this),
                if (res.oldTotal != null && res.oldTotal!!.isNotEmpty()) res.oldTotal + " KWD" else ""
            )
        )

        arrayOrderCost.add(
            OrderData(
                AppHelper.getRemoteString("DiscountAmount", this),
                if (res.totalDiscountAmount != null && res.totalDiscountAmount!!.isNotEmpty()) res.totalDiscountAmount + " KWD" else ""
            )
        )
        arrayOrderCost.add(
            OrderData(
                AppHelper.getRemoteString("NewTotal", this),
                if (res.newTotal != null && res.newTotal!!.isNotEmpty()) res.newTotal + " KWD" else ""
            )
        )

        rvOtherData.layoutManager = LinearLayoutManager(this)
        adapterOrderCost = AdapterOtherOrderData(arrayOrderCost, this, this)
        rvOtherData.adapter = adapterOrderCost
        reviewed = true

    }

    fun applyCoupon() {
        loading.show()
        var request = RequestCouponReview(
            MyApplication.selectedOrder!!.orderId!!.toInt(),
            etCoupon.text.toString(),
            MyApplication.languageCode
        )
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.applyCoupon(request)?.enqueue(object : Callback<ResponsePreviewCoupon> {
                override fun onResponse(
                    call: Call<ResponsePreviewCoupon>,
                    response: Response<ResponsePreviewCoupon>
                ) {
                    try {
                        loading.hide()
                        if (response.body()!!.result!!.equals("1")) {
                            updatePayment()
                        } else {
                            AppHelper.createYesNoDialog(
                                this@ActivityPlaceOrder,
                                "Place Order",
                                "Different code",
                                "Incorrect coupon code, do you want to move forward without coupon or try a different code ?"
                            ) {
                                updatePayment()
                            }
                        }
                    } catch (E: java.lang.Exception) {
                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponsePreviewCoupon>, throwable: Throwable) {
                    loading.hide()

                }
            })
    }

    fun couponDialog(res: ResponsePreviewCoupon) {

        var dialog = Dialog(this, R.style.Base_ThemeOverlay_AppCompat_Dialog)
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
        var layout: LinearLayout = dialog.findViewById(R.id.llCoupon)
        var btApply: Button = dialog.findViewById(R.id.btApply)
        var btProceed: Button = dialog.findViewById(R.id.btProceed)
        var Title: TextView = dialog.findViewById(R.id.tvCouponTitle)
        layout.show()
        Title.show()

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        var arr: ArrayList<OrderData> = arrayListOf()
        arr.add(
            OrderData(
                AppHelper.getRemoteString("OldAmount", this),
                if (res.oldTotal != null && res.oldTotal!!.isNotEmpty()) res.oldTotal + " KWD" else ""
            )
        )

        arr.add(
            OrderData(
                AppHelper.getRemoteString("DiscountAmount", this),
                if (res.totalDiscountAmount != null && res.totalDiscountAmount!!.isNotEmpty()) res.totalDiscountAmount + " KWD" else ""
            )
        )
        arr.add(
            OrderData(
                AppHelper.getRemoteString("NewTotal", this),
                if (res.newTotal != null && res.newTotal!!.isNotEmpty()) res.newTotal + " KWD" else ""
            )
        )
        rv.layoutManager = layoutManager
        var adapter = AdapterOtherOrderData(arr, this, this)
        rv.adapter = adapter

        dialog!!.show()


        btApply.onOneClick {
            dialog.dismiss()
            applyCoupon()
        }

        btProceed.onOneClick {
            dialog.dismiss()
            updatePayment()

        }

    }

    fun reviewCoupon(type: Int) {
        loading.show()
        var request = RequestCouponReview(
            MyApplication.selectedOrder!!.orderId!!.toInt(),
            etCoupon.text.toString(),
            MyApplication.languageCode
        )
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.previewCoupon(request)?.enqueue(object : Callback<ResponsePreviewCoupon> {
                override fun onResponse(
                    call: Call<ResponsePreviewCoupon>,
                    response: Response<ResponsePreviewCoupon>
                ) {
                    try {
                        loading.hide()
                        if (response.body()!!.result!!.equals("1")) {
                            if (type == 1)
                                setCouponData(response.body()!!)
                            else if (type == 2) {
                                couponDialog(response.body()!!)
                            }
                        } else {
                            if (type == 1) {
                                AppHelper.createDialog(
                                    this@ActivityPlaceOrder,
                                    response.body()!!.message!!
                                )
                                setData(MyApplication.selectedOrder!!)
                            } else if (type == 2) {
                                AppHelper.createYesNoDialog(
                                    this@ActivityPlaceOrder,
                                    "Place Order",
                                    "Different code",
                                    "Incorrect coupon code, do you want to move forward without coupon or try a different code ?"
                                ) {
                                    updatePayment()
                                }
                            }
                        }
                    } catch (E: java.lang.Exception) {
                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponsePreviewCoupon>, throwable: Throwable) {
                    loading.hide()

                }
            })
    }

    fun paymentCoupon(message: String, doAction: () -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder
            .setMessage(message)
            .setCancelable(true)
            .setNegativeButton("Proceed without Coupon") { dialog, _ ->
                updatePayment()
            }
            .setPositiveButton("Apply Coupon") { dialog, _ ->
                doAction()
            }
        val alert = builder.create()
        alert.show()
    }

    fun setListeners() {

        btPLaceOrder.onOneClick {
            if (arrayPaymentMethods.count { it.selected } == 0)
                AppHelper.createDialog(this, "Please choose payment method")
            else {

                if (etCoupon.text.isNullOrEmpty())
                    updatePayment()
                else {
                    if (reviewed!!) {
                        reviewed = false
                        paymentCoupon("Do you want to apply this coupon before placing order ?") {
                            applyCoupon()
                        }
                    } else {
                        reviewCoupon(2)
                    }
                }
            }
        }

        btReviewCoupon.onOneClick {
            if (etCoupon.text.isNullOrEmpty()) {
                AppHelper.createDialog(this, AppHelper.getRemoteString("fill_all_field", this))
            } else {
                reviewCoupon(1)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        //  loading.hide()
    }

    fun finalStep() {
        if (orderId == "")
            orderId = "0"
        loading.show()
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.updatePaymentOrder(request!!)?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(
                    call: Call<ResponseMessage>,
                    response: Response<ResponseMessage>
                ) {
                    try {
                        loading.hide()
                        if (response.body()!!.result == 1) {
                            nextStep()
                        }
                    } catch (E: java.lang.Exception) {

                    }
                }

                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    loading.hide()

                }
            })
    }

    fun updatePayment() {
        paymentMethodStep()
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

                var sha1 =
                    AppHelper.getSha256Hash(merchantId + username + apiKey + MyApplication.currency + MyApplication.selectedOrder!!.orderId + MyApplication.selectedOrder!!.product!!.qty + postUpayData.payMentId + postUpayData.ref + postUpayData.tranID + postUpayData.trackID + postUpayData.auth + postUpayData.cust_ref)

                var sha15 = sha1 + MyApplication.salt
                var sha2 = AppHelper.getSha256Hash(sha15)
              //  var bytes = org.apache.commons.codec.digest.DigestUtils.getSha256Hash(sha15)
             //   var myJsonString = Gson().toJson(bytes)

                var cal = Calendar.getInstance()
                var pickedDate = cal.time
                val myFormat = "yyyy-MM-dd" //Change as you need
                var sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
                var date = sdf.format(cal.time)

                request = RequestPaymentOrder(
                    orderId.toInt(),
                    selectedPaymentId,
                    MyApplication.selectedOrder!!.grand_total,
                    1,
                    "paid",
                    MyApplication.currency,
                    postUpayData.ref,
                    postUpayData.trackID,
                    postUpayData.tranID,
                    postUpayData.auth,
                    sha2,
                    date,
                    postUpayData.cust_ref,
                    "",
                    ""
                )
                finalStep()
            }
        } else {

            var str = merchantId + username + apiKey + MyApplication.currency + MyApplication.selectedOrder!!.orderId + MyApplication.selectedOrder!!.product!!.qty + postUpayData.payMentId + postUpayData.ref + postUpayData.tranID + postUpayData.trackID + postUpayData.auth + postUpayData.cust_ref
            var sha1 =
                AppHelper.getSha256Hash(str)

            var sha15 = sha1 + MyApplication.salt
            var sha2 = AppHelper.getSha256Hash(sha15)

            var cal = Calendar.getInstance()
            var pickedDate = cal.time
            val myFormat = "yyyy-MM-dd" //Change as you need
            var sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
            var date = sdf.format(cal.time)
            runOnUiThread(Runnable {
                AppHelper.createDialog(this@ActivityPlaceOrder, postUpayData.result)
                request = RequestPaymentOrder(
                    orderId.toInt(),
                    selectedPaymentId,
                    MyApplication.selectedOrder!!.grand_total,
                    1,
                    "failed",
                    MyApplication.currency,
                    postUpayData.ref,
                    postUpayData.trackID,
                    postUpayData.tranID,
                    postUpayData.auth,
                    sha2,
                    date,
                    postUpayData.cust_ref,
                    postUpayData.result,
                    postUpayData.result
                )
                finalStep()
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

        var str = merchantId + username + apiKey +  MyApplication.currency + MyApplication.selectedOrder!!.orderId + MyApplication.selectedOrder!!.product!!.qty
        Log.wtf("tagSTR",str)
        var sha1 =
            AppHelper.getSha256Hash(str/* + "" + "" + "" + "" + "" + ""*/)
     //   $token = hash("sha256",hash("sha256", $merchant_id.$merchant_username.$api_key.$currency.$order_number.$order_amount.$payment_id.$reference.$trans.$track.$authorization.$customer_ref).$salt);

        var sha15 = sha1 + MyApplication.salt
        var sha2 = AppHelper.getSha256Hash(sha15)

        var cal = Calendar.getInstance()
        var pickedDate = cal.time
        val myFormat = "yyyy-MM-dd" //Change as you need
        var sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        var date = sdf.format(cal.time)
        runOnUiThread(Runnable {
            request = RequestPaymentOrder(
                MyApplication.selectedOrder!!.orderId!!.toInt(),
                selectedPaymentId,
                MyApplication.selectedOrder!!.product!!.qty,
                0,
                "failed",
                MyApplication.currency,
                "",
                "",
                "",
                "",
                sha2,
                date,
                "",
                data,
                data
            )
            var x = data
            var y = sha2
            finalStep()
            loading.hide()
        })


    }


    override fun onDataRetrieved(success: Boolean, response: Any, apiId: Int) {
        var orderData = response as ResponseOrders
        MyApplication.selectedOrder = orderData
        setData(orderData)
        loading.hide()
    }
}