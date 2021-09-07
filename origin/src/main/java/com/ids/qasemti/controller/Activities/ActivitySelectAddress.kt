package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.utils.AppHelper
import kotlinx.android.synthetic.main.activity_select_address.*
import kotlinx.android.synthetic.main.toolbar.*

class ActivitySelectAddress: AppCompactBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_address)
        setListeners()
    }

    fun setListeners(){
        AppHelper.setLogoTint(btBackTool,this,R.color.white)
        btBackTool.setOnClickListener {
            super.onBackPressed()
        }
        llCurrentLocation.setOnClickListener {
        }
        llLocationOnMap.setOnClickListener {
            startActivity(Intent(this,ActivityMap::class.java))
        }
        llSavedLocation.setOnClickListener {
            startActivity(Intent(this,ActivityAddresses::class.java))
        }
        llNewAddress.setOnClickListener {
            startActivity(Intent(this,ActivityAddNewAddress::class.java))
        }
    }
}
