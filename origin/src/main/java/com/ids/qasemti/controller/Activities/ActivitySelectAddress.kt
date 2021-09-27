package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.onOneClick
import com.ids.qasemti.utils.setColorTypeface
import com.ids.qasemti.utils.show
import kotlinx.android.synthetic.main.activity_place_order.*
import kotlinx.android.synthetic.main.activity_select_address.*
import kotlinx.android.synthetic.main.activity_select_address.rootLayout
import kotlinx.android.synthetic.main.toolbar.*

class ActivitySelectAddress: AppCompactBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_address)
        AppHelper.setAllTexts(rootLayout,this)
        init()
        setListeners()
    }

    fun init(){
        tvPageTitle.setColorTypeface(this,R.color.white,AppHelper.getRemoteString("select_delivery_address",this),true)
        AppHelper.setLogoTint(btBackTool,this,R.color.white)
        btBackTool.show()
    }
    fun setListeners(){


        btBackTool.onOneClick {
            super.onBackPressed()
        }
        llCurrentLocation.onOneClick {
        }
        llLocationMap .onOneClick {
            startActivity(Intent(this,ActivityMap::class.java)
                .putExtra("mapTitle",AppHelper.getRemoteString("LocationOnMap",this)))
        }
        llSavedLocation.onOneClick {
            startActivity(Intent(this,ActivityAddresses::class.java)
                .putExtra("mapTitle",AppHelper.getRemoteString("SavedLocation",this)))
        }
        llNewAddress.onOneClick {
            startActivity(Intent(this,ActivityAddNewAddress::class.java)
                .putExtra("mapTitle",AppHelper.getRemoteString("NewAddress",this)))
        }
    }
}
