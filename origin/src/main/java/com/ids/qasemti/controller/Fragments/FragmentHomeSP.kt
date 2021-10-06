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

    private var ordersArray: ArrayList<ResponseOrders> = arrayListOf()
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

    fun getBroadcastedOrders() {
        var newReq = RequestServices(MyApplication.userId, MyApplication.languageCode)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getBroadcastedOrders(newReq)?.enqueue(object : Callback<ResponseMainOrder> {
                override fun onResponse(
                    call: Call<ResponseMainOrder>,
                    response: Response<ResponseMainOrder>
                ) {

                }

                override fun onFailure(call: Call<ResponseMainOrder>, throwable: Throwable) {
                }
            })
    }

    fun setAvailability(available: Int) {
        var newReq = RequestAvailability(MyApplication.userId, available)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.updateAvailability(newReq)?.enqueue(object : Callback<ResponseCancel> {
                override fun onResponse(
                    call: Call<ResponseCancel>,
                    response: Response<ResponseCancel>
                ) {

                }

                override fun onFailure(call: Call<ResponseCancel>, throwable: Throwable) {
                }
            })
    }

    fun getRating() {
        loading.show()
        var newReq = RequestUserStatus(MyApplication.userId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getRatings(newReq)?.enqueue(object : Callback<ResponseRatings> {
                override fun onResponse(
                    call: Call<ResponseRatings>,
                    response: Response<ResponseRatings>
                ) {
                    try {
                        rbMainUser.rating = response.body()!!.rate!!.toFloat()
                    } catch (E: java.lang.Exception) {
                        rbMainUser.rating = 0f
                    }
                    loading.hide()
                }

                override fun onFailure(call: Call<ResponseRatings>, throwable: Throwable) {
                    rbMainUser.rating = 0f
                    loading.hide()
                }
            })
    }

    fun init() {
        (activity as ActivityHome?)!!.showLogout(false)
        (activity as ActivityHome?)!!.setTintLogo(R.color.redPrimary)

        setListeners()
        getRating()
        getData()
        getOrders()


    }

    fun getData() {
        try {
            loading.show()
        } catch (ex: Exception) {

        }
        var newReq = RequestUserStatus(MyApplication.userId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getOrdersCount(newReq)?.enqueue(object : Callback<ResponeOrderCount> {
                override fun onResponse(
                    call: Call<ResponeOrderCount>,
                    response: Response<ResponeOrderCount>
                ) {
                    try {
                        tvActiveOrdersNbr.text = response.body()!!.activeOrders.toString()
                        tvUpcomingOrderNumber.text = response.body()!!.upcomingOrders.toString()
                        try {
                            loading.hide()
                        } catch (ex: Exception) {

                        }
                    } catch (E: java.lang.Exception) {
                        try {
                            loading.hide()
                        } catch (ex: Exception) {

                        }
                    }
                }

                override fun onFailure(call: Call<ResponeOrderCount>, throwable: Throwable) {
                    try {
                        loading.hide()
                    } catch (ex: Exception) {

                    }
                }
            })
    }

    fun setListeners() {
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

    fun getOrders() {
        try {
            loading.show()
        } catch (ex: Exception) {

        }
        var newReq = RequestServices(MyApplication.userId, MyApplication.languageCode)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getBroadcastedOrders(newReq)?.enqueue(object : Callback<ResponseMainOrder> {
                override fun onResponse(
                    call: Call<ResponseMainOrder>,
                    response: Response<ResponseMainOrder>
                ) {
                    try {
                        ordersArray.clear()
                        ordersArray.addAll(response.body()!!.orders)
                        setOrders()
                    } catch (E: java.lang.Exception) {
                        try {
                            loading.hide()
                            setOrders()
                        } catch (ex: Exception) {

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseMainOrder>, throwable: Throwable) {
                    try {
                        loading.hide()
                    } catch (ex: Exception) {

                    }
                }
            })
    }

    fun accepted(res:Int){
        if(res==1){
            AppHelper.createDialog(requireActivity(),getString(R.string.order_accept_succ))
        }else{
            AppHelper.createDialog(requireActivity(),getString(R.string.error_acc_order))
        }
        loading.hide()
    }

    fun acceptOrder(orderId : Int ) {
        loading.show()

        var newReq = RequestAcceptBroadccast(MyApplication.userId,orderId,50)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.acceptBroadcast(newReq)?.enqueue(object : Callback<ResponseUser> {
                override fun onResponse(
                    call: Call<ResponseUser>,
                    response: Response<ResponseUser>
                ) {
                    try {
                        accepted(response.body()!!.result!!)
                    } catch (E: java.lang.Exception) {
                        accepted(0)
                    }
                }

                override fun onFailure(call: Call<ResponseUser>, throwable: Throwable) {
                    accepted(0)
                }
            })
    }

    private fun setOrders() {
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
        } else if (view.id == R.id.btAcceptOrder) {
            acceptOrder(ordersArray.get(position).orderId!!.toInt())
        }
    }
}