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
import com.ids.qasemti.controller.Fragments.FragmentHomeClient
import com.ids.qasemti.controller.Fragments.FragmentHomeSP
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_code_verification.*
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class ActivityCodeVerification : ActivityBase(), ApiListener {

    var firstTime = true
    var time = 60
    var first = true
    var timer: CountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_verification)
        AppHelper.setAllTexts(rootlayoutCodeVerification, this)
        /* pvCode.setOnFocusChangeListener { view, b ->
             pvCode.text!!.clear()
         }*/
        pvCode.requestFocus()
        pvCode.showKeyboard(true)

        tvTitleVerf.onOneClick {
            if (MyApplication.isClient) {
                MyApplication.userId = 51
                MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_CLIENT
                MyApplication.selectedFragment = FragmentHomeClient()
            } else {
                MyApplication.userId = 41
                MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_SP
                MyApplication.selectedFragment = FragmentHomeSP()
            }
            MyApplication.isSignedIn = true
            startActivity(Intent(this, ActivityHome::class.java))
            finish()
        }

        tvCodeSentVef.onOneClick {
            if (MyApplication.isClient) {
                MyApplication.userId = 51
            } else {
                MyApplication.userId = 41
            }
            MyApplication.isSignedIn = true
            startActivity(Intent(this, ActivityRegistration::class.java))
            finish()
        }

        if (MyApplication.isClient) {
            btVerifyCode.hide()
            llClientVerfCode.show()
            btCancelVerf.onOneClick {
                super.onBackPressed()
            }
            btRegisterVerf.onOneClick {
                verifyOTP()
                //startActivity(Intent(this,ActivityAccountStatus::class.java))
            }
        } else {
            btVerifyCode.show()
            llClientVerfCode.hide()

            btVerifyCode.onOneClick {
                verifyOTP()
                //startActivity(Intent(this,ActivityAccountStatus::class.java))
            }


        }
        timer = object : CountDownTimer(60000, 1015) {
            override fun onTick(millisUntilFinished: Long) {
                if (first) {
                    tvTimer.text = "1:00"
                    first = false
                } else {
                    tvTimer.text = "0:" + checkDigit(time)

                }

                time--

                if (time == 0) {
                    this.onFinish()
                }
            }

            override fun onFinish() {
                first = true
                tvTimer.text = ""
                tvTimerTitle.text =
                    AppHelper.getRemoteString("resend_code", this@ActivityCodeVerification)
                AppHelper.setTextColor(
                    this@ActivityCodeVerification,
                    tvTimerTitle,
                    R.color.button_blue
                )
                tvTimerTitle.onOneClick {
                    if (first) {
                        tvTimerTitle.text = AppHelper.getRemoteString(
                            "resend_code_in",
                            this@ActivityCodeVerification
                        )
                        AppHelper.setTextColor(
                            this@ActivityCodeVerification,
                            tvTimerTitle,
                            R.color.gray_font_title
                        )

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
                if (s.length == 4) {
                    verifyOTP()
                }

            }
        })


    }


    fun sendOTP() {
        pvCode.text!!.clear()
        var req = RequestOTP(MyApplication.selectedPhone, MyApplication.deviceId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.sendOTP(
                req
            )?.enqueue(object : Callback<ResponseUpdate> {
                override fun onResponse(
                    call: Call<ResponseUpdate>,
                    response: Response<ResponseUpdate>
                ) {
                    try {

                    } catch (E: java.lang.Exception) {
                    }
                }

                override fun onFailure(call: Call<ResponseUpdate>, throwable: Throwable) {
                }
            })
    }

    fun nextStep() {
        MyApplication.phoneNumber = MyApplication.selectedPhone
        MyApplication.isSignedIn = true
        startActivity(Intent(this, ActivityHome::class.java))
    }

    fun requestSucc(respone: ResponseVerification) {
        if (respone.result.equals("1")) {
            //    AppHelper.createDialog(this,"Correct Code")
            if (respone.user != null) {
                var fullnameEmail=""
                try{fullnameEmail=respone.user!!.firstName!!+respone.user!!.lastName!!+respone.user!!.email!!}catch (e:Exception){}
                if(fullnameEmail.isNotEmpty()){
                MyApplication.userId = respone.user!!.userId!!.toInt()
                CallAPIs.getUserStatus(this, this)
                }else{
                    MyApplication.isSignedIn = false
                    startActivity(Intent(this, ActivityRegistration::class.java))
                }

            } else {
                MyApplication.isSignedIn = false
                startActivity(Intent(this, ActivityRegistration::class.java))
            }
        } else {
            AppHelper.createDialog(this, "Incorrect Code")
            first = true
            timer!!.cancel()
            tvTimer.setText("")
            tvTimerTitle.onOneClick {
                if (first) {
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

    fun verifyOTP() {

        loading.show()
        var pv = pvCode.text.toString()
        var req = RequestVerifyOTP(pvCode.text.toString(), MyApplication.deviceId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.verifyOTP(
                req
            )?.enqueue(object : Callback<ResponseVerification> {
                override fun onResponse(
                    call: Call<ResponseVerification>,
                    response: Response<ResponseVerification>
                ) {
                    try {
                        loading.hide()
                        requestSucc(response.body()!!)
                    } catch (E: java.lang.Exception) {
                        requestSucc(ResponseVerification("0"))
                    }
                }

                override fun onFailure(call: Call<ResponseVerification>, throwable: Throwable) {
                    loading.hide()
                    requestSucc(ResponseVerification("0"))
                }
            })
    }

    override fun onDataRetrieved(success: Boolean, response: Any, apiId: Int) {
        var res = response as ResponseUserStatus
        MyApplication.userStatus = res
        if (res.suspended == 1) {
            AppHelper.createDialog(this, AppHelper.getRemoteString("suspended_user_msg", this))
        } else {
            if (MyApplication.isClient) {
                nextStep()
            }else{
                AppHelper.createDialog(
                    this,
                    AppHelper.getRemoteString(
                        "services_is_inactive",
                        this
                    )
                ) {
                    nextStep()
                }
            }

        }

    }
}