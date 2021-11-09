package com.ids.qasemti.controller.Activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import com.google.android.gms.maps.model.LatLng
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

class ActivityCheckout : ActivityBase(), RVOnItemClickListener,ApiListener {

    var open = true
    var REQUEST_LOCATION = 5
    var stamp: Long? = 0
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
        setListeners()
    }


    override fun onItemClicked(view: View, position: Int) {

    }


    fun setUpCurr() {
        var cal = Calendar.getInstance()
        pickedDate = cal.time
        val myFormat = "dd MMM yyyy" //Change as you need
        var sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        var date = sdf.format(cal.time)
        etFromDate.text = date.toEditable()
        var time = String.format("%02d", cal.get(Calendar.HOUR_OF_DAY)) + ":" + String.format(
            "%02d",
            cal.get(Calendar.MINUTE)
        )
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
                SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
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
            SimpleDateFormat("dd MMM yyyy HH:mm", Locale.ENGLISH)
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
                if (MyApplication.selectedService!!.type!!.trim().lowercase() == "rental") {

                    if (!etToDate.text.isNullOrEmpty() && !etToTime.text.isNullOrEmpty()) {
                        if (rbNow.isChecked || checkGivenDate()) {
                            update = MyApplication.selectedPlaceOrder != null
                            if (MyApplication.selectedUser != null) {
                                try {
                                    setPlacedOrder()
                                    placeOrder()
                                } catch (e: Exception) {
                                    toast(getString(R.string.failure))
                                }
                            }
                        }else{
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
                                setPlacedOrder()
                                placeOrder()
                            } catch (e: Exception) {
                                toast(getString(R.string.failure))
                            }
                        }else{
                            CallAPIs.getUserInfo(this,this)
                        }
                    }else{
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
                var mcurrentDate = Calendar.getInstance()
                var mYear = 0
                var mMonth = 0
                var mDay = 0
                mYear = mcurrentDate[Calendar.YEAR]
                mMonth = mcurrentDate[Calendar.MONTH]
                mDay = mcurrentDate[Calendar.DAY_OF_MONTH]

                val mDatePicker = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { datepicker, selectedyear, selectedmonth, selectedday ->
                        val myCalendar = Calendar.getInstance()
                        myCalendar[Calendar.YEAR] = selectedyear
                        myCalendar[Calendar.MONTH] = selectedmonth
                        myCalendar[Calendar.DAY_OF_MONTH] = selectedday
                        val myFormat = "dd MMM yyyy" //Change as you need
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
                mDatePicker.datePicker.minDate = mcurrentDate.time.time
                mDatePicker.show()
            }
            rlFromTime.onOneClick {
                // TODO Auto-generated method stub
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val myFormat = "dd MMM yyyy" //Change as you need
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
                        var date =
                            selectedDate!!.split(" ").get(0) + " " + selectedDate!!.split(" ")
                                .get(1) + " " + selectedDate!!.split(" ").get(2)
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

                    var time = String.format("%02d", selectedHour) + " : " + String.format(
                        "%02d",
                        selectedMinute
                    )
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
            var mcurrentDate = Calendar.getInstance()
            var mYear = 0
            var mMonth = 0
            var mDay = 0
            mYear = mcurrentDate[Calendar.YEAR]
            mMonth = mcurrentDate[Calendar.MONTH]
            mDay = mcurrentDate[Calendar.DAY_OF_MONTH]

            mcurrentDate.set(mYear, mMonth, mDay)
            val mDatePicker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { datepicker, selectedyear, selectedmonth, selectedday ->
                    val myCalendar = Calendar.getInstance()
                    myCalendar[Calendar.YEAR] = selectedyear
                    myCalendar[Calendar.MONTH] = selectedmonth
                    myCalendar[Calendar.DAY_OF_MONTH] = selectedday
                    val myFormat = "dd MMM yyyy" //Change as you need
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
            var up = -90
            var down = 90
            if (open) {
                ivOpenDateTime.animate().rotation(up.toFloat()).setDuration(400)
                llTimeDate.hide()
            } else {
                ivOpenDateTime.animate().rotation(down.toFloat()).setDuration(400)
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

        if (MyApplication.selectedService!!.type!!.trim().lowercase() == "rental") {
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
            tvSizeCapacity.text = MyApplication.selectedSize
        } catch (e: Exception) {
        }
        try {
            tvPrice.text = MyApplication.selectedPrice + ""
        } catch (e: Exception) {
        }
        try {
            tvVariationType.text = MyApplication.selectedVariationType
        } catch (e: Exception) {
        }
        try {
            tvSize.text = MyApplication.selectedSize
        } catch (e: Exception) {
        }
    }

    private fun setPlacedOrder() {
        var lat = ""
        var long = ""
        try {
            lat = latLng!!.latitude.toString()
            long = latLng!!.longitude.toString()
        } catch (ex: Exception) {

        }
        if (MyApplication.selectedAddress == null) {
            var address = AppHelper.getAddressLoc(latLng!!.latitude, latLng!!.longitude, this)
            MyApplication.selectedAddress = ResponseAddress(
                "0",
                address.featureName,
                address.latitude.toString(),
                address.longitude.toString(),
                address.thoroughfare,
                "",
                "",
                address.premises
            )
        }

        MyApplication.selectedPlaceOrder = RequestPlaceOrder(
            MyApplication.userId,
            MyApplication.selectedService!!.type!!,
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
            if (MyApplication.selectedUser!!.firstName != null && MyApplication.selectedUser!!.firstName!!.isNotEmpty()) MyApplication.selectedUser!!.firstName else "",
            if (MyApplication.selectedUser!!.lastName != null && MyApplication.selectedUser!!.lastName!!.isNotEmpty()) MyApplication.selectedUser!!.lastName else "",
            if (MyApplication.selectedUser!!.billingCompany != null && MyApplication.selectedUser!!.billingCompany!!.isNotEmpty()) MyApplication.selectedUser!!.billingCompany else "",
            if (MyApplication.selectedUser!!.email != null && MyApplication.selectedUser!!.email!!.isNotEmpty()) MyApplication.selectedUser!!.email else "",
            if (MyApplication.selectedUser!!.mobileNumber != null && MyApplication.selectedUser!!.mobileNumber!!.isNotEmpty()) MyApplication.selectedUser!!.mobileNumber else "",
            if (MyApplication.selectedService!!.name!!.isNotEmpty()) MyApplication.selectedService!!.name else "",
            MyApplication.selectedPrice

        )
        if (update) {
            var i = MyApplication.arrayCart.size - 1
            MyApplication.arrayCart[i] = MyApplication.selectedPlaceOrder!!
        } else {
            MyApplication.arrayCart.add(MyApplication.selectedPlaceOrder!!)
        }
        MyApplication.seletedPosCart = MyApplication.arrayCart.size - 1

        AppHelper.toGSOn(MyApplication.arrayCart)

    }


    fun placeOrder() {
        loading.show()
        //testing success scenario
/*        MyApplication.selectedPlaceOrder!!.addressLatitude=""
        MyApplication.selectedPlaceOrder!!.addressLongitude=""*/
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.placeOrder(MyApplication.selectedPlaceOrder!!)?.enqueue(object :
                Callback<ResponseOrderId> {
                override fun onResponse(
                    call: Call<ResponseOrderId>,
                    response: Response<ResponseOrderId>
                ) {
                    try {
                        loading.hide()
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
        if(success) {
            setPlacedOrder()
            placeOrder()
        }else{
            toast(AppHelper.getRemoteString("error_getting_data",this))
        }

    }
}