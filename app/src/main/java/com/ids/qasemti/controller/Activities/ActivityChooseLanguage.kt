package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
            changeLanguage(AppConstants.LANG_ARABIC)

        }

        btToEnglish.setOnClickListener {
            changeLanguage(AppConstants.LANG_ENGLISH)
        }


    }

    fun changeLanguage(language: String) {
        when (language) {
            AppConstants.LANG_ARABIC -> Locale.setDefault(Locale("ar"))
            AppConstants.LANG_ENGLISH -> Locale.setDefault(Locale.ENGLISH)
            "0" -> {
                Locale.setDefault(Locale.ENGLISH)
            }
        }

        val configuration = Configuration()
        var x = Locale.getDefault()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(Locale.getDefault())
            configuration.setLayoutDirection(Locale.getDefault())

        } else
            configuration.locale = Locale.getDefault()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(
                configuration,
                resources.displayMetrics
            )
        }
        MyApplication.languageCode = language
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, ActivityMobileRegistration::class.java))
        }, 500)

    }


}