package com.ids.qasemti.controller.Activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.model.LatLng
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.ResponseAddress
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.onOneClick
import com.ids.qasemti.utils.setColorTypeface
import com.ids.qasemti.utils.show
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.activity_new_address.*
import kotlinx.android.synthetic.main.activity_place_order.*
import kotlinx.android.synthetic.main.activity_select_address.*
import kotlinx.android.synthetic.main.activity_select_address.rootLayout
import kotlinx.android.synthetic.main.toolbar.*

class ActivitySelectAddress : AppCompactBase() {

    var latLng: LatLng? = null
    var REQUEST_LOCATION = 5
    var addressName : String ?=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_address)
        AppHelper.setAllTexts(rootLayout, this)
        init()
        setListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOCATION) {
            if (resultCode == RESULT_OK) {
                val extras = data!!.extras
                if (extras != null) {

                    try {
                        var lat = extras.getDouble("lat")
                        var long = extras.getDouble("long")
                        latLng = LatLng(lat,long)
                    }catch (ex:Exception){
                    }

                    addressName = extras.getString("address")
                }

            }
        }
    }

    fun init() {
        tvPageTitle.setColorTypeface(
            this,
            R.color.white,
            AppHelper.getRemoteString("select_address", this),
            true
        )
        AppHelper.setLogoTint(btBackTool, this, R.color.white)
        btBackTool.show()
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
                if(latLng!=null){
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

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

            }
            try {
                val locationManager =
                    getSystemService(Context.LOCATION_SERVICE) as LocationManager
                var gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                latLng = LatLng(gps_loc!!.latitude, gps_loc!!.longitude)
                addressName = AppHelper.getAddress(latLng!!.latitude, latLng!!.longitude, this)
                Toast.makeText(this, "Current Location Saved", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error getting current Location", Toast.LENGTH_SHORT).show()
            }
        }
        llLocationMap.onOneClick {
            startActivityForResult(
                Intent(this, ActivityMapAddress::class.java)
                    .putExtra("mapTitle", AppHelper.getRemoteString("LocationOnMap", this)),
                REQUEST_LOCATION
            )
        }
        llSavedLocation.onOneClick {
            startActivityForResult(
                Intent(this, ActivityAddresses::class.java)
                    .putExtra("mapTitle", AppHelper.getRemoteString("SavedLocation", this)),
                REQUEST_LOCATION
            )
        }
        llNewAddress.onOneClick {
            startActivityForResult(
                Intent(this, ActivityAddNewAddress::class.java)
                    .putExtra("mapTitle", AppHelper.getRemoteString("address", this)),
                REQUEST_LOCATION
            )
        }
    }
}
