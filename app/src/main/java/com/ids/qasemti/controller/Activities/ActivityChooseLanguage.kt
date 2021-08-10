package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.LocaleUtils
import kotlinx.android.synthetic.main.activity_choose_language.*
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class ActivityChooseLanguage : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_language)
        AppHelper.setAllTexts(rootLayoutChooseLogin)

        btToArabic.setOnClickListener {
            AppHelper.changeLanguage(this,AppConstants.LANG_ARABIC)
            startActivity(Intent(this,ActivityMobileRegistration::class.java))
        }

        btToEnglish.setOnClickListener {
            AppHelper.changeLanguage(this,AppConstants.LANG_ENGLISH)
            startActivity(Intent(this,ActivityMobileRegistration::class.java))
        }


    }

}