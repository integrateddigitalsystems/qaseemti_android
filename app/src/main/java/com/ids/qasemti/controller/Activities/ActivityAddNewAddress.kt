package com.ids.qasemti.controller.Activities


import android.content.Intent
import android.os.Bundle
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.hideView
import com.ids.qasemti.utils.showView
import kotlinx.android.synthetic.main.activity_new_address.*
import kotlinx.android.synthetic.main.toolbar.*


class ActivityAddNewAddress : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_address)
        AppHelper.setAllTexts(rootLayout)
        init()
        listeners()

    }

    private fun init(){
        btDrawer.hideView()
        btBack.showView()
    }

    private fun listeners(){
        btBack.setOnClickListener{super.onBackPressed()}
        btMapAddress.setOnClickListener{startActivity(Intent(this,ActivityMapAddress::class.java))}
    }
}