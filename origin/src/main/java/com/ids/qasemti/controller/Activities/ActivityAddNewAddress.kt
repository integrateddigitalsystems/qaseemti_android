package com.ids.qasemti.controller.Activities


import android.content.Intent
import android.os.Bundle
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_new_address.*
import kotlinx.android.synthetic.main.toolbar.*


class ActivityAddNewAddress : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_address)
        AppHelper.setAllTexts(rootLayout,this)
        init()
        listeners()

    }

    private fun init(){
        btDrawer.hide()
        btBackTool.show()
        AppHelper.setLogoTint(btBackTool,this,R.color.redPrimary)
    }

    private fun listeners(){
        btBackTool.onOneClick{super.onBackPressed()}
        var title = intent.getStringExtra("mapTitle")
        tvPageTitle.setColorTypeface(this,R.color.redPrimary,title!!,true)
        btMapAddress.onOneClick{startActivity(Intent(this,ActivityMapAddress::class.java))}
    }
}