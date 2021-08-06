package com.ids.qasemti.controller.Activities


import android.os.Bundle
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.FirebaseLocalizeArray

import com.ids.qasemti.utils.AppConstants.FIREBASE_FORCE_UPDATE
import com.ids.qasemti.utils.AppConstants.FIREBASE_LOCALIZE
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.logw
import kotlinx.android.synthetic.main.activity_splash.*


class ActivitySplash : ActivityBase() {
    var  mFirebaseRemoteConfig : FirebaseRemoteConfig?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //  MyApplication.isLoggedIn = true
        getFirebasePrefs()


}


    private fun getFirebasePrefs(){
        mFirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        mFirebaseRemoteConfig!!.setConfigSettingsAsync(configSettings)
        mFirebaseRemoteConfig!!.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                     MyApplication.localizeArray = Gson().fromJson(mFirebaseRemoteConfig!!.getString(FIREBASE_LOCALIZE), FirebaseLocalizeArray::class.java)
                     AppHelper.setAllTexts(rootLayout)
                }
            }
    }
}
