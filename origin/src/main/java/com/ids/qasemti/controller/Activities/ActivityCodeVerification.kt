package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.RequestOTP
import com.ids.qasemti.model.RequestVerifyOTP
import com.ids.qasemti.model.ResponseUpdate
import com.ids.qasemti.model.ResponseVerification
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_code_verification.*
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.loading.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class ActivityCodeVerification : ActivityBase() {

    var firstTime = true
    var time = 60
    var first = true
    var timer : CountDownTimer ?=null
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
            btCancelVerf.onOneClick {
                super.onBackPressed()
            }
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
        timer = object : CountDownTimer(60000, 1015) {
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

        pvCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
               if(s.length==6){
                   verifyOTP()
               }

            }
        })

    }


    fun sendOTP(){
        pvCode.text!!.clear()
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
    fun requestSucc(respone:ResponseVerification){
        if(respone.result.equals("1")){
            AppHelper.createDialog(this,"Correct Code")
            if(respone.user!=null) {
                MyApplication.phoneNumber = MyApplication.selectedPhone
                MyApplication.isSignedIn = true
                MyApplication.userId = respone.user!!.userId!!.toInt()
                startActivity(Intent(this, ActivityHome::class.java))
            }else{
                MyApplication.isSignedIn = false
                startActivity(Intent(this, ActivityRegistration::class.java))
            }
        }else{
            AppHelper.createDialog(this,"Incorrect Code")
            first = true
            timer!!.cancel()
            tvTimer.setText("try again")
            tvTimer.onOneClick {
                if(first) {
                    time = 59
                    timer!!.start()
                    sendOTP()
                }
            }
            //startActivity(Intent(this, ActivityRegistration::class.java))
        }

       /* MyApplication.isSignedIn = true
        try{
        MyApplication.userId=respone.user!!.userId!!.toInt()
        }catch (e:Exception){
            MyApplication.userId=6
        }
        AppHelper.getUserInfo()
        startActivity(Intent(this, ActivityHome::class.java))*/

    }

    fun checkDigit(number: Int): String? {
        return if (number <= 9) "0$number" else number.toString()
    }
    fun verifyOTP(){

        loading.show()
        var pv =pvCode.text.toString()
        var req = RequestVerifyOTP(pvCode.text.toString(),MyApplication.deviceId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.verifyOTP(
                req
            )?.enqueue(object : Callback<ResponseVerification> {
                override fun onResponse(call: Call<ResponseVerification>, response: Response<ResponseVerification>) {
                    try{
                        loading.hide()
                        requestSucc(response.body()!!)
                    }catch (E: java.lang.Exception){
                        requestSucc(ResponseVerification("0"))
                    }
                }
                override fun onFailure(call: Call<ResponseVerification>, throwable: Throwable) {
                    loading.hide()
                    requestSucc(ResponseVerification("0"))
                }
            })
    }
}