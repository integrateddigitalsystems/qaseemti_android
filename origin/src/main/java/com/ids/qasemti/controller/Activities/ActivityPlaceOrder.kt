package com.ids.qasemti.controller.Activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterOrderData
import com.ids.qasemti.controller.Adapters.AdapterOtherOrderData
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.controller.Fragments.*
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.activity_place_order.*
import kotlinx.android.synthetic.main.activity_place_order.rootLayout
import kotlinx.android.synthetic.main.footer.*
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.android.synthetic.main.home_container.*
import kotlinx.android.synthetic.main.layout_border_data.*
import kotlinx.android.synthetic.main.layout_border_red.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class ActivityPlaceOrder : AppCompactBase(), RVOnItemClickListener {

    var fragMang: FragmentManager? = null
    var selected: Int = 0
    var selectedPayment : String ?=""
    override fun onItemClicked(view: View, position: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_order)
        init()
        AppHelper.setAllTexts(rootLayout,this)
        getSupportActionBar()!!.hide();



    }
    fun setData(){
            tvLocationPlaceOrder.text =
                MyApplication.selectedPlaceOrder!!.addressDescription + " ," +  MyApplication.selectedPlaceOrder!!.addressStreet + " ," +  MyApplication.selectedPlaceOrder!!.addressBuilding + " ," +  MyApplication.selectedPlaceOrder!!.addressFloor
        tvOrderDate.text = MyApplication.selectedPlaceOrder!!.deliveryDate
        fragMang = supportFragmentManager
        var array:ArrayList<OrderData> = arrayListOf()
        array.add(OrderData(AppHelper.getRemoteString("category",this),MyApplication.selectedPlaceOrder!!.productCategory!!))
        array.add(OrderData(AppHelper.getRemoteString("service",this),MyApplication.selectedPlaceOrder!!.title))
        array.add(OrderData(AppHelper.getRemoteString("type",this),MyApplication.selectedPlaceOrder!!.types))
        array.add(OrderData(AppHelper.getRemoteString("SizeCapacity",this),MyApplication.selectedPlaceOrder!!.sizeCapacity))
        array.add(OrderData(AppHelper.getRemoteString("Quantity",this),"1"))
        rvDataBorder.layoutManager = LinearLayoutManager(this)
        rvDataBorder.adapter = AdapterOrderData(array,this,this)

        var array2:ArrayList<OrderData> = arrayListOf()
        array2.add(OrderData(AppHelper.getRemoteString("Subtotal",this),MyApplication.selectedPlaceOrder!!.price+" KWD"))
        array2.add(OrderData(AppHelper.getRemoteString("AdditionalFees",this),"0 KWD"))
        array2.add(OrderData(AppHelper.getRemoteString("TotalAmount",this),(MyApplication.selectedPlaceOrder!!.price)+" KWD"))
        rvOtherData.layoutManager = LinearLayoutManager(this)
        rvOtherData.adapter = AdapterOtherOrderData(array2,this,this)
    }

    fun setListeners(){
        rbCash.onOneClick {
            if (selected != 0) {
                ivCash.setImageDrawable(
                    getResources().getDrawable(
                        R.drawable.blue_circle_border,
                        theme
                    )
                )
                ivKnet.setImageDrawable(
                    getResources().getDrawable(
                        R.drawable.blue_circle,
                        theme
                    )
                )
                ivVisa.setImageDrawable(
                    getResources().getDrawable(
                        R.drawable.blue_circle,
                        theme
                    )
                )

                selected = 0
                selectedPayment = "Cash"
            }
        }
        rbKnet.onOneClick {
            if (selected != 1) {
                ivCash.setImageDrawable(
                    getResources().getDrawable(
                        R.drawable.blue_circle,
                        theme
                    )
                )
                ivKnet.setImageDrawable(
                    getResources().getDrawable(
                        R.drawable.blue_circle_border,
                        theme
                    )
                )
                ivVisa.setImageDrawable(
                    getResources().getDrawable(
                        R.drawable.blue_circle,
                        theme
                    )
                )
                selected = 1
                selectedPayment = "KNET"
            }
        }
        rbVisa.onOneClick {
            if (selected != 2) {
                ivCash.setImageDrawable(
                    getResources().getDrawable(
                        R.drawable.blue_circle,
                        theme
                    )
                )
                ivKnet.setImageDrawable(
                    getResources().getDrawable(
                        R.drawable.blue_circle,
                        theme
                    )
                )
                ivVisa.setImageDrawable(
                    getResources().getDrawable(
                        R.drawable.blue_circle_border,
                        theme
                    )
                )
                selected = 2
                selectedPayment ="Credit Card"
            }
        }

        btPLaceOrder.setOnClickListener {
            placeOrder()
        }
    }


    fun updatePayment(orderId : Int ){
        try {
            loading.show()
        } catch (ex: Exception) {

        }
        var request = RequestPaymentOrder(orderId,selectedPayment,selectedPayment)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.placeOrder(MyApplication.selectedPlaceOrder!!)?.enqueue(object : Callback<ResponseOrderId> {
                override fun onResponse(
                    call: Call<ResponseOrderId>,
                    response: Response<ResponseOrderId>
                ) {
                    try {
                        nextStep(response.body()!!)
                    } catch (E: java.lang.Exception) {

                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponseOrderId>, throwable: Throwable) {
                    loading.hide()
                }
            })
    }
    fun nextStep(res:ResponseOrderId){

        if(res.result!!.toInt()==1) {
            updatePayment(res.orderId!!.toInt())
            var ok = AppHelper.getRemoteString("ok", this)

            val builder = AlertDialog.Builder(this)
            builder
                .setMessage(res.message)
                .setCancelable(true)
                .setNegativeButton(ok) { dialog, _ ->
                    finishAffinity()
                    MyApplication.selectedPos = 1
                    MyApplication.arrayCart.remove(MyApplication.selectedPlaceOrder)
                    AppHelper.toGSOn(MyApplication.arrayCart)
                    MyApplication.selectedFragment = FragmentOrders()
                    MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_ORDER
                    startActivity(Intent(this, ActivityHome::class.java))
                }
            val alert = builder.create()
            alert.show()
        }
        loading.hide()
    }
    fun placeOrder(){
        try {
            loading.show()
        } catch (ex: Exception) {

        }
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.placeOrder(MyApplication.selectedPlaceOrder!!)?.enqueue(object : Callback<ResponseOrderId> {
                override fun onResponse(
                    call: Call<ResponseOrderId>,
                    response: Response<ResponseOrderId>
                ) {
                    try {
                        nextStep(response.body()!!)
                    } catch (E: java.lang.Exception) {

                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponseOrderId>, throwable: Throwable) {
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

        if(MyApplication.position==1) {
            rlNotService.show()
            llMain.hide()
        }else{
            rlNotService.hide()
            llMain.show()
        }


        if (MyApplication.isClient) {
            llFooterProducts.hide()
            llFooterCart.show()
        } else {
            llFooterProducts.show()
            llFooterCart.hide()

        }
        setListeners()
        setData()
        AppHelper.setLogoTint(btDrawer, this, R.color.redPrimary)
    }
}