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
    var from : String ?=""
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
            intent.putExtra("lat", MyApplication.selectedAddress!!.lat)
            intent.putExtra("long",  MyApplication.selectedAddress!!.long)

            /*var latLng = com.google.android.gms.maps.model.LatLng(
                array.get(position).lat!!.toDouble(),
                array.get(position).long!!.toDouble()
            )*/
         //   MyApplication.selectedAddress = array.get(position)
            intent.putExtra(
                "address",
                MyApplication.selectedAddress!!.desc + " ," + MyApplication.selectedAddress!!.street + " ," +  MyApplication.selectedAddress!!.bldg + " ," +  MyApplication.selectedAddress!!.floor
            )
            setResult(RESULT_OK, intent)
            finish()
        }
    }
    fun init(){
       // var title = intent.getStringExtra("mapTitle")
     //   tvPageTitle.setColorTypeface(this,R.color.white,title!!,true)
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
        }

        loading.hide()

    }


    override fun onItemClicked(view: View, position: Int) {
        if(!MyApplication.fromProfile!!) {
            val intent = Intent()
            intent.putExtra("lat", array.get(position).lat)
            intent.putExtra("long", array.get(position).long)
            var addr =""
            MyApplication.selectedAddress = array.get(position)
            if(!array.get(position).desc.equals("null")&&!array.get(position).desc.isNullOrEmpty()){
              addr = array.get(position).desc!!
            }
            if(!array.get(position).street.equals("null")&&!array.get(position).street.isNullOrEmpty()){
               addr =addr+","+array.get(position).street
            }
            if(!array.get(position).bldg.equals("null")&&!array.get(position).bldg.isNullOrEmpty()){
                addr =addr+","+array.get(position).bldg
            }
            if(!array.get(position).floor.equals("null")&&!array.get(position).floor.isNullOrEmpty()){
                addr =addr+","+array.get(position).floor
            }
            intent.putExtra(
                "address",
                addr
            )
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}