package com.ids.qasemti.controller.Activities

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import androidx.core.widget.ImageViewCompat
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.utils.AppHelper
import kotlinx.android.synthetic.main.activity_mobile_registration.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.white_logo_layout.*

class ActivityRegistration : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        AppHelper.setAllTexts(rootLayoutRegistration)
        AppHelper.setLogoTint(logo_main,this)


        btRegister.setOnClickListener {
            if(etFirstName.text.isNullOrEmpty() || etLastName.text.isNullOrEmpty() || etEmail.text.isNullOrEmpty()){
                AppHelper.createDialog(this,getString(R.string.please_fill_data))
            }else if(AppHelper.isValidEmail(etEmail.text.toString()) ){

            }else{
                
            }
        }
    }
}