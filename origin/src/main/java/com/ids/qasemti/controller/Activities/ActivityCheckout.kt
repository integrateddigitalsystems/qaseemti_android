package com.ids.qasemti.controller.Activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.AppHelper.Companion.toEditable
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.isNumeric
import com.ids.qasemti.utils.show
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class ActivityCheckout : AppCompatActivity() , RVOnItemClickListener  {

    var open = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_checkout)
        AppHelper.setAllTexts(rootLayoutCheckout)
        init()
    }


    override fun onItemClicked(view: View, position: Int) {

    }



    fun setUpCurr(){
        var cal = Calendar.getInstance()
        val myFormat = "dd/MM/yyyy" //Change as you need
        var sdf =
            SimpleDateFormat(myFormat, Locale.ENGLISH)
        var date = sdf.format(cal.time)
        etCheckoutDate.text = date.toEditable()
        var time = cal.get(Calendar.HOUR_OF_DAY).toString()+" : "+cal.get(Calendar.MINUTE)
        etCheckoutTime.text = time.toEditable()
    }

    fun setTintLogo(color:Int){
        AppHelper.setLogoTint(btDrawer, this, color)
        btDrawer.hide()
        AppHelper.setTextColor(this,tvPageTitle,color)
        AppHelper.setLogoTint(btBackTool,this,color)
        AppHelper.setLogoTint(btLogout,this,color)
        btBackTool.setOnClickListener {
            super.onBackPressed()
        }
    }

    fun init(){
        setTintLogo(R.color.redPrimary)
        tvPageTitle.text = getString(R.string.checkout)
        tvPageTitle.typeface = AppHelper.getTypeFace(this)
        etCheckoutTime.setFocusable(false);
        etCheckoutDate.setFocusable(false);
        etToDate.setFocusable(false)
        etFromDate.setFocusable(false)

        rbNow.isChecked = true
        rbSpecify.isChecked = false
        btBackTool.show()

        btPlaceOrder.typeface = AppHelper.getTypeFace(this)
        btPlaceOrder.setOnClickListener {
            startActivity(Intent(this,ActivityPlaceOrder::class.java))
        }
        setUpCurr()
        rbNow.setOnClickListener {
            setUpCurr()
            rlCheckoutTime.setOnClickListener {

            }
            rlCheckoutDate.setOnClickListener {

            }
        }
        rbSpecify.setOnClickListener {

            rlCheckoutDate.setOnClickListener {
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
                        etCheckoutDate.text = date.toEditable()
                    }, mYear, mMonth, mDay
                )
                mDatePicker.show()
            }
            rlCheckoutTime.setOnClickListener {
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
                        etCheckoutTime.text = time.toEditable()
                    }, hour, minute, false
                ) //Yes 24 hour time
                timePickerDialog.show()
            }
        }

        rlFromDate.setOnClickListener {
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
                    etFromDate.text = date.toEditable()
                }, mYear, mMonth, mDay
            )
            mDatePicker.show()
        }
        rlToDate.setOnClickListener {
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
                    etToDate.text = date.toEditable()
                }, mYear, mMonth, mDay
            )
            mDatePicker.show()
        }



        btPlus.setOnClickListener {
            var quant = tvQuant.text.toString()
            if(quant.isNumeric()){
                var value = quant.toInt()
                value++
                tvQuant.text = value.toString()
            }
        }
        btMinus.setOnClickListener {
            var quant = tvQuant.text.toString()
            if(quant.isNumeric()){
                var value = quant.toInt()
                value--
                tvQuant.text = value.toString()
            }
        }

        ivOpenDateTime.setOnClickListener {
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
}