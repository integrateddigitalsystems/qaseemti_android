package com.ids.qasemti.controller.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Activities.ActivityServiceInformation
import com.ids.qasemti.controller.Adapters.AdapterMyServices
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.RequestServices
import com.ids.qasemti.model.ResponseMainServices
import com.ids.qasemti.model.ResponseService
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_services.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class FragmentMyServices : Fragment(), RVOnItemClickListener {

    var array: ArrayList<ResponseService> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(com.ids.qasemti.R.layout.activity_services, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AppHelper.setAllTexts(rootLayout, requireContext())
        init()
    }

    fun getServices() {

        loading.show()

        var newReq = RequestServices(MyApplication.userId, MyApplication.languageCode)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getServices(
                newReq
            )?.enqueue(object : Callback<ResponseMainServices> {
                override fun onResponse(
                    call: Call<ResponseMainServices>,
                    response: Response<ResponseMainServices>
                ) {
                    try {
                        array.clear()
                        array.addAll(response.body()!!.responseService!!)
                        setData()
                    } catch (E: java.lang.Exception) {
                        array.clear()
                        try {
                            setData()
                        } catch (e: Exception) {
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseMainServices>, throwable: Throwable) {
                    array.clear()
                    try {
                        setData()
                    } catch (e: Exception) {
                    }
                }
            })
    }

    private fun init() {


        if (MyApplication.fromAccount) {
            MyApplication.fromAccount = false
            (activity as ActivityHome).showBack(true)
        } else {
            (activity as ActivityHome).showBack(false)
        }
        (activity as ActivityHome).showLogout(false)
        (activity as ActivityHome).showTitle(true)
        AppHelper.setTitle(requireContext(),MyApplication.selectedTitle!!,"",R.color.white)
        listeners()
    }

    override fun onResume() {
        getServices()
        super.onResume()
    }

    private fun listeners() {
        //  btBck.setOnClickListener{super.onBackPressed()}
        btAdd.onOneClick {
            //  if(MyApplication.userStatus!!.online!=0){
            MyApplication.isEditService=false
            startActivity(Intent(requireContext(), ActivityServiceInformation::class.java))
            //  }

        }
    }

    private fun setData() {


        try {
            var adapter = AdapterMyServices(array, this, requireContext())
            rvServices.layoutManager = LinearLayoutManager(requireContext())
            rvServices.adapter = adapter
            rvServices.isNestedScrollingEnabled = false
            if(array.size==0){
                rvServices.hide()
                tvNoData.show()
            }
            loading.hide()
        } catch (ex: Exception) {

        }
    }

    override fun onItemClicked(view: View, position: Int) {
        MyApplication.selectedService=array[position]
        MyApplication.isEditService=true
        startActivity(Intent(requireContext(), ActivityServiceInformation::class.java))
    }


}