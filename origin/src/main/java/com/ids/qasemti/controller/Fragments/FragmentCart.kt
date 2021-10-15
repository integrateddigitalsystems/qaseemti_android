package com.ids.qasemti.controller.Fragments

import android.content.Intent
import android.graphics.Outline
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityPlaceOrder
import com.ids.qasemti.controller.Adapters.AdapterCart
import com.ids.qasemti.controller.Adapters.AdapterServices
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*

import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.rootLayout
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_home_client.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class FragmentCart : Fragment() , RVOnItemClickListener {

    var array : ArrayList<ResponseOrders> = arrayListOf()
    var adapter : AdapterCart ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.qasemti.R.layout.fragment_cart, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayout,requireContext())
        init()

    }

    fun deleteCartItem(orderID : Int ){
        try {
            loading.show()
        }catch (ex: Exception){

        }
        var newReq = RequestOrderId(orderID)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.deleteCartItem(
                newReq
            )?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                    try{
                       getCartData()
                    }catch (E: java.lang.Exception){
                        loading.hide()
                    }
                }
                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    loading.hide()
                }
            })
    }


    fun getCartData(){
        try {
            loading.show()
        }catch (ex: Exception){

        }
        var newReq = RequestCart(1,MyApplication.languageCode)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getCarts(
                newReq
            )?.enqueue(object : Callback<ResponseMainOrder> {
                override fun onResponse(call: Call<ResponseMainOrder>, response: Response<ResponseMainOrder>) {
                    try{
                        array.clear()
                        array.addAll(response.body()!!.orders)
                        setData()
                    }catch (E: java.lang.Exception){
                        setData()
                    }
                }
                override fun onFailure(call: Call<ResponseMainOrder>, throwable: Throwable) {
                    setData()
                }
            })
    }
    fun setData(){
        adapter = AdapterCart(array, this, requireContext())
        rvCart.layoutManager = LinearLayoutManager(requireContext())
        rvCart.adapter = adapter
        rvCart.isNestedScrollingEnabled = false
        loading.hide()
    }

    fun init(){
        var cart = AppHelper.getRemoteString("Cart",requireContext())
        tvPageTitle.setColorTypeface(requireContext(),R.color.white,cart,true)
        getCartData()







    }

    override fun onItemClicked(view: View, position: Int) {
        if(view.id == R.id.ivDeleteItem){
            AppHelper.createYesNoDialog(requireActivity(),AppHelper.getRemoteString("yes",requireContext()),AppHelper.getRemoteString("cancel",requireContext()),AppHelper.getRemoteString("are_you_sure_delete",requireContext())) {
                /*MyApplication.arrayCart.removeAt(position)
                AppHelper.toGSOn(MyApplication.arrayCart)
                adapter!!.notifyItemRemoved(position)*/
                deleteCartItem(array.get(position).orderId!!.toInt())
            }
        }else{
            MyApplication.seletedPosCart = position
            MyApplication.selectedPlaceOrder = MyApplication.arrayCart.get(position)
            startActivity(Intent(requireContext(),ActivityPlaceOrder::class.java))
        }
    }
}