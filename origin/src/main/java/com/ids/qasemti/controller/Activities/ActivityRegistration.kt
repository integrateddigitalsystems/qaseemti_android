package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.onOneClick
import com.ids.qasemti.utils.show
import kotlinx.android.synthetic.main.activity_mobile_registration.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.white_logo_layout.*

class ActivityRegistration : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        AppHelper.setAllTexts(rootLayoutRegistration,this)
        AppHelper.setLogoTint(logo_main,this,R.color.white)

        if(MyApplication.isClient){
            rlNoLogo.show()
            llTopService.hide()
            tvRegisterTitle.hide()
        }else{
            rlNoLogo.hide()
            llTopService.show()
            tvRegisterTitle.show()
        }

        btRegister.onOneClick {
            if(etFirstName.text.isNullOrEmpty() || etLastName.text.isNullOrEmpty() || etEmail.text.isNullOrEmpty()){
                AppHelper.createDialog(this,AppHelper.getRemoteString("fill_all_field",this))
            }else if(!AppHelper.isEmailValid(etEmail.text.toString()) ){
                AppHelper.createDialog(this,AppHelper.getRemoteString("email_valid_error",this))
            }else{
                startActivity(Intent(this,ActivityCodeVerification::class.java))
            }
        }
    }
}