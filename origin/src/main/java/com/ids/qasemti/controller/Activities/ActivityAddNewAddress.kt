
package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.maps.model.LatLng
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.Fragments.FragmentAccount
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import com.ids.qasemti.utils.AppHelper.Companion.toEditable
import kotlinx.android.synthetic.main.activity_new_address.*
import kotlinx.android.synthetic.main.activity_new_address.etAddressName
import kotlinx.android.synthetic.main.activity_new_address.etAddressProvince
import kotlinx.android.synthetic.main.activity_new_address.etApartment
import kotlinx.android.synthetic.main.activity_new_address.etArea
import kotlinx.android.synthetic.main.activity_new_address.etAvenue
import kotlinx.android.synthetic.main.activity_new_address.etBlock
import kotlinx.android.synthetic.main.activity_new_address.etBuilding
import kotlinx.android.synthetic.main.activity_new_address.etFloor
import kotlinx.android.synthetic.main.activity_new_address.etMoreDetails
import kotlinx.android.synthetic.main.activity_new_address.etStreet
import kotlinx.android.synthetic.main.layout_profile.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*


class ActivityAddNewAddress : ActivityBase() {

    var REQUEST_CODE = 1005
    var from = ""
    var latlng: LatLng? = null
    var resultLauncher: ActivityResultLauncher<Intent>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_address)
        AppHelper.setAllTexts(rootLayout, this)
        init()
        listeners()
        setHint()

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
            etMoreDetails.text.toString(),
            etBuilding.text.toString(),
            etArea.text.toString(),
            etAvenue.text.toString(),
            etApartment.text.toString(),
            etBlock.text.toString(),
            etAddressProvince.text.toString(),
            etAddressProvince.text.toString()
        )
          intent.putExtra("lat",latlng!!.latitude)
          intent.putExtra("long",latlng!!.longitude)
        //   var latLng = com.google.android.gms.maps.model.LatLng(array.get(position).lat!!.toDouble(), array.get(position).long!!.toDouble())
        intent.putExtra(
            "address",
            etAddressProvince.text.toString() + " ," + etStreet.text.toString() + " ," + etBuilding.text.toString() + " ," + etFloor.text.toString()
        )
        MyApplication.submitted = true
        intent.putExtra(
            "submitted",
            true
        )
        setResult(RESULT_OK, intent)
        finish()
    }

    fun setHint(){
        etAddressName.hint = etAddressName.hint.toString() + "*"
        etAddressProvince.hint = etAddressProvince.hint.toString() + "*"
        etBuilding.hint = etBuilding.hint.toString() + "*"
        etArea.hint = etArea.hint.toString() + "*"
        etBlock.hint = etBlock.hint.toString() + "*"
        etFloor.hint = etFloor.hint.toString() + "*"
        etStreet.hint = etStreet.hint.toString() + "*"
    }
    fun addAddress() {

            loading.show()
        var addressId=0
        try{
        if(!MyApplication.isClient && !MyApplication.addNewAddress)
            addressId= MyApplication.selectedUser!!.addresses!![0].addressId!!.toInt()}catch (e:Exception){}
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
            addressId,
            etAddressName.text.toString(),
            etStreet.text.toString(),
            etBuilding.text.toString(),
            etFloor.text.toString(),
            etMoreDetails.text.toString(),
            "",
            etAddressProvince.text.toString(),
            etArea.text.toString(),
            etBlock.text.toString(),
            etAvenue.text.toString(),
            etApartment.text.toString()
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
                        if(MyApplication.isClient)
                            setData()
                        else{
                            MyApplication.register=true
                            MyApplication.selectedPos=4
                            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_ACCOUNT
                            MyApplication.selectedFragment = FragmentAccount()
                            startActivity(Intent(this@ActivityAddNewAddress,ActivityHome::class.java))
                            finishAffinity()

                        }
                    } catch (E: java.lang.Exception) {

                    }

                    loading.hide()
                }

                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    loading.hide()
                }
            })

    }

    fun setUpData(ll:LatLng){
        var address: Address? = null
        Thread {
            try {
                try {
                    latlng =ll
                    val myLocation = Geocoder(this, Locale.getDefault())
                    val myList =
                        myLocation.getFromLocation(latlng!!.latitude, latlng!!.longitude, 1)
                    address = myList[0]
                } catch (ex: Exception) {

                }
            }catch (ex:Exception){
                Log.wtf("LOCEX",ex.toString())

            }

            runOnUiThread {
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
                if(etStreet.text.toString().isEmpty() && !address!!.featureName.isNullOrEmpty()){
                    try {
                        etStreet.text = address!!.featureName.toEditable()
                    } catch (ex: Exception) {
                        etStreet.text.clear()
                    }
                }
               /* try {
                    etAddressName.text = address!!.featureName.toEditable()
                } catch (ex: Exception) {
                    etAddressName.text.clear()
                }*/

                editData(llAddForm)
            }
        }.start()



    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == RESULT_OK) {


            var address: Address? = null
            val extras = intent.extras
            latlng = LatLng(extras!!.getDouble("lat"), extras.getDouble("long"))
            setUpData(latlng!!)
            }


        }


    private fun init() {
        val lat = MyApplication.latSelected
        val long = MyApplication.longSelected
        btDrawer.hide()
        btBackTool.show()
        try {
            from = intent.getStringExtra("from")!!
        }catch (ex:Exception){

        }

        tvPageTitle.setColorTypeface(this,R.color.primary,AppHelper.getRemoteString("address",this),true)
        AppHelper.setLogoTint(btBackTool, this, R.color.primary)
        if (MyApplication.fromProfile!!)
            btOnlyOnce.hide()
        else
            btOnlyOnce.show()

        try{

        if(MyApplication.isClient){
        if(from.equals("current")){
            setUpData(LatLng(MyApplication.selectedCurrentAddress!!.latitude, MyApplication.selectedCurrentAddress!!.longitude))
            btSaveAddress.text =AppHelper.getRemoteString("select_address",this)
            btOnlyOnce.hide()
        }else {
            setUpData(LatLng(lat!!.toDouble(), long!!.toDouble()))
        }}else{
            btOnlyOnce.hide()
            var lat=0.0
            var long=0.0
            try{ lat=intent.extras!!.getDouble("lat",0.0)}catch (e:Exception){}
            try{ long=intent.extras!!.getDouble("long",0.0)}catch (e:Exception){}
            latlng = LatLng(lat, long)
            setUpData(latlng!!)
        }

        }catch (e:Exception){}

    }

    fun editData(v:View){
        if (v is ViewGroup) {
            val vg = v as ViewGroup
            for (i in 0 until vg.childCount) {
                val child = vg.getChildAt(i)
                // recursively call this method
                editData(child)
            }
        } else if (v is EditText) {
            v.isEnabled = v.text.isNullOrEmpty()
        }
    }

    private fun listeners() {
        btBackTool.onOneClick { super.onBackPressed() }
        var title = intent.getStringExtra("mapTitle")
    //    tvPageTitle.setColorTypeface(this, R.color.redPrimary, title!!, true)
        btMapAddress.onOneClick {
            resultLauncher!!.launch(Intent(this, ActivityMapAddress::class.java))
        }
        etAddressProvince.onOneClick {
            resultLauncher!!.launch(Intent(this, ActivityMapAddress::class.java))
        }
        btSaveAddress.onOneClick {
            if (etAddressName.text.isNullOrEmpty() || etAddressProvince.text.isNullOrEmpty() || etBuilding.text.toString()
                    .isNullOrEmpty() || etFloor.text.toString()
                    .isNullOrEmpty() || etStreet.text.isNullOrEmpty()
            ) {
                AppHelper.createDialog(this, AppHelper.getRemoteString("fill_all_field", this))
            } else {
                if(MyApplication.isClient){
                MyApplication.fromAdd = true
                if(from=="current"){
                    setData()
                }else{
                    addAddress()
                }}else{
                    addAddress()
                }

            }
        }
        btOnlyOnce.onOneClick {

            if (etAddressName.text.isNullOrEmpty() || etAddressProvince.text.isNullOrEmpty() || etBuilding.text.toString()
                    .isNullOrEmpty() || etFloor.text.toString()
                    .isNullOrEmpty() || etStreet.text.isNullOrEmpty()
            ) {
                AppHelper.createDialog(this, AppHelper.getRemoteString("fill_all_field", this))
            } else {
                MyApplication.fromAdd = true
                setData()
            }
        }



    }
}