package com.ids.qasemti.controller.Activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TimePicker
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import com.ids.qasemti.utils.AppHelper.Companion.toEditable
import kotlinx.android.synthetic.main.activity_new_address.*
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ActivityCheckout : ActivityBase(), RVOnItemClickListener, ApiListener {

    var open = true
    var REQUEST_LOCATION = 5
    var stamp: Long? = 0
    var minRenewTime: Date? = null
    var minRenewTo: Date? = null
    var pickedDate: Date? = null
    var update = false
    var fromHour: Int? = 0
    var fromMin: Int? = 0
    var locationSelected = false
    var latLng: LatLng? = null
    var selectedDate: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_checkout)
        AppHelper.setAllTexts(rootLayoutCheckout, this)
        stamp = Calendar.getInstance().timeInMillis
        MyApplication.selectedPlaceOrder = null
        init()

    }


    override fun onItemClicked(view: View, position: Int) {

    }


    fun setUpCurr() {
        var cal = Calendar.getInstance()
        pickedDate = cal.time
        val myFormat = "yyyy-MM-dd" //Change as you need
        var sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        var date = sdf.format(cal.time)
        etFromDate.text = date.toEditable()
        var time = String.format("%02d", cal.get(Calendar.HOUR_OF_DAY)) + ":" + String.format(
            "%02d",
            cal.get(Calendar.MINUTE)
        )+":00"
        fromHour = cal.get(Calendar.HOUR_OF_DAY)
        fromMin = cal.get(Calendar.MINUTE)
        etFromTime.text = time.toEditable()
        selectedDate = date + " " + time
    }

    fun setTintLogo(color: Int) {
        AppHelper.setLogoTint(btDrawer, this, color)
        btDrawer.hide()

        // AppHelper.setTextColor(this, tvPageTitle, color)
        AppHelper.setLogoTint(btBackTool, this, color)
        AppHelper.setLogoTint(btLogout, this, color)
        btBackTool.onOneClick {
            super.onBackPressed()
        }
    }

    fun compareDates(): Int {

        try {
            var sdf =
                SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            var dateFrom = etFromDate.text.toString()
            var dateTo = etToDate.text.toString()

            if (sdf.parse(dateFrom).time > sdf.parse(dateTo).time)
                return 1
            else if (sdf.parse(dateFrom).time == sdf.parse(dateTo).time)
                return -1
            return 0
        } catch (ex: Exception) {
            return 1
        }

    }

    fun checkGivenDate(): Boolean {
        var sdf =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ssss", Locale.ENGLISH)
        var date = sdf.parse(selectedDate)

        if (Calendar.getInstance().time.time > date.time) {
            return false
        }

        return true
    }

    fun compareTimes(selectH: Int, selectM: Int): Boolean {
        if (selectH < fromHour!!) {
            return false
        } else if (selectM < fromMin!!) {
            return false
        }
        return true

    }

    fun setListeners() {
        setUpCurr()
        btPlaceOrder.onOneClick {
            if (locationSelected) {
                if (MyApplication.selectedService!!.typeId!!.equals(MyApplication.categories.find {
                        it.valEn!!.lowercase().equals("rental")
                    }!!.id!!.toInt())) {

                    if (!etToDate.text.isNullOrEmpty() && !etToTime.text.isNullOrEmpty()) {
                        if (rbNow.isChecked || checkGivenDate()) {
                            update = MyApplication.selectedPlaceOrder != null
                            if (MyApplication.selectedUser != null) {
                                try {
                                  // setPlacedOrder()
                                    if(MyApplication.selectedAddress ==null )
                                        CallAPIs.getAddressName(latLng!!,this,this)
                                    else
                                        setPlacedOrder()

                                } catch (e: Exception) {
                                    toast(getString(R.string.failure))
                                }
                            }
                        } else {
                            AppHelper.createDialog(this, "Please select a later date or time")
                        }
                    } else {
                        AppHelper.createDialog(
                            this,
                            AppHelper.getRemoteString("select_to_rent_date", this)
                        )
                    }
                } else {

                    if (rbNow.isChecked || checkGivenDate()) {
                        update = MyApplication.selectedPlaceOrder != null
                        if (MyApplication.selectedUser != null) {
                            try {
                                if(MyApplication.selectedAddress ==null )
                                    CallAPIs.getAddressName(latLng!!,this,this)
                                else
                                    setPlacedOrder()
                            } catch (e: Exception) {
                                toast(getString(R.string.failure))
                            }
                        } else {
                            CallAPIs.getUserInfo(this, this)
                        }
                    } else {
                        AppHelper.createDialog(this, "Please select a later date or time")
                    }
                }
            } else {
                AppHelper.createDialog(
                    this,
                    AppHelper.getRemoteString("please_select_location", this)
                )
            }
        }

        rbNow.onOneClick {
            setUpCurr()
            rlFromDate.onOneClick {

            }
            rlFromTime.onOneClick {

            }

            AppHelper.setTextColor(this, etFromDate, R.color.gray_font_light)
            AppHelper.setTextColor(this, etFromTime, R.color.gray_font_light)
        }
        rbSpecify.onOneClick {

            AppHelper.setTextColor(this, etFromDate, R.color.gray_font)
            AppHelper.setTextColor(this, etFromTime, R.color.gray_font)
            rlFromDate.onOneClick {
                val myFormat = "yyyy-MM-dd" //Change as you need
                var sdf =
                    SimpleDateFormat(myFormat, Locale.ENGLISH)
                var mcurrentDate = sdf.parse(etFromDate.text.toString())
                var mYear = 0
                var mMonth = 0
                var mDay = 0
                var calTime = Calendar.getInstance()
                var cal = Calendar.getInstance()
                cal.time = mcurrentDate
                mYear = cal[Calendar.YEAR]
                mMonth = cal[Calendar.MONTH]
                mDay = cal[Calendar.DAY_OF_MONTH]

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
                        pickedDate = myCalendar.time
                        var date = sdf.format(myCalendar.time)
                        etFromDate.text = date.toEditable()
                        if (compareDates() == 1)
                            etToDate.text = date.toEditable()
                        var x = selectedDate
                        var time = selectedDate!!.split(" ").get(1)
                        selectedDate = etFromDate.text.toString() + " " + time
                    }, mYear, mMonth, mDay
                )
                mDatePicker.datePicker.minDate = calTime.time.time
                mDatePicker.show()
            }
            rlFromTime.onOneClick {
                // TODO Auto-generated method stub
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val myFormat = "yyyy-MM-dd" //Change as you need
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                val now = mcurrentTime.time
                val nowDate = sdf.format(now)
                val timePickerDialog = TimePickerDialog(
                    this, R.style.DatePickerDialog,
                    { timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int ->

                        fromHour = selectedHour
                        fromMin = selectedMinute
                        var time = String.format("%02d", selectedHour) + ":" + String.format(
                            "%02d",
                            selectedMinute
                        )+":00"
                        etFromTime.text = time.toEditable()
                        var date =
                            selectedDate!!.split(" ").get(0)
                        selectedDate = date + " " + time
                    }, hour, minute, true
                ) //Yes 24 hour time
                timePickerDialog.show()
            }
        }


        rlToTime.onOneClick {
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
                    )+":"+"00"
                    if (compareDates() == -1) {
                        if (!compareTimes(selectedHour, selectedMinute)) {
                            AppHelper.createDialog(this, "You cannot pick a time before selected")
                        } else {
                            etToTime.text = time.toEditable()
                        }
                    } else {
                        etToTime.text = time.toEditable()
                    }

                }, hour, minute, true
            ) //Yes 24 hour time
            timePickerDialog.show()
        }
        rlToDate.onOneClick {
            var mYear = 0
            var mMonth = 0
            var mDay = 0
            val myFormat = "yyyy-MM-dd" //Change as you need
            var sdf =
                SimpleDateFormat(myFormat, Locale.ENGLISH)
            var mcurrentDate: Date? = null
            var cal = Calendar.getInstance()
            if (!etToDate.text.isNullOrEmpty()) {
                mcurrentDate = sdf.parse(etToDate.text.toString())
                cal.time = mcurrentDate
            }
            mYear = cal[Calendar.YEAR]
            mMonth = cal[Calendar.MONTH]
            mDay = cal[Calendar.DAY_OF_MONTH]

            // mcurrentDate.set(mYear, mMonth, mDay)
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
                    etToDate.text = date.toEditable()
                }, mYear, mMonth, mDay
            )
            mDatePicker.datePicker.minDate = pickedDate!!.time
            mDatePicker.show()
        }

        llAddresses.onOneClick {
            startActivityForResult(
                Intent(this, ActivitySelectAddress::class.java),
                REQUEST_LOCATION
            )
        }

        btPlus.onOneClick {
            var quant = tvQuant.text.toString()
            if (quant.isNumeric()) {
                var value = quant.toInt()
                value++
                tvQuant.text = value.toString()
            }
        }
        btMinus.onOneClick {
            var quant = tvQuant.text.toString()
            if (quant.isNumeric()) {
                var value = quant.toInt()
                value--
                tvQuant.text = value.toString()
            }
        }

        llSetDateTime.onOneClick {
            var up = -180
            var down = -180
            if (open) {
                // ivOpenDateTime.animate().rotation(up.toFloat()).setDuration(400)
                ivOpenDateTime.hide()
                ivCloseDateTime.show()
                llTimeDate.hide()
            } else {
                ivOpenDateTime.show()
                ivCloseDateTime.hide()
//                ivOpenDateTime.animate().rotation(down.toFloat()).setDuration(400)
                llTimeDate.show()
            }

            open = !open
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            val extras = data!!.extras
            if (extras != null) {
                var address = extras.getString("address")
                //  tvSelectedAddressCheck.setColorTypeface(this,R.color.gray_font_title,address!!,false)
                tvSelectedAddressCheck.text = address
                locationSelected = true
                try {
                    var lat = extras.getDouble("lat")
                    var long = extras.getDouble("long")
                    latLng = LatLng(lat, long)
                } catch (ex: Exception) {
                }
            }

        }

    }

    fun init() {
        setTintLogo(R.color.primary)
        //  tvSelectedAddressCheck.setColorTypeface(this,R.color.gray_font_title,"",false)
        tvPageTitle.textRemote("Checkout", this)
        tvPageTitle.setColorTypeface(this, R.color.primary, "", true)
        etFromDate.isFocusable = false;
        etFromTime.isFocusable = false;
        rbNow.isChecked = true
        rbSpecify.isChecked = false
        btBackTool.show()

        btPlaceOrder.typeface = AppHelper.getTypeFace(this)

        if (!MyApplication.renewing) {
            if (MyApplication.selectedService!!.typeId!!.equals(MyApplication.categories.find {
                    it.valEn!!.lowercase().equals("rental")
                }!!.id!!.toInt())) {
                tvFromTitle.show()
                tvToTitle.show()
                llToLayout.show()
            } else {
                tvFromTitle.hide()
                tvToTitle.hide()
                llToLayout.hide()
            }

            setUpCurr()
            setOrderSummary()
            setListeners()
        } else {
            tvFromTitle.show()
            tvToTitle.show()
            llToLayout.show()
            setUpRenting()
            llAddresses.isEnabled = false


            var current = Calendar.getInstance()
            var simp = SimpleDateFormat("yyyy-MM-dd hh:mm:ssss", Locale.ENGLISH)
            var simp2 = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            var simp3= SimpleDateFormat ("dd MMM yyyy",Locale.ENGLISH)
            var from : Date ?=null
            var to : Date ?=null
            try {
                from = simp.parse(MyApplication.selectedOrder!!.product!!.booking_start_date)
            }catch (ex:Exception){
                try{
                    from = simp2.parse(MyApplication.selectedOrder!!.product!!.booking_start_date)
                }catch (ex:Exception){
                    try{
                        from = simp3.parse(MyApplication.selectedOrder!!.product!!.booking_start_date)
                    }catch (ex:Exception){
                        from = Calendar.getInstance().time
                    }
                }

            }
            try {
                to = simp.parse(MyApplication.selectedOrder!!.product!!.booking_end_date)
            }catch (ex:Exception){
                try{
                    to = simp2.parse(MyApplication.selectedOrder!!.product!!.booking_end_date)
                }catch (ex:Exception){
                    try{
                        to = simp3.parse(MyApplication.selectedOrder!!.product!!.booking_end_date)
                    }catch (ex:Exception){
                        to = Calendar.getInstance().time
                    }
                }

            }
            /* var from = simp.parse("2021-11-18")
             var to = simp.parse("2021-11-21")*/


            if (current.time.time < from!!.time || current.time.time > from!!.time && current.time.time < to!!.time) {
                minRenewTime = from
                minRenewTo = to
            } else {
                minRenewTime = current.time
                minRenewTo = current.time
            }

            etFromDate.text = simp.format(minRenewTime).toEditable()
            etToDate.text = simp.format(minRenewTo).toEditable()

            rlFromDate.onOneClick {
                var cal = Calendar.getInstance()
                cal.time = minRenewTime
                var mYear = cal[Calendar.YEAR]
                var mMonth = cal[Calendar.MONTH]
                var mDay = cal[Calendar.DAY_OF_MONTH]

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
                        pickedDate = myCalendar.time
                        var date = sdf.format(myCalendar.time)
                        etFromDate.text = date.toEditable()
                        if (compareDates() == 1)
                            etToDate.text = date.toEditable()
                        var x = selectedDate
                        var time = selectedDate!!.split(" ").get(3)
                        selectedDate = etFromDate.text.toString() + " " + time
                    }, mYear, mMonth, mDay
                )
                mDatePicker.datePicker.minDate = cal.time.time
                mDatePicker.show()
            }
            rlFromTime.onOneClick {
                // TODO Auto-generated method stub
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val myFormat = "yyyy-MM-dd" //Change as you need
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                val now = mcurrentTime.time
                val nowDate = sdf.format(now)
                val timePickerDialog = TimePickerDialog(
                    this, R.style.DatePickerDialog,
                    { timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int ->

                        fromHour = selectedHour
                        fromMin = selectedMinute
                        var time = String.format("%02d", selectedHour) + ":" + String.format(
                            "%02d",
                            selectedMinute
                        )
                        etFromTime.text = time.toEditable()
                        if(!selectedDate.isNullOrEmpty()){
                            var date =
                                selectedDate!!.split(" ").get(0)
                            selectedDate = date + " " + time
                        }else{
                            selectedDate =  time
                        }

                    }, hour, minute, true
                ) //Yes 24 hour time
                timePickerDialog.show()
            }

            rlToTime.onOneClick {
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

                        var time = String.format("%02d", selectedHour) + " : " + String.format(
                            "%02d",
                            selectedMinute
                        )
                        if (compareDates() == -1) {
                            if (!compareTimes(selectedHour, selectedMinute)) {
                                AppHelper.createDialog(
                                    this,
                                    "You cannot pick a time before selected"
                                )
                            } else {
                                etToTime.text = time.toEditable()
                            }
                        } else {
                            etToTime.text = time.toEditable()
                        }

                    }, hour, minute, true
                ) //Yes 24 hour time
                timePickerDialog.show()
            }
            rlToDate.onOneClick {
                var mYear = 0
                var mMonth = 0
                var mDay = 0
                val myFormat = "yyyy-MM-dd" //Change as you need
                var sdf =
                    SimpleDateFormat(myFormat, Locale.ENGLISH)
                var mcurrentDate: Date? = null
                var cal = Calendar.getInstance()
                if (!etToDate.text.isNullOrEmpty()) {
                    mcurrentDate = sdf.parse(etToDate.text.toString())
                    cal.time = mcurrentDate
                }
                mYear = cal[Calendar.YEAR]
                mMonth = cal[Calendar.MONTH]
                mDay = cal[Calendar.DAY_OF_MONTH]

                // mcurrentDate.set(mYear, mMonth, mDay)
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
                        etToDate.text = date.toEditable()
                    }, mYear, mMonth, mDay
                )
                mDatePicker.datePicker.minDate = minRenewTo!!.time
                mDatePicker.show()
            }


            btPlaceOrder.text = AppHelper.getRemoteString("renew_order", this)
            btPlaceOrder.onOneClick {
                try {
                    renewOrder()
                } catch (ex: Exception) {
                    Log.wtf("renewError", ex.toString())
                    loading.hide()
                }
            }
        }


    }


    fun nextStepRenew() {

        var type =
            MyApplication.categories.find { it.valEn == MyApplication.selectedOrder!!.type }!!.id!!.toInt()
        MyApplication.selectedPlaceOrder = RequestPlaceOrder(
            MyApplication.userId,
            MyApplication.selectedOrder!!.typeId,
            MyApplication.selectedOrder!!.product!!.id!!.toInt(),
            MyApplication.selectedOrder!!.typesId,
            MyApplication.selectedOrder!!.sizeCapacityId,
            selectedDate,
            if (MyApplication.selectedAddress!!.addressName != null && MyApplication.selectedAddress!!.addressName!!.isNotEmpty()) MyApplication.selectedAddress!!.addressName else "",
            MyApplication.selectedOrder!!.shipping_latitude,
            MyApplication.selectedOrder!!.shipping_longitude,
            if (MyApplication.selectedOrder!!.addressStreet != null && MyApplication.selectedOrder!!.addressStreet!!.isNotEmpty()) MyApplication.selectedOrder!!.addressStreet else "",
            if (MyApplication.selectedOrder!!.addressBuilding != null && MyApplication.selectedOrder!!.addressBuilding!!.isNotEmpty()) MyApplication.selectedOrder!!.addressBuilding else "",
            if (MyApplication.selectedOrder!!.addressFloor != null && MyApplication.selectedOrder!!.addressFloor!!.isNotEmpty()) MyApplication.selectedOrder!!.addressFloor else "",
            if (MyApplication.selectedOrder!!.addressDescription != null && MyApplication.selectedOrder!!.addressDescription!!.isNotEmpty()) MyApplication.selectedOrder!!.addressDescription else "",
            if (MyApplication.selectedAddress!!.addressId != null && MyApplication.selectedAddress!!.addressId!!.isNotEmpty()) MyApplication.selectedAddress!!.addressId!!.toInt() else 0,
            etFromDate.text.toString(),
            etToDate.text.toString()
        )

        startActivity(
            Intent(
                this,
                ActivityPlaceOrder::class.java
            )
        )
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

        var typeId = if(MyApplication.selectedOrder!!.product!!.typesId.isNullOrEmpty()) 0 else MyApplication.selectedOrder!!.product!!.typesId!!.toInt()
        var sizeId =if(MyApplication.selectedOrder!!.product!!.sizeCapacityId.isNullOrEmpty()) 0 else MyApplication.selectedOrder!!.product!!.sizeCapacityId!!.toInt()

        var smp = SimpleDateFormat("yyyy-MM-dd hh:mm:ssss")
        var toSmp = SimpleDateFormat("yyyy-MM-dd")

        var cal = Calendar.getInstance()
        var date = AppHelper.formatDate(cal.time, "dd/mm/yy hh:mm:ssss")
        var req = RequestRenewOrder(
            MyApplication.selectedOrder!!.customer!!.user_id!!.toInt(),
            MyApplication.selectedOrder!!.orderId!!.toInt(),
            vendorId,
            MyApplication.selectedOrder!!.type,
            MyApplication.selectedOrder!!.product!!.productId!!.toInt(),
            typeId,
            sizeId,
            toSmp.format(smp.parse(MyApplication.selectedOrder!!.deliveryDate)),
            toSmp.format(smp.parse(etFromDate.text.toString())),
            toSmp.format(smp.parse(etToDate.text.toString())),
            MyApplication.selectedOrder!!.shipping_address_name,
            35.0078688,
            37.9990099,
            MyApplication.selectedOrder!!.shipping_address_street,
            MyApplication.selectedOrder!!.shipping_address_building,
            MyApplication.selectedOrder!!.shipping_address_floor,
            MyApplication.selectedOrder!!.shipping_address_description,
            MyApplication.selectedOrder!!.shipping_province,
            MyApplication.selectedOrder!!.shipping_area,
            MyApplication.selectedOrder!!.shipping_block,
            MyApplication.selectedOrder!!.shippingAvenu ,
            MyApplication.selectedOrder!!.shippingApartment
        )
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.renewOrder(req)?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(
                    call: Call<ResponseMessage>,
                    response: Response<ResponseMessage>
                ) {
                    try {
                        loading.hide()
                        if(response.body()!!.result==1){
                            AppHelper.createDialog(this@ActivityCheckout ,AppHelper.getRemoteString("success",this@ActivityCheckout))
                        }else{
                            AppHelper.createDialog(this@ActivityCheckout ,response.body()!!.message!!)
                        }
                        //nextStepRenew()
                        //nextStep(response.body()!!.result!!)
                    } catch (E: java.lang.Exception) {

                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    loading.hide()
                }
            })
    }

    fun setUpRenting() {
        try {
            tvServiceName.text = MyApplication.selectedOrder!!.product!!.name!!
        } catch (e: Exception) {
        }
        try {
            tvServiceType.text = MyApplication.selectedOrder!!.product!!.type!!
        } catch (e: Exception) {
        }
        try {
            tvSizeCapacity.text = MyApplication.selectedOrder!!.product!!.sizeCapacity
        } catch (e: Exception) {
        }
        try {
            tvPrice.text = MyApplication.selectedOrder!!.grand_total
        } catch (e: Exception) {
        }
        try {
            tvVariationType.text = MyApplication.selectedOrder!!.product!!.types
        } catch (e: Exception) {
        }
        try {
            MyApplication.selectedOrder!!.product!!.sizeCapacity
        } catch (e: Exception) {
        }
    }

    private fun setOrderSummary() {
        try {
            tvServiceName.text = MyApplication.selectedService!!.name!!
        } catch (e: Exception) {
        }
        try {
            tvServiceType.text = MyApplication.selectedService!!.type!!
        } catch (e: Exception) {
        }
        try {
            tvSizeCapacity.text =
                MyApplication.selectedService!!.variations.find { it.sizeCapacityId!!.toInt() == MyApplication.selectedSize!! }!!.sizeCapacity
        } catch (e: Exception) {
        }
        try {
            tvPrice.text = MyApplication.selectedPrice + ""
        } catch (e: Exception) {
        }
        try {
            tvVariationType.text =
                MyApplication.selectedService!!.variations.find { it.typesId!!.toInt() == MyApplication.selectedVariationType!! }!!.types
        } catch (e: Exception) {
        }
        try {
            tvSize.text =
                MyApplication.selectedService!!.variations.find { it.sizeCapacityId!!.toInt() == MyApplication.selectedSize!! }!!.sizeCapacity
        } catch (e: Exception) {
        }
    }

    private fun setPlacedOrder() {


        var type = MyApplication.selectedService!!.typeId
        var lat = ""
        var long = ""
        try {
            lat = latLng!!.latitude.toString()
            long = latLng!!.longitude.toString()
        } catch (ex: Exception) {

        }


        /*if(MyApplication.selectedService!!.typeId!!.equals(MyApplication.categories.find { it.valEn!!.lowercase().equals("purchase") }!!.id!!.toInt() ))
            type = AppConstants.TYPE_PURCHASE
        else
            type = AppConstants.TYPE_RENTAL*/

        var x = etFromDate.text.toString()
        var y = etToDate.text.toString()
        if (type == 345) {
            MyApplication.selectedPlaceOrder = RequestPlaceOrder(
                MyApplication.userId,
                type,
                MyApplication.selectedService!!.id!!.toInt(),
                MyApplication.selectedVariationType,
                MyApplication.selectedSize,
                selectedDate,
                if (MyApplication.selectedAddress!!.addressName != null && MyApplication.selectedAddress!!.addressName!!.isNotEmpty()) MyApplication.selectedAddress!!.addressName else "",
                lat,
                long,
                if (MyApplication.selectedAddress!!.street != null && MyApplication.selectedAddress!!.street!!.isNotEmpty()) MyApplication.selectedAddress!!.street else "",
                if (MyApplication.selectedAddress!!.bldg != null && MyApplication.selectedAddress!!.bldg!!.isNotEmpty()) MyApplication.selectedAddress!!.bldg else "",
                if (MyApplication.selectedAddress!!.floor != null && MyApplication.selectedAddress!!.floor!!.isNotEmpty()) MyApplication.selectedAddress!!.floor else "",
                if (MyApplication.selectedAddress!!.desc != null && MyApplication.selectedAddress!!.desc!!.isNotEmpty()) MyApplication.selectedAddress!!.desc else "",
                if (MyApplication.selectedService!!.name!!.isNotEmpty()) MyApplication.selectedService!!.name else "",
                if (MyApplication.selectedAddress!!.addressId != null && MyApplication.selectedAddress!!.addressId!!.isNotEmpty()) MyApplication.selectedAddress!!.addressId!!.toInt() else 0,
                etFromDate.text.toString(),
                etToDate.text.toString(),
                if (MyApplication.selectedAddress!!.avenue != null && MyApplication.selectedAddress!!.avenue!!.isNotEmpty()) MyApplication.selectedAddress!!.avenue else "",
                if (MyApplication.selectedAddress!!.block != null && MyApplication.selectedAddress!!.block!!.isNotEmpty()) MyApplication.selectedAddress!!.block else "",
                if (MyApplication.selectedAddress!!.province != null && MyApplication.selectedAddress!!.province!!.isNotEmpty()) MyApplication.selectedAddress!!.province else ""
            )
        } else {

            MyApplication.selectedPlaceOrder = RequestPlaceOrder(
                MyApplication.userId,
                type,
                MyApplication.selectedService!!.id!!.toInt(),
                MyApplication.selectedVariationType,
                MyApplication.selectedSize,
                selectedDate,
                if (MyApplication.selectedAddress!!.addressName != null && MyApplication.selectedAddress!!.addressName!!.isNotEmpty()) MyApplication.selectedAddress!!.addressName else "",
                lat,
                long,
                if (MyApplication.selectedAddress!!.street != null && MyApplication.selectedAddress!!.street!!.isNotEmpty()) MyApplication.selectedAddress!!.street else "",
                if (MyApplication.selectedAddress!!.bldg != null && MyApplication.selectedAddress!!.bldg!!.isNotEmpty()) MyApplication.selectedAddress!!.bldg else "",
                if (MyApplication.selectedAddress!!.floor != null && MyApplication.selectedAddress!!.floor!!.isNotEmpty()) MyApplication.selectedAddress!!.floor else "",
                if (MyApplication.selectedAddress!!.desc != null && MyApplication.selectedAddress!!.desc!!.isNotEmpty()) MyApplication.selectedAddress!!.desc else "",
                MyApplication.selectedAddress!!.addressId!!.toInt(),
                etFromDate.text.toString(),
                etToDate.text.toString()
            )
        }
        if (update) {
            var i = MyApplication.arrayCart.size - 1
            MyApplication.arrayCart[i] = MyApplication.selectedPlaceOrder!!
        } else {
            MyApplication.arrayCart.add(MyApplication.selectedPlaceOrder!!)
        }
        MyApplication.seletedPosCart = MyApplication.arrayCart.size - 1

        AppHelper.toGSOn(MyApplication.arrayCart)

        placeOrder()

    }


    fun placeOrder() {
        loading.show()
        //testing success scenario
/*        MyApplication.selectedPlaceOrder!!.addressLatitude=""
        MyApplication.selectedPlaceOrder!!.addressLongitude=""*/
        var jsonString = Gson().toJson(MyApplication.selectedPlaceOrder)
        logw("JSONcheckout", jsonString)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.placeOrder(MyApplication.selectedPlaceOrder!!)?.enqueue(object :
                Callback<ResponseOrderId> {
                override fun onResponse(
                    call: Call<ResponseOrderId>,
                    response: Response<ResponseOrderId>
                ) {
                    try {
                        loading.hide()
                        if (!response.body()!!.result.equals("0")) {
                            if (response.body()!!.action == AppConstants.PLACE_ORDER_AVAILABLE_IN) {
                                startActivity(
                                    Intent(
                                        this@ActivityCheckout,
                                        ActivityPlaceOrder::class.java
                                    ).putExtra(AppConstants.ORDER_ID, response.body()!!.orderId)
                                )
                            } else if (response.body()!!.action == AppConstants.PLACE_ORDER_AVAILABLE_OUT)
                                showProviderMessage(response.body()!!)
                            else
                                startActivity(
                                    Intent(this@ActivityCheckout, ActivityPlaceOrder::class.java)
                                        .putExtra(AppConstants.ORDER_ID, response.body()!!.orderId)
                                        .putExtra(AppConstants.SP_FOUND, false)
                                )
                        } else {
                            AppHelper.createDialog(
                                this@ActivityCheckout,
                                response.body()!!.message!!
                            )
                        }

                    } catch (E: java.lang.Exception) {

                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponseOrderId>, throwable: Throwable) {
                    loading.hide()
                }
            })
    }

    fun showProviderMessage(res: ResponseOrderId) {
        var ok = AppHelper.getRemoteString("ok", this)
        var cancel = AppHelper.getRemoteString("cancel", this)
        val builder = AlertDialog.Builder(this)
        builder
            .setMessage(AppHelper.getRemoteString("find_service_provider_msg", this))
            .setCancelable(true)
            .setNegativeButton(cancel) { dialog, _ ->
                startActivity(
                    Intent(this@ActivityCheckout, ActivityPlaceOrder::class.java)
                        .putExtra(AppConstants.SP_FOUND, false)
                        .putExtra(AppConstants.ORDER_ID, res.orderId)
                )
            }
            .setPositiveButton(ok) { dialog, _ ->
                broadcastOutOfRange(res.orderId!!)
            }
        val alert = builder.create()
        alert.show()

    }


    fun broadcastOutOfRange(orderId: String) {
        loading.show()
        var req = RequestOrderId(orderId.toInt())
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.broadcastOutofRange(req)?.enqueue(object :
                Callback<ResponseOrderId> {
                override fun onResponse(
                    call: Call<ResponseOrderId>,
                    response: Response<ResponseOrderId>
                ) {
                    try {
                        loading.hide()
                        if (response.body()!!.number_of_sps != null && response.body()!!.number_of_sps!! > 0) {
                            startActivity(
                                Intent(
                                    this@ActivityCheckout,
                                    ActivityPlaceOrder::class.java
                                ).putExtra(AppConstants.ORDER_ID, orderId)
                            )
                        } else
                            startActivity(
                                Intent(
                                    this@ActivityCheckout,
                                    ActivityPlaceOrder::class.java
                                ).putExtra(AppConstants.SP_FOUND, false)
                                    .putExtra(AppConstants.ORDER_ID, response.body()!!.orderId)
                            )
                    } catch (E: java.lang.Exception) {

                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponseOrderId>, throwable: Throwable) {
                    loading.hide()
                }
            })
    }

    override fun onDataRetrieved(success: Boolean, response: Any, apiId: Int) {

        if (apiId == AppConstants.ADDRESS_GEO) {

            var addr = AppHelper.getAddressNames(response as ResponseGeoAddress)

            var lat = ""
            var long = ""
            try {
                lat = latLng!!.latitude.toString()
                long = latLng!!.longitude.toString()
            } catch (ex: Exception) {

            }
            if (MyApplication.selectedAddress == null) {
                MyApplication.selectedAddress = addr
                addr.lat = lat
                addr.long = long
            }

            setPlacedOrder()

        } else {
            if (success) {
                if(MyApplication.selectedAddress ==null )
                    CallAPIs.getAddressName(latLng!!,this,this)
                else
                    setPlacedOrder()
            } else {
                toast(AppHelper.getRemoteString("error_getting_data", this))
            }

        }
    }
}