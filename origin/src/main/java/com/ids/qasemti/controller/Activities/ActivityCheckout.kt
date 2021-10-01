package com.ids.qasemti.controller.Activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.*
import com.ids.qasemti.utils.AppHelper.Companion.toEditable
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class ActivityCheckout : AppCompatActivity() , RVOnItemClickListener  {

    var open = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_checkout)
        AppHelper.setAllTexts(rootLayoutCheckout,this)
        init()
        setListeners()
    }


    override fun onItemClicked(view: View, position: Int) {

    }



    fun setUpCurr(){
        var cal = Calendar.getInstance()
        val myFormat = "dd/MM/yyyy" //Change as you need
        var sdf =
            SimpleDateFormat(myFormat, Locale.ENGLISH)
        var date = sdf.format(cal.time)
        etFromDate.text = date.toEditable()
        var time = String.format("%02d", cal.get(Calendar.HOUR_OF_DAY))+" : "+ String.format("%02d", cal.get(Calendar.MINUTE))
        etFromTime.text = time.toEditable()
    }

    fun setTintLogo(color:Int){
        AppHelper.setLogoTint(btDrawer, this, color)
        btDrawer.hide()
        AppHelper.setTextColor(this,tvPageTitle,color)
        AppHelper.setLogoTint(btBackTool,this,color)
        AppHelper.setLogoTint(btLogout,this,color)
        btBackTool.onOneClick {
            super.onBackPressed()
        }
    }

    fun setListeners(){
        btPlaceOrder.onOneClick {
            startActivity(Intent(this,ActivityPlaceOrder::class.java))
        }

        rbNow.onOneClick {
            setUpCurr()
            rlFromDate.onOneClick {

            }
            rlFromTime.onOneClick {

            }

            AppHelper.setTextColor(this,etFromDate,R.color.gray_font_light)
            AppHelper.setTextColor(this,etFromTime,R.color.gray_font_light)
        }
        rbSpecify.onOneClick {

            AppHelper.setTextColor(this,etFromDate,R.color.gray_font)
            AppHelper.setTextColor(this,etFromTime,R.color.gray_font)
            rlFromDate.onOneClick {
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
                        etFromDate.text = (String.format("%02d", selectedday) + "/"+String.format("%02d", selectedmonth+1)+"/"+String.format("%02d", selectedyear)).toEditable()
                    }, mYear, mMonth, mDay
                )
                mDatePicker.show()
            }
            rlFromTime.onOneClick {
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
                        etFromTime.text = time.toEditable()
                    }, hour, minute, true
                ) //Yes 24 hour time
                timePickerDialog.show()
            }
        }


        rlToTime.onOneClick {
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
                    etToTime.text = time.toEditable()
                }, hour, minute, true
            ) //Yes 24 hour time
            timePickerDialog.show()
        }
        rlToDate.onOneClick {
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
                    val myFormat = "dd/mm/yyyy" //Change as you need
                    var sdf =
                        SimpleDateFormat(myFormat, Locale.ENGLISH)
                    var date = sdf.format(myCalendar.time)
                    etToDate.text = (String.format("%02d", selectedday) + "/"+String.format("%02d", selectedmonth+1)+"/"+String.format("%02d", selectedyear)).toEditable()
                }, mYear, mMonth, mDay
            )
            mDatePicker.show()
        }

        llAddresses.onOneClick {
            startActivity(Intent(this,ActivitySelectAddress::class.java))
        }

        btPlus.onOneClick {
            var quant = tvQuant.text.toString()
            if(quant.isNumeric()){
                var value = quant.toInt()
                value++
                tvQuant.text = value.toString()
            }
        }
        btMinus.onOneClick {
            var quant = tvQuant.text.toString()
            if(quant.isNumeric()){
                var value = quant.toInt()
                value--
                tvQuant.text = value.toString()
            }
        }

        llSetDateTime.onOneClick {
            var up = -90
            var down = 90
            if(open){
                ivOpenDateTime.animate().rotation(up.toFloat()).setDuration(400)
                llTimeDate.hide()
            }else {
                ivOpenDateTime.animate().rotation(down.toFloat()).setDuration(400)
                llTimeDate.show()
            }

            open = !open
        }

    }
    fun init(){
        setTintLogo(R.color.redPrimary)
        tvPageTitle.textRemote("Checkout",this)
        tvPageTitle.setColorTypeface(this,R.color.redPrimary,"",true)
        etFromDate.isFocusable = false;
        etFromTime.isFocusable = false;

        rbNow.isChecked = true
        rbSpecify.isChecked = false
        btBackTool.show()

        btPlaceOrder.typeface = AppHelper.getTypeFace(this)

        if(MyApplication.rental!!){
            tvFromTitle.show()
            tvToTitle.show()
            llToLayout.show()
        }else{
            tvFromTitle.hide()
            tvToTitle.hide()
            llToLayout.hide()
        }

        setUpCurr()
        setOrderSummary()

    }

    private fun setOrderSummary(){
        try{tvServiceName.text=MyApplication.selectedService!!.name!!}catch (e:Exception){}
        try{tvServiceType.text=MyApplication.selectedService!!.type!!}catch (e:Exception){}
        try{tvSizeCapacity.text=MyApplication.selectedSize}catch (e:Exception){}
        try{tvPrice.text=MyApplication.selectedPrice+" KWD"}catch (e:Exception){}
        try{tvVariationType.text=MyApplication.selectedVariationType}catch (e:Exception){}
        try{tvSize.text=MyApplication.selectedSize}catch (e:Exception){}
    }
}