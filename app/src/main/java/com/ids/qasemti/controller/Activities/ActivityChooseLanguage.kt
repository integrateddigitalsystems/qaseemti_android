package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
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

        logoClient.setOnClickListener {
            MyApplication.isClient = !MyApplication.isClient
            if(MyApplication.isClient){
                Toast.makeText(this,"Client",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"Service",Toast.LENGTH_LONG).show()
            }
        }
        btToArabic.setOnClickListener {
            changeLanguage(AppConstants.LANG_ARABIC)

        }

        btToEnglish.setOnClickListener {
            changeLanguage(AppConstants.LANG_ENGLISH)
        }


    }

    fun changeLanguage(language: String) {
        MyApplication.languageCode = language
        when (language) {
            AppConstants.LANG_ARABIC -> LocaleUtils.setLocale(Locale(AppConstants.LANG_ARABIC))
            AppConstants.LANG_ENGLISH -> LocaleUtils.setLocale(Locale(AppConstants.LANG_ENGLISH))
            "0" -> {
                LocaleUtils.setLocale(Locale(AppConstants.LANG_ENGLISH))
            }
        }


        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, ActivityMobileRegistration::class.java))
        }, 500)

    }


}