package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import com.ids.qasemti.utils.AppHelper.Companion.toEditable
import kotlinx.android.synthetic.main.activity_new_address.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*


class ActivityAddNewAddress : ActivityBase() {

    var REQUEST_CODE = 1000
    var latlng: LatLng? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_address)
        AppHelper.setAllTexts(rootLayout, this)
        init()
        listeners()

    }

    override fun onResume() {
        super.onResume()
        Log.wtf("log", "test_here")
    }

    fun setData() {
        var lat = ""
        var long = ""
        if (latlng != null) {
            lat = latlng!!.latitude.toString()
            long = latlng!!.longitude.toString()
        }
        MyApplication.selectedAddress = ResponseAddress(
            "",
            etAddressName.text.toString(),
            lat,
            long,
            etStreet.text.toString(),
            etFloor.text.toString(),
            etAddressBody.text.toString(),
            etBuilding.text.toString()
        )
        //  intent.putExtra("lat",array.get(position).lat)
        //  intent.putExtra("long",array.get(position).long)
        //   var latLng = com.google.android.gms.maps.model.LatLng(array.get(position).lat!!.toDouble(), array.get(position).long!!.toDouble())
        intent.putExtra(
            "address",
            etAddressBody.text.toString() + " ," + etStreet.text.toString() + " ," + etBuilding.text.toString() + " ," + etFloor.text.toString()
        )
        setResult(RESULT_OK, intent)
        finish()
    }

    fun addAddress() {

        try {
            loading.show()
        } catch (ex: Exception) {

        }
        var lat :Double ?= 0.0
        var long : Double ?=0.0
        try{
            lat = latlng!!.latitude
            long = latlng!!.longitude
        }catch (ex:Exception){}

        var newReq = RequestAddAddress(
            MyApplication.userId,
           lat,
            long,
           lat!!.toInt(),
            etAddressBody.text.toString(),
            etStreet.text.toString(),
            etBuilding.text.toString(),
            etFloor.text.toString(),
            etMoreDetails.text.toString()
        )
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.addClAddress(
                newReq
            )?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(
                    call: Call<ResponseMessage>,
                    response: Response<ResponseMessage>
                ) {
                    try {
                        setData()
                    } catch (E: java.lang.Exception) {

                    }

                    loading.hide()
                }

                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    loading.hide()
                }
            })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == RESULT_OK) {
            var address: Address? = null
            val extras = intent.extras
            if (extras != null) {
                try {
                    latlng = LatLng(extras.getDouble("lat"), extras.getDouble("long"))
                    val myLocation = Geocoder(this, Locale.getDefault())
                    val myList =
                        myLocation.getFromLocation(latlng!!.latitude, latlng!!.longitude, 1)
                    address = myList[0]
                } catch (ex: Exception) {

                }
                try {
                    etStreet.text = address!!.thoroughfare.toEditable()
                } catch (ex: Exception) {
                    etStreet.text.clear()
                }
                try {
                    etBuilding.text = address!!.premises.toEditable()
                } catch (ex: Exception) {
                    etBuilding.text.clear()
                }
                try {
                    etAddressName.text = address!!.featureName.toEditable()
                } catch (ex: Exception) {
                    etAddressName.text.clear()
                }
            }


        }
    }

    private fun init() {
        btDrawer.hide()
        btBackTool.show()
        AppHelper.setLogoTint(btBackTool, this, R.color.redPrimary)
        if (MyApplication.fromProfile!!)
            btOnlyOnce.hide()
        else
            btOnlyOnce.show()

    }


    private fun listeners() {
        btBackTool.onOneClick { super.onBackPressed() }
        var title = intent.getStringExtra("mapTitle")
        tvPageTitle.setColorTypeface(this, R.color.redPrimary, title!!, true)
        btMapAddress.onOneClick {
            startActivityForResult(
                Intent(
                    this,
                    ActivityMapAddress::class.java
                ), REQUEST_CODE
            )
        }
        etAddressBody.onOneClick {
            startActivityForResult(
                Intent(
                    this,
                    ActivityMapAddress::class.java
                ), REQUEST_CODE
            )
        }
        btSaveAddress.onOneClick {
            if (etAddressName.text.isNullOrEmpty() || etAddressBody.text.isNullOrEmpty() || etBuilding.text.toString()
                    .isNullOrEmpty() || etFloor.text.toString()
                    .isNullOrEmpty() || etStreet.text.isNullOrEmpty() || etMoreDetails.text.isNullOrEmpty()
            ) {
                AppHelper.createDialog(this, AppHelper.getRemoteString("fill_all_field", this))
            } else {
                MyApplication.useOnce = true
                addAddress()
            }
        }
        btOnlyOnce.onOneClick {

            if (etAddressName.text.isNullOrEmpty() || etAddressBody.text.isNullOrEmpty() || etBuilding.text.toString()
                    .isNullOrEmpty() || etFloor.text.toString()
                    .isNullOrEmpty() || etStreet.text.isNullOrEmpty() || etMoreDetails.text.isNullOrEmpty()
            ) {
                AppHelper.createDialog(this, AppHelper.getRemoteString("fill_all_field", this))
            } else {
                MyApplication.useOnce = true
                setData()
            }
        }
    }
}