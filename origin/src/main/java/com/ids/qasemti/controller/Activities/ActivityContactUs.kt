package com.ids.qasemti.controller.Activities

import android.os.Bundle
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.RequestContactUs
import com.ids.qasemti.model.RequestNotifications
import com.ids.qasemti.model.ResponseNotification
import com.ids.qasemti.model.ResponseUpdate
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ActivityContactUs: AppCompactBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        AppHelper.setAllTexts(rootLayout,this)
        init()

    }
    fun init(){

        btBackTool.show()
        btBackTool.setOnClickListener {
            super.onBackPressed()
        }
       AppHelper.setLogoTint(btBackTool,this,R.color.white)
        tvPageTitle.setColorTypeface(this,R.color.white,"",true)
        tvPageTitle.textRemote("ContactAdministrator",this)
        tvPageTitle.show()

        btContactUs.onOneClick {
            if(etFullNameContact.text.isNullOrEmpty()||etEmailContact.text.isNullOrEmpty()||etPhoneContact.text.isNullOrEmpty()||etMessageContact.text.isNullOrEmpty()||etSubjectContact.text.isNullOrEmpty()){
                AppHelper.createDialog(this,AppHelper.getRemoteString("fill_all_field",this))
            }else if(!AppHelper.isEmailValid(etEmailContact.text.toString())){
                AppHelper.createDialog(this,AppHelper.getRemoteString("email_valid_error",this))
            }else{
                sendContact()
            }
        }
    }
    fun resultContact(req:Int){
        if(req==1){
            AppHelper.createDialog(this,AppHelper.getRemoteString("success",this))
        }else{
            AppHelper.createDialog(this,AppHelper.getRemoteString("failure",this))
        }
        try {
            loading.hide()
        }catch (ex: Exception){

        }
        etFullNameContact.text.clear()
        etEmailContact.text.clear()
        etMessageContact.text.clear()
        etPhoneContact.text.clear()
        etSubjectContact.text.clear()
    }
    fun sendContact(){
        try {
            loading.show()
        }catch (ex: Exception){

        }
        var newReq = RequestContactUs(etFullNameContact.text.toString(),etPhoneContact.text.toString(),etEmailContact.text.toString(),etSubjectContact.text.toString(),etMessageContact.text.toString())
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.contactUs(
                newReq
            )?.enqueue(object : Callback<ResponseUpdate> {
                override fun onResponse(call: Call<ResponseUpdate>, response: Response<ResponseUpdate>) {
                    try{
                        resultContact(response.body()!!.result!!.toInt())
                    }catch (E: java.lang.Exception){
                        resultContact(0)
                    }
                }
                override fun onFailure(call: Call<ResponseUpdate>, throwable: Throwable) {
                    resultContact(0)
                }
            })
    }
}