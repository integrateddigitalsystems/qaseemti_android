package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.ids.qasemti.R

import com.ids.qasemti.controller.Adapters.AdapterGeneralSpinner
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.*
import com.ids.sampleapp.model.ItemSpinner
import kotlinx.android.synthetic.main.activity_mobile_registration.*
import kotlinx.android.synthetic.main.white_logo_layout.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

class ActivityMobileRegistration: ActivityBase(){


    override fun onBackPressed() {
        if(MyApplication.fromLogout) {
            AppHelper.createYesNoDialog(
                this,  AppHelper.getRemoteString("exit",this), AppHelper.getRemoteString("cancel",this), AppHelper.getRemoteString("sureExit",this)
            ) {
                finishAffinity()
                exitProcess(0)
            }
        }else{
            super.onBackPressed()
        }

        logo_main.onOneClick {
            Toast.makeText(this,"Howdy Gov",Toast.LENGTH_SHORT).show()
        }
        /*if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer(drawerLayout, true)
        } else {
            checkBack()
            super.onBackPressed()
        }*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_registration)
        AppHelper.setAllTexts(rootLayoutMobileRegister,this)
        AppHelper.setLogoTint(logo_main,this,R.color.white)


        if(MyApplication.isClient){
            btLogin.hide()
            llNewMember.show()
        }else{
            btLogin.show()
            llNewMember.hide()
        }

        tvRegisterNewMember.onOneClick {
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

        btLoginClient.onOneClick {
            if(etPhone.text.isNullOrEmpty()){
                AppHelper.createDialog(this,AppHelper.getRemoteString("fill_all_field",this))
            }else {
                MyApplication.isSignedIn = true
                startActivity(Intent(this, ActivityHome::class.java))
            }

        }

        btLogin.onOneClick {
            if(etPhone.text.isNullOrEmpty()){
                AppHelper.createDialog(this,AppHelper.getRemoteString("fill_all_field",this))
            }else {
                MyApplication.isSignedIn = true
                startActivity(Intent(this,ActivityRegistration::class.java))
            }

        }
    }
}