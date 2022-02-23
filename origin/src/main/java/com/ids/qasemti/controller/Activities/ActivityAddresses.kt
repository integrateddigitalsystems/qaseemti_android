package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.type.LatLng
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterAddress
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*

import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.fragment_addresses.*
import kotlinx.android.synthetic.main.item_address.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ActivityAddresses : ActivityBase() , RVOnItemClickListener {

    var array : ArrayList<ResponseAddress> = arrayListOf()
    var from : String ?=""
    var submitted : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_addresses)
        AppHelper.setAllTexts(rootLayout,this)
        init()
        listeners()
        getAddresses()


    }

    override fun onResume() {
        super.onResume()
        if(!MyApplication.fromAdd!!||from=="account") {
            getAddresses()
        }else{
            MyApplication.fromAdd = false
            val intent = Intent()
            intent.putExtra("lat", MyApplication.selectedAddress!!.lat!!.toDouble())
            intent.putExtra("long",  MyApplication.selectedAddress!!.long!!.toDouble())

            /*var latLng = com.google.android.gms.maps.model.LatLng(
                array.get(position).lat!!.toDouble(),
                array.get(position).long!!.toDouble()
            )*/
         //   MyApplication.selectedAddress = array.get(position)
            intent.putExtra(
                "address",
                MyApplication.selectedAddress!!.province + " ," + MyApplication.selectedAddress!!.area + " ," +  MyApplication.selectedAddress!!.block + " ," +  MyApplication.selectedAddress!!.street
            )
            intent.putExtra("submitted",MyApplication.submitted)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
    fun init(){
        try {
            var title = intent.getStringExtra("mapTitle")
            tvPageTitle.setColorTypeface(this, R.color.white, title!!, true)
        }catch (ex:Exception){

        }
        try {
            from = intent.getStringExtra("from")!!
        }catch (ex:Exception){

        }
        btBackTool.onOneClick {
            super.onBackPressed()
        }
        btBackTool.show()
        AppHelper.setLogoTint(btBackTool,this,R.color.white)
     }

    fun getAddresses(){

            loading.show()

        var newReq = RequestUserStatus(MyApplication.userId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getAddresses(newReq)?.enqueue(object : Callback<ResponseMainAddress> {
                override fun onResponse(
                    call: Call<ResponseMainAddress>,
                    response: Response<ResponseMainAddress>
                ) {
                    try {
                        array.clear()
                        array.addAll(response.body()!!.addresses)
                        setData()
                    } catch (E: java.lang.Exception) {

                           setData()
                    }
                }

                override fun onFailure(call: Call<ResponseMainAddress>, throwable: Throwable) {
                        loading.hide()
                }
            })
    }
    private fun listeners(){
        btAddNew.onOneClick{
            MyApplication.finish = true
            startActivity(Intent(this,ActivityMapAddress::class.java))
        }
    }

    private fun setData(){

        var adapter = AdapterAddress(array,this,this)
        rvAddresses.layoutManager = LinearLayoutManager(this)
        rvAddresses.adapter = adapter
        rvAddresses.isNestedScrollingEnabled = false

        if(array.size==0){
            rvAddresses.hide()
            llNodata.show()
        }else{
            rvAddresses.show()
            llNodata.hide()
        }

        loading.hide()

    }

    fun nextStep(response:ResponseMessage){
        if(response.message.equals("success")){
            getAddresses()
        }else{
            AppHelper.createDialog(this@ActivityAddresses,AppHelper.getRemoteString("Failed",this@ActivityAddresses))
            loading.hide()
        }
    }
    fun deleteAddress(addId : Int){

        loading.show()

        var newReq = RequestDeleteAddress(MyApplication.userId,addId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.deleteAddress(newReq)?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(
                    call: Call<ResponseMessage>,
                    response: Response<ResponseMessage>
                ) {
                    try {
                        nextStep(response.body()!!)
                    } catch (E: java.lang.Exception) {
                        AppHelper.createDialog(this@ActivityAddresses,AppHelper.getRemoteString("Failed",this@ActivityAddresses))
                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    AppHelper.createDialog(this@ActivityAddresses,AppHelper.getRemoteString("Failed",this@ActivityAddresses))
                    loading.hide()
                }
            })
    }


    override fun onItemClicked(view: View, position: Int) {

        if(view.id==R.id.btDeleteAddress){
            if (AppHelper.isOnline(this)) {
                AppHelper.createYesNoDialog(this,AppHelper.getRemoteString("yes",this),AppHelper.getRemoteString("Cancel",this),AppHelper.getRemoteString("are_you_sure_delete",this)){
                    deleteAddress(array.get(position).addressId!!.toInt())
                }
            }else{
                AppHelper.createDialog(this,AppHelper.getRemoteString("no_internet",this))
            }

        }else {
            if (!MyApplication.fromProfile!!) {
                val intent = Intent()
                intent.putExtra("lat", array.get(position).lat!!.toDouble())
                intent.putExtra("long", array.get(position).long!!.toDouble())
                var addr = ""
                MyApplication.selectedAddress = array.get(position)
                addr = AppHelper.getAddressText(MyApplication.selectedAddress!!)
                /*if (!array.get(position).desc.equals("null") && !array.get(position).desc.isNullOrEmpty()) {
                    addr = array.get(position).desc!!
                }
                if (!array.get(position).street.equals("null") && !array.get(position).street.isNullOrEmpty()) {
                    addr = addr + "," + array.get(position).street
                }
                if (!array.get(position).bldg.equals("null") && !array.get(position).bldg.isNullOrEmpty()) {
                    addr = addr + "," + array.get(position).bldg
                }
                if (!array.get(position).floor.equals("null") && !array.get(position).floor.isNullOrEmpty()) {
                    addr = addr + "," + array.get(position).floor
                }*/
                intent.putExtra(
                    "address",
                    addr
                )
                intent.putExtra(
                    "submitted",
                    true
                )
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}