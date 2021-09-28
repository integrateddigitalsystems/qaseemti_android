package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.RequestVerifyOTP
import com.ids.qasemti.model.ResponseVerification
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_code_verification.*
import kotlinx.android.synthetic.main.loading.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivityCodeVerification : ActivityBase() {

    var time = 30
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_verification)
        AppHelper.setAllTexts(rootlayoutCodeVerification,this)
        pvCode.setOnFocusChangeListener { view, b ->
            pvCode.text!!.clear()
        }

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

        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTimer.setText("0:" + checkDigit(time))
                time--
            }

            override fun onFinish() {
                tvTimer.setText("try again")
            }
        }.start()
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