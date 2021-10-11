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

import com.ids.qasemti.model.Address
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.setColorTypeface
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.rootLayout
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_home_client.*
import kotlinx.android.synthetic.main.toolbar.*

class FragmentCart : Fragment() , RVOnItemClickListener {

    var array : ArrayList<Int> = arrayListOf()
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



    fun init(){
        var cart = AppHelper.getRemoteString("Cart",requireContext())
        tvPageTitle.setColorTypeface(requireContext(),R.color.white,cart,true)

        adapter = AdapterCart(MyApplication.arrayCart, this, requireContext())
        rvCart.layoutManager = LinearLayoutManager(requireContext())
        rvCart.adapter = adapter
        rvCart.isNestedScrollingEnabled = false





    }

    override fun onItemClicked(view: View, position: Int) {
        if(view.id == R.id.ivDeleteItem){
            AppHelper.createYesNoDialog(requireActivity(),AppHelper.getRemoteString("yes",requireContext()),AppHelper.getRemoteString("cancel",requireContext()),AppHelper.getRemoteString("are_you_sure_delete",requireContext())) {
                MyApplication.arrayCart.removeAt(position)
                AppHelper.toGSOn(MyApplication.arrayCart)
                adapter!!.notifyItemRemoved(position)
            }
        }else{
            MyApplication.seletedPosCart = position
            MyApplication.selectedPlaceOrder = MyApplication.arrayCart.get(position)
            startActivity(Intent(requireContext(),ActivityPlaceOrder::class.java))
        }
    }
}