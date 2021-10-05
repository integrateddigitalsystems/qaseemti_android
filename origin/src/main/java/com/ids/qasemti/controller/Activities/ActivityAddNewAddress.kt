package com.ids.qasemti.controller.Activities


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.RequestAddAddress
import com.ids.qasemti.model.RequestUserStatus
import com.ids.qasemti.model.ResponseMainOrder
import com.ids.qasemti.model.ResponseMessage
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_new_address.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class ActivityAddNewAddress : ActivityBase() {

    var REQUEST_CODE = 1000
    var lat : Double ?=0.0
    var long : Double ?=0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_address)
        AppHelper.setAllTexts(rootLayout,this)
        init()
        listeners()

    }

    fun addAddress(){
            try {
                loading.show()
            }catch (ex: Exception){

            }
            var newReq = RequestAddAddress(MyApplication.userId,lat,long,lat!!.toInt(),etAddressBody.text.toString(),etStreet.text.toString(),etBuilding.text.toString(),etFloor.text.toString(),etMoreDetails.text.toString())
            RetrofitClient.client?.create(RetrofitInterface::class.java)
                ?.addClAddress(
                    newReq
                )?.enqueue(object : Callback<ResponseMessage> {
                    override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                        try{
                        }catch (E: java.lang.Exception){

                        }
                    }
                    override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {

                    }
                })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val extras = intent.extras
                if (extras != null){
                    lat = extras.getDouble("lat")
                    long = extras.getDouble("long")
                    etAddressBody.text = Editable.Factory.getInstance().newEditable(AppHelper.getAddress(lat!!,long!!,this))
                }

            }
        }
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
        btMapAddress.onOneClick{ startActivityForResult(Intent(this,ActivityMapAddress::class.java),REQUEST_CODE)}
        etAddressBody.onOneClick {startActivityForResult(Intent(this,ActivityMapAddress::class.java),REQUEST_CODE)  }
        btSaveAddress.onOneClick {
            if(etAddressName.text.isNullOrEmpty()||etAddressBody.text.isNullOrEmpty()||etBuilding.text.toString().isNullOrEmpty()||etFloor.text.toString().isNullOrEmpty()||etStreet.text.isNullOrEmpty()||etMoreDetails.text.isNullOrEmpty()){
                AppHelper.createDialog(this, AppHelper.getRemoteString("fill_all_field", this))
            }else {
                addAddress()
            }
        }
    }
}