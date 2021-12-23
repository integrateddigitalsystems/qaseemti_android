package com.ids.qasemti.controller.Activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.model.LatLng
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.ResponseGeoAddress
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.activity_new_address.*
import kotlinx.android.synthetic.main.activity_place_order.*
import kotlinx.android.synthetic.main.activity_select_address.*
import kotlinx.android.synthetic.main.activity_select_address.rootLayout
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class ActivitySelectAddress : AppCompactBase() , ApiListener {

    var latLng: LatLng? = null
    var REQUEST_LOCATION = 1005
    var firstTime: Boolean = true
    var resultLauncher: ActivityResultLauncher<Intent>? = null
    var addressName: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_address)
        AppHelper.setAllTexts(rootLayout, this)
        init()
        setListeners()
    }


    /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)

     }*/

    fun init() {
        tvPageTitle.setColorTypeface(
            this,
            R.color.white,
            AppHelper.getRemoteString("select_address", this),
            true
        )
        AppHelper.setLogoTint(btBackTool, this, R.color.white)
        btBackTool.show()

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {


                    val extras = result.data!!.extras
                    if (extras != null) {

                        try {
                            var lat = extras.getDouble("lat")
                            var long = extras.getDouble("long")
                            latLng = LatLng(lat, long)
                        } catch (ex: Exception) {
                        }
                        var tryy = extras.getString("lat")

                        var sumbtted = extras.getBoolean("submitted")

                        addressName = extras.getString("address")
                        if (sumbtted) {
                            if (!addressName.isNullOrEmpty()) {
                                val intent = Intent()

                                val extras = Bundle()
                                MyApplication.myAddress = addressName
                                extras.putString(
                                    "address",
                                    addressName
                                )
                                if (latLng != null) {
                                    extras.putDouble(
                                        "lat",
                                        latLng!!.latitude
                                    )
                                    extras.putDouble(
                                        "long",
                                        latLng!!.longitude
                                    )
                                }
                                intent.putExtras(extras)


                                setResult(RESULT_OK, intent)
                                finish()
                            } else {
                                AppHelper.createDialog(this, "Please pick location")
                            }
                        }

                        Toast.makeText(this, addressName, Toast.LENGTH_SHORT).show()
                    }


                }
            }
    }

    fun setListeners() {

        MyApplication.fromProfile = false
        btContinueAddress.setOnClickListener {


            if (!addressName.isNullOrEmpty()) {
                val intent = Intent()

                val extras = Bundle()
                extras.putString(
                    "address",
                    addressName
                )
                if (latLng != null) {
                    extras.putDouble(
                        "lat",
                        latLng!!.latitude
                    )
                    extras.putDouble(
                        "long",
                        latLng!!.longitude
                    )
                }
                intent.putExtras(extras)


                setResult(RESULT_OK, intent)
                finish()
            } else {
                AppHelper.createDialog(this, "Please pick location")
            }
        }

        btBackTool.onOneClick {
            super.onBackPressed()
        }
        llCurrentLocation.onOneClick {

            getCurrentLocation()

        }
        llLocationMap.onOneClick {
            resultLauncher!!.launch(
                Intent(this, ActivityMapAddress::class.java)
                    .putExtra("mapTitle", AppHelper.getRemoteString("LocationOnMap", this)))

        }
        llSavedLocation.onOneClick {
            MyApplication.addNew = false
            resultLauncher!!.launch(
                Intent(this, ActivityAddresses::class.java)
                    .putExtra("mapTitle", AppHelper.getRemoteString("SavedLocation", this))
            )

        }
        llNewAddress.onOneClick {
            MyApplication.finish = true
            MyApplication.addNew = true
            resultLauncher!!.launch(Intent(this, ActivityMapAddress::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        firstTime = true

    }

    private fun getCurrentLocation() {
        loading.show()
        val locationResult = object : MyLocation.LocationResult() {
            override fun gotLocation(location: Location?) {

                if (location != null) {
                    val lat = location!!.latitude
                    val lon = location.longitude
                    //toast("$lat --SLocRes-- $lon")
                    latLng = LatLng(lat, lon)
                    loading.show()



                   /* Thread {
                        var address : Address?=null
                        try {
                            val myLocation = Geocoder(this@ActivitySelectAddress, Locale.getDefault())
                            val myList = myLocation.getFromLocation(lat, lon, 1)
                            address = myList[0]
                        }catch (ex:Exception){
                            Log.wtf("LOCEX",ex.toString())

                        }

                        runOnUiThread {
                            MyApplication.selectedCurrentAddress = address
                        }
                    }.start()*/



                   /*
                    MyApplication.selectedCurrentAddress = AppHelper.getAddressLoc(
                        latLng!!.latitude,
                        latLng!!.longitude,
                        this@ActivitySelectAddress
                    )*/

                     if(firstTime) {
                         var latLng = LatLng(lat,lon)
                         firstTime = false
                         CallAPIs.getAddressName(latLng,this@ActivitySelectAddress,this@ActivitySelectAddress)

                     }


                } else {
                    toast("cannot detect location")
                }

                loading.hide()
            }

        }

        val myLocation = MyLocation()
        myLocation.getLocation(this, locationResult)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.checkSelfPermission(
                    this@ActivitySelectAddress,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this@ActivitySelectAddress,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && requestCode == AppConstants.REQUEST_LOCATION_PERMISSION
            ) {
                getCurrentLocation()
            }

        } else {
            toast("Please accept requested permission in order to detect your current location")
            loading.hide()
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    override fun onDataRetrieved(success: Boolean, response: Any, apiId: Int) {

        if(success) {

            MyApplication.selectedCurrentAddress = AppHelper.getAddressNames(response as ResponseGeoAddress)
            resultLauncher!!.launch(
                Intent(
                    this@ActivitySelectAddress,
                    ActivityAddNewAddress::class.java
                ).putExtra("from", "current")
            )
            loading.hide()
        }
    }
}
