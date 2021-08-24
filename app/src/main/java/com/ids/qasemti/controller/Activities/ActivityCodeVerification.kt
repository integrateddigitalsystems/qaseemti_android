package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppHelper
import kotlinx.android.synthetic.main.activity_choose_language.*
import kotlinx.android.synthetic.main.activity_choose_language.rootLayoutChooseLogin
import kotlinx.android.synthetic.main.activity_code_verification.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.no_logo_layout.*
import kotlinx.android.synthetic.main.white_logo_layout.*

class ActivityCodeVerification : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_verification)
        AppHelper.setAllTexts(rootlayoutCodeVerification)
        pvCode.setOnFocusChangeListener { view, b ->
            pvCode.text!!.clear()
        }

        if(MyApplication.isClient){
            btVerifyCode.visibility =View.GONE
            llClientVerfCode.visibility = View.VISIBLE
        }else{
            btVerifyCode.visibility =View.VISIBLE
            llClientVerfCode.visibility = View.GONE

            btVerifyCode.setOnClickListener {
                startActivity(Intent(this,ActivityAccountStatus::class.java))
            }

            btRegisterVerf.setOnClickListener {
                startActivity(Intent(this,ActivityAccountStatus::class.java))
            }
        }
    }
}