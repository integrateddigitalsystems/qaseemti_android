package com.ids.qasemti.controller.Activities



import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterMyServices
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_services.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.no_logo_layout.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class ActivityServices : ActivityBase(),RVOnItemClickListener,ApiListener {
    var array : ArrayList<ResponseService> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)
        AppHelper.setAllTexts(rootLayout,this)
        init()


    }


    fun getServices(){
        try {
            loading.show()
        }catch (ex: Exception){

        }
        var newReq = RequestServices(MyApplication.userId,MyApplication.languageCode)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getServices(
                newReq
            )?.enqueue(object : Callback<ResponseMainServices> {
                override fun onResponse(call: Call<ResponseMainServices>, response: Response<ResponseMainServices>) {
                    try{
                       array.clear()
                        array.addAll(response.body()!!.responseService!!)
                        setData()
                    }catch (E: java.lang.Exception){
                        logw("FIRST-SERVICE",E.toString())
                        array.clear()
                        setData()
                    }
                }
                override fun onFailure(call: Call<ResponseMainServices>, throwable: Throwable) {
                    array.clear()
                    logw("FIRST-SERVICE",throwable.toString())
                    setData()
                }
            })
    }

    private fun init(){
       // btBck.show()
        tvPageTitle.setColorTypeface(this,R.color.white, AppHelper.getRemoteString("MyServices",this),true)
        btBackTool.show()
        btBackTool.onOneClick {
            super.onBackPressed()
        }

        listeners()
    }

    override fun onResume() {
        getServices()
        super.onResume()
    }

    private fun listeners(){
      //  btBck.setOnClickListener{super.onBackPressed()}
        btAdd.onOneClick{

            loading.show()
            if(MyApplication.selectedUser!!.active==1)
                startActivity(Intent(this,ActivityServiceInformation::class.java))
            else
                CallAPIs.getUserInfo(this,this)

            //  if(MyApplication.userStatus!!.online!=0){

          //  }

        }
    }

    private fun setData(){





        var adapter = AdapterMyServices(array,this,this)
        rvServices.layoutManager = LinearLayoutManager(this)
        rvServices.adapter = adapter
        rvServices.isNestedScrollingEnabled = false
        try {
            loading.hide()
        }catch (ex: Exception){

        }
    }

    override fun onItemClicked(view: View, position: Int) {

    }

    override fun onDataRetrieved(success: Boolean, response: Any, apiId: Int) {
        loading.hide()
        if(MyApplication.selectedUser!!.active == 1)
            startActivity(Intent(this,ActivityServiceInformation::class.java))
        else
            AppHelper.createDialog(this,AppHelper.getRemoteString("inactive_user_msg",this))
    }
}