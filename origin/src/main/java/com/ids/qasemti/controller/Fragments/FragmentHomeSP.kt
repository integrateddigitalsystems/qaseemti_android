package com.ids.qasemti.controller.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Activities.ActivityMap
import com.ids.qasemti.controller.Activities.ActivityOrderDetails
import com.ids.qasemti.controller.Adapters.AdapterOrders
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.layout_home_orders.*
import kotlinx.android.synthetic.main.loading.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class FragmentHomeSP : Fragment(), RVOnItemClickListener {

    private var ordersArray: ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.layout_home_orders, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutOrders, requireContext())
        init()

    }

    fun setAvailability(available : Int ){
        var newReq = RequestAvailability(MyApplication.userId,available)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.updateAvailability(newReq)?.enqueue(object : Callback<ResponseCancel> {
                override fun onResponse(call: Call<ResponseCancel>, response: Response<ResponseCancel>) {
                    try{
                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ResponseCancel>, throwable: Throwable) {
                }
            })
    }

    fun init() {
        (activity as ActivityHome?)!!.showLogout(false)
        (activity as ActivityHome?)!!.setTintLogo(R.color.redPrimary)

        setOrders()
        setListeners()
        getData()



    }

    fun getData(){
        try {
            loading.show()
        }catch (ex: Exception){

        }
        var newReq = RequestUserStatus(MyApplication.userId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getOrdersCount(newReq)?.enqueue(object : Callback<ResponeOrderCount> {
                override fun onResponse(call: Call<ResponeOrderCount>, response: Response<ResponeOrderCount>) {
                    try{
                        tvActiveOrdersNbr.text = response.body()!!.activeOrders.toString()
                        tvUpcomingOrderNumber.text = response.body()!!.upcomingOrders.toString()
                        try {
                            loading.hide()
                        }catch (ex: Exception){

                        }
                    }catch (E: java.lang.Exception){
                        try {
                            loading.hide()
                        }catch (ex: Exception){

                        }
                    }
                }
                override fun onFailure(call: Call<ResponeOrderCount>, throwable: Throwable) {
                    try {
                        loading.hide()
                    }catch (ex: Exception){

                    }
                }
            })
    }
    fun setListeners(){
        rlActive.onOneClick {
            MyApplication.fromFooterOrder = false
            MyApplication.selectedFragment = FragmentOrders()
            (requireActivity() as ActivityHome?)!!.addFrag(
                FragmentOrders(),
                AppConstants.FRAGMENT_ORDER_FROM
            )
            MyApplication.typeSelected = 0

        }
        rlUpcoming.onOneClick {
            MyApplication.fromFooterOrder = false
            MyApplication.selectedFragment = FragmentOrders()
            (requireActivity() as ActivityHome?)!!.addFrag(
                FragmentOrders(),
                AppConstants.FRAGMENT_ORDER_FROM
            )
            MyApplication.typeSelected = 1
        }
        swAvailable.setOnCheckedChangeListener { compoundButton, b ->
            if (swAvailable.isChecked) {
                rvOrders.show()
                setAvailability(1)
                swAvailable.text = AppHelper.getRemoteString("available", requireContext())
                llNodata.hide()
            } else {
                rvOrders.hide()
                llNodata.show()
                setAvailability(0)
                swAvailable.text = AppHelper.getRemoteString("unavailable", requireContext())
            }
        }
    }

    private fun setOrders() {
        ordersArray.clear()
        ordersArray.add("1")
        ordersArray.add("1")
        ordersArray.add("1")
        var adapter = AdapterOrders(ordersArray, this, requireContext())
        rvOrders.adapter = adapter
        var glm2 = GridLayoutManager(requireContext(), 1)
        rvOrders.layoutManager = glm2

        if (ordersArray.size == 0) {
            rvOrders.hide()
            llNodata.show()
        }
    }

    override fun onItemClicked(view: View, position: Int) {

        if (view.id == R.id.llLocation) {
            AppHelper.onOneClick {
                startActivity(
                    Intent(requireActivity(), ActivityMap::class.java)
                        .putExtra(
                            "mapTitle",
                            AppHelper.getRemoteString("view_address", requireContext())
                        )
                )
            }
        } else if (view.id == R.id.llViewOrderDetails) {
            AppHelper.onOneClick {
                startActivity(Intent(requireActivity(), ActivityOrderDetails::class.java))
            }
        }else if(view.id == R.id.btAcceptOrder){
           /* if(MyApplication.userStatus!!.online==1){
                //accept action
            }*/
        }
    }
}