package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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


class ActivityPlaceOrder : AppCompactBase(), RVOnItemClickListener , UPaymentCallBack {

    var fragMang: FragmentManager? = null
    var selectedPaymentId : Int ?=0
    var selectedSlug : String ?=""
    var orderId="0"

    var arrayPaymentMethods: ArrayList<PaymentMethod> = arrayListOf()
    var adapterPaymentMethods : AdapterPaymentMethods?=null
    override fun onItemClicked(view: View, position: Int) {
      if(view.id==R.id.linearPaymentMethod){
          try{
          arrayPaymentMethods.forEach { it.selected=false }
          arrayPaymentMethods[position].selected=true
          selectedPaymentId=  arrayPaymentMethods[position].id
          adapterPaymentMethods!!.notifyDataSetChanged()}catch (e:Exception){}
      }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_order)
        init()
        AppHelper.setAllTexts(rootLayout,this)
        supportActionBar!!.hide()



    }
    fun setData(){

        if(!MyApplication.selectedPlaceOrder!!.addressDescription.equals("null")&&! MyApplication.selectedPlaceOrder!!.addressDescription.isNullOrEmpty()){
            tvLocationPlaceOrder.text = MyApplication.selectedPlaceOrder!!.addressDescription
        }
        if(!MyApplication.selectedPlaceOrder!!.addressStreet.equals("null")&&!MyApplication.selectedPlaceOrder!!.addressStreet.isNullOrEmpty()){
            tvLocationPlaceOrder.text =   tvLocationPlaceOrder.text.toString()+","+MyApplication.selectedPlaceOrder!!.addressStreet
        }
        if(!MyApplication.selectedPlaceOrder!!.addressBuilding.equals("null")&&!MyApplication.selectedPlaceOrder!!.addressBuilding.isNullOrEmpty()){
            tvLocationPlaceOrder.text =   tvLocationPlaceOrder.text.toString()+","+MyApplication.selectedPlaceOrder!!.addressBuilding
        }
        if(!MyApplication.selectedPlaceOrder!!.addressFloor.equals("null")&&!MyApplication.selectedPlaceOrder!!.addressFloor.isNullOrEmpty()){
            tvLocationPlaceOrder.text =   tvLocationPlaceOrder.text.toString()+","+MyApplication.selectedPlaceOrder!!.addressFloor
        }

        try{tvOrderDate.text = MyApplication.selectedPlaceOrder!!.deliveryDate}catch (e:Exception){}
        fragMang = supportFragmentManager
        var array:ArrayList<OrderData> = arrayListOf()
        array.add(OrderData(AppHelper.getRemoteString("category",this),if(MyApplication.selectedPlaceOrder!!.productCategory!=null && MyApplication.selectedPlaceOrder!!.productCategory!!.isNotEmpty()) MyApplication.selectedPlaceOrder!!.productCategory!! else ""))
        array.add(OrderData(AppHelper.getRemoteString("service",this),if(MyApplication.selectedPlaceOrder!!.title!=null && MyApplication.selectedPlaceOrder!!.title!!.isNotEmpty()) MyApplication.selectedPlaceOrder!!.title else ""))
        array.add(OrderData(AppHelper.getRemoteString("type",this),if(MyApplication.selectedPlaceOrder!!.types!=null && MyApplication.selectedPlaceOrder!!.types!!.isNotEmpty()) MyApplication.selectedPlaceOrder!!.types else ""))
        array.add(OrderData(AppHelper.getRemoteString("SizeCapacity",this),if(MyApplication.selectedPlaceOrder!!.sizeCapacity!=null && MyApplication.selectedPlaceOrder!!.sizeCapacity!!.isNotEmpty()) MyApplication.selectedPlaceOrder!!.sizeCapacity else ""))
        array.add(OrderData(AppHelper.getRemoteString("Quantity",this),"1"))
        rvDataBorder.layoutManager = LinearLayoutManager(this)
        rvDataBorder.adapter = AdapterOrderData(array,this,this)

        var array2:ArrayList<OrderData> = arrayListOf()
        array2.add(OrderData(AppHelper.getRemoteString("Subtotal",this),if(MyApplication.selectedPlaceOrder!!.sizeCapacity!=null && MyApplication.selectedPlaceOrder!!.sizeCapacity!!.isNotEmpty()) MyApplication.selectedPlaceOrder!!.price  else ""))
        array2.add(OrderData(AppHelper.getRemoteString("AdditionalFees",this),"0"))
        array2.add(OrderData(AppHelper.getRemoteString("TotalAmount",this),if(MyApplication.selectedPlaceOrder!!.price!=null && MyApplication.selectedPlaceOrder!!.price!!.isNotEmpty()) MyApplication.selectedPlaceOrder!!.price else ""))
        rvOtherData.layoutManager = LinearLayoutManager(this)
        rvOtherData.adapter = AdapterOtherOrderData(array2,this,this)
    }

    fun nextStep(){
        finishAffinity()
        MyApplication.selectedPos = 1
        MyApplication.fromOrderPlaced = true
        MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_ORDER
        MyApplication.selectedFragment = FragmentOrders()
        MyApplication.tintColor = R.color.redPrimary
        startActivity(Intent(this@ActivityPlaceOrder,ActivityHome::class.java))
    }

    fun paymentMethodStep(){

        selectedSlug = arrayPaymentMethods.find { it.id == selectedPaymentId }!!.slug
         if(selectedSlug.equals("cod")){
             nextStep()
         }else{
             paymentGateway()
         }
    }

    fun paymentGateway(){




        //
        var array : ArrayList<String> = arrayListOf()
       /* val analyticsEvent3 = Builder<Builder<Builder<*>>>(MyApplication.selectedPlaceOrder!!.userId!!.toString())
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
            .build()*/

        loading.show()
        val listProductName: MutableList<String> = java.util.ArrayList()
        listProductName.add(MyApplication.selectedPlaceOrder!!.title!!)

        val listProductPrice: MutableList<String> = java.util.ArrayList()
        listProductPrice.add(MyApplication.selectedPlaceOrder!!.price!!)

        val listProductQuantity: MutableList<String> = java.util.ArrayList()
        listProductQuantity.add(MyApplication.selectedPlaceOrder!!.sizeCapacity!!)



        var merchantId = MyApplication.payparams!!.params.find { it.key == "merchant_id" }!!.value
        var username = MyApplication.payparams!!.params.find { it.key == "username" }!!.value
        var password = MyApplication.payparams!!.params.find { it.key == "password" }!!.value
        var apiKey = MyApplication.payparams!!.params.find { it.key == "apiKey" }!!.value
        var succURL = MyApplication.payparams!!.params.find { it.key == "successURL" }!!.value
        var errorURL = MyApplication.payparams!!.params.find { it.key == "errorURL" }!!.value
        var refNum = MyApplication.payparams!!.params.find { it.key == "reference" }!!.value
        var notifyURl = MyApplication.payparams!!.params.find { it.key == "notifyURL" }!!.value

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
                       .setTestMode("1")
                       .setCustomerName(MyApplication.selectedPlaceOrder!!.firstName+" "+MyApplication.selectedPlaceOrder!!.lastName)
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

    fun setListeners(){

        btPLaceOrder.setOnClickListener {
            if(arrayPaymentMethods.count { it.selected }==0)
                AppHelper.createDialog(this,"Please choose payment method")
            else
               updatePayment()
        }
    }


    fun updatePayment(){
        if(orderId=="")
            orderId="0"
        loading.show()
        var request = RequestPaymentOrder(orderId.toInt(),selectedPaymentId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.updatePaymentOrder(request)?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(
                    call: Call<ResponseMessage>,
                    response: Response<ResponseMessage>
                ) {
                    try {
                        loading.hide()
                       if(response.body()!!.result==1){
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



        tvLocationPlaceOrder.setColorTypeface(this,R.color.redPrimary,"",false)
        tvPageTitle.show()
        tvPageTitle.textRemote("PlaceOrder",this)
        tvPageTitle.setColorTypeface(this,R.color.redPrimary,"",true)
        btBackTool.show()
        btBackTool.onOneClick {
            super.onBackPressed()
        }
        btPLaceOrder.typeface = AppHelper.getTypeFace(this)
        btClose.hide()
        AppHelper.setLogoTint(btBackTool,this,R.color.redPrimary)
        setListeners()
        try{
        setData()}catch (e:Exception){}
        AppHelper.setLogoTint(btDrawer, this, R.color.redPrimary)
        var spFound=true
        if(intent.hasExtra(AppConstants.SP_FOUND))
            spFound=intent.extras!!.getBoolean(AppConstants.SP_FOUND,true)
        if(intent.hasExtra(AppConstants.ORDER_ID))
            orderId=intent.extras!!.getString(AppConstants.ORDER_ID,"0")
        if(!spFound) {
            rlNotService.show()
            llMain.hide()
        }else{
            rlNotService.hide()
            llMain.show()
        }


        getPaymentMethods()

    }


    fun getPaymentMethods(){
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
                        if(response.body()!!.result==1){
                            arrayPaymentMethods.addAll(response.body()!!.paymentMethods!!)
                            setPaymentMethods()
                        }else{
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


    private fun setPaymentMethods(){
        adapterPaymentMethods = AdapterPaymentMethods(arrayPaymentMethods, this, this)
        rvPaymentMethod.layoutManager = GridLayoutManager(this,1)
        rvPaymentMethod.adapter = adapterPaymentMethods
        rvPaymentMethod.isNestedScrollingEnabled = false
    }

    override fun callBackUpayment(postUpayData: PostUpayData?) {
        loading.hide()
        if(postUpayData!!.result==AppConstants.PAYMENT_SUCCESS){
            nextStep()
        }else{
            runOnUiThread(Runnable {
                AppHelper.createDialog(this@ActivityPlaceOrder,postUpayData.result)
                loading.hide()
            })
        }
       Log.wtf("callBack","data")
    }

    override fun errorPayUpayment(data: String?) {

        var message : String ?=""
        if(MyApplication.languageCode == AppConstants.LANG_ARABIC)
            message = MyApplication.payparams!!.errorCode.find { it.key == data }!!.codeAr
        else
           message = MyApplication.payparams!!.errorCode.find { it.key == data }!!.codeEn

        runOnUiThread(Runnable {
            AppHelper.createDialog(this@ActivityPlaceOrder,message!!)
            loading.hide()
        })


    }
}