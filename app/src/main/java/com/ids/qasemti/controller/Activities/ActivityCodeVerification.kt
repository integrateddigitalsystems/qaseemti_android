package com.ids.qasemti.controller.Activities

import android.os.Bundle
import android.view.View
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.utils.AppHelper
import kotlinx.android.synthetic.main.activity_choose_language.*
import kotlinx.android.synthetic.main.activity_choose_language.rootLayoutChooseLogin
import kotlinx.android.synthetic.main.activity_code_verification.*
import kotlinx.android.synthetic.main.white_logo_layout.*

class ActivityCodeVerification : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_verification)
        AppHelper.setAllTexts(rootlayoutCodeVerification)
        pvCode.setOnFocusChangeListener { view, b ->
            pvCode.text!!.clear()
        }
    }
}