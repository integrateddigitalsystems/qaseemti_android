package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.type.LatLng
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterAddress
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*

import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.fragment_addresses.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ActivityAddresses : ActivityBase() , RVOnItemClickListener {

    var array : ArrayList<ResponseAddress> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_addresses)
        AppHelper.setAllTexts(rootLayout,this)
        init()
        listeners()
        getAddresses()


    }

    fun init(){
       // var title = intent.getStringExtra("mapTitle")
     //   tvPageTitle.setColorTypeface(this,R.color.white,title!!,true)
        btBackTool.onOneClick {
            super.onBackPressed()
        }
        btBackTool.show()
        AppHelper.setLogoTint(btBackTool,this,R.color.white)
     }

    fun getAddresses(){
        try {
            loading.show()
        } catch (ex: Exception) {

        }
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

                            loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponseMainAddress>, throwable: Throwable) {
                        loading.hide()
                }
            })
    }
    private fun listeners(){
        btAddNew.onOneClick{
            startActivity(Intent(this,ActivityAddNewAddress::class.java)
                .putExtra("mapTitle",AppHelper.getRemoteString("address",this)))
        }
    }

    private fun setData(){

        var adapter = AdapterAddress(array,this,this)
        rvAddresses.layoutManager = LinearLayoutManager(this)
        rvAddresses.adapter = adapter
        rvAddresses.isNestedScrollingEnabled = false

        if(array.size==0){
            rvAddresses.hide()
        }

        loading.hide()

    }


    override fun onItemClicked(view: View, position: Int) {
        val intent = Intent()
        intent.putExtra("lat",array.get(position).lat)
        intent.putExtra("long",array.get(position).long)
        var latLng = com.google.android.gms.maps.model.LatLng(array.get(position).lat!!.toDouble(), array.get(position).long!!.toDouble())
        intent.putExtra(
            "address",
            array[position].desc + " ,"+ array[position].street + " ,"+ array[position].bldg + " ,"+array[position].floor
        )
        setResult(RESULT_OK, intent)
        finish()
    }
}