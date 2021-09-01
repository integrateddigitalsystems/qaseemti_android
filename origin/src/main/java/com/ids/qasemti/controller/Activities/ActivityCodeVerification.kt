package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.show
import kotlinx.android.synthetic.main.activity_code_verification.*

class ActivityCodeVerification : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_verification)
        AppHelper.setAllTexts(rootlayoutCodeVerification)
        pvCode.setOnFocusChangeListener { view, b ->
            pvCode.text!!.clear()
        }

        if(MyApplication.isClient){
            btVerifyCode.hide()
            llClientVerfCode.show()

            btRegisterVerf.setOnClickListener {
                startActivity(Intent(this,ActivityAccountStatus::class.java))
            }
        }else{
            btVerifyCode.show()
            llClientVerfCode.hide()

            btVerifyCode.setOnClickListener {
                startActivity(Intent(this,ActivityAccountStatus::class.java))
            }


        }
    }
}