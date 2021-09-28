package com.ids.qasemti.controller.Activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TimePicker
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterOrderData
import com.ids.qasemti.controller.Adapters.AdapterOtherOrderData
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import com.ids.qasemti.utils.AppHelper.Companion.toEditable
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.layout_border_data.*
import kotlinx.android.synthetic.main.layout_home_orders.*
import kotlinx.android.synthetic.main.layout_order_contact_tab.*
import kotlinx.android.synthetic.main.layout_order_switch.*
import kotlinx.android.synthetic.main.layout_request_new_time.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ActivityOrderDetails: ActivityBase() , RVOnItemClickListener {

    var orderId = 1
    var onTrack : Int ?=0
    var delivered : Int ?=0
    var paid : Int ?=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        getWindow().setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        btDrawer.hide()
        btBackTool.show()
        btBackTool.onOneClick {
            super.onBackPressed()
        }
       orderId = intent.getIntExtra("orderId",1)

        AppHelper.setAllTexts(rootLayoutOrderDetails,this)
        tvPageTitle.show()
        tvPageTitle.setColorTypeface(this,R.color.white,"",true)
        if(MyApplication.typeSelected==0) {
            tvPageTitle.textRemote("ActiveOrderDetails",this)
            if(!MyApplication.isClient){
                llEditOrderTime.show()
            }else{
                llEditOrderTime.hide()
                llOrderSwitches.hide()
            }
            llActualDelivery.hide()
        }else{
            tvPageTitle.textRemote("CompletedOrderDetails",this)
            llRatingOrder.visibility=View.VISIBLE
            llEditOrderTime.hide()
            llActualDelivery.show()
            llOrderSwitches.hide()
            btCancelOrder.hide()
            if(MyApplication.isClient)
                btRenewOrder.show()


        }

        tvLocationOrderDeatils.setColorTypeface(this,R.color.redPrimary,"",false)

        setListeners()
        var array:ArrayList<OrderData> = arrayListOf()
        array.add(OrderData("Category","Purchase"))
        array.add(OrderData("Service","Water Tank"))
        array.add(OrderData("Type","Fresh"))
        array.add(OrderData("Size/Capacity","200 Gallons"))
        array.add(OrderData("Quantity","1 Trip"))

        rvDataBorder.layoutManager = LinearLayoutManager(this)
        rvDataBorder.adapter = AdapterOrderData(array,this,this)

    }

    fun setListeners(){
        llCall.onOneClick {
            val intent = Intent(Intent.ACTION_DIAL)
            startActivity(intent)
        }
        llMessage.onOneClick {
            val uri = Uri.parse("smsto:12346556")
            val it = Intent(Intent.ACTION_SENDTO, uri)
            it.putExtra("sms_body", "Here you can set the SMS text to be sent")
            startActivity(it)
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
                    val myFormat = "dd/MM/yyyy" //Change as you need
                    var sdf =
                        SimpleDateFormat(myFormat, Locale.ENGLISH)
                    var date = sdf.format(myCalendar.time)
                    etOrderDetailDate.text = date.toEditable()
                }, mYear, mMonth, mDay
            )
            mDatePicker.show()
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

                    var time = String.format("%02d", selectedHour)+" : "+ String.format("%02d", selectedMinute)
                    etOrderDetailTime.text = time.toEditable()
                }, hour, minute, false
            ) //Yes 24 hour time
            timePickerDialog.show()
        }

        btCancelOrder.onOneClick {
            try {
                loading.show()
            }catch (ex: Exception){

            }
            var cal = Calendar.getInstance()
            var dateNow = AppHelper.formatDate(cal.time!!,"dd-MM-yyyy")
            var newReq = RequestCancelOrder(orderId,MyApplication.userId,dateNow,"not accepted yet")
            RetrofitClient.client?.create(RetrofitInterface::class.java)
                ?.cancelOrder(
                    newReq
                )?.enqueue(object : Callback<ResponseCancel> {
                    override fun onResponse(call: Call<ResponseCancel>, response: Response<ResponseCancel>) {
                        try{
                            resultCancel(response.body()!!.result!!)
                        }catch (E: java.lang.Exception){
                           resultCancel(0)
                        }
                    }
                    override fun onFailure(call: Call<ResponseCancel>, throwable: Throwable) {
                        resultCancel(0)
                    }
                })
        }

        swOnTrack.setOnCheckedChangeListener { compoundButton, b ->

            if(swOnTrack.isChecked){
                onTrack = 1
            }else{
                onTrack = 0
            }
            setStatus()
        }
        swPaid.setOnCheckedChangeListener { compoundButton, b ->
            if(swOnTrack.isChecked){
                paid = 1
            }else{
                paid = 0
            }
            setStatus()
        }
        swDelivered.setOnCheckedChangeListener { compoundButton, b ->
            if(swOnTrack.isChecked){
                delivered = 1
            }else{
                delivered = 0
            }
            setStatus()
        }
    }
    fun setStatus(){
        var newReq = RequestUpdateOrder(orderId ,onTrack,delivered,paid)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.updateOrderCustomStatus(newReq)?.enqueue(object : Callback<ResponseUpdate> {
                override fun onResponse(call: Call<ResponseUpdate>, response: Response<ResponseUpdate>) {
                    try{
                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ResponseUpdate>, throwable: Throwable) {
                }
            })
    }

    fun resultCancel(req:Int){
        if(req==1){
            AppHelper.createDialog(this,AppHelper.getRemoteString("success",this))
        }else{
            AppHelper.createDialog(this,AppHelper.getRemoteString("failure",this))
        }
        try {
            loading.hide()
        }catch (ex: Exception){

        }
    }

    override fun onItemClicked(view: View, position: Int) {


    }
}