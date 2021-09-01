package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.ids.qasemti.R

import com.ids.qasemti.controller.Adapters.AdapterGeneralSpinner
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.show
import com.ids.sampleapp.model.ItemSpinner
import kotlinx.android.synthetic.main.activity_mobile_registration.*
import kotlinx.android.synthetic.main.white_logo_layout.*
import kotlin.collections.ArrayList

class ActivityMobileRegistration: ActivityBase(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_registration)
        AppHelper.setAllTexts(rootLayoutMobileRegister)
        AppHelper.setLogoTint(logo_main,this,R.color.white)


        if(MyApplication.isClient){
            llNewMember.show()
        }else{
            llNewMember.hide()
        }

        tvRegisterNewMember.setOnClickListener {
            startActivity(Intent(this,ActivityRegistration::class.java))
        }


        var items: ArrayList<ItemSpinner> = arrayListOf()
        items.add(ItemSpinner(0, "961", ""))
        items.add(ItemSpinner(0, "965", ""))
        items.add(ItemSpinner(0, "1", ""))
        items.add(ItemSpinner(0, "31", ""))
        val adapterMobileCode = AdapterGeneralSpinner(
            this,
            R.layout.spinner_layout,
            items,
            AppConstants.LEFT_BLACK
        )

        spMobileCode.adapter = adapterMobileCode
        adapterMobileCode.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spMobileCode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }


        }

        btLogin.setOnClickListener {
            if(etPhone.text.isNullOrEmpty()){
                AppHelper.createDialog(this,getString(R.string.please_fill_data))
            }else {
                MyApplication.isSignedIn = true
                startActivity(Intent(this,ActivityRegistration::class.java))
            }

        }
    }
}