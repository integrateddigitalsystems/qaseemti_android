package com.ids.qasemti.controller.Fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.*
import com.ids.qasemti.controller.Adapters.AdapterOrderType
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.controller.MyApplication.Companion.typeSelected
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.layout_order_contact_tab.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentOrders : Fragment(), RVOnItemClickListener {

    var ordersArray: ArrayList<ResponseOrders> = arrayListOf()
    var adapter: AdapterOrderType? = null
    var mainArray: ArrayList<ResponseOrders> = arrayListOf()
    var orderType: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (MyApplication.renewed == true) {
            MyApplication.renewed = false
            setTabLayout(0)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_orders, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutOrderType, requireContext())
        requireActivity().getWindow().setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        init()
        setTabs()
        setTabLayout(typeSelected)
        //  setData(true)

    }

    fun getClientOrders() {
        try {
            loading.show()
        } catch (ex: Exception) {

        }
        var newReq = RequestOrders(MyApplication.userId, MyApplication.languageCode, orderType)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getClientOrders(
                newReq
            )?.enqueue(object : Callback<ResponseMainOrder> {
                override fun onResponse(
                    call: Call<ResponseMainOrder>,
                    response: Response<ResponseMainOrder>
                ) {
                    try {
                        mainArray.clear()
                        mainArray.addAll(response.body()!!.orders)
                        ordersArray.clear()
                        ordersArray.addAll(mainArray)
                        setData(true)
                    } catch (E: java.lang.Exception) {
                        mainArray.clear()
                        ordersArray.clear()
                        setData(true)
                    }
                }

                override fun onFailure(call: Call<ResponseMainOrder>, throwable: Throwable) {
                    mainArray.clear()
                    ordersArray.clear()
                    setData(true)
                }
            })
    }

    fun getOrders() {
        try {
            loading.show()
        } catch (ex: Exception) {

        }

        var newReq = RequestOrders(MyApplication.userId, MyApplication.languageCode, orderType)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getOrders(
                newReq
            )?.enqueue(object : Callback<ResponseMainOrder> {
                override fun onResponse(
                    call: Call<ResponseMainOrder>,
                    response: Response<ResponseMainOrder>
                ) {
                    try {
                        mainArray.clear()
                        mainArray.addAll(response.body()!!.orders)
                        ordersArray.clear()
                        ordersArray.addAll(mainArray)
                        setData(true)
                    } catch (E: java.lang.Exception) {
                        mainArray.clear()
                        ordersArray.clear()
                        setData(true)
                    }
                }

                override fun onFailure(call: Call<ResponseMainOrder>, throwable: Throwable) {
                    mainArray.clear()
                    ordersArray.clear()
                    setData(true)
                }
            })
    }

    fun init() {
        /*ordersArray.clear()
        ordersArray.add("1")
        ordersArray.add("2")
        ordersArray.add("1")
        ordersArray.add("3")
        mainArray.addAll(ordersArray)*/
        (activity as ActivityHome?)!!.drawColor()
        /*(activity as ActivityHome?)!!.setTitleAc(
            AppHelper.getRemoteString(
                "order_type",
                requireContext()
            )
        )*/


        AppHelper.setTitle(requireActivity(), MyApplication.selectedTitle!!, "",R.color.redPrimary)
        (activity as ActivityHome).showTitle(true)
        (activity as ActivityHome).showLogout(false)
        (activity as ActivityHome).setTintLogo(R.color.redPrimary)
        if (!MyApplication.fromFooterOrder) {
            (activity as ActivityHome).showBack(true)
        }

        etSearchOrders.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.length > 0) {
                    ordersArray.clear()
                    ordersArray.addAll(mainArray.filter {
                        it.orderId!!.contains(s) || it.customer!!.first_name!!.contains(
                            s
                        ) || it.product!!.name!!.contains(s)
                    })
                    adapter!!.notifyDataSetChanged()
                } else {
                    ordersArray.clear()
                    ordersArray.addAll(mainArray)
                    try {
                        adapter!!.notifyDataSetChanged()
                    } catch (ex: Exception) {

                    }
                }

            }
        })

        slRefresh.setOnRefreshListener(OnRefreshListener {
            retrieveOrders()
        })


    }

    private fun setTabs() {
        for (i in 0 until linearTabs.childCount) {
            linearTabs.getChildAt(i).onOneClick {
                if (typeSelected != i) {
                    var tv = linearTabs.getChildAt(i) as TextView
                    setTabLayout(i)
                    // setData(true)
                }
            }
        }
    }

    override fun onItemClicked(view: View, position: Int) {
        if (view.id == R.id.llLocation) {
            if (view.id == R.id.llLocation) {
                AppHelper.onOneClick {
                    MyApplication.selectedOrder = ordersArray.get(position)
                    if (!MyApplication.selectedOrder!!.customerLocation.isNullOrEmpty() && !MyApplication.selectedOrder!!.customerLocation.equals(
                            "null"
                        )
                    ) {
                        startActivity(
                            Intent(requireActivity(), ActivityMapAddress::class.java)
                                .putExtra(
                                    "mapTitle",
                                    AppHelper.getRemoteString("view_address", requireContext())
                                )
                                .putExtra("seeOnly", true)
                        )
                    }
                }
            }
        } else if (view.id == R.id.llViewOrderDetails) {
            AppHelper.onOneClick {
                MyApplication.selectedOrder = ordersArray[position]
                MyApplication.rental = position == 1
                MyApplication.rental = position == 1
                mainArray
                startActivity(
                    Intent(requireActivity(), ActivityOrderDetails::class.java)
                        .putExtra("orderId", ordersArray.get(position).orderId)
                        .putExtra("type", typeSelected)
                )

            }
        } else if (view.id == R.id.ivOrderCall) {
            AppHelper.onOneClick {
                val intent = Intent(Intent.ACTION_DIAL)
                startActivity(intent)
            }

        } else if (view.id == R.id.ivOrderMessage) {
            AppHelper.onOneClick {
                /*val uri = Uri.parse("smsto:12346556")
                val it = Intent(Intent.ACTION_SENDTO, uri)
                it.putExtra("sms_body", "Here you can set the SMS text to be sent")
                startActivity(it)*/
                MyApplication.selectedOrder = ordersArray[position]
                startActivity(Intent(requireContext(), ActivityChat::class.java))
            }
        } else if (view.id == R.id.llTrackOrder) {
            startActivity(Intent(requireActivity(), ActivityTrackOrder::class.java))
        } else if (view.id==R.id.swPaid){

        }
    }

    fun setSelected(position: Int) {

        when (position) {
            0 -> {
                etSearchOrders.text.clear()
                tvActive.setBackgroundResource(R.drawable.rounded_red_background)
                AppHelper.setTextColor(requireContext(), tvActive, R.color.white)
                orderType = AppConstants.ORDER_TYPE_ACTIVE
                retrieveOrders()
            }
            1 -> {
                etSearchOrders.text.clear()
                tvUpcoming.setBackgroundResource(R.drawable.rounded_red_background)
                AppHelper.setTextColor(requireContext(), tvUpcoming, R.color.white)
                orderType = AppConstants.ORDER_TYPE_UPCOMING
                retrieveOrders()
            }
            2 -> {
                etSearchOrders.text.clear()
                tvCompleted.setBackgroundResource(R.drawable.rounded_red_background)
                AppHelper.setTextColor(requireContext(), tvCompleted, R.color.white)
                orderType = AppConstants.ORDER_TYPE_COMPLETED
                retrieveOrders()
            }
            3 -> {
                etSearchOrders.text.clear()
                tvCancelled.setBackgroundResource(R.drawable.rounded_red_background)
                AppHelper.setTextColor(requireContext(), tvCancelled, R.color.white)
                orderType = AppConstants.ORDER_TYPE_CANCELED
                retrieveOrders()
            }
            else -> {
                etSearchOrders.text.clear()
                tvFailed.setBackgroundResource(R.drawable.rounded_red_background)
                AppHelper.setTextColor(requireContext(), tvFailed, R.color.white)
                orderType = AppConstants.ORDER_TYPE_FAILED
                retrieveOrders()
            }
        }
    }

    fun setTabLayout(position: Int) {
        typeSelected = position
        for (i in 0 until linearTabs.childCount) {
            if (linearTabs.getChildAt(i) is TextView) {
                var tv = linearTabs.getChildAt(i) as TextView
                tv.setBackgroundResource(R.color.transparent)
                AppHelper.setTextColor(requireContext(), tv, R.color.redPrimary)
            }
        }

        setSelected(position)

    }

    private fun setData(type: Boolean) {
        slRefresh.isRefreshing = false
        try {
            adapter = AdapterOrderType(ordersArray, this, requireActivity())
            rvOrderDetails.adapter = adapter
            var glm2 = GridLayoutManager(requireContext(), 1)
            rvOrderDetails.layoutManager = glm2

            if(ordersArray.size==0){
                tvNoData.show()
            }else{
                tvNoData.hide()
            }
        } catch (ex: Exception) {

        }
        /* if(adapter!=null){
             adapter!!.notifyDataSetChanged()
             adapter!!.notifyDataSetChanged()
         }else {
             try {
                 adapter = AdapterOrderType(ordersArray, this, requireActivity())
                 rvOrderDetails.adapter = adapter
                 var glm2 = GridLayoutManager(requireContext(), 1)
                 rvOrderDetails.layoutManager = glm2
             }catch (ex:Exception){

             }
         }*/
        try {
            loading.hide()
        } catch (ex: Exception) {

        }
    }

    private fun retrieveOrders(){
        slRefresh.isRefreshing=false
        if (!MyApplication.isClient)
            getOrders()
        else
            getClientOrders()
    }
}