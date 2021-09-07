package com.ids.qasemti.controller.Fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityAddresses
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Activities.ActivityPlaceOrder
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

class FragmentCheckout : Fragment() , RVOnItemClickListener {

    var open = false
    override fun onItemClicked(view: View, position: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.qasemti.R.layout.fragment_checkout, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutCheckout,requireContext())
        init()
        tvPageTitle.typeface=AppHelper.getTypeFace(requireContext())
        AppHelper.setTextColor(requireContext(),tvPageTitle,R.color.redPrimary)
        tvPageTitle.show()



    }

    fun setUpCurr(){
        var cal = Calendar.getInstance()
        val myFormat = "dd/MM/yyyy" //Change as you need
        var sdf =
            SimpleDateFormat(myFormat, Locale.ENGLISH)
        var date = sdf.format(cal.time)
        etFromDate.text = date.toEditable()
        var time = cal.get(Calendar.HOUR_OF_DAY).toString()+" : "+cal.get(Calendar.MINUTE)
        etFromTime.text = time.toEditable()
    }

    fun init(){
        (activity as ActivityHome?)!!.setTintLogo(R.color.redPrimary)
        AppHelper.setTitle(requireActivity(),getString(R.string.Checkout),"checkout")
        etFromTime.setFocusable(false);
        etFromDate.setFocusable(false);
        etToDate.setFocusable(false)
        etFromDate.setFocusable(false)

        rbNow.isChecked = true
        rbSpecify.isChecked = false
        (activity as ActivityHome?)!!.showBack(true)

        btPlaceOrder.typeface = AppHelper.getTypeFace(requireContext())
        btPlaceOrder.setOnClickListener {
            startActivity(Intent(requireContext(),ActivityPlaceOrder::class.java))
        }
       setUpCurr()
        rbNow.setOnClickListener {
            setUpCurr()
            rlFromDate.setOnClickListener {

            }
            rlFromTime.setOnClickListener {

            }
        }
        rbSpecify.setOnClickListener {

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
                    requireContext(),
                    DatePickerDialog.OnDateSetListener { datepicker, selectedyear, selectedmonth, selectedday ->
                        val myCalendar = Calendar.getInstance()
                        myCalendar[Calendar.YEAR] = selectedyear
                        myCalendar[Calendar.MONTH] = selectedmonth
                        myCalendar[Calendar.DAY_OF_MONTH] = selectedday
                        val myFormat = "dd/MM/yyyy" //Change as you need
                        var sdf =
                            SimpleDateFormat(myFormat, Locale.ENGLISH)
                        var date = sdf.format(myCalendar.time)
                        etFromDate.text = (String.format("%02d", selectedday) + "/"+String.format("%02d", selectedmonth)+"/"+String.format("%02d", selectedyear)).toEditable()
                    }, mYear, mMonth, mDay
                )
                mDatePicker.show()
            }
            rlFromTime.setOnClickListener {
                // TODO Auto-generated method stub
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val myFormat = "dd/MM/yyyy" //Change as you need
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                val now = mcurrentTime.time
                val nowDate = sdf.format(now)
                val timePickerDialog = TimePickerDialog(
                    activity, R.style.DatePickerDialog,
                    { timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int ->

                        var time = String.format("%02d", selectedHour)+" : "+ String.format("%02d", selectedMinute)
                        etFromTime.text = time.toEditable()
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
                requireContext(),
                DatePickerDialog.OnDateSetListener { datepicker, selectedyear, selectedmonth, selectedday ->
                    val myCalendar = Calendar.getInstance()
                    myCalendar[Calendar.YEAR] = selectedyear
                    myCalendar[Calendar.MONTH] = selectedmonth
                    myCalendar[Calendar.DAY_OF_MONTH] = selectedday
                    val myFormat = "dd/mm/yyyy" //Change as you need
                    var sdf =
                        SimpleDateFormat(myFormat, Locale.ENGLISH)
                    var date = sdf.format(myCalendar.time)
                    //etFromDate.text = date.toEditable()
                    etFromDate.text = (String.format("%02d", selectedday) + "/"+String.format("%02d", selectedmonth)+"/"+String.format("%02d", selectedyear)).toEditable()
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
                requireContext(),
                DatePickerDialog.OnDateSetListener { datepicker, selectedyear, selectedmonth, selectedday ->
                    val myCalendar = Calendar.getInstance()
                    myCalendar[Calendar.YEAR] = selectedyear
                    myCalendar[Calendar.MONTH] = selectedmonth
                    myCalendar[Calendar.DAY_OF_MONTH] = selectedday
                    val myFormat = "dd/mm/yyyy" //Change as you need
                    var sdf =
                        SimpleDateFormat(myFormat, Locale.ENGLISH)
                    var date = sdf.format(myCalendar.time)
                    etToDate.text = (String.format("%02d", selectedday) + "/"+String.format("%02d", selectedmonth)+"/"+String.format("%02d", selectedyear)).toEditable()
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