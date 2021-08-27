package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.utils.AppHelper
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.btRegister
import kotlinx.android.synthetic.main.activity_register.etEmail
import kotlinx.android.synthetic.main.activity_register.etFirstName
import kotlinx.android.synthetic.main.activity_register.etLastName
import kotlinx.android.synthetic.main.activity_register_client.*
import kotlinx.android.synthetic.main.white_logo_layout.*

class ActivityRegistrationClient  : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_client)
    }

    fun init(){


        AppHelper.setAllTexts(rootLayoutRegistrationClient)
        AppHelper.setLogoTint(logo_main,this,R.color.white)


        btRegister.setOnClickListener {
            if(etFirstName.text.isNullOrEmpty() || etLastName.text.isNullOrEmpty() || etEmail.text.isNullOrEmpty()){
                AppHelper.createDialog(this,getString(R.string.please_fill_data))
            }else if(!AppHelper.isEmailValid(etEmail.text.toString()) ){
                AppHelper.createDialog(this,getString(R.string.enter_correct_email))
            }else{
                startActivity(Intent(this,ActivityCodeVerification::class.java))
            }
        }

    }
}