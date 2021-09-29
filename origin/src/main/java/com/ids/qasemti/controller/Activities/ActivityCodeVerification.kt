package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.RequestOTP
import com.ids.qasemti.model.RequestVerifyOTP
import com.ids.qasemti.model.ResponseUpdate
import com.ids.qasemti.model.ResponseVerification
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_code_verification.*
import kotlinx.android.synthetic.main.loading.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivityCodeVerification : ActivityBase() {

    var firstTime = true
    var time = 60
    var first = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_verification)
        AppHelper.setAllTexts(rootlayoutCodeVerification,this)
       /* pvCode.setOnFocusChangeListener { view, b ->
            pvCode.text!!.clear()
        }*/
        pvCode.requestFocus()

        if(MyApplication.isClient){
            btVerifyCode.hide()
            llClientVerfCode.show()

            btRegisterVerf.onOneClick {
                verifyOTP()
                //startActivity(Intent(this,ActivityAccountStatus::class.java))
            }
        }else{
            btVerifyCode.show()
            llClientVerfCode.hide()

            btVerifyCode.onOneClick {
                verifyOTP()
                //startActivity(Intent(this,ActivityAccountStatus::class.java))
            }


        }

       var timer = object : CountDownTimer(60000, 1015) {
            override fun onTick(millisUntilFinished: Long) {
                if(first){
                    tvTimer.setText("1:00")
                    first = false
                }else {
                    tvTimer.setText("0:" + checkDigit(time))

                }

                time--

                if(time ==0){
                    this.onFinish()
                }
            }

            override fun onFinish() {
                first = true
                tvTimer.setText("try again")
                tvTimer.onOneClick {
                    if(first) {
                        time = 59
                        this.start()
                        sendOTP()
                    }
                }
            }
        }.start()

        timer.start()
    }

    fun sendOTP(){
        var req = RequestOTP(MyApplication.selectedPhone,MyApplication.deviceId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.sendOTP(
                req
            )?.enqueue(object : Callback<ResponseUpdate> {
                override fun onResponse(call: Call<ResponseUpdate>, response: Response<ResponseUpdate>) {
                    try{

                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ResponseUpdate>, throwable: Throwable) {
                }
            })
    }
    fun requestSucc(code:String){
        if(code.equals("1")){
            AppHelper.createDialog(this,"Correct Code")
        }else{
            AppHelper.createDialog(this,"Incorrect Code")
        }
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this,ActivityAccountStatus::class.java))
        }, 1000)

        try {
            loading.hide()
        }catch (ex: Exception){

        }
    }

    fun checkDigit(number: Int): String? {
        return if (number <= 9) "0$number" else number.toString()
    }
    fun verifyOTP(){
        try {
            loading.show()
        }catch (ex: Exception){

        }
        var req = RequestVerifyOTP(pvCode.text.toString(),MyApplication.deviceId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.verifyOTP(
                req
            )?.enqueue(object : Callback<ResponseVerification> {
                override fun onResponse(call: Call<ResponseVerification>, response: Response<ResponseVerification>) {
                    try{
                        requestSucc(response.body()!!.result!!)
                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ResponseVerification>, throwable: Throwable) {
                    requestSucc("0")
                }
            })
    }
}