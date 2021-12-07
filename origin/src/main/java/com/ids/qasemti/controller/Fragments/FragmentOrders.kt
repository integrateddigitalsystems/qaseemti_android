package com.ids.qasemti.controller.Fragments

import android.content.Intent
import android.net.Uri
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
import androidx.recyclerview.widget.LinearLayoutManager
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
import java.util.*
import kotlin.collections.ArrayList


class FragmentOrders : Fragment(), RVOnItemClickListener , ReloadData {

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
        }else if(MyApplication.completed){
            MyApplication.completed = false
            setTabLayout(2)
        }else{
            setTabLayout(typeSelected)
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
     //   setTabLayout(typeSelected)
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
                       try{ setData(true)}catch (e:Exception){}
                    }
                }

                override fun onFailure(call: Call<ResponseMainOrder>, throwable: Throwable) {
                    mainArray.clear()
                    ordersArray.clear()
                    try{setData(true)}catch (e:Exception){}
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
                        try{setData(true)}catch (e:Exception){}
                    }
                }

                override fun onFailure(call: Call<ResponseMainOrder>, throwable: Throwable) {
                    mainArray.clear()
                    ordersArray.clear()
                    try{setData(true)}catch (e:Exception){}
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

        etSearchOrders.typeface = AppHelper.getTypeFace(requireContext())
        if(MyApplication.isClient){
            etSearchOrders.hint = AppHelper.getRemoteString("search_by_service",requireContext())
        }else{
            etSearchOrders.hint = AppHelper.getRemoteString("search_by",requireContext())
        }
        AppHelper.setTitle(requireActivity(), MyApplication.selectedTitle!!, "",R.color.primary)
        (activity as ActivityHome).showTitle(true)
        (activity as ActivityHome).showLogout(false)
        (activity as ActivityHome).setTintLogo(R.color.primary)
        if (!MyApplication.fromFooterOrder ) {
            (activity as ActivityHome).showBack(true)
        }
        if(MyApplication.fromOrderPlaced ){
            (activity as ActivityHome).showBack(false)
            MyApplication.fromOrderPlaced = false
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
                    if(!MyApplication.isClient) {
                        ordersArray.addAll(mainArray.filter {
                            it.orderId!!.contains(s) ||  (it.customer!!.first_name!=null && it.customer!!.first_name!!.contains(
                                s
                            ) )|| (it.product!!.name!=null && it.product!!.name!!.contains(
                                s
                            ) )
                        })
                    }else{
                        ordersArray.addAll(mainArray.filter {
                            it.orderId!!.contains(s) || (it.vendor!=null && it.vendor!!.firstName!!.contains(
                                s
                            ) )||  (it.product!!.name!=null && it.product!!.name!!.contains(
                                s
                            ) )
                        })
                    }
                    try {
                        adapter!!.notifyDataSetChanged()
                    }catch (ex:Exception){}
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
            var tv = linearTabs.getChildAt(i) as TextView
            tv.setColorTypeface(requireContext(),R.color.primary,"",false)
        }
    }

    override fun onItemClicked(view: View, position: Int) {
        if (view.id == R.id.llLocation) {


            val intent=Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("geo:0,0?q="+ordersArray.get(position).shipping_latitude!!+","+ordersArray.get(position).shipping_longitude!!+"("+ordersArray.get(position).addressname+")")
            startActivity(intent)
               /* AppHelper.onOneClick {
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
                }*/

        }
        else if (view.id == R.id.llViewOrderDetails) {
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
        }
        else if (view.id == R.id.ivOrderCall) {
            AppHelper.onOneClick {
                try {
                    if (!MyApplication.isClient) {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.setData(Uri.parse("tel:" + ordersArray.get(position).customer!!.mobile_number));
                        startActivity(intent)
                    } else {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.setData(Uri.parse("tel:" + ordersArray.get(position).vendor!!.mobileNum));
                        startActivity(intent)
                    }
                }catch (ex:Exception){
                    val intent = Intent(Intent.ACTION_DIAL)
                    startActivity(intent)
                }
            }

        }
        else if (view.id == R.id.ivOrderMessage) {
            AppHelper.onOneClick {
                /*val uri = Uri.parse("smsto:12346556")
                val it = Intent(Intent.ACTION_SENDTO, uri)
                it.putExtra("sms_body", "Here you can set the SMS text to be sent")
                startActivity(it)*/
                MyApplication.selectedOrder = ordersArray[position]
                startActivity(Intent(requireContext(), ActivityChat::class.java))
            }
        }
        else if (view.id == R.id.llTrackOrder) {
            MyApplication.selectedOrder = ordersArray.get(position)
            startActivity(Intent(requireActivity(), ActivityTrackOrder::class.java))
        }
        else if (view.id==R.id.swDelivered || view.id==R.id.swPaid){
            if(ordersArray.get(position).done)
                getOrders()
        }
    }

    fun setSelected(position: Int) {

        when (position) {
            0 -> {
                etSearchOrders.text.clear()
                tvActive.setBackgroundResource(R.drawable.rounded_orders)
                AppHelper.setTextColor(requireContext(), tvActive, R.color.white)
                orderType = AppConstants.ORDER_TYPE_ACTIVE
                retrieveOrders()
            }
            1 -> {
                etSearchOrders.text.clear()
                tvUpcoming.setBackgroundResource(R.drawable.rounded_orders)
                AppHelper.setTextColor(requireContext(), tvUpcoming, R.color.white)
                orderType = AppConstants.ORDER_TYPE_UPCOMING
                retrieveOrders()
            }
            2 -> {
                etSearchOrders.text.clear()
                tvCompleted.setBackgroundResource(R.drawable.rounded_orders)
                AppHelper.setTextColor(requireContext(), tvCompleted, R.color.white)
                orderType = AppConstants.ORDER_TYPE_COMPLETED
                retrieveOrders()
            }
            3 -> {
                etSearchOrders.text.clear()
                tvCancelled.setBackgroundResource(R.drawable.rounded_orders)
                AppHelper.setTextColor(requireContext(), tvCancelled, R.color.white)
                orderType = AppConstants.ORDER_TYPE_CANCELED
                retrieveOrders()
            }
            else -> {
                etSearchOrders.text.clear()
                tvFailed.setBackgroundResource(R.drawable.rounded_orders)
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
                AppHelper.setTextColor(requireContext(), tv, R.color.primary)
            }
        }

        setSelected(position)

    }

    private fun setData(type: Boolean) {

        try {
            if(ordersArray.size==0){
                tvNoData.show()
            }else{
                tvNoData.hide()
                slRefresh.isRefreshing = false
                var glm2 = LinearLayoutManager(requireContext())
                rvOrderDetails.layoutManager = glm2
                adapter = AdapterOrderType(ordersArray, this, this,requireActivity(),loading)
                rvOrderDetails.adapter = adapter

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

    override fun reload() {
        getOrders()
    }
}