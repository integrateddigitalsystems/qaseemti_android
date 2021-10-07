package com.ids.qasemti.controller.Activities

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_select_address.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*


class ActivitySelectAddress: AppCompactBase() {


    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_address)
        AppHelper.setAllTexts(rootLayout,this)
        init()
        setListeners()
    }

    fun init(){
        tvPageTitle.setColorTypeface(this,R.color.white,AppHelper.getRemoteString("select_address",this),true)
        AppHelper.setLogoTint(btBackTool,this,R.color.white)
        btBackTool.show()
    }
    fun setListeners(){


        btBackTool.onOneClick {
            super.onBackPressed()
        }
        llCurrentLocation.onOneClick {
            getCurrentLocation()
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
                .putExtra("mapTitle",AppHelper.getRemoteString("address",this)))
        }
    }


    private fun getCurrentLocation(){
        loading.show()


    }



}
