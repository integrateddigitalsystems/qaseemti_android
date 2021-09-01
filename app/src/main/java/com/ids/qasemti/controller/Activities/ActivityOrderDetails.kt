package com.ids.qasemti.controller.Activities

import android.os.Bundle
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.MyExceptionHandler
import kotlinx.android.synthetic.main.activity_mobile_registration.*
import kotlinx.android.synthetic.main.activity_mobile_registration.rootLayoutMobileRegister
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.curve_layout_home.*
import kotlinx.android.synthetic.main.toolbar.*

class ActivityOrderDetails: ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        AppHelper.setAllTexts(rootLayoutOrderDetails)
        tvPageTitle.text = getString(R.string.active_order_det)
        tvPageTitle.typeface = AppHelper.getTypeFace(this)

    }
}