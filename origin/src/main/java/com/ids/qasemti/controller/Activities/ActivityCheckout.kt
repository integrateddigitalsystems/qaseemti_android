package com.ids.qasemti.controller.Activities

import android.app.*
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Adapters.com.ids.qasemti.model.RequestJOrderid
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.Fragments.FragmentOrders
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import com.ids.qasemti.utils.AppHelper.Companion.toEditable
import kotlinx.android.synthetic.main.activity_new_address.*
import kotlinx.android.synthetic.main.activity_place_order.*
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.android.synthetic.main.layout_order_contact_tab.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import com.ids.qasemti.controller.Adapters.*
import com.ids.qasemti.controller.Adapters.com.ids.qasemti.controller.Adapters.AdapterDays
import com.ids.qasemti.controller.Adapters.com.ids.qasemti.controller.Adapters.com.ids.qasemti.model.TimeDuration
import com.ids.qasemti.controller.Adapters.com.ids.qasemti.model.RequestOrderPayment
import com.ids.qasemti.controller.Adapters.com.ids.qasemti.model.ServiceAvailableDateTime
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.sampleapp.model.ItemSpinner
import kotlinx.android.synthetic.main.fragment_checkout.tvPrice
import kotlinx.android.synthetic.main.fragment_service_details.*
import kotlinx.android.synthetic.main.layout_request_new_time.*
import java.sql.Time
import java.time.LocalDate
import java.time.Month
import kotlin.collections.ArrayList


class ActivityCheckout : AppCompactBase(), RVOnItemClickListener, ApiListener,
    com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    var open = true
    var REQUEST_LOCATION = 5
    var stamp: Long? = 0
    var timeSelected: Boolean? = false
    var selectedFromSlot: String? = ""
    var selectedToSlot: String? = ""
    var editer: String? = ""
    var dialog: Dialog? = null
    var arrayDays: ArrayList<String> = arrayListOf()
    var reasonString: String? = ""
    var selectedDayId: Int? = -1
    var dateDay: Date? = null
    var selectedNumDays = false
    var reasonId: Int? = -1
    var myTimes: ArrayList<TimeDuration> = arrayListOf()
    var sendFormat: String = "yyyy-MM-dd"
    var viewFormat: String = "dd/MM/yyyy"
    var timeFormat: String = "HH:mm"
    var fullTimeFormat: String = "HH:mm:ss"
    var sendFormatter = SimpleDateFormat(sendFormat, Locale.ENGLISH)
    var viewFormatter = SimpleDateFormat(sendFormat, Locale.ENGLISH)
    var fullTimeFormatter = SimpleDateFormat(fullTimeFormat, Locale.ENGLISH)
    var mainFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
    var main2Formatter = SimpleDateFormat("yyyy-MM-dd")
    var getFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
    var lastFormatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
    var timeFormatter = SimpleDateFormat(timeFormat, Locale.ENGLISH)
    var minRenewTime: Date? = null
    var minRenewTo: Date? = null
    var pickedDate: Date? = null
    var selectedFrom: String? = ""
    var selectedTo: String? = ""
    var update = false
    var fromHour: Int? = 0
    var fromMin: Int? = 0
    var locationSelected = false
    var latLng: LatLng? = null
    var selectedDate: String? = ""
    var adapterPaymentMethods: AdapterCheckoutPayment? = null
    var selectedPaymentId: Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_checkout)
        AppHelper.setAllTexts(rootLayoutCheckout, this)
        stamp = Calendar.getInstance().timeInMillis
        MyApplication.selectedPlaceOrder = null
        init()

    }


    override fun onItemClicked(view: View, position: Int) {
        if (view.id == R.id.linearPaymentMethod) {
            if (!MyApplication.repeating && !MyApplication.renewing) {
                try {
                    MyApplication.selectedService!!.availablePaymentMethods.forEach {
                        it.selected = false
                    }
                    MyApplication.selectedService!!.availablePaymentMethods[position].selected =
                        true
                    selectedPaymentId =
                        MyApplication.selectedService!!.availablePaymentMethods[position].id!!.toInt()
                    adapterPaymentMethods!!.notifyDataSetChanged()
                } catch (e: Exception) {
                    var ex = e
                    var x = 1
                }
            }
            else {
                try {
                    MyApplication.selectedOrder!!.product!!.availablePaymentMethods.forEach {
                        it.selected = false
                    }
                    MyApplication.selectedOrder!!.product!!.availablePaymentMethods[position].selected =
                        true
                    selectedPaymentId =
                        MyApplication.selectedOrder!!.product!!.availablePaymentMethods[position].id!!.toInt()
                    adapterPaymentMethods!!.notifyDataSetChanged()
                } catch (e: Exception) {
                    var ex = e
                    var x = 1
                }
            }
        } else if (view.id == R.id.linearPopUpDay) {
            selectedNumDays = true
            dialog!!.cancel()
            tvNumDaysValue.text = arrayDays.get(position)
            changeTo()
        } else {
            timeSelected = true
            selectedFromSlot = myTimes.get(position).from
            selectedToSlot = myTimes.get(position).to
            var dateF = fullTimeFormatter.parse(selectedFromSlot)
            var dateT = fullTimeFormatter.parse(selectedToSlot)
            var simp = SimpleDateFormat("HH:mm", Locale.ENGLISH)
            var res1 = simp.format(dateF)
            var res2 = simp.format(dateT)


            try {
                var simpp = SimpleDateFormat("yyyy-MM-dd HH:mm")
                dateDay = simpp.parse(etFromDate.text.toString() + " " + res2)
                changeTo()
            } catch (ex: Exception) {

            }

            etFromTime.text = (res1 + "-" + res2).toEditable()
            dialog!!.cancel()
        }
    }


    fun setUpCurr() {
        var cal = Calendar.getInstance()
        pickedDate = cal.time
        var date = sendFormatter.format(cal.time)
        etFromDate.text = date.toEditable()
        var time = cal.get(Calendar.HOUR_OF_DAY).toString().formatNumber("00") + ":" + cal.get(
            Calendar.MINUTE
        ).toString().formatNumber("00")
        fromHour = cal.get(Calendar.HOUR_OF_DAY)
        fromMin = cal.get(Calendar.MINUTE)
        dateDay = cal.time
        etFromTime.text = time.toEditable()
        selectedFrom = getFormatter.format(cal.time)
        etFromDate.text = viewFormatter.format(cal.time).toEditable()

        selectedDate = getFormatter.format(cal.time)
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
            var dateFrom = etFromDate.text.toString()
            var dateTo = etToDate.text.toString()

            if (sendFormatter.parse(dateFrom).time > sendFormatter.parse(dateTo).time)
                return 1
            else if (sendFormatter.parse(dateFrom).time == sendFormatter.parse(dateTo).time)
                return -1
            return 0
        } catch (ex: Exception) {
            return 1
        }

    }

    fun checkGivenDate(): Boolean {

        if (!selectedDate.isNullOrEmpty()) {
            var date: Date? = null
            if (MyApplication.selectedService!!.availableDates != null && MyApplication.selectedService!!.availableDates.size > 0) {
                date = main2Formatter.parse(selectedDate)!!
            } else {
                date = mainFormatter.parse(selectedDate)!!
            }

            if (Calendar.getInstance().time.time > date!!.time) {
                return false
            }

            return true
        } else
            return false
    }

    fun compareTimes(selectH: Int, selectM: Int): Boolean {
        if (selectH < fromHour!!) {
            return false
        } else if (selectM < fromMin!!) {
            return false
        }
        return true

    }

    fun limitDatePicker(
        datePicker: com.wdullaer.materialdatetimepicker.date.DatePickerDialog,
        daysOfWeek: ArrayList<Int>
    ) {
        var loopdate: Calendar = Calendar.getInstance()
        var min_date_c = Calendar.getInstance()
        var max_date_c = Calendar.getInstance()
        max_date_c.add(Calendar.YEAR, 100)
        var tr = loopdate.before(max_date_c)
        while (loopdate.before(max_date_c)) {
            val dayOfWeek = loopdate[Calendar.DAY_OF_WEEK]
            if (daysOfWeek.contains(dayOfWeek)) {
                val disabledDays = arrayOfNulls<Calendar>(1)
                disabledDays[0] = loopdate
                datePicker.setDisabledDays(disabledDays)
            }
            min_date_c.add(Calendar.DATE, 1)
            loopdate = min_date_c
        }
    }

    fun dateTimetoDays(array: ArrayList<ServiceAvailableDateTime>): ArrayList<Int> {
        var arrayInt: ArrayList<Int> = arrayListOf()
        for (item in array) {
            if (item.day!!.equals("Monday", true) && !(item.time!=null && item.time.size > 0 )) {
                arrayInt.add(Calendar.MONDAY)
            } else if (item!!.day!!.equals("Tuesday", true)&& !(item.time!=null && item.time.size > 0 )) {
                arrayInt.add(Calendar.TUESDAY)
            } else if (item!!.day!!.equals("Wednesday", true)&& !(item.time!=null && item.time.size > 0 )) {
                arrayInt.add(Calendar.WEDNESDAY)
            } else if (item!!.day!!.equals("Thursday", true)&& !(item.time!=null && item.time.size > 0 )) {
                arrayInt.add(Calendar.THURSDAY)
            } else if (item!!.day!!.equals("Friday", true)&& !(item.time!=null && item.time.size > 0 )) {
                arrayInt.add(Calendar.FRIDAY)
            } else if (item!!.day!!.equals("Saturday", true)&& !(item.time!=null && item.time.size > 0 )) {
                arrayInt.add(Calendar.SATURDAY)
            } else if (item!!.day!!.equals("Sunday", true)&& !(item.time!=null && item.time.size > 0 )) {
                arrayInt.add(Calendar.SUNDAY)
            }
        }

        return arrayInt
    }

    private fun timeDialogRepeat() {
        dialog = Dialog(this, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_recyler)
        dialog!!.setCancelable(true)
        val rv: RecyclerView = dialog!!.findViewById(R.id.rvData)

        var day = dayIdtoString(selectedDayId!!)
        try {
            myTimes.clear()
                                myTimes.addAll(
                MyApplication.selectedOrder!!.product!!.availableDates.find {
                    it.day.equals(
                        day,
                        true
                    )
                }!!.time
            )
                    }catch (ex:Exception){
                        myTimes.clear()
                    }
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv.layoutManager = layoutManager
        var adapter = AdapterInterval(myTimes, this)
        rv.adapter = adapter

        /* try{
             var item=arrayCountries.find { it.code!!.replace("+","").trim()==MyApplication.selectedItemDialog.replace("+","").trim() }
             var position=arrayCountries.indexOf(item!!)
             rv.scrollToPosition(position)}catch (e: java.lang.Exception){}*/

        dialog!!.show()
    }

    private fun timeDialog(array : ArrayList<TimeDuration>) {
        dialog = Dialog(this, R.style.Base_ThemeOverlay_AppCompat_Dialog)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.time_picker_dialog)
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.rounded_white_background)
        dialog!!.setCancelable(true)
        val rv: RecyclerView = dialog!!.findViewById(R.id.rvUnavailableTimes)
        val picker :  TimePicker = dialog!!.findViewById(R.id.tpEvent);
        val but : Button = dialog!!.findViewById(R.id.btDoneTime)
        var subTitle : TextView = dialog!!.findViewById(R.id.tvTimeSubTitle);
        subTitle.typeface = AppHelper.getTypefaceBoldItalic(this)
        picker.setIs24HourView(true)
        var day = dayIdtoString(selectedDayId!!)
        /*try {
            myTimes.clear()
                                myTimes.addAll(
                MyApplication.selectedService!!.availableDates.find {
                    it.day.equals(
                        day,
                        true
                    )
                }!!.time)
        }catch (ex:Exception){

        }*/
        val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rv.layoutManager = layoutManager
        var adapter = AdapterInterval(array, this)
        rv.adapter = adapter

        but.onOneClick {

            var selectedCal = Calendar.getInstance()
            var time = picker.hour.toString()+":"+picker.minute.toString()+":"+"00"
            var date = fullTimeFormatter.parse(time)
            selectedCal.time = date


            var isInRange = false

            for (item in array) {
                var fromTime = fullTimeFormatter.parse(item.from)
                var toTime = fullTimeFormatter.parse(item.to)
                var calFrom = Calendar.getInstance()
                var calTo = Calendar.getInstance()
                calFrom.time = fromTime
                calTo.time = toTime

                if ((selectedCal.timeInMillis > calFrom.timeInMillis && selectedCal.timeInMillis < calTo.timeInMillis)) {
                    isInRange = false
                    break
                }else{
                    isInRange = true
                }
            }

            if (isInRange) {
                dialog!!.dismiss()
                var time = timeFormatter.format(selectedCal.time).toEditable()
                etFromTime.text = timeFormatter.format(selectedCal.time).toEditable()
                try {
                    var dateF = selectedFrom!!.split(" ").get(0)
                    selectedFrom = dateF + " " + time
                }catch (ex:Exception){
                    logw("keyCode",ex.toString())
                }
                try {
                    var dateE = selectedDate!!.split(" ").get(0)
                    selectedDate = dateE + " " + time
                }catch (ex:Exception){
                    logw("keyCode",ex.toString())
                }
               // myTimes.clear()
            }else{
                AppHelper.createDialog(this,AppHelper.getRemoteString("select_available_time",this))
            }
        }



        /* try{
             var item=arrayCountries.find { it.code!!.replace("+","").trim()==MyApplication.selectedItemDialog.replace("+","").trim() }
             var position=arrayCountries.indexOf(item!!)
             rv.scrollToPosition(position)}catch (e: java.lang.Exception){}*/

        dialog!!.show()
    }

    /*fun timeDialog(){
        dialog = Dialog(this, R.style.Base_ThemeOverlay_AppCompat_Dialog)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.setContentView(R.layout.dialog_popup_intervals)
        dialog!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialog!!.window!!.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )


        var day = dayIdtoString(selectedDayId!!)
        var times = MyApplication.selectedService!!.availableDates.find { it.day.equals(day,true)  }
        var adapterIntervals : AdapterInterval = AdapterInterval(times!!.time,this)
        var rec = dialog!!.findViewById<RecyclerView>(R.id.rvTimeIntervals)
        rec.layoutManager = GridLayoutManager(this,1)
        rec.adapter = adapterIntervals
        dialog!!.setCancelable(true)

        dialog!!.show()
    }*/
    fun numDaysDialog() {

        dialog = Dialog(this, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_recyler)
        dialog!!.setCancelable(true)
        dialog!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialog!!.window!!.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )


        if (MyApplication.maxDay == 0) {
            MyApplication.maxDay = 30
        }


        var adapterIntervals = AdapterDays(arrayDays, this)
        var rec = dialog!!.findViewById<RecyclerView>(R.id.rvData)
        rec.layoutManager = GridLayoutManager(this, 1)
        rec.adapter = adapterIntervals
        dialog!!.setCancelable(true)

        dialog!!.show()
    }

    fun changeTo() {
        try {
            if (selectedNumDays) {
                var dateSimp = SimpleDateFormat("yyyy-MM-dd")
                var timeSimp = SimpleDateFormat("HH:mm")
                var cal = Calendar.getInstance()
                try {
                    cal.time = mainFormatter.parse(selectedDate)
                }catch (ex:Exception){
                    cal.time = mainFormatter.parse(selectedFrom)
                }
                var days = tvNumDaysValue.text.toString().toInt()
                cal.add(Calendar.DAY_OF_MONTH, days)
                selectedTo = mainFormatter.format(cal.time)
                etToDate.text = dateSimp.format(cal.time).toEditable()
                etToTime.text = timeSimp.format(cal.time).toEditable()


            }
        } catch (ex: Exception) {
           logw("Error",ex.toString())
        }
    }

    fun setListeners() {

        if (MyApplication.selectedService!!.availableDates != null && MyApplication.selectedService!!.availableDates.size > 0 /*&& !checkDatesService()*/) {
            var cal = Calendar.getInstance()
            selectedDayId = cal.get(Calendar.DAY_OF_WEEK)
            var dayName = dayIdtoString(cal.get(Calendar.DAY_OF_WEEK))
            var thisDay =
                MyApplication.selectedService!!.availableDates.find { it.day.equals(dayName, true) }
            var isInRange : Boolean = true
            if(thisDay!=null){
                var times :  ArrayList<TimeDuration> = arrayListOf()
                try{
                    times.addAll(thisDay.time)
                    for(item in times){
                        var fromTime = fullTimeFormatter.parse(item.from)
                        var toTime = fullTimeFormatter.parse(item.to)
                        var calFrom = Calendar.getInstance()
                        var calTo = Calendar.getInstance()
                        calFrom.time = fromTime
                        calTo.time = toTime

                        if ((cal.timeInMillis > calFrom.timeInMillis && cal.timeInMillis < calTo.timeInMillis)) {
                            isInRange = false
                            break
                        }else{
                            isInRange = true
                        }
                    }


                }catch (ex:Exception){}

            }else{
                isInRange = true
            }

            if(isInRange){
                setUpCurr()
                etFromTime.isEnabled = false
                etFromDate.isEnabled = false
                rlFromDate.onOneClick {

                }
                rlFromTime.onOneClick {

                }
            }else{
                etFromDate.text.clear()
                etFromTime.text.clear()
                rbNow.isEnabled = false
                rbSpecify.isChecked = true

                selectedDayId = -1
                rlFromDate.onOneClick {
                    val datePicker =
                        com.wdullaer.materialdatetimepicker.date.DatePickerDialog()

                    datePicker.setAccentColor(AppHelper.getColor(this, R.color.primary))


                    var intArray =
                        dateTimetoDays(MyApplication.selectedService!!.availableDates)
                    limitDatePicker(datePicker, intArray)


                    datePicker.minDate = Calendar.getInstance()
                    var x = datePicker.disabledDays
                    datePicker.onDateSetListener = this
                    editer = "from"

                    datePicker.show(supportFragmentManager!!, "")

                }


                rlNUmDays.onOneClick {

                    if (!etFromDate.text.toString().isNullOrEmpty() && !etFromTime.text.toString()
                            .isNullOrEmpty()
                    )
                        numDaysDialog()
                    else
                        toast(AppHelper.getRemoteString("you_must_fill_from", this))
                }

                rlFromTime.onOneClick {
                    if (selectedDayId!! == -1) {
                        toast(getString(R.string.need_specific_date))
                    } else {
                        //test
                        var day = dayIdtoString(selectedDayId!!)
                        try {
                            myTimes.clear()
                            myTimes.addAll(
                                MyApplication.selectedService!!.availableDates.find {
                                    it.day.equals(
                                        day,
                                        true
                                    )
                                }!!.time)
                        }catch (ex : Exception){
                            myTimes.clear()
                        }


                        if (myTimes!!.size > 0) {
                            timeDialog(myTimes)
                        } else {
                            val mcurrentTime = Calendar.getInstance()
                            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                            val minute = mcurrentTime[Calendar.MINUTE]
                            val now = mcurrentTime.time
                            val timePickerDialog = TimePickerDialog(
                                this, R.style.DatePickerDialog,
                                { timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int ->

                                    fromHour = selectedHour
                                    fromMin = selectedMinute
                                    timeSelected = true
                                    var time = selectedHour.toString()
                                        .formatNumber("00") + ":" + selectedMinute.toString()
                                        .formatNumber("00")
                                    etFromTime.text = time.toEditable()
                                    var date =
                                        selectedDate!!.split(" ").get(0)
                                    selectedDate = date + " " + time
                                    try {
                                        var simpp = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                        dateDay = simpp.parse(selectedDate)
                                        changeTo()
                                    } catch (ex: java.lang.Exception) {

                                    }
                                    var dateF =
                                        selectedFrom!!.split(" ").get(0)
                                    selectedFrom = dateF + " " + time
                                }, hour, minute, true
                            ) //Yes 24 hour time
                            timePickerDialog.show()
                        }
                    }
                }

                rlNUmDays.onOneClick {

                    if (!etFromDate.text.toString().isNullOrEmpty() && !etFromTime.text.toString()
                            .isNullOrEmpty()
                    )
                        numDaysDialog()
                    else
                        toast(AppHelper.getRemoteString("you_must_fill_from", this))
                }
            }



        } else {
            setUpCurr()
        }
        /*  btPICKER.setOnClickListener {
              val datePicker =
                  com.wdullaer.materialdatetimepicker.date.DatePickerDialog()

              datePicker.setAccentColor(AppHelper.getColor(this,R.color.primary))

              var arr : ArrayList<ServiceAvailableDateTime> = arrayListOf()

              arr.add(ServiceAvailableDateTime("MondAy", arrayListOf()))
              arr.add(ServiceAvailableDateTime("FrIdaY", arrayListOf()))

              var intArray = dateTimetoDays(arr)
              limitDatePicker(datePicker,intArray)


              datePicker.minDate = Calendar.getInstance()
              var x = datePicker.disabledDays
              datePicker.onDateSetListener= this

            *//* datePicker.setOnDismissListener {

           }

            datePicker.registerOnDateChangedListener {

            }
            datePicker.unregisterOnDateChangedListener {

            }
*//*
            datePicker.show(supportFragmentManager!!,"")
        }*/
        btPlaceOrder.onOneClick {
            if(llNumDays.visibility == View.GONE || (llNumDays.visibility == View.VISIBLE  && selectedNumDays)){
                if (selectedPaymentId != -1) {
                    if (locationSelected) {
                        if (MyApplication.selectedService!!.typeId!!.equals(MyApplication.categories.find {
                                it.valEn!!.lowercase().equals("rental")
                            }!!.id!!.toInt())) {

                            if (!etToDate.text.isNullOrEmpty() && !etToTime.text.isNullOrEmpty()) {
                                if (rbNow.isChecked || /*checkGivenDate() &&*/ !etFromDate.text.toString()
                                        .isNullOrEmpty() && !etFromTime.text.toString()
                                        .isNullOrEmpty()
                                ) {
                                    update = MyApplication.selectedPlaceOrder != null
                                    if (MyApplication.selectedUser != null) {
                                        try {
                                            // setPlacedOrder()
                                            if (MyApplication.selectedAddress == null)
                                                CallAPIs.getAddressName(latLng!!, this, this)
                                            else {

                                                if ((MyApplication.selectedService!!.serviceReasons == null || MyApplication.selectedService!!.serviceReasons.size == 0) || (reasonId != 0 && reasonId!=MyApplication.otherId) || (reasonId == MyApplication.otherId && !etServiceReason.text.toString()
                                                        .isNullOrEmpty())
                                                ) {
                                                    if (reasonId == -1) {
                                                        reasonId = 0
                                                    } else {
                                                        if (reasonId == MyApplication.otherId) {
                                                            reasonString =
                                                                etServiceReason.text.toString()
                                                        } else {
                                                            reasonString = ""
                                                        }
                                                    }
                                                    setPlacedOrder()
                                                } else {
                                                    AppHelper.createDialog(
                                                        this,
                                                        AppHelper.getRemoteString("fill_all_field", this)
                                                    )
                                                }
                                            }

                                        } catch (e: Exception) {
                                            toast(getString(R.string.failure))
                                        }
                                    }
                                } else {
                                    AppHelper.createDialog(
                                        this,
                                        AppHelper.getRemoteString("fill_all_field", this)
                                    )
                                }
                            } else {
                                AppHelper.createDialog(
                                    this,
                                    AppHelper.getRemoteString("fill_all_field", this)
                                )
                            }
                        } else {

                            if (rbNow.isChecked || /*checkGivenDate() &&*/ !etFromDate.text.toString()
                                    .isNullOrEmpty() && !etFromTime.text.toString().isNullOrEmpty()
                            ) {
                                update = MyApplication.selectedPlaceOrder != null
                                if (MyApplication.selectedUser != null) {
                                    try {
                                        if (MyApplication.selectedAddress == null)
                                            CallAPIs.getAddressName(latLng!!, this, this)
                                        else {
                                            if ((MyApplication.selectedService!!.serviceReasons == null || MyApplication.selectedService!!.serviceReasons.size == 0)) {
                                                reasonId = 0
                                                reasonString = ""

                                                setPlacedOrder()
                                            } else if ( (reasonId != 0 && reasonId!=MyApplication.otherId) || (reasonId == MyApplication.otherId && !etServiceReason.text.toString()
                                                    .isNullOrEmpty())
                                            ) {
                                                if (reasonId == MyApplication.otherId) {
                                                    reasonString = etServiceReason.text.toString()
                                                } else {
                                                    reasonString = ""
                                                }
                                                setPlacedOrder()


                                            } else {
                                                AppHelper.createDialog(
                                                    this,
                                                    AppHelper.getRemoteString(
                                                        "fill_reason_field",
                                                        this
                                                    )
                                                )
                                            }
                                        }
                                    } catch (e: Exception) {
                                        toast(getString(R.string.failure))
                                    }
                                } else {
                                    CallAPIs.getUserInfo(this)
                                }
                            } else {
                                AppHelper.createDialog(
                                    this,
                                    AppHelper.getRemoteString("fill_all_field", this)
                                )
                            }
                        }
                    } else {
                        AppHelper.createDialog(
                            this,
                            AppHelper.getRemoteString("please_select_location", this)
                        )
                    }
                } else {
                    toast(AppHelper.getRemoteString("select_payment_method", this))
                }
            }else{
                toast(AppHelper.getRemoteString("pleaseSelectNumberOfDays", this))
            }
        }

        rbNow.onOneClick {
            /*if (MyApplication.selectedService!!.availableDates != null && MyApplication.selectedService!!.availableDates.size > 0 *//*&& !checkDatesService()*//*) {
                var cal = Calendar.getInstance()
                selectedDayId = cal.get(Calendar.DAY_OF_WEEK)
                var dayName = dayIdtoString(cal.get(Calendar.DAY_OF_WEEK))
                var thisDay = MyApplication.selectedService!!.availableDates.find {
                    it.day.equals(
                        dayName,
                        true
                    )
                }
                var isInRange = true
                if(thisDay!=null){

                    var times :  ArrayList<TimeDuration> = arrayListOf()
                    try{
                        times.addAll(thisDay.time)
                        for(item in times){
                            var fromTime = fullTimeFormatter.parse(item.from)
                            var toTime = fullTimeFormatter.parse(item.to)
                            var calFrom = Calendar.getInstance()
                            var calTo = Calendar.getInstance()
                            calFrom.time = fromTime
                            calTo.time = toTime

                            if ((cal.timeInMillis > calFrom.timeInMillis && cal.timeInMillis < calTo.timeInMillis)) {
                                isInRange = false
                                break
                            }else{
                                isInRange = true
                            }
                        }



                    }catch (ex:Exception){}

                }else{
                    isInRange = true
                }

                if(isInRange){
                    etFromTime.isEnabled = false
                    etFromDate.isEnabled = false
                    rlFromDate.onOneClick {

                    }
                    rlFromTime.onOneClick {

                    }

                }else{
                    etFromDate.text.clear()
                    etFromTime.text.clear()
                    rbNow.isEnabled = false
                    rbSpecify.isChecked = true
                }
                selectedDayId = -1
                rlFromDate.onOneClick {
                    val datePicker =
                        com.wdullaer.materialdatetimepicker.date.DatePickerDialog()

                    datePicker.setAccentColor(AppHelper.getColor(this, R.color.primary))


                    var intArray =
                        dateTimetoDays(MyApplication.selectedService!!.availableDates)
                    limitDatePicker(datePicker, intArray)


                    datePicker.minDate = Calendar.getInstance()
                    var x = datePicker.disabledDays
                    datePicker.onDateSetListener = this
                    editer = "from"

                    datePicker.show(supportFragmentManager!!, "")

                }

                rlFromTime.onOneClick {
                    if (selectedDayId!! == -1) {
                        toast(getString(R.string.need_specific_date))
                    } else {
                        var day = dayIdtoString(selectedDayId!!)
                        try {
                            myTimes.clear()
                                myTimes.addAll(
                                MyApplication.selectedService!!.availableDates.find {
                                    it.day.equals(
                                        day,
                                        true
                                    )
                                }!!.time
                            )
                                    }catch (ex:Exception){
                                        myTimes.clear()
                                    }
                        if(myTimes.size >0 ) {
                            timeDialog(myTimes)
                        }else{
                              val mcurrentTime = Calendar.getInstance()
                    val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                    val minute = mcurrentTime[Calendar.MINUTE]
                    val now = mcurrentTime.time
                    val timePickerDialog = TimePickerDialog(
                        this, R.style.DatePickerDialog,
                        { timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int ->

                            fromHour = selectedHour
                            fromMin = selectedMinute
                            timeSelected = true
                            var time = selectedHour.toString()
                                .formatNumber("00") + ":" + selectedMinute.toString()
                                .formatNumber("00")
                            etFromTime.text = time.toEditable()
                            var date =
                                selectedDate!!.split(" ").get(0)
                            selectedDate = date + " " + time
                            var dateF =
                                selectedFrom!!.split(" ").get(0)
                            selectedFrom = dateF + " " + time
                            try {
                                var simpp = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                dateDay = simpp.parse(selectedFrom)
                                changeTo()
                            } catch (ex: java.lang.Exception) {

                            }
                        }, hour, minute, true
                    ) //Yes 24 hour time
                    timePickerDialog.show()
                        }
                    }
                }


            }
            else {*/
                setUpCurr()
                rlFromDate.onOneClick {

                }
                rlFromTime.onOneClick {

                }
           // }

            AppHelper.setTextColor(this, etFromDate, R.color.gray_font_light)
            AppHelper.setTextColor(this, etFromTime, R.color.gray_font_light)
        }

        rlNUmDays.onOneClick {

            if (!etFromDate.text.toString().isNullOrEmpty() && !etFromTime.text.toString()
                    .isNullOrEmpty()
            )
                numDaysDialog()
            else
                toast(AppHelper.getRemoteString("you_must_fill_from", this))
        }

        rbSpecify.onOneClick {

            AppHelper.setTextColor(this, etFromDate, R.color.gray_font)
            AppHelper.setTextColor(this, etFromTime, R.color.gray_font)
            rlFromDate.onOneClick {
                if ((MyApplication.selectedService!!.availableDates != null && MyApplication.selectedService!!.availableDates.size > 0 /*&& !checkDatesService()*/) ) {
                    val datePicker =
                        com.wdullaer.materialdatetimepicker.date.DatePickerDialog()

                    datePicker.setAccentColor(AppHelper.getColor(this, R.color.primary))


                    var intArray = dateTimetoDays(MyApplication.selectedService!!.availableDates)
                    limitDatePicker(datePicker, intArray)


                    datePicker.minDate = Calendar.getInstance()
                    var x = datePicker.disabledDays
                    datePicker.onDateSetListener = this
                    editer = "from"

                    datePicker.show(supportFragmentManager!!, "")
                } else {
                    var mcurrentDate = sendFormatter.parse(selectedFrom)
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
                            pickedDate = myCalendar.time
                            var date = getFormatter.format(myCalendar.time)
                            var dateShow = viewFormatter.format(myCalendar.time)
                            etFromDate.text = date.toEditable()
                            timeSelected = false

                            if (compareDates() == 1) {
                                selectedTo = date
                                var cal = myCalendar
                                cal.add(Calendar.DAY_OF_MONTH, 1)
                                selectedTo = viewFormatter.format(cal.time)
                                //   etToDate.text = selectedTo!!.toEditable()
                            }
                            etFromTime.text = "".toEditable()
                            etToTime.text = "".toEditable()
                            var x = selectedDate
                            var time = selectedDate!!.split(" ").get(1)
                            selectedDate = etFromDate.text.toString() + " " + time
                            try {
                                var simpp = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                dateDay = simpp.parse(selectedDate)
                                changeTo()
                            } catch (ex: Exception) {

                            }
                            selectedFrom = date
                            etFromDate.text = dateShow.toEditable()
                        }, mYear, mMonth, mDay
                    )
                    mDatePicker.datePicker.minDate = calTime.time.time
                    mDatePicker.show()
                }
            }
            rlFromTime.onOneClick {

                if (MyApplication.selectedService!!.availableDates != null && MyApplication.selectedService!!.availableDates.size > 0) {
                    if (selectedDayId!! == -1) {
                        toast(AppHelper.getRemoteString("need_specific_date", this))
                    } else {
                        var day = dayIdtoString(selectedDayId!!)
                        try {
                            myTimes.clear()
                                myTimes.addAll(
                                MyApplication.selectedService!!.availableDates.find {
                                    it.day.equals(
                                        day,
                                        true
                                    )
                                }!!.time
                            )
                                        }catch (ex:Exception){
                                            myTimes.clear()
                                        }
                        if(myTimes.size > 0 ) {
                            timeDialog(myTimes)
                        }else{
                            // TODO Auto-generated method stub
                    val mcurrentTime = Calendar.getInstance()
                    val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                    val minute = mcurrentTime[Calendar.MINUTE]
                    val now = mcurrentTime.time
                    val timePickerDialog = TimePickerDialog(
                        this, R.style.DatePickerDialog,
                        { timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int ->

                            fromHour = selectedHour
                            fromMin = selectedMinute

                            var time = selectedHour.toString()
                                .formatNumber("00") + ":" + selectedMinute.toString()
                                .formatNumber("00")
                            etFromTime.text = time.toEditable()
                            timeSelected = true
                            var date =
                                selectedDate!!.split(" ").get(0)
                            selectedDate = date + " " + time
                            var dateF =
                                selectedFrom!!.split(" ").get(0)
                            selectedFrom = dateF + " " + time
                            try {
                                var simpp = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                dateDay = simpp.parse(selectedFrom)
                                changeTo()
                            } catch (ex: java.lang.Exception) {

                            }
                        }, hour, minute, true
                    ) //Yes 24 hour time
                    timePickerDialog.show()
                        }
                    }
                } else {
                    // TODO Auto-generated method stub
                    val mcurrentTime = Calendar.getInstance()
                    val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                    val minute = mcurrentTime[Calendar.MINUTE]
                    val now = mcurrentTime.time
                    val timePickerDialog = TimePickerDialog(
                        this, R.style.DatePickerDialog,
                        { timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int ->

                            fromHour = selectedHour
                            fromMin = selectedMinute

                            var time = selectedHour.toString()
                                .formatNumber("00") + ":" + selectedMinute.toString()
                                .formatNumber("00")
                            etFromTime.text = time.toEditable()
                            timeSelected = true
                            var date =
                                selectedDate!!.split(" ").get(0)
                            selectedDate = date + " " + time
                            var dateF =
                                selectedFrom!!.split(" ").get(0)
                            selectedFrom = dateF + " " + time
                            try {
                                var simpp = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                dateDay = simpp.parse(selectedFrom)
                                changeTo()
                            } catch (ex: java.lang.Exception) {

                            }
                        }, hour, minute, true
                    ) //Yes 24 hour time
                    timePickerDialog.show()
                }
            }
        }


        rlToTime.onOneClick {
            /*val mcurrentTime = Calendar.getInstance()
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
                    )
                    var date = selectedTo!!.split(" ").get(0)
                    selectedTo = date + " " + time

                    if (compareDates() == -1) {
                        if (!compareTimes(selectedHour, selectedMinute)) {
                            timeSelected = false
                            AppHelper.createDialog(this, "You cannot pick a time before selected")
                        } else {
                            etToTime.text = time.toEditable()
                        }
                    } else {
                        etToTime.text = time.toEditable()
                    }

                }, hour, minute, true
            ) //Yes 24 hour time
            timePickerDialog.show()*/
        }
        rlToDate.onOneClick {
            /*if(MyApplication.selectedService!!.availableDates!=null && MyApplication.selectedService!!.availableDates.size > 0){
                   val datePicker =
                       com.wdullaer.materialdatetimepicker.date.DatePickerDialog()

                   datePicker.setAccentColor(AppHelper.getColor(this,R.color.primary))



                   var intArray = dateTimetoDays(MyApplication.selectedService!!.availableDates)
                   limitDatePicker(datePicker,intArray)


                var cal = Calendar.getInstance()
                cal.time = pickedDate
                   datePicker.minDate = cal
                   var x = datePicker.disabledDays
                   datePicker.onDateSetListener= this
                   editer = "to"

                   datePicker.show(supportFragmentManager!!,"")
               }else {*/
            /*var mYear = 0
            var mMonth = 0
            var mDay = 0
            var mcurrentDate: Date? = null
            var cal = Calendar.getInstance()
            if (!etToDate.text.isNullOrEmpty()) {
                mcurrentDate = getFormatter.parse(selectedTo)
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
                    var date = getFormatter.format(myCalendar.time)
                    selectedTo = date
                    etToDate.text = viewFormatter.format(myCalendar.time).toEditable()
                }, mYear, mMonth, mDay
            )
            var call = Calendar.getInstance()
            call.time = pickedDate
            call.add(Calendar.DAY_OF_MONTH,1)
            pickedDate = call.time


            mDatePicker.datePicker.minDate = pickedDate!!.time
            mDatePicker.show()*/
            // }
        }

        llAddresses.onOneClick {
            startActivityForResult(
                Intent(this, ActivitySelectAddress::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
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


    fun setDataAddr() {
        tvSelectedAddressCheck.text = MyApplication.myAddress
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            val extras = data!!.extras
            if (extras != null) {
                //  tvSelectedAddressCheck.setColorTypeface(this,R.color.gray_font_title,address!!,false)
                locationSelected = true
                try {
                    var lat = extras.getDouble("lat")
                    var long = extras.getDouble("long")
                    latLng = LatLng(lat, long)
                    setDataAddr()
                } catch (ex: Exception) {
                }
            }

        }

    }


    fun setUpCategoryRepeat() {

        setUpRenting()
        if (MyApplication.selectedOrder!!.product!!.availableDates != null && MyApplication.selectedOrder!!.product!!.availableDates.size > 0) {
            var cal = Calendar.getInstance()
            selectedDayId = cal.get(Calendar.DAY_OF_WEEK)
            var dayName = dayIdtoString(cal.get(Calendar.DAY_OF_WEEK))
            var thisDay = MyApplication.selectedOrder!!.product!!.availableDates.find {
                it.day.equals(
                    dayName,
                    true
                )
            }
            var isInRange = true
            if(thisDay!=null){

                var times :  ArrayList<TimeDuration> = arrayListOf()
                try{
                    times.addAll(thisDay.time)
                    for(item in times){
                        var fromTime = fullTimeFormatter.parse(item.from)
                        var toTime = fullTimeFormatter.parse(item.to)
                        var calFrom = Calendar.getInstance()
                        var calTo = Calendar.getInstance()
                        calFrom.time = fromTime
                        calTo.time = toTime

                        if ((cal.timeInMillis > calFrom.timeInMillis && cal.timeInMillis < calTo.timeInMillis)) {
                            isInRange = false
                            break
                        }else{
                            isInRange = true
                        }
                    }



                }catch (ex:Exception){}

            }else{
                isInRange = true
            }

            if(isInRange){
                setUpCurr()
                etFromTime.isEnabled = false
                etFromDate.isEnabled = false
                rlFromDate.onOneClick {

                }
                rlFromTime.onOneClick {

                }
            }else{
                etFromDate.text.clear()
                etFromTime.text.clear()
                rbNow.isEnabled = false
                rbSpecify.isChecked = true
            }
            selectedDayId = -1

            rlNUmDays.onOneClick {

                if (!etFromDate.text.toString().isNullOrEmpty() && !etFromTime.text.toString()
                        .isNullOrEmpty()
                )
                    numDaysDialog()
                else
                    toast(AppHelper.getRemoteString("you_must_fill_from", this))
            }
            rlFromDate.onOneClick {
                val datePicker =
                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog()

                datePicker.setAccentColor(AppHelper.getColor(this, R.color.primary))


                var intArray =
                    dateTimetoDays(MyApplication.selectedOrder!!.product!!.availableDates)
                limitDatePicker(datePicker, intArray)


                datePicker.minDate = Calendar.getInstance()
                var x = datePicker.disabledDays
                datePicker.onDateSetListener = this
                editer = "from"

                datePicker.show(supportFragmentManager!!, "")

            }

            rlFromTime.onOneClick {

                if (MyApplication.selectedService!!.availableDates != null && MyApplication.selectedService!!.availableDates.size > 0) {
                    if (selectedDayId!! == -1) {
                        toast(AppHelper.getRemoteString("need_specific_date", this))
                    } else {
                        var day = dayIdtoString(selectedDayId!!)
                        try {
                            myTimes.clear()
                                myTimes.addAll(
                                MyApplication.selectedService!!.availableDates.find {
                                    it.day.equals(
                                        day,
                                        true
                                    )
                                }!!.time
                            )
                        }catch (ex:Exception){
                            myTimes.clear()
                        }
                        if(myTimes.size > 0 )
                            timeDialog(myTimes)
                        else{
                             val mcurrentTime = Calendar.getInstance()
                    val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                    val minute = mcurrentTime[Calendar.MINUTE]
                    val now = mcurrentTime.time
                    val timePickerDialog = TimePickerDialog(
                        this, R.style.DatePickerDialog,
                        { timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int ->

                            fromHour = selectedHour
                            fromMin = selectedMinute
                            timeSelected = true
                            var time = selectedHour.toString()
                                .formatNumber("00") + ":" + selectedMinute.toString()
                                .formatNumber("00")
                            etFromTime.text = time.toEditable()
                            var date =
                                selectedDate!!.split(" ").get(0)
                            selectedDate = date + " " + time
                            var dateF =
                                selectedFrom!!.split(" ").get(0)
                            selectedFrom = dateF + " " + time
                            timeSelected = true
                            try {
                                var simpp = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                dateDay = simpp.parse(selectedFrom)
                                changeTo()
                            } catch (ex: Exception) {

                            }
                        }, hour, minute, true
                    ) //Yes 24 hour time
                    timePickerDialog.show()
                        }
                    }
                } else {
                    // TODO Auto-generated method stub
                    val mcurrentTime = Calendar.getInstance()
                    val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                    val minute = mcurrentTime[Calendar.MINUTE]
                    val now = mcurrentTime.time
                    val timePickerDialog = TimePickerDialog(
                        this, R.style.DatePickerDialog,
                        { timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int ->

                            fromHour = selectedHour
                            fromMin = selectedMinute
                            timeSelected = true
                            var time = selectedHour.toString()
                                .formatNumber("00") + ":" + selectedMinute.toString()
                                .formatNumber("00")
                            etFromTime.text = time.toEditable()
                            var date =
                                selectedDate!!.split(" ").get(0)
                            selectedDate = date + " " + time
                            var dateF =
                                selectedFrom!!.split(" ").get(0)
                            selectedFrom = dateF + " " + time
                            timeSelected = true
                            try {
                                var simpp = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                dateDay = simpp.parse(selectedFrom)
                                changeTo()
                            } catch (ex: Exception) {

                            }
                        }, hour, minute, true
                    ) //Yes 24 hour time
                    timePickerDialog.show()
                }
            }
        } else {
            setUpCurr()
        }
        //   setUpRenting()
        //  setListeners()
        //   setPaymentMethods()
        //  setSpinnerData()

    }


    fun checkDatesService():Boolean{
        for(item in MyApplication.selectedService!!.availableDates){
            if(item.time== null || item.time!!.size == 0)
                return false
        }

        return true
    }

    fun setUpCategory() {
        if (MyApplication.selectedService!!.typeId!!.equals(MyApplication.categories.find {
                it.valEn!!.lowercase().equals("rental")
            }!!.id!!.toInt())) {
            tvFromTitle.show()
            tvToTitle.show()
            llToLayout.show()
            llNumDays.show()
        } else {
            tvFromTitle.hide()
            tvToTitle.hide()
            llToLayout.hide()
            llNumDays.hide()
        }

        if (MyApplication.selectedService!!.availableDates != null && MyApplication.selectedService!!.availableDates.size > 0 ) {
            var cal = Calendar.getInstance()
            //  selectedDayId = cal.get(Calendar.DAY_OF_WEEK)
            var dayName = dayIdtoString(cal.get(Calendar.DAY_OF_WEEK))
            var thisDay =
                MyApplication.selectedService!!.availableDates.find { it.day.equals(dayName, true) }
            var isInRange = true
            if(thisDay!=null){

                var times :  ArrayList<TimeDuration> = arrayListOf()
                try{
                    times.addAll(thisDay.time)
                    for(item in times){
                        var fromTime = fullTimeFormatter.parse(item.from)
                        var toTime = fullTimeFormatter.parse(item.to)
                        var calFrom = Calendar.getInstance()
                        var calTo = Calendar.getInstance()
                        calFrom.time = fromTime
                        calTo.time = toTime

                        if ((cal.timeInMillis > calFrom.timeInMillis && cal.timeInMillis < calTo.timeInMillis)) {
                            isInRange = false
                            break
                        }else{
                            isInRange = true
                        }
                    }



                }catch (ex:Exception){}

            }else{
                isInRange = true
            }

            if(isInRange){
                setUpCurr()
                etFromTime.isEnabled = false
                etFromDate.isEnabled = false
                rlFromDate.onOneClick {

                }
                rlFromTime.onOneClick {

                }
                    }else{
                        etFromDate.text.clear()
                        etFromTime.text.clear()
                        rbNow.isEnabled = false
                        rbSpecify.isChecked = true
                    }
            selectedDayId = -1
            rlFromDate.onOneClick {
                val datePicker =
                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog()

                datePicker.setAccentColor(AppHelper.getColor(this, R.color.primary))


                var intArray =
                    dateTimetoDays(MyApplication.selectedService!!.availableDates)
                limitDatePicker(datePicker, intArray)


                datePicker.minDate = Calendar.getInstance()
                var x = datePicker.disabledDays
                datePicker.onDateSetListener = this
                editer = "from"

                datePicker.show(supportFragmentManager!!, "")
            }

            rlNUmDays.onOneClick {

                if (!etFromDate.text.toString().isNullOrEmpty() && !etFromTime.text.toString()
                        .isNullOrEmpty()
                )
                    numDaysDialog()
                else
                    toast(AppHelper.getRemoteString("you_must_fill_from", this))
            }
            rlFromTime.onOneClick {


                if (MyApplication.selectedService!!.availableDates != null && MyApplication.selectedService!!.availableDates.size > 0) {


                    if (MyApplication.selectedService!!.availableDates != null && MyApplication.selectedService!!.availableDates.size > 0) {
                        if (selectedDayId!! == -1) {
                            toast(AppHelper.getRemoteString("need_specific_date", this))
                        } else {
                            var day = dayIdtoString(selectedDayId!!)
                            try {
                                myTimes.clear()
                                myTimes.addAll(
                                    MyApplication.selectedService!!.availableDates.find {
                                        it.day.equals(
                                            day,
                                            true
                                        )
                                    }!!.time)
                            }catch (ex:Exception){
                                myTimes = arrayListOf()
                            }
                            if(myTimes.size > 0 ) {
                                timeDialog(myTimes)
                            }else{
                                 val mcurrentTime = Calendar.getInstance()
                        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                        val minute = mcurrentTime[Calendar.MINUTE]
                        val now = mcurrentTime.time
                        val timePickerDialog = TimePickerDialog(
                            this, R.style.DatePickerDialog,
                            { timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int ->

                                fromHour = selectedHour
                                fromMin = selectedMinute
                                timeSelected = true
                                var time = selectedHour.toString()
                                    .formatNumber("00") + ":" + selectedMinute.toString()
                                    .formatNumber("00")
                                etFromTime.text = time.toEditable()
                                var date =
                                    selectedDate!!.split(" ").get(0)
                                selectedDate = date + " " + time
                                var dateF =
                                    selectedFrom!!.split(" ").get(0)
                                selectedFrom = dateF + " " + time
                                try {
                                    var simpp = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                    dateDay = simpp.parse(selectedFrom)
                                    changeTo()
                                } catch (ex: java.lang.Exception) {

                                }

                            }, hour, minute, true
                        ) //Yes 24 hour time
                        timePickerDialog.show()
                            }
                        }
                    } else {
                        // TODO Auto-generated method stub
                        val mcurrentTime = Calendar.getInstance()
                        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                        val minute = mcurrentTime[Calendar.MINUTE]
                        val now = mcurrentTime.time
                        val timePickerDialog = TimePickerDialog(
                            this, R.style.DatePickerDialog,
                            { timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int ->

                                fromHour = selectedHour
                                fromMin = selectedMinute
                                timeSelected = true
                                var time = selectedHour.toString()
                                    .formatNumber("00") + ":" + selectedMinute.toString()
                                    .formatNumber("00")
                                etFromTime.text = time.toEditable()
                                var date =
                                    selectedDate!!.split(" ").get(0)
                                selectedDate = date + " " + time
                                var dateF =
                                    selectedFrom!!.split(" ").get(0)
                                selectedFrom = dateF + " " + time
                                try {
                                    var simpp = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                    dateDay = simpp.parse(selectedFrom)
                                    changeTo()
                                } catch (ex: java.lang.Exception) {

                                }

                            }, hour, minute, true
                        ) //Yes 24 hour time
                        timePickerDialog.show()
                    }
                }
                else {
                    // TODO Auto-generated method stub
                    val mcurrentTime = Calendar.getInstance()
                    val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                    val minute = mcurrentTime[Calendar.MINUTE]
                    val now = mcurrentTime.time
                    val timePickerDialog = TimePickerDialog(
                        this, R.style.DatePickerDialog,
                        { timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int ->

                            fromHour = selectedHour
                            fromMin = selectedMinute
                            timeSelected = true
                            var time = selectedHour.toString()
                                .formatNumber("00") + ":" + selectedMinute.toString()
                                .formatNumber("00")
                            etFromTime.text = time.toEditable()
                            var date =
                                selectedDate!!.split(" ").get(0)
                            selectedDate = date + " " + time
                        }, hour, minute, true
                    ) //Yes 24 hour time
                    timePickerDialog.show()
                }
            }
        } else {
            setUpCurr()
        }
        setOrderSummary()
        setListeners()
        setPaymentMethods()
        if (MyApplication.selectedService!!.serviceReasons != null && MyApplication.selectedService!!.serviceReasons.size > 0) {
            llSpReason.show()
            setSpinnerData()
        } else {
            llSpReason.hide()
        }

    }

    fun setSpinnerRepeat() {
        var arraySpinnerTypes: ArrayList<ItemSpinner> = arrayListOf()
        for (item in MyApplication.selectedOrder!!.product!!.serviceReasons) {
            if (MyApplication.languageCode == AppConstants.LANG_ENGLISH) {
                arraySpinnerTypes.add(ItemSpinner(item.id!!.toInt(), item.metaValueEn!!, ""))
            } else {
                arraySpinnerTypes.add(ItemSpinner(item.id!!.toInt(), item.metaValueAr!!, ""))
            }
        }
        arraySpinnerTypes.add(0, ItemSpinner(0,AppHelper.getRemoteString("please__select",this),""))
      //  arraySpinnerTypes.add(ItemSpinner(0, AppHelper.getRemoteString("other", this), ""))
        val adapterTypes =
            AdapterGeneralSpinner(this, R.layout.spinner_layout, arraySpinnerTypes, 0)
        spServiceReason.adapter = adapterTypes
        //reasonId = arraySpinnerTypes.get(0).id
        adapterTypes.setDropDownViewResource(R.layout.item_spinner_drop_down)
        spServiceReason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                if (arraySpinnerTypes.get(position).id == MyApplication.otherId) {
                    reasonId = arraySpinnerTypes.get(position).id
                    llServiceReason.show()
                } else {
                    llServiceReason.hide()
                    reasonId = arraySpinnerTypes.get(position).id

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }

    fun setSpinnerData() {
        var arraySpinnerTypes: ArrayList<ItemSpinner> = arrayListOf()
        for (item in MyApplication.selectedService!!.serviceReasons) {
            var price = item.reasonPrice
            if(price.isNullOrEmpty())
                price = "0 KWD"
            if (MyApplication.languageCode == AppConstants.LANG_ENGLISH) {
                arraySpinnerTypes.add(ItemSpinner(item.id!!.toInt(), item.metaValueEn!!/*+"-"+price*/, ""))
            } else {
                arraySpinnerTypes.add(ItemSpinner(item.id!!.toInt(), item.metaValueAr!!/*+"-"+price*/, ""))
            }
        }
       // reasonId = arraySpinnerTypes.get(0).id

        arraySpinnerTypes.add(0, ItemSpinner(0,AppHelper.getRemoteString("please__select",this),""))
       // arraySpinnerTypes.add(ItemSpinner(0, AppHelper.getRemoteString("other", this), ""))
        val adapterTypes =
            AdapterGeneralSpinner(this, R.layout.spinner_layout, arraySpinnerTypes, 0)
        spServiceReason.adapter = adapterTypes
        adapterTypes.setDropDownViewResource(R.layout.item_spinner_drop_down)
        spServiceReason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                if (arraySpinnerTypes.get(position).name!!.contains("other",true)) {
                    reasonId = arraySpinnerTypes.get(position).id
                    llServiceReason.show()
                } else{
                    llServiceReason.hide()
                    reasonId = arraySpinnerTypes.get(position).id

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }


    private fun setPaymentMethods() {
        if (MyApplication.selectedService!!.availablePaymentMethods.size > 0) {
            /*MyApplication.selectedService!!.availablePaymentMethods.get(0).selected = true
            selectedPaymentId =
                MyApplication.selectedService!!.availablePaymentMethods.get(0).id!!.toInt()*/
            adapterPaymentMethods = AdapterCheckoutPayment(
                MyApplication.selectedService!!.availablePaymentMethods!!,
                this,
                this
            )
            if (MyApplication.selectedService!!.availablePaymentMethods.size == 1) {
                MyApplication.selectedService!!.availablePaymentMethods!!.get(0).selected = true
                selectedPaymentId =
                    MyApplication.selectedService!!.availablePaymentMethods!!.get(0).id!!.toInt()
            }
            rvPaymentMethodCheckout.layoutManager = GridLayoutManager(this, 1)
            rvPaymentMethodCheckout.adapter = adapterPaymentMethods
            rvPaymentMethodCheckout.isNestedScrollingEnabled = false
        } else {
            tvPaymentMethodTitleCheckout.hide()
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

       /* var dates : ArrayList<ServiceAvailableDateTime> = arrayListOf()
        var times : ArrayList<TimeDuration> = arrayListOf()
        times.add(TimeDuration("12:00:00","14:00:00"))
        times.add(TimeDuration("15:00:00","16:00:00"))
        times.add(TimeDuration("18:00:00","20:00:00"))

        dates.add(ServiceAvailableDateTime("Monday",times))
        dates.add(ServiceAvailableDateTime("Thursday", arrayListOf()))
        try {
            if(MyApplication.renewing || MyApplication.repeating){
                MyApplication.selectedOrder!!.product!!.availableDates.clear()
                MyApplication.selectedOrder!!.product!!.availableDates.addAll(dates)
            }else {
                MyApplication.selectedService!!.availableDates.clear()
                MyApplication.selectedService!!.availableDates.addAll(dates)
            }
        }catch (ex : Exception){

            if(MyApplication.renewing || MyApplication.repeating){
                MyApplication.selectedOrder!!.product!!.availableDates = dates
            }else {
                MyApplication.selectedService!!.availableDates = dates
            }

        }*/

        var ct = 1
        while (ct <= MyApplication.maxDay!!) {
            arrayDays.add(ct.toString())
            ct++
        }

        tvRestrictedCHeckout.typeface = AppHelper.getTypefaceBoldItalic(this)

        if (!MyApplication.renewing && !MyApplication.repeating) {
            if (MyApplication.selectedService!!.availablePaymentMethods == null || MyApplication.selectedService!!.availablePaymentMethods.size == 0) {
                btPlaceOrder.hide()
                noPaymentMethods.show()
            } else {
                btPlaceOrder.show()
                noPaymentMethods.hide()
            }
            if (MyApplication.selectedService!!.availableDates != null && MyApplication.selectedService!!.availableDates!!.size > 0) {
                tvRestrictedCHeckout.show()
            } else {
                tvRestrictedCHeckout.hide()
            }
            if (MyApplication.selectedService != null) {
                if (MyApplication.categories.size > 0) {
                    setUpCategory()
                } else {
                    loading.show()
                    CallAPIs.getCategories(this, this)
                }
            } else {
                logw("rebirth", "done")
                AppHelper.triggerRebirth(this)
            }
        }
        else if (MyApplication.renewing) {
            /*if(MyApplication.selectedOrder!!.product!!.availablePaymentMethods== null || MyApplication.selectedOrder!!.product!!.availablePaymentMethods.size == 0 ){
                btPlaceOrder.hide()
                noPaymentMethods.show()
            }else{
                btPlaceOrder.show()
                noPaymentMethods.hide()
            }*/

            llSpReason.hide()
            /*if (MyApplication.selectedOrder!!.product!!.serviceReasons == null || MyApplication.selectedOrder!!.product!!.serviceReasons.size == 0) {
                llSpReason.hide()
            } else {
                llSpReason.show()
            }*/
            tvFromTitle.show()
            tvToTitle.show()
            llToLayout.show()
            setUpRenting()
            llAddresses.isEnabled = false

            if (MyApplication.selectedOrder!!.product!!.availableDates != null && MyApplication.selectedOrder!!.product!!.availableDates.size > 0) {
                tvRestrictedCHeckout.show()
            } else {
                tvRestrictedCHeckout.hide()
            }

            rlNUmDays.onOneClick {

                if (!etFromDate.text.toString().isNullOrEmpty() && !etFromTime.text.toString()
                        .isNullOrEmpty()
                )
                    numDaysDialog()
                else
                    toast(AppHelper.getRemoteString("you_must_fill_from", this))
            }

            var current = Calendar.getInstance()

            var from: Date? = null
            var to: Date? = null
            try {
                from =
                    mainFormatter.parse(MyApplication.selectedOrder!!.product!!.booking_start_date)
            } catch (ex: Exception) {
                try {
                    from =
                        sendFormatter.parse(MyApplication.selectedOrder!!.product!!.booking_start_date)
                } catch (ex: Exception) {
                    try {
                        from =
                            lastFormatter.parse(MyApplication.selectedOrder!!.product!!.booking_start_date)
                    } catch (ex: Exception) {
                        from = Calendar.getInstance().time
                    }
                }

            }
            try {
                to = mainFormatter.parse(MyApplication.selectedOrder!!.product!!.booking_end_date)
            } catch (ex: Exception) {
                try {
                    to =
                        sendFormatter.parse(MyApplication.selectedOrder!!.product!!.booking_end_date)
                } catch (ex: Exception) {
                    try {
                        to =
                            lastFormatter.parse(MyApplication.selectedOrder!!.product!!.booking_end_date)
                    } catch (ex: Exception) {
                        to = Calendar.getInstance().time
                    }
                }

            }
            /* var from = simp.parse("2021-11-18")
             var to = simp.parse("2021-11-21")*/

            minRenewTime = to
            minRenewTo = to

            /*if (current.time.time < from!!.time || current.time.time > from!!.time && current.time.time < to!!.time) {
                minRenewTime = from
                minRenewTo = to
            } else {
                minRenewTime = current.time
                minRenewTo = current.time
            }*/

            dateDay = minRenewTime
            selectedFrom = getFormatter.format(minRenewTime)
            selectedTo = getFormatter.format(minRenewTo)

            //  var x = mainFormatter.parse(MyApplication.selectedOrder!!.product!!.booking_end_date)
            // var y = viewFormatter.format(x)
            etFromDate.text = main2Formatter.format(minRenewTime).toEditable()
            timeSelected = true
            try {
                etFromTime.text =
                    timeFormatter.format(mainFormatter.parse(MyApplication.selectedOrder!!.product!!.booking_end_date))
                        .toEditable()
            } catch (ex: Exception) {
                try {
                    etFromTime.text =
                        timeFormatter.format(main2Formatter.parse(MyApplication.selectedOrder!!.product!!.booking_end_date))
                            .toEditable()
                } catch (ex: Exception) {

                }
            }
            etToTime.text.clear()
            etToDate.text.clear()

            etFromDate.isEnabled = false
            etFromTime.isEnabled = false
            if (MyApplication.selectedOrder!!.product!!.availablePaymentMethods.size > 0) {
                adapterPaymentMethods = AdapterCheckoutPayment(
                    MyApplication.selectedOrder!!.product!!.availablePaymentMethods,
                    this,
                    this
                )
                if (MyApplication.selectedOrder!!.product!!.availablePaymentMethods.size == 1) {
                    MyApplication.selectedOrder!!.product!!.availablePaymentMethods!!.get(0).selected =
                        true
                    selectedPaymentId =
                        MyApplication.selectedOrder!!.product!!.availablePaymentMethods!!.get(0).id!!.toInt()
                }
                rvPaymentMethodCheckout.layoutManager = GridLayoutManager(this, 1)
                rvPaymentMethodCheckout.adapter = adapterPaymentMethods
                rvPaymentMethodCheckout.isNestedScrollingEnabled = false
            } else {
                tvPaymentMethodTitleCheckout.hide()
            }

            /*rlFromDate.onOneClick {
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
                        pickedDate = myCalendar.time
                        var date = sendFormatter.format(myCalendar.time)
                        etFromDate.text = date.toEditable()
                        if (compareDates() == 1) {
                            selectedTo = date
                            etToDate.text = viewFormatter.format(myCalendar.time).toEditable()
                        }
                        var x = selectedDate
                        var time = selectedDate!!.split(" ").get(3)
                        selectedDate = etFromDate.text.toString() + " " + time
                        selectedFrom = etFromDate.text.toString()
                        etFromDate.text = viewFormatter.format(myCalendar.time).toEditable()
                    }, mYear, mMonth, mDay
                )
                mDatePicker.datePicker.minDate = cal.time.time
                mDatePicker.show()
            }*/
            /*rlFromTime.onOneClick {
                // TODO Auto-generated method stub
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val now = mcurrentTime.time
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
                        if (!selectedDate.isNullOrEmpty()) {
                            var date =
                                selectedDate!!.split(" ").get(0)
                            selectedDate = date + " " + time
                        } else {
                            selectedDate = time
                        }

                    }, hour, minute, true
                ) //Yes 24 hour time
                timePickerDialog.show()
            }*/

            /*   if(MyApplication.selectedOrder!!.product!!.serviceReasons!=null && MyApplication.selectedOrder!!.product!!.serviceReasons.size > 0){
                   setSpinnerRepeat()
                   llSpReason.show()
               }else{
                   llSpReason.hide()
               }

               if(MyApplication.selectedOrder!!.product!!.availableDates!=null && MyApplication.selectedOrder!!.product.availableDates.size > 0){

               }
   */
            rlToTime.onOneClick {
                /*val mcurrentTime = Calendar.getInstance()
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
                        )
                        var date = selectedTo!!.split(" ").get(0)
                        selectedTo = date + " " + time
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
                ) //Yes 24 hour time*/
                //timePickerDialog.show()
            }
            rlToDate.onOneClick {
                /* var mYear = 0
                 var mMonth = 0
                 var mDay = 0
                 var mcurrentDate: Date? = null
                 var cal = Calendar.getInstance()
                 if (!etToDate.text.isNullOrEmpty()) {
                     mcurrentDate = sendFormatter.parse(selectedTo)
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
                         var date = getFormatter.format(myCalendar.time)
                         selectedTo = date
                         etToDate.text = viewFormatter.format(myCalendar.time).toEditable()
                     }, mYear, mMonth, mDay
                 )
                 var call = Calendar.getInstance()
                 call.time = minRenewTo
                 call.add(Calendar.DAY_OF_MONTH,1)
                 minRenewTo = call.time
                 mDatePicker.datePicker.minDate = minRenewTo!!.time
                 mDatePicker.show()*/
            }

            var addr = ""
            if (!MyApplication.selectedOrder!!.shipping_province.isNullOrEmpty())
                addr = addr + MyApplication.selectedOrder!!.shipping_province + ","
            if (!MyApplication.selectedOrder!!.shipping_area.isNullOrEmpty())
                addr = addr + MyApplication.selectedOrder!!.shipping_area + ","
            if (!MyApplication.selectedOrder!!.shipping_block.isNullOrEmpty())
                addr = addr + MyApplication.selectedOrder!!.shipping_block + ","
            if (!MyApplication.selectedOrder!!.shipping_address_street.isNullOrEmpty())
                addr = addr + MyApplication.selectedOrder!!.shipping_address_street

            tvSelectedAddressCheck.text = addr
            btPlaceOrder.text = AppHelper.getRemoteString("renew_order", this)
            btPlaceOrder.onOneClick {
                if(selectedNumDays) {
                    if (selectedPaymentId != -1) {
                        try {
                            if (AppHelper.isOnline(this)) {
                                renewOrder()
                            } else {
                                AppHelper.createDialog(
                                    this,
                                    AppHelper.getRemoteString("no_internet", this)
                                )
                            }

                        } catch (ex: Exception) {
                            Log.wtf("renewError", ex.toString())
                            loading.hide()
                        }
                    } else {
                        toast(AppHelper.getRemoteString("select_payment_method", this))
                    }
                }else{
                    toast(AppHelper.getRemoteString("pleaseSelectNumberOfDays", this))
                }
            }
             /*etFromDate.text.clear()
                    etFromTime.text.clear()*/
            rbNow.isEnabled = false
            rbSpecify.isEnabled = false
        } else {


            rlNUmDays.onOneClick {

                if (!etFromDate.text.toString().isNullOrEmpty() && !etFromTime.text.toString()
                        .isNullOrEmpty()
                )
                    numDaysDialog()
                else
                    toast(AppHelper.getRemoteString("you_must_fill_from", this))
            }

            if (MyApplication.selectedOrder!!.typeId!!.equals(MyApplication.categories.find {
                    it.valEn!!.lowercase().equals("rental")
                }!!.id!!.toInt())) {
                tvFromTitle.show()
                tvToTitle.show()
                llToLayout.show()
            } else {
                tvFromTitle.hide()
                tvToTitle.hide()
                llNumDays.hide()
                llToLayout.hide()
            }


            if (MyApplication.selectedOrder!!.product!!.availablePaymentMethods == null || MyApplication.selectedOrder!!.product!!.availablePaymentMethods.size == 0) {
                btPlaceOrder.hide()
                noPaymentMethods.show()
            } else {
                btPlaceOrder.show()
                noPaymentMethods.hide()
            }

            if (MyApplication.selectedOrder!!.product!!.availableDates != null && MyApplication.selectedOrder!!.product!!.availableDates.size > 0) {
                tvRestrictedCHeckout.show()
            } else {
                tvRestrictedCHeckout.hide()
            }

            var addr = ""
            if (!MyApplication.selectedOrder!!.shipping_province.isNullOrEmpty())
                addr = addr + MyApplication.selectedOrder!!.shipping_province + ","
            if (!MyApplication.selectedOrder!!.shipping_area.isNullOrEmpty())
                addr = addr + MyApplication.selectedOrder!!.shipping_area + ","
            if (!MyApplication.selectedOrder!!.shipping_block.isNullOrEmpty())
                addr = addr + MyApplication.selectedOrder!!.shipping_block + ","
            if (!MyApplication.selectedOrder!!.shipping_address_street.isNullOrEmpty())
                addr = addr + MyApplication.selectedOrder!!.shipping_address_street

            tvSelectedAddressCheck.text = addr
            btPlaceOrder.text = AppHelper.getRemoteString("RepeatOrder", this)


            rlToTime.onOneClick {
                /* val mcurrentTime = Calendar.getInstance()
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
                         )
                         var date = selectedTo!!.split(" ").get(0)
                         selectedTo = date + " " + time
                         if (compareDates() == -1) {
                             if (!compareTimes(selectedHour, selectedMinute)) {
                                 timeSelected = false
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
                 timePickerDialog.show()*/
            }
            rlToDate.onOneClick {
                /*if(MyApplication.selectedService!!.availableDates!=null && MyApplication.selectedService!!.availableDates.size > 0){
                       val datePicker =
                           com.wdullaer.materialdatetimepicker.date.DatePickerDialog()

                       datePicker.setAccentColor(AppHelper.getColor(this,R.color.primary))



                       var intArray = dateTimetoDays(MyApplication.selectedService!!.availableDates)
                       limitDatePicker(datePicker,intArray)


                    var cal = Calendar.getInstance()
                    cal.time = pickedDate
                       datePicker.minDate = cal
                       var x = datePicker.disabledDays
                       datePicker.onDateSetListener= this
                       editer = "to"

                       datePicker.show(supportFragmentManager!!,"")
                   }else {*/
                /*var mYear = 0
                var mMonth = 0
                var mDay = 0
                var mcurrentDate: Date? = null
                var cal = Calendar.getInstance()
                if (!etToDate.text.isNullOrEmpty()) {
                    mcurrentDate = sendFormatter.parse(selectedTo)
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
                        var date = getFormatter.format(myCalendar.time)
                        selectedTo = date
                        etToDate.text = viewFormatter.format(myCalendar.time).toEditable()
                    }, mYear, mMonth, mDay
                )
                var call = Calendar.getInstance()
                call.time = pickedDate
                call.add(Calendar.DAY_OF_MONTH,1)
                pickedDate = call.time
                mDatePicker.datePicker.minDate = pickedDate!!.time
                mDatePicker.show()*/
                // }
            }

            setUpCategoryRepeat()
            llAddresses.onOneClick {
                startActivityForResult(
                    Intent(this, ActivitySelectAddress::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                    REQUEST_LOCATION
                )
            }
            rlNUmDays.onOneClick {

                if (!etFromDate.text.toString().isNullOrEmpty() && !etFromTime.text.toString()
                        .isNullOrEmpty()
                )
                    numDaysDialog()
                else
                    toast(AppHelper.getRemoteString("you_must_fill_from", this))
            }
            rbNow.onOneClick {
                /*selectedDayId = -1*/
              /*  if (MyApplication.selectedOrder!!.product!!.availableDates != null && MyApplication.selectedOrder!!.product!!.availableDates.size > 0) {
                    var cal = Calendar.getInstance()
                    selectedDayId = cal.get(Calendar.DAY_OF_WEEK)
                    var dayName = dayIdtoString(cal.get(Calendar.DAY_OF_WEEK))
                    var thisDay = MyApplication.selectedOrder!!.product!!.availableDates.find {
                        it.day.equals(
                            dayName,
                            true
                        )
                    }
                    var isInRange = true
                    if(thisDay!=null){

                        var times :  ArrayList<TimeDuration> = arrayListOf()
                        try{
                            times.addAll(thisDay.time)
                            for(item in times){
                                var fromTime = fullTimeFormatter.parse(item.from)
                                var toTime = fullTimeFormatter.parse(item.to)
                                var calFrom = Calendar.getInstance()
                                var calTo = Calendar.getInstance()
                                calFrom.time = fromTime
                                calTo.time = toTime

                                if ((cal.timeInMillis > calFrom.timeInMillis && cal.timeInMillis < calTo.timeInMillis)) {
                                    isInRange = false
                                    break
                                }else{
                                    isInRange = true
                                }
                            }



                        }catch (ex:Exception){}

                    }else{
                        isInRange = true
                    }
                      if(isInRange){
                          etFromTime.isEnabled = false
                          etFromDate.isEnabled = false
                          rlFromDate.onOneClick {

                          }
                          rlFromTime.onOneClick {

                          }
                    }else{
                        etFromDate.text.clear()
                        etFromTime.text.clear()
                        rbNow.isEnabled = false
                        rbSpecify.isChecked = true
                    }
                    selectedDayId = -1
                    rlFromDate.onOneClick {
                        val datePicker =
                            com.wdullaer.materialdatetimepicker.date.DatePickerDialog()

                        datePicker.setAccentColor(AppHelper.getColor(this, R.color.primary))


                        var intArray =
                            dateTimetoDays(MyApplication.selectedService!!.availableDates)
                        limitDatePicker(datePicker, intArray)


                        datePicker.minDate = Calendar.getInstance()
                        var x = datePicker.disabledDays
                        datePicker.onDateSetListener = this
                        editer = "from"

                        datePicker.show(supportFragmentManager!!, "")

                        rlFromTime.onOneClick {
                            if (selectedDayId!! == -1) {
                                toast(getString(R.string.need_specific_date))
                            } else {
                                var day = dayIdtoString(selectedDayId!!)
                                try {
                                    myTimes.clear()
                                myTimes.addAll(
                                        MyApplication.selectedOrder!!.product!!.availableDates.find {
                                            it.day.equals(
                                                day,
                                                true
                                            )
                                        }!!.time
                                    )
                                }catch (ex:Exception){
                                    myTimes.clear()
                                }
                                if (myTimes.size > 0) {
                                    timeDialog(myTimes)
                                } else {
                                    // TODO Auto-generated method stub
                                    val mcurrentTime = Calendar.getInstance()
                                    val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                                    val minute = mcurrentTime[Calendar.MINUTE]
                                    val now = mcurrentTime.time
                                    val timePickerDialog = TimePickerDialog(
                                        this, R.style.DatePickerDialog,
                                        { timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int ->

                                            fromHour = selectedHour
                                            fromMin = selectedMinute

                                            var time = selectedHour.toString()
                                                .formatNumber("00") + ":" + selectedMinute.toString()
                                                .formatNumber("00")
                                            etFromTime.text = time.toEditable()
                                            timeSelected = true
                                            var date =
                                                selectedDate!!.split(" ").get(0)
                                            selectedDate = date + " " + time
                                            var dateF =
                                                selectedFrom!!.split(" ").get(0)
                                            selectedFrom = dateF + " " + time
                                            try {
                                                var simpp = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                                dateDay = simpp.parse(selectedFrom)
                                                changeTo()
                                            } catch (ex: java.lang.Exception) {

                                            }
                                        }, hour, minute, true
                                    ) //Yes 24 hour time
                                    timePickerDialog.show()
                                }
                            }

                        }
                    }
                } else {*/
                    setUpCurr()
                    rlFromDate.onOneClick {

                    }
                    rlFromTime.onOneClick {

                    }
              //  }



                AppHelper.setTextColor(this, etFromDate, R.color.gray_font_light)
                AppHelper.setTextColor(this, etFromTime, R.color.gray_font_light)
            }

            rlNUmDays.onOneClick {

                if (!etFromDate.text.toString().isNullOrEmpty() && !etFromTime.text.toString()
                        .isNullOrEmpty()
                )
                    numDaysDialog()
                else
                    toast(AppHelper.getRemoteString("you_must_fill_from", this))
            }
            rbSpecify.onOneClick {

                AppHelper.setTextColor(this, etFromDate, R.color.gray_font)
                AppHelper.setTextColor(this, etFromTime, R.color.gray_font)
                rlFromDate.onOneClick {
                    if (MyApplication.selectedOrder!!.product!!.availableDates != null && MyApplication.selectedOrder!!.product!!.availableDates.size > 0) {
                        val datePicker =
                            com.wdullaer.materialdatetimepicker.date.DatePickerDialog()

                        datePicker.setAccentColor(AppHelper.getColor(this, R.color.primary))


                        var intArray =
                            dateTimetoDays(MyApplication.selectedService!!.availableDates)
                        limitDatePicker(datePicker, intArray)


                        datePicker.minDate = Calendar.getInstance()
                        var x = datePicker.disabledDays
                        datePicker.onDateSetListener = this
                        editer = "from"

                        datePicker.show(supportFragmentManager!!, "")
                    } else {
                        var mcurrentDate = getFormatter.parse(selectedFrom)
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
                                pickedDate = myCalendar.time
                                var date = getFormatter.format(myCalendar.time)
                                var dateShow = viewFormatter.format(myCalendar.time)
                                etFromDate.text = date.toEditable()
                                if (compareDates() == 1) {
                                    selectedTo = date
                                    var cal = myCalendar
                                    cal.add(Calendar.DAY_OF_MONTH, 1)
                                    selectedTo = viewFormatter.format(cal.time)
                                    //   etToDate.text = selectedTo!!.toEditable()
                                }
                                etFromTime.text = "".toEditable()
                                etToTime.text = "".toEditable()
                                var x = selectedDate
                                var time = selectedDate!!.split(" ").get(1)
                                selectedDate = etFromDate.text.toString() + " " + time
                                try {
                                    var simpp = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                    dateDay = simpp.parse(selectedDate)
                                    changeTo()
                                } catch (ex: Exception) {

                                }
                                selectedFrom = date
                                etFromDate.text = dateShow.toEditable()
                            }, mYear, mMonth, mDay
                        )
                        mDatePicker.datePicker.minDate = calTime.time.time
                        mDatePicker.show()
                    }
                }
                rlFromTime.onOneClick {

                    if (MyApplication.selectedOrder!!.product!!.availableDates != null && MyApplication.selectedOrder!!.product!!.availableDates.size > 0) {
                        if (selectedDayId!! == -1) {
                            toast(getString(R.string.need_specific_date))
                        } else {
                            var day = dayIdtoString(selectedDayId!!)
                            try {
                                myTimes.clear()
                                myTimes.addAll(
                                    MyApplication.selectedOrder!!.product!!.availableDates.find {
                                        it.day.equals(
                                            day,
                                            true
                                        )
                                    }!!.time
                                )
                                }catch (ex:Exception){
                                myTimes.clear()
                                }
                            if (myTimes.size > 0) {
                                timeDialog(myTimes)
                            } else {
                                val mcurrentTime = Calendar.getInstance()
                                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                                val minute = mcurrentTime[Calendar.MINUTE]
                                val now = mcurrentTime.time
                                val timePickerDialog = TimePickerDialog(
                                    this, R.style.DatePickerDialog,
                                    { timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int ->

                                        fromHour = selectedHour
                                        fromMin = selectedMinute

                                        var time = selectedHour.toString()
                                            .formatNumber("00") + ":" + selectedMinute.toString()
                                            .formatNumber("00")
                                        etFromTime.text = time.toEditable()
                                        timeSelected = true
                                        var date =
                                            selectedDate!!.split(" ").get(0)
                                        selectedDate = date + " " + time
                                        var dateF =
                                            selectedFrom!!.split(" ").get(0)
                                        selectedFrom = dateF + " " + time
                                        try {
                                            var simpp = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                            dateDay = simpp.parse(selectedFrom)
                                            changeTo()
                                        } catch (ex: java.lang.Exception) {

                                        }
                                    }, hour, minute, true
                                ) //Yes 24 hour time
                                timePickerDialog.show()
                            }
                        }
                    } else {
                        // TODO Auto-generated method stub
                        val mcurrentTime = Calendar.getInstance()
                        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                        val minute = mcurrentTime[Calendar.MINUTE]
                        val now = mcurrentTime.time
                        val timePickerDialog = TimePickerDialog(
                            this, R.style.DatePickerDialog,
                            { timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int ->

                                fromHour = selectedHour
                                fromMin = selectedMinute

                                var time = selectedHour.toString()
                                    .formatNumber("00") + ":" + selectedMinute.toString()
                                    .formatNumber("00")
                                etFromTime.text = time.toEditable()
                                timeSelected = true
                                var date =
                                    selectedDate!!.split(" ").get(0)
                                selectedDate = date + " " + time
                                var dateF =
                                    selectedFrom!!.split(" ").get(0)
                                selectedFrom = dateF + " " + time
                                try {
                                    var simpp = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                    dateDay = simpp.parse(selectedFrom)
                                    changeTo()
                                } catch (ex: java.lang.Exception) {

                                }
                            }, hour, minute, true
                        ) //Yes 24 hour time
                        timePickerDialog.show()
                    }
                }
            }
            rlNUmDays.onOneClick {

                if (!etFromDate.text.toString().isNullOrEmpty() && !etFromTime.text.toString()
                        .isNullOrEmpty()
                )
                    numDaysDialog()
                else
                    toast(AppHelper.getRemoteString("you_must_fill_from", this))
            }
            rlFromDate.onOneClick {
                if (MyApplication.selectedOrder!!.product!!.availableDates != null && MyApplication.selectedOrder!!.product!!.availableDates.size > 0) {
                    val datePicker =
                        com.wdullaer.materialdatetimepicker.date.DatePickerDialog()

                    datePicker.setAccentColor(AppHelper.getColor(this, R.color.primary))


                    var intArray =
                        dateTimetoDays(MyApplication.selectedOrder!!.product!!.availableDates)
                    limitDatePicker(datePicker, intArray)


                    datePicker.minDate = Calendar.getInstance()
                    var x = datePicker.disabledDays
                    datePicker.onDateSetListener = this
                    editer = "from"

                    datePicker.show(supportFragmentManager!!, "")
                } else {
                    var mcurrentDate = sendFormatter.parse(selectedFrom)
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
                            pickedDate = myCalendar.time
                            timeSelected = false
                            var date = getFormatter.format(myCalendar.time)
                            var dateShow = viewFormatter.format(myCalendar.time)
                            etFromDate.text = date.toEditable()
                            if (compareDates() == 1) {
                                selectedTo = date
                                var cal = myCalendar
                                cal.add(Calendar.DAY_OF_MONTH, 1)
                                selectedTo = viewFormatter.format(cal.time)
                                //   etToDate.text = selectedTo!!.toEditable()
                            }
                            etFromTime.text = "".toEditable()
                            etToTime.text = "".toEditable()
                            var x = selectedDate
                            var time = selectedDate!!.split(" ").get(1)
                            selectedDate = etFromDate.text.toString() + " " + time
                            try {
                                var simpp = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                dateDay = simpp.parse(selectedDate)
                                changeTo()
                            } catch (ex: Exception) {

                            }
                            selectedFrom = date
                            etFromDate.text = dateShow.toEditable()
                        }, mYear, mMonth, mDay
                    )
                    mDatePicker.datePicker.minDate = calTime.time.time
                    mDatePicker.show()
                }
            }
            rlFromTime.onOneClick {

                if (MyApplication.selectedOrder!!.product!!.availableDates != null && MyApplication.selectedOrder!!.product!!.availableDates.size > 0) {
                    if (selectedDayId!! == -1) {
                        toast(getString(R.string.need_specific_date))
                    } else {
                        var day = dayIdtoString(selectedDayId!!)
                        try {
                            myTimes.clear()
                                myTimes.addAll(
                                MyApplication.selectedOrder!!.product!!.availableDates.find {
                                    it.day.equals(
                                        day,
                                        true
                                    )
                                }!!.time
                            )
                        }catch (ex:Exception){
                            myTimes.clear()
                        }
                        if(myTimes.size > 0 ) {
                            timeDialog(myTimes)
                        }else{
                              val mcurrentTime = Calendar.getInstance()
                    val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                    val minute = mcurrentTime[Calendar.MINUTE]
                    val now = mcurrentTime.time
                    val timePickerDialog = TimePickerDialog(
                        this, R.style.DatePickerDialog,
                        { timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int ->

                            fromHour = selectedHour
                            fromMin = selectedMinute
                            timeSelected = true
                            var time = selectedHour.toString()
                                .formatNumber("00") + ":" + selectedMinute.toString()
                                .formatNumber("00")
                            etFromTime.text = time.toEditable()
                            var date =
                                selectedDate!!.split(" ").get(0)
                            selectedDate = date + " " + time
                            var dateF =
                                selectedFrom!!.split(" ").get(0)
                            selectedFrom = dateF + " " + time
                            try {
                                var simpp = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                dateDay = simpp.parse(selectedFrom)
                                changeTo()
                            } catch (ex: java.lang.Exception) {

                            }
                        }, hour, minute, true
                    ) //Yes 24 hour time
                    timePickerDialog.show()
                        }
                    }
                } else {
                    // TODO Auto-generated method stub
                    val mcurrentTime = Calendar.getInstance()
                    val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                    val minute = mcurrentTime[Calendar.MINUTE]
                    val now = mcurrentTime.time
                    val timePickerDialog = TimePickerDialog(
                        this, R.style.DatePickerDialog,
                        { timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int ->

                            fromHour = selectedHour
                            fromMin = selectedMinute
                            timeSelected = true
                            var time = selectedHour.toString()
                                .formatNumber("00") + ":" + selectedMinute.toString()
                                .formatNumber("00")
                            etFromTime.text = time.toEditable()
                            var date =
                                selectedDate!!.split(" ").get(0)
                            selectedDate = date + " " + time
                            var dateF =
                                selectedFrom!!.split(" ").get(0)
                            selectedFrom = dateF + " " + time
                            try {
                                var simpp = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                dateDay = simpp.parse(selectedFrom)
                                changeTo()
                            } catch (ex: java.lang.Exception) {

                            }
                        }, hour, minute, true
                    ) //Yes 24 hour time
                    timePickerDialog.show()
                }
            }

            if (MyApplication.selectedOrder!!.product!!.availablePaymentMethods.size > 0) {
                adapterPaymentMethods = AdapterCheckoutPayment(
                    MyApplication.selectedOrder!!.product!!.availablePaymentMethods,
                    this,
                    this
                )
                rvPaymentMethodCheckout.layoutManager = GridLayoutManager(this, 1)
                rvPaymentMethodCheckout.adapter = adapterPaymentMethods
                rvPaymentMethodCheckout.isNestedScrollingEnabled = false
            } else {
                tvPaymentMethodTitleCheckout.hide()
            }

            latLng = LatLng(
                MyApplication.selectedOrder!!.shipping_latitude!!.toDouble(),
                MyApplication.selectedOrder!!.shipping_longitude!!.toDouble()
            )

            if (MyApplication.selectedOrder!!.product!!.serviceReasons != null && MyApplication.selectedOrder!!.product!!.serviceReasons.size > 0) {
                setSpinnerRepeat()
                llSpReason.show()
            } else {
                llSpReason.hide()
            }
            btPlaceOrder.onOneClick {

                if(llNumDays.visibility == View.GONE || (llNumDays.visibility == View.VISIBLE  && selectedNumDays)){
                if (selectedPaymentId != -1) {

                    var lat = ""
                    var long = ""
                    try {
                        lat = latLng!!.latitude.toString()
                        long = latLng!!.longitude.toString()
                    } catch (ex: Exception) {

                    }

                    var typesSelected =
                        if (MyApplication.selectedOrder!!.product!!.typesId != null) MyApplication.selectedOrder!!.product!!.typesId!!.toInt() else 0
                    var sizeSelected =
                        if (MyApplication.selectedOrder!!.product!!.sizeCapacityId != null) MyApplication.selectedOrder!!.product!!.sizeCapacityId!!.toInt() else 0


                    if (MyApplication.selectedOrder!!.typeId!!.equals(MyApplication.categories.find {
                            it.valEn!!.lowercase().equals("rental")
                        }!!.id!!.toInt())) {
                        if (!etToDate.text.toString().isNullOrEmpty() && !etToTime.text.toString()
                                .isNullOrEmpty()
                        ) {
                            if (MyApplication.selectedAddress != null) {

                                if ((MyApplication.selectedOrder!!.product!!.availableDates == null || MyApplication.selectedOrder!!.product!!.availableDates.size == 0) || (MyApplication.selectedOrder!!.product!!.availableDates != null && MyApplication.selectedOrder!!.product!!.availableDates.size > 0 && !selectedFromSlot.isNullOrEmpty() && !selectedFromSlot.isNullOrEmpty())) {

                                    if ((MyApplication.selectedOrder!!.product!!.availablePaymentMethods == null || MyApplication.selectedOrder!!.product!!.availablePaymentMethods.size == 0) || (MyApplication.selectedOrder!!.product!!.availablePaymentMethods != null && MyApplication.selectedOrder!!.product!!.availablePaymentMethods.size > 0 && selectedPaymentId != -1)) {

                                        if ((MyApplication.selectedOrder!!.product!!.serviceReasons == null || MyApplication.selectedOrder!!.product!!.serviceReasons.size == 0) ||  (reasonId != 0 && reasonId!=MyApplication.otherId) || (reasonId == MyApplication.otherId && !etServiceReason.text.toString()!!
                                                .isNullOrEmpty())
                                        ) {
                                            if (reasonId == -1) {
                                                reasonId = 0
                                                reasonString = ""
                                            } else if (reasonId == MyApplication.otherId) {
                                                reasonString = etServiceReason.text.toString()
                                            } else {
                                                reasonString = ""
                                            }



                                            MyApplication.selectedPlaceOrder =
                                                RequestPlaceOrder(
                                                    MyApplication.userId,
                                                    MyApplication.selectedOrder!!.typeId!!.toInt(),
                                                    MyApplication.selectedOrder!!.product!!.productParent!!.toInt(),
                                                    typesSelected,
                                                    sizeSelected,
                                                    selectedDate,
                                                    if (MyApplication.selectedAddress!!.addressName != null && MyApplication.selectedAddress!!.addressName!!.isNotEmpty()) MyApplication.selectedAddress!!.addressName else "",
                                                    lat,
                                                    long,
                                                    if (MyApplication.selectedAddress!!.street != null && MyApplication.selectedAddress!!.street!!.isNotEmpty()) MyApplication.selectedAddress!!.street else "",
                                                    if (MyApplication.selectedAddress!!.bldg != null && MyApplication.selectedAddress!!.bldg!!.isNotEmpty()) MyApplication.selectedAddress!!.bldg else "",
                                                    if (MyApplication.selectedAddress!!.floor != null && MyApplication.selectedAddress!!.floor!!.isNotEmpty()) MyApplication.selectedAddress!!.floor else "",
                                                    if (MyApplication.selectedAddress!!.desc != null && MyApplication.selectedAddress!!.desc!!.isNotEmpty()) MyApplication.selectedAddress!!.desc else "",
                                                    if (MyApplication.selectedAddress!!.addressId != null && MyApplication.selectedAddress!!.addressId!!.isNotEmpty()) MyApplication.selectedAddress!!.addressId!!.toInt() else 0,
                                                    selectedFrom,
                                                    selectedTo,
                                                    MyApplication.languageCode,
                                                    reasonId,
                                                    reasonString!!,
                                                    if (!selectedFromSlot.equals("none")) selectedFromSlot!! else "",
                                                    if (!selectedToSlot.equals("none")) selectedToSlot!! else ""
                                                )
                                            placeOrder()
                                        } else {
                                            AppHelper.createDialog(
                                                this,
                                                AppHelper.getRemoteString(
                                                    "fill_reason_field",
                                                    this
                                                )
                                            )
                                        }
                                    } else {
                                        AppHelper.createDialog(
                                            this,
                                            AppHelper.getRemoteString("fill_all_field", this)
                                        )
                                    }
                                } else {
                                    AppHelper.createDialog(
                                        this,
                                        AppHelper.getRemoteString("fill_all_field", this)
                                    )
                                }
                            } else {
                                if ((MyApplication.selectedOrder!!.product!!.availableDates == null || MyApplication.selectedOrder!!.product!!.availableDates.size == 0) || (MyApplication.selectedOrder!!.product!!.availableDates != null && MyApplication.selectedOrder!!.product!!.availableDates.size > 0 && !selectedFromSlot.isNullOrEmpty() && !selectedFromSlot.isNullOrEmpty())) {

                                    if ((MyApplication.selectedOrder!!.product!!.availablePaymentMethods == null || MyApplication.selectedOrder!!.product!!.availablePaymentMethods.size == 0) || (MyApplication.selectedOrder!!.product!!.availablePaymentMethods != null && MyApplication.selectedOrder!!.product!!.availablePaymentMethods.size > 0 && selectedPaymentId != -1)) {

                                        if ((MyApplication.selectedOrder!!.product!!.serviceReasons == null || MyApplication.selectedOrder!!.product!!.serviceReasons.size == 0) ||  (reasonId != 0 && reasonId!=MyApplication.otherId) || (reasonId == MyApplication.otherId && !etServiceReason.text.toString()!!
                                                .isNullOrEmpty())
                                        ) {
                                            if (reasonId == -1) {
                                                reasonId = 0
                                                reasonString = ""
                                            } else if (reasonId == MyApplication.otherId) {
                                                reasonString = etServiceReason.text.toString()
                                            } else {
                                                reasonString = ""
                                            }
                                            MyApplication.selectedAddress =
                                                MyApplication.selectedOrder!!.customer!!.addresses!!.get(
                                                    0
                                                )



                                            MyApplication.selectedPlaceOrder =
                                                RequestPlaceOrder(
                                                    MyApplication.userId,
                                                    MyApplication.selectedOrder!!.typeId!!.toInt(),
                                                    MyApplication.selectedOrder!!.product!!.productParent!!.toInt(),
                                                    typesSelected,
                                                    sizeSelected,
                                                    selectedDate,
                                                    MyApplication.selectedOrder!!.shipping_address_name!!,
                                                    lat,
                                                    long,
                                                    MyApplication.selectedOrder!!.shipping_address_street!!,
                                                    MyApplication.selectedOrder!!.shipping_address_building!!,
                                                    MyApplication.selectedOrder!!.shipping_floor!!,
                                                    MyApplication.selectedOrder!!.shipping_address_description!!,
                                                    MyApplication.selectedOrder!!.shipping_id!!.toInt(),
                                                    selectedFrom,
                                                    selectedTo,
                                                    MyApplication.languageCode,
                                                    reasonId,
                                                    reasonString!!,
                                                    if (!selectedFromSlot.equals("none")) selectedFromSlot!! else "",
                                                    if (!selectedToSlot.equals("none")) selectedToSlot!! else ""
                                                )

                                            placeOrder()
                                        } else {
                                            AppHelper.createDialog(
                                                this,
                                                AppHelper.getRemoteString(
                                                    "fill_reason_field",
                                                    this
                                                )
                                            )
                                        }
                                    } else {
                                        AppHelper.createDialog(
                                            this,
                                            AppHelper.getRemoteString("fill_all_field", this)
                                        )
                                    }
                                } else {
                                    AppHelper.createDialog(
                                        this,
                                        AppHelper.getRemoteString("fill_all_field", this)
                                    )
                                }
                            }
                        } else {
                            AppHelper.createDialog(
                                this,
                                AppHelper.getRemoteString("fill_all_field", this)
                            )
                        }


                    } else {

                        if (MyApplication.selectedAddress != null) {

                            if ((MyApplication.selectedOrder!!.product!!.availableDates == null || MyApplication.selectedOrder!!.product!!.availableDates.size == 0) || (MyApplication.selectedOrder!!.product!!.availableDates != null && MyApplication.selectedOrder!!.product!!.availableDates.size > 0 && !selectedFromSlot.isNullOrEmpty() && !selectedFromSlot.isNullOrEmpty())) {

                                if ((MyApplication.selectedOrder!!.product!!.availablePaymentMethods == null || MyApplication.selectedOrder!!.product!!.availablePaymentMethods.size == 0) || (MyApplication.selectedOrder!!.product!!.availablePaymentMethods != null && MyApplication.selectedOrder!!.product!!.availablePaymentMethods.size > 0 && selectedPaymentId != -1)) {

                                    if ((MyApplication.selectedOrder!!.product!!.serviceReasons == null || MyApplication.selectedOrder!!.product!!.serviceReasons.size == 0) ||  (reasonId != 0 && reasonId!=MyApplication.otherId) || (reasonId == MyApplication.otherId && !etServiceReason.text.toString()!!
                                            .isNullOrEmpty())
                                    ) {
                                        if (reasonId == -1) {
                                            reasonId = 0
                                            reasonString = ""
                                        } else if (reasonId == MyApplication.otherId) {
                                            reasonString = etServiceReason.text.toString()
                                        } else {
                                            reasonString = ""
                                        }
                                        if (MyApplication.selectedOrder!!.product!!.availableDates != null && MyApplication.selectedOrder!!.product!!.availableDates.size > 0) {


                                            MyApplication.selectedPlaceOrder = RequestPlaceOrder(
                                                MyApplication.userId,
                                                345,
                                                MyApplication.selectedOrder!!.product!!.productParent!!.toInt(),
                                                typesSelected,
                                                sizeSelected,
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
                                                if (MyApplication.selectedAddress!!.avenue != null && MyApplication.selectedAddress!!.avenue!!.isNotEmpty()) MyApplication.selectedAddress!!.avenue else "",
                                                if (MyApplication.selectedAddress!!.area != null && MyApplication.selectedAddress!!.area!!.isNotEmpty()) MyApplication.selectedAddress!!.area else "",
                                                if (MyApplication.selectedAddress!!.apartment != null && MyApplication.selectedAddress!!.apartment!!.isNotEmpty()) MyApplication.selectedAddress!!.apartment else "",
                                                if (MyApplication.selectedAddress!!.block != null && MyApplication.selectedAddress!!.block!!.isNotEmpty()) MyApplication.selectedAddress!!.block else "",
                                                if (MyApplication.selectedAddress!!.province != null && MyApplication.selectedAddress!!.province!!.isNotEmpty()) MyApplication.selectedAddress!!.province else "",
                                                MyApplication.languageCode,
                                                reasonId,
                                                reasonString!!,
                                                if (!selectedFromSlot.equals("none")) selectedFromSlot!! else "",
                                                if (!selectedToSlot.equals("none")) selectedToSlot!! else ""
                                            )
                                        } else {
                                            MyApplication.selectedPlaceOrder = RequestPlaceOrder(
                                                MyApplication.userId,
                                                345,
                                                MyApplication.selectedOrder!!.product!!.productParent!!.toInt(),
                                                typesSelected,
                                                sizeSelected,
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
                                                if (MyApplication.selectedAddress!!.avenue != null && MyApplication.selectedAddress!!.avenue!!.isNotEmpty()) MyApplication.selectedAddress!!.avenue else "",
                                                if (MyApplication.selectedAddress!!.area != null && MyApplication.selectedAddress!!.area!!.isNotEmpty()) MyApplication.selectedAddress!!.area else "",
                                                if (MyApplication.selectedAddress!!.apartment != null && MyApplication.selectedAddress!!.apartment!!.isNotEmpty()) MyApplication.selectedAddress!!.apartment else "",
                                                if (MyApplication.selectedAddress!!.block != null && MyApplication.selectedAddress!!.block!!.isNotEmpty()) MyApplication.selectedAddress!!.block else "",
                                                if (MyApplication.selectedAddress!!.province != null && MyApplication.selectedAddress!!.province!!.isNotEmpty()) MyApplication.selectedAddress!!.province else "",
                                                MyApplication.languageCode,
                                                reasonId,
                                                reasonString!!,
                                                if (!selectedFromSlot.equals("none")) selectedFromSlot!! else "",
                                                if (!selectedToSlot.equals("none")) selectedToSlot!! else ""
                                            )
                                        }

                                        placeOrder()
                                    } else {
                                        AppHelper.createDialog(
                                            this,
                                            AppHelper.getRemoteString("fill_reason_field", this)
                                        )
                                    }
                                } else {
                                    AppHelper.createDialog(
                                        this,
                                        AppHelper.getRemoteString("fill_all_field", this)
                                    )
                                }
                            } else {
                                AppHelper.createDialog(
                                    this,
                                    AppHelper.getRemoteString("fill_all_field", this)
                                )
                            }
                        } else {
                            if ((MyApplication.selectedOrder!!.product!!.availableDates == null || MyApplication.selectedOrder!!.product!!.availableDates.size == 0) || (MyApplication.selectedOrder!!.product!!.availableDates != null && MyApplication.selectedOrder!!.product!!.availableDates.size > 0 && !selectedFromSlot.isNullOrEmpty() && !selectedFromSlot.isNullOrEmpty())) {

                                if ((MyApplication.selectedOrder!!.product!!.availablePaymentMethods == null || MyApplication.selectedOrder!!.product!!.availablePaymentMethods.size == 0) || (MyApplication.selectedOrder!!.product!!.availablePaymentMethods != null && MyApplication.selectedOrder!!.product!!.availablePaymentMethods.size > 0 && selectedPaymentId != -1)) {

                                    if ((MyApplication.selectedOrder!!.product!!.serviceReasons == null || MyApplication.selectedOrder!!.product!!.serviceReasons.size == 0) ||  (reasonId != 0 && reasonId!=MyApplication.otherId) || (reasonId == MyApplication.otherId && !etServiceReason.text.toString()!!
                                            .isNullOrEmpty())
                                    ) {
                                        if (reasonId == -1) {
                                            reasonId = 0
                                            reasonString = ""
                                        } else if (reasonId == MyApplication.otherId) {
                                            reasonString = etServiceReason.text.toString()
                                        } else {
                                            reasonString = ""
                                        }
                                        MyApplication.selectedAddress =
                                            MyApplication.selectedOrder!!.customer!!.addresses!!.get(
                                                0
                                            )
                                        if (MyApplication.selectedOrder!!.product!!.availableDates != null && MyApplication.selectedOrder!!.product!!.availableDates.size > 0) {


                                            MyApplication.selectedPlaceOrder = RequestPlaceOrder(
                                                MyApplication.userId,
                                                345,
                                                MyApplication.selectedOrder!!.product!!.productParent!!.toInt(),
                                                typesSelected,
                                                sizeSelected,
                                                selectedDate,
                                                MyApplication.selectedOrder!!.shipping_address_name,
                                                MyApplication.selectedOrder!!.shipping_latitude,
                                                MyApplication.selectedOrder!!.shipping_longitude,
                                                MyApplication.selectedOrder!!.shipping_address_street,
                                                MyApplication.selectedOrder!!.shipping_building,
                                                MyApplication.selectedOrder!!.shipping_floor,
                                                MyApplication.selectedOrder!!.shipping_address_description,
                                                MyApplication.selectedOrder!!.product!!.name,
                                                MyApplication.selectedOrder!!.shipping_id!!.toInt(),
                                                MyApplication.selectedOrder!!.shippingAvenu,
                                                MyApplication.selectedOrder!!.shipping_area,
                                                MyApplication.selectedOrder!!.shippingApartment,
                                                MyApplication.selectedOrder!!.shipping_block,
                                                MyApplication.selectedOrder!!.shipping_province,
                                                MyApplication.languageCode,
                                                reasonId,
                                                reasonString!!,
                                                if (!selectedFromSlot.equals("none")) selectedFromSlot!! else "",
                                                if (!selectedToSlot.equals("none")) selectedToSlot!! else ""
                                            )
                                        } else {
                                            MyApplication.selectedPlaceOrder = RequestPlaceOrder(
                                                MyApplication.userId,
                                                345,
                                                MyApplication.selectedOrder!!.product!!.productParent!!.toInt(),
                                                typesSelected,
                                                sizeSelected,
                                                selectedDate,
                                                MyApplication.selectedOrder!!.shipping_address_name,
                                                MyApplication.selectedOrder!!.shipping_latitude,
                                                MyApplication.selectedOrder!!.shipping_longitude,
                                                MyApplication.selectedOrder!!.shipping_address_street,
                                                MyApplication.selectedOrder!!.shipping_building,
                                                MyApplication.selectedOrder!!.shipping_floor,
                                                MyApplication.selectedOrder!!.shipping_address_description,
                                                MyApplication.selectedOrder!!.product!!.name,
                                                MyApplication.selectedOrder!!.shipping_id!!.toInt(),
                                                MyApplication.selectedOrder!!.shippingAvenu,
                                                MyApplication.selectedOrder!!.shipping_area,
                                                MyApplication.selectedOrder!!.shippingApartment,
                                                MyApplication.selectedOrder!!.shipping_block,
                                                MyApplication.selectedOrder!!.shipping_province,
                                                MyApplication.languageCode,
                                                reasonId,
                                                reasonString!!,
                                                if (!selectedFromSlot.equals("none")) selectedFromSlot!! else "",
                                                if (!selectedToSlot.equals("none")) selectedToSlot!! else ""
                                            )
                                        }

                                        placeOrder()
                                    } else {
                                        AppHelper.createDialog(
                                            this,
                                            AppHelper.getRemoteString("fill_reason_field", this)
                                        )
                                    }
                                } else {
                                    AppHelper.createDialog(
                                        this,
                                        AppHelper.getRemoteString("fill_all_field", this)
                                    )
                                }
                            } else {
                                AppHelper.createDialog(
                                    this,
                                    AppHelper.getRemoteString("fill_all_field", this)
                                )
                            }
                        }
                    }

                } else {
                    toast(AppHelper.getRemoteString("select_payment_method", this))
                }
                }else{
                    toast(AppHelper.getRemoteString("pleaseSelectNumberOfDays",this))
                }
            }


            if (MyApplication.selectedOrder!!.product!!.availableDates == null || MyApplication.selectedOrder!!.product!!.availableDates.size == 0) {
                rbNow.isChecked = true
                rlFromDate.onOneClick {

                }
                rlFromTime.onOneClick {

                }
                rbNow.isEnabled = true
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
            selectedFrom,
            selectedTo,
            MyApplication.languageCode,
            reasonId,
            reasonString
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

        var typeId =
            if (MyApplication.selectedOrder!!.product!!.typesId.isNullOrEmpty()) 0 else MyApplication.selectedOrder!!.product!!.typesId!!.toInt()
        var sizeId =
            if (MyApplication.selectedOrder!!.product!!.sizeCapacityId.isNullOrEmpty()) 0 else MyApplication.selectedOrder!!.product!!.sizeCapacityId!!.toInt()


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
            MyApplication.selectedOrder!!.deliveryDate,
            selectedFrom,
            selectedTo,
            MyApplication.selectedOrder!!.shipping_address_name,
            MyApplication.selectedOrder!!.shipping_longitude!!.toDouble(),
            MyApplication.selectedOrder!!.shipping_latitude!!.toDouble(),
            MyApplication.selectedOrder!!.shipping_address_street,
            MyApplication.selectedOrder!!.shipping_address_building,
            MyApplication.selectedOrder!!.shipping_address_floor,
            MyApplication.selectedOrder!!.shipping_address_description,
            MyApplication.selectedOrder!!.shipping_province,
            MyApplication.selectedOrder!!.shipping_area,
            MyApplication.selectedOrder!!.shipping_block,
            MyApplication.selectedOrder!!.shippingAvenu,
            MyApplication.selectedOrder!!.shippingApartment,
            MyApplication.languageCode
        )
        logw("renewed", Gson().toJson(req))
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.renewOrder(req)?.enqueue(object : Callback<ResponseOrderId> {
                override fun onResponse(
                    call: Call<ResponseOrderId>,
                    response: Response<ResponseOrderId>
                ) {
                    try {
                        loading.hide()
                        if (response.body()!!.result!!.toInt() == 1) {
                            /*AppHelper.createDialog(
                                this@ActivityCheckout,
                                AppHelper.getRemoteString("success", this@ActivityCheckout)
                            )*/
                            updatePayment(
                                response.body()!!.orderId!!.toInt(),
                                selectedPaymentId!!,
                                true
                            )
                        } else {
                            AppHelper.createDialog(
                                this@ActivityCheckout,
                                response.body()!!.message!!
                            )
                        }
                        //nextStepRenew()
                        //nextStep(response.body()!!.result!!)
                    } catch (E: java.lang.Exception) {

                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponseOrderId>, throwable: Throwable) {
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
            tvSize.text = MyApplication.selectedOrder!!.product!!.sizeCapacity
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
                MyApplication.selectedService!!.variations.find { it.sizeCapacityId != null && it.sizeCapacityId!!.toInt() == MyApplication.selectedSize!! }!!.sizeCapacity
        } catch (e: Exception) {
        }
        try {
            tvPrice.text = MyApplication.selectedPrice + ""
        } catch (e: Exception) {
        }
        try {
            tvVariationType.text =
                MyApplication.selectedService!!.variations.find { it.typesId != null && it.typesId!!.toInt() == MyApplication.selectedVariationType!! }!!.types
        } catch (e: Exception) {
        }
        try {
            tvSize.text =
                MyApplication.selectedService!!.variations.find { it.sizeCapacityId != null && it.sizeCapacityId!!.toInt() == MyApplication.selectedSize!! }!!.sizeCapacity
        } catch (e: Exception) {
        }

        loading.hide()
    }

    private fun setPlacedOrder() {

        if ((MyApplication.selectedService!!.availableDates == null || MyApplication.selectedService!!.availableDates.size == 0) || (MyApplication.selectedService!!.availableDates != null && MyApplication.selectedService!!.availableDates.size > 0 /*&& !selectedFromSlot.isNullOrEmpty() && !selectedFromSlot.isNullOrEmpty()*/)) {

            if ((MyApplication.selectedService!!.availablePaymentMethods == null || MyApplication.selectedService!!.availablePaymentMethods.size == 0) || (MyApplication.selectedService!!.availablePaymentMethods != null && MyApplication.selectedService!!.availablePaymentMethods.size > 0 && selectedPaymentId != -1)) {

                if ((MyApplication.selectedService!!.serviceReasons == null || MyApplication.selectedService!!.serviceReasons.size == 0)) {
                    reasonId = 0
                } else {
                    if (reasonId == MyApplication.otherId) {
                        reasonString = etServiceReason.text.toString()
                    } else {
                        reasonString = ""
                    }
                }
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

                if (MyApplication.selectedService!!.availableDates != null && MyApplication.selectedService!!.availableDates.size > 0) {
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
                            if (MyApplication.selectedAddress!!.avenue != null && MyApplication.selectedAddress!!.avenue!!.isNotEmpty()) MyApplication.selectedAddress!!.avenue else "",
                            if (MyApplication.selectedAddress!!.area != null && MyApplication.selectedAddress!!.area!!.isNotEmpty()) MyApplication.selectedAddress!!.area else "",
                            if (MyApplication.selectedAddress!!.apartment != null && MyApplication.selectedAddress!!.apartment!!.isNotEmpty()) MyApplication.selectedAddress!!.apartment else "",
                            if (MyApplication.selectedAddress!!.block != null && MyApplication.selectedAddress!!.block!!.isNotEmpty()) MyApplication.selectedAddress!!.block else "",
                            if (MyApplication.selectedAddress!!.province != null && MyApplication.selectedAddress!!.province!!.isNotEmpty()) MyApplication.selectedAddress!!.province else "",
                            MyApplication.languageCode,
                            reasonId,
                            reasonString!!,
                            if (!selectedFromSlot.equals("none")) selectedFromSlot!! else "",
                            if (!selectedToSlot.equals("none")) selectedToSlot!! else ""
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
                            if (MyApplication.selectedAddress!!.addressId != null && MyApplication.selectedAddress!!.addressId!!.isNotEmpty()) MyApplication.selectedAddress!!.addressId!!.toInt() else 0,
                            selectedFrom,
                            selectedTo,
                            MyApplication.languageCode,
                            reasonId,
                            reasonString!!,
                            if (!selectedFromSlot.equals("none")) selectedFromSlot!! else "",
                            if (!selectedToSlot.equals("none")) selectedToSlot!! else ""
                        )
                    }
                } else {
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
                            if (MyApplication.selectedAddress!!.avenue != null && MyApplication.selectedAddress!!.avenue!!.isNotEmpty()) MyApplication.selectedAddress!!.avenue else "",
                            if (MyApplication.selectedAddress!!.area != null && MyApplication.selectedAddress!!.area!!.isNotEmpty()) MyApplication.selectedAddress!!.area else "",
                            if (MyApplication.selectedAddress!!.apartment != null && MyApplication.selectedAddress!!.apartment!!.isNotEmpty()) MyApplication.selectedAddress!!.apartment else "",
                            if (MyApplication.selectedAddress!!.block != null && MyApplication.selectedAddress!!.block!!.isNotEmpty()) MyApplication.selectedAddress!!.block else "",
                            if (MyApplication.selectedAddress!!.province != null && MyApplication.selectedAddress!!.province!!.isNotEmpty()) MyApplication.selectedAddress!!.province else "",
                            MyApplication.languageCode,
                            reasonId,
                            reasonString!!
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
                            if (MyApplication.selectedAddress!!.addressId != null && MyApplication.selectedAddress!!.addressId!!.isNotEmpty()) MyApplication.selectedAddress!!.addressId!!.toInt() else 0,
                            selectedFrom,
                            selectedTo,
                            MyApplication.languageCode,
                            reasonId,
                            reasonString!!
                        )
                    }
                }
                if (update) {
                    var i = MyApplication.arrayCart.size - 1
                    MyApplication.arrayCart[i] = MyApplication.selectedPlaceOrder!!
                } else {
                    MyApplication.arrayCart.add(MyApplication.selectedPlaceOrder!!)
                }
                MyApplication.seletedPosCart = MyApplication.arrayCart.size - 1

                AppHelper.toGSOn(MyApplication.arrayCart)

                if (AppHelper.isOnline(this)) {
                    placeOrder()
                } else {
                    AppHelper.createDialog(this, AppHelper.getRemoteString("no_internet", this))
                }
            } else {
                AppHelper.createDialog(this, AppHelper.getRemoteString("fill_all_field", this))
            }
        } else {

            AppHelper.createDialog(this, AppHelper.getRemoteString("fill_all_field", this))
        }


    }


    fun updatePayment(orderid: Int, paymentId: Int, worked: Boolean) {
        loading.show()
        var req = RequestOrderPayment(orderid, paymentId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.updateCheckoutPayment(req)?.enqueue(object :
                Callback<ResponseMessage> {
                override fun onResponse(
                    call: Call<ResponseMessage>,
                    response: Response<ResponseMessage>
                ) {
                    try {
                        if (response.body()!!.result == 1) {
                            if (worked) {
                                nextStep()
                            } else {
                                startActivity(
                                    Intent(
                                        this@ActivityCheckout,
                                        ActivityPlaceOrder::class.java
                                    ).putExtra(AppConstants.SP_FOUND, false)

                                )
                            }
                        } else {
                            loading.hide()
                        }

                    } catch (ex: Exception) {

                    }
                }

                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    loading.hide()
                }
            })
    }

    fun nextStep() {
        toast(AppHelper.getRemoteString("service_pending", this))
        finishAffinity()
        MyApplication.selectedPos = 1
        MyApplication.fromOrderPlaced = true
        MyApplication.typeSelected = 0
        MyApplication.fromFooterOrder = true
        MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_ORDER
        MyApplication.selectedFragment = FragmentOrders()
        MyApplication.tintColor = R.color.primary
        loading.hide()
        startActivity(Intent(this, ActivityHome::class.java))

    }

    fun placeOrder() {
        loading.show()
        MyApplication.repeating = false
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
                                updatePayment(
                                    response.body()!!.orderId!!.toInt(),
                                    selectedPaymentId!!,
                                    true
                                )
                                /*startActivity(
                                    Intent(
                                        this@ActivityCheckout,
                                        ActivityPlaceOrder::class.java
                                    ).putExtra(AppConstants.ORDER_ID, response.body()!!.orderId)
                                )*/
                            } else if (response.body()!!.action == AppConstants.PLACE_ORDER_AVAILABLE_OUT)
                                showProviderMessage(response.body()!!)
                            else {
                                startActivity(
                                    Intent(this@ActivityCheckout, ActivityPlaceOrder::class.java)
                                        .putExtra(AppConstants.ORDER_ID, response.body()!!.orderId)
                                        .putExtra(AppConstants.SP_FOUND, false)
                                )
                            }
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
                dontBroadcastOutOfRange(res.orderId!!)

            }
            .setPositiveButton(ok) { dialog, _ ->
                if (AppHelper.isOnline(this)) {
                    broadcastOutOfRange(res.orderId!!)
                } else {
                    AppHelper.createDialog(this, AppHelper.getRemoteString("no_internet", this))
                }

            }
        val alert = builder.create()
        alert.show()

    }

    fun failedStep(orderId: String) {
        loading.hide()
        startActivity(
            Intent(this@ActivityCheckout, ActivityPlaceOrder::class.java)
                .putExtra(AppConstants.SP_FOUND, false)
                .putExtra(AppConstants.ORDER_ID, orderId)
        )
    }

    fun dontBroadcastOutOfRange(orderId: String) {
        loading.show()
        var req = RequestJOrderid(orderId.toInt())
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.dontBroadcast(req)?.enqueue(object :
                Callback<ResponseMessage> {
                override fun onResponse(
                    call: Call<ResponseMessage>,
                    response: Response<ResponseMessage>
                ) {
                    try {
                        if (response.body()!!.result == 1)
                            failedStep(orderId)
                        else {
                            loading.hide()
                            AppHelper.createDialog(
                                this@ActivityCheckout,
                                AppHelper.getRemoteString(
                                    "error_getting_data",
                                    this@ActivityCheckout
                                )
                            )
                        }

                    } catch (ex: Exception) {
                        loading.hide()
                        AppHelper.createDialog(
                            this@ActivityCheckout,
                            AppHelper.getRemoteString("error_getting_data", this@ActivityCheckout)
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    loading.hide()
                    AppHelper.createDialog(
                        this@ActivityCheckout,
                        AppHelper.getRemoteString("error_getting_data", this@ActivityCheckout)
                    )
                }
            })
    }


    fun broadcastOutOfRange(orderId: String) {
        loading.show()
        var req = RequestOrderId(orderId.toInt(), MyApplication.languageCode)
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
                            updatePayment(orderId.toInt(), selectedPaymentId!!, true)
                        } else
                            updatePayment(orderId.toInt(), selectedPaymentId!!, false)

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

        } else if (apiId == AppConstants.GET_CATEGORIES) {

            var res = response as ResponseMainCategories
            MyApplication.categories.clear()
            MyApplication.categories.addAll(res.categories)
            MyApplication.purchaseId =
                MyApplication.categories.find { it.valEn.equals("purchase") }!!.id!!.toInt()
            MyApplication.rentalId =
                MyApplication.categories.find { it.valEn.equals("rental") }!!.id!!.toInt()
            setUpCategory()
        } else {
            if (success) {
                if (MyApplication.selectedAddress == null)
                    CallAPIs.getAddressName(latLng!!, this, this)
                else
                    setPlacedOrder()
            } else {
                toast(AppHelper.getRemoteString("error_getting_data", this))
            }

        }
    }

    fun dayIdtoString(id: Int): String {
        if (id == Calendar.MONDAY) {
            return "MONDAY"
        } else if (id == Calendar.TUESDAY) {
            return "TUESDAY"
        } else if (id == Calendar.WEDNESDAY) {
            return "WEDNESDAY"
        } else if (id == Calendar.THURSDAY) {
            return "THURSDAY"
        } else if (id == Calendar.FRIDAY) {
            return "FRIDAY"
        } else if (id == Calendar.SATURDAY) {
            return "SATURDAY"
        } else {
            return "SUNDAY"
        }
    }

    override fun onDateSet(
        view: com.wdullaer.materialdatetimepicker.date.DatePickerDialog?,
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int
    ) {
        if (editer == "from") {
            timeSelected = false
            val myCalendar = Calendar.getInstance()
            myCalendar[Calendar.YEAR] = year
            etFromTime.text = "".toEditable()
            etToTime.text = "".toEditable()
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            selectedDayId = myCalendar.get(Calendar.DAY_OF_WEEK)
            pickedDate = myCalendar.time
            var date = getFormatter.format(myCalendar.time)
            var dateShow = viewFormatter.format(myCalendar.time)
            etFromDate.text = date.toEditable()
            if (compareDates() == 1) {
                selectedTo = date
                //     etToDate.text = dateShow.toEditable()
            }
            var x = selectedDate
            var time = selectedDate!!
            selectedDate = dateShow
            selectedFrom = date
            var day = dayIdtoString(selectedDayId!!)
            if (MyApplication.renewing || MyApplication.repeating) {
                try {
                    myTimes.clear()
                                myTimes.addAll(
                        MyApplication.selectedOrder!!.product!!.availableDates.find {
                            it.day.equals(
                                day,
                                true
                            )
                        }!!.time
                    )
                }catch (ex:Exception){
                    myTimes.clear()
                }
            } else {
                try {
                    myTimes.clear()
                                myTimes.addAll(
                        MyApplication.selectedService!!.availableDates.find {
                            it.day.equals(
                                day,
                                true
                            )
                        }!!.time)
                }catch (ex : Exception){
                    myTimes.clear()
                }
            }
            if (myTimes.size == 1) {
                timeSelected = true
                var date = fullTimeFormatter.parse(myTimes.get(0).to)
                var cal = Calendar.getInstance()
                cal.time = date
                timeSelected = true
                selectedFromSlot = myTimes.get(0).from
                selectedToSlot = myTimes.get(0).to
                var dateF = fullTimeFormatter.parse(selectedFromSlot)
                var dateT = fullTimeFormatter.parse(selectedToSlot)
                var simp = SimpleDateFormat("HH:mm", Locale.ENGLISH)
                var res1 = simp.format(dateF)
                var res2 = simp.format(dateT)



                etFromTime.text = (res1 + "-" + res2).toEditable()
                fromHour = cal.get(Calendar.HOUR_OF_DAY)
                fromMin = cal.get(Calendar.MINUTE)

                try {
                    var simpp = SimpleDateFormat("yyyy-MM-dd HH:mm")
                    dateDay = simpp.parse(dateShow + " " + res2)
                    changeTo()
                } catch (ex: Exception) {

                }
            } else {
                selectedFromSlot = "none"
                selectedToSlot = "none"
            }
            etFromDate.text = dateShow.toEditable()
        } else {
            etFromTime.text = "".toEditable()
            val myCalendar = Calendar.getInstance()
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            var date = sendFormatter.format(myCalendar.time)
            selectedTo = date
            etToDate.text = viewFormatter.format(myCalendar.time).toEditable()
        }
    }
}