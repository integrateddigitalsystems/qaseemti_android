package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.Fragments.FragmentHomeClient
import com.ids.qasemti.controller.Fragments.FragmentHomeSP
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_choose_language.*
import java.util.*

class ActivityChooseLanguage : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_language)
        AppHelper.setAllTexts(rootLayoutChooseLogin,this)

        btToArabic.onOneClick {
            changeLanguage(AppConstants.LANG_ARABIC)

        }

        btToEnglish.onOneClick {
            changeLanguage(AppConstants.LANG_ENGLISH)
        }


        logoClient.onOneClick {
            startActivity(Intent(this,ActivityHome::class.java))
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

        if(MyApplication.isClient){
            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_CLIENT
            MyApplication.selectedFragment = FragmentHomeClient()
        }else{
            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_SP
            MyApplication.selectedFragment = FragmentHomeSP()
        }
        MyApplication.selectedPos = 2
        Handler(Looper.getMainLooper()).postDelayed({
            if(MyApplication.isClient) {
               AppHelper.goHome(this)
            }else{
                MyApplication.isSignedIn = true
                startActivity(Intent(this, ActivityMobileRegistration::class.java))
            }
        }, 500)


    }


}