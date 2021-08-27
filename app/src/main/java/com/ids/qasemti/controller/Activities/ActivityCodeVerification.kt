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
            tvSubTitleVerf.visibility=View.VISIBLE
            btVerifyCode.visibility =View.GONE
            llClientVerfCode.visibility = View.VISIBLE
            tvCodeSentVef.text = getString(R.string.verification_code_sent)


            btRegisterVerf.setOnClickListener {
                startActivity(Intent(this,ActivityAccountStatus::class.java))
            }
        }else{
            tvSubTitleVerf.visibility=View.GONE
            btVerifyCode.visibility =View.VISIBLE
            llClientVerfCode.visibility = View.GONE
            tvCodeSentVef.text = getString(R.string.enter_the_4_digit_code_sent_to_you_from_qasemati)

            btVerifyCode.setOnClickListener {
                startActivity(Intent(this,ActivityAccountStatus::class.java))
            }


        }
    }
}