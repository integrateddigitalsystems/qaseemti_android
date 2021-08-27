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
import com.ids.qasemti.controller.Adapters.AdapterOrderCancelled
import com.ids.qasemti.controller.Adapters.AdapterOrderType
import com.ids.qasemti.controller.Adapters.AdapterOrders
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication.Companion.typeSelected
import com.ids.qasemti.utils.AppHelper
import kotlinx.android.synthetic.main.activity_order_type.*
import kotlinx.android.synthetic.main.layout_home_orders.*
import kotlinx.android.synthetic.main.toolbar.*

class FragmentOrderTypes : Fragment() , RVOnItemClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.qasemti.R.layout.activity_order_type, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutOrderType)
        init()

    }

    fun init(){
        (activity as ActivityHome?)!!.showLogout(false)
        if(typeSelected==1){
            tvActive.setBackgroundResource(R.drawable.rounded_red_background)
            tvCompleted.setBackgroundResource(R.color.transparent)
            tvUpcoming.setBackgroundResource(R.color.transparent)
            tvCancelled.setBackgroundResource(R.color.transparent)

            AppHelper.setTextColor(requireContext(),tvActive,R.color.white)
            AppHelper.setTextColor(requireContext(),tvUpcoming,R.color.redPrimary)
            AppHelper.setTextColor(requireContext(),tvCancelled,R.color.redPrimary)
            AppHelper.setTextColor(requireContext(),tvCompleted,R.color.redPrimary)

        }else{
            tvUpcoming.setBackgroundResource(R.drawable.rounded_red_background)
            tvCompleted.setBackgroundResource(R.color.transparent)
            tvActive.setBackgroundResource(R.color.transparent)
            tvCancelled.setBackgroundResource(R.color.transparent)


            AppHelper.setTextColor(requireContext(),tvActive,R.color.redPrimary)
            AppHelper.setTextColor(requireContext(),tvUpcoming,R.color.white)
            AppHelper.setTextColor(requireContext(),tvCancelled,R.color.redPrimary)
            AppHelper.setTextColor(requireContext(),tvCompleted,R.color.redPrimary)
        }
        var ordersArray : ArrayList<String> = arrayListOf()

        ordersArray.clear()
        ordersArray.add("1")
        ordersArray.add("1")
        ordersArray.add("1")

        var adapter = AdapterOrderType(ordersArray, this, requireContext(),true)
        rvOrderDetails.adapter = adapter
        var glm2 = GridLayoutManager(requireContext(), 1)
        rvOrderDetails.layoutManager = glm2

        (activity as ActivityHome?)!!.drawColor()

        tvActive.setOnClickListener {
            if(typeSelected!=1){
                typeSelected=1
                tvActive.setBackgroundResource(R.drawable.rounded_red_background)
                tvCompleted.setBackgroundResource(R.color.transparent)
                tvUpcoming.setBackgroundResource(R.color.transparent)
                tvCancelled.setBackgroundResource(R.color.transparent)

                AppHelper.setTextColor(requireContext(),tvActive,R.color.white)
                AppHelper.setTextColor(requireContext(),tvUpcoming,R.color.redPrimary)
                AppHelper.setTextColor(requireContext(),tvCancelled,R.color.redPrimary)
                AppHelper.setTextColor(requireContext(),tvCompleted,R.color.redPrimary)

                var adapter = AdapterOrderType(ordersArray, this, requireContext(),true)
                rvOrderDetails.adapter = adapter
                var glm2 = GridLayoutManager(requireContext(), 1)
                rvOrderDetails.layoutManager = glm2

            }


        }
        tvUpcoming.setOnClickListener {
            if(typeSelected!=2){
                typeSelected=2
                tvUpcoming.setBackgroundResource(R.drawable.rounded_red_background)
                tvCompleted.setBackgroundResource(R.color.transparent)
                tvActive.setBackgroundResource(R.color.transparent)
                tvCancelled.setBackgroundResource(R.color.transparent)


                AppHelper.setTextColor(requireContext(),tvActive,R.color.redPrimary)
                AppHelper.setTextColor(requireContext(),tvUpcoming,R.color.white)
                AppHelper.setTextColor(requireContext(),tvCancelled,R.color.redPrimary)
                AppHelper.setTextColor(requireContext(),tvCompleted,R.color.redPrimary)

                var adapter = AdapterOrderType(ordersArray, this, requireContext(),true)
                rvOrderDetails.adapter = adapter
                var glm2 = GridLayoutManager(requireContext(), 1)
                rvOrderDetails.layoutManager = glm2
            }


        }
        tvCancelled.setOnClickListener {
            if(typeSelected!=3){
                typeSelected=3
                tvCancelled.setBackgroundResource(R.drawable.rounded_red_background)
                tvCompleted.setBackgroundResource(R.color.transparent)
                tvActive.setBackgroundResource(R.color.transparent)
                tvUpcoming.setBackgroundResource(R.color.transparent)


                AppHelper.setTextColor(requireContext(),tvActive,R.color.redPrimary)
                AppHelper.setTextColor(requireContext(),tvUpcoming,R.color.redPrimary)
                AppHelper.setTextColor(requireContext(),tvCancelled,R.color.white)
                AppHelper.setTextColor(requireContext(),tvCompleted,R.color.redPrimary)

                var adapter = AdapterOrderCancelled(ordersArray, this, requireContext(),false)
                rvOrderDetails.adapter = adapter
                var glm2 = GridLayoutManager(requireContext(), 1)
                rvOrderDetails.layoutManager = glm2

            }



        }
        tvCompleted.setOnClickListener {
            if(typeSelected!=4){
                typeSelected=4
                tvCompleted.setBackgroundResource(R.drawable.rounded_red_background)
                tvCancelled.setBackgroundResource(R.color.transparent)
                tvActive.setBackgroundResource(R.color.transparent)
                tvUpcoming.setBackgroundResource(R.color.transparent)


                AppHelper.setTextColor(requireContext(),tvActive,R.color.redPrimary)
                AppHelper.setTextColor(requireContext(),tvUpcoming,R.color.redPrimary)
                AppHelper.setTextColor(requireContext(),tvCancelled,R.color.redPrimary)
                AppHelper.setTextColor(requireContext(),tvCompleted,R.color.white)


                var adapter = AdapterOrderType(ordersArray, this, requireContext(),false)
                rvOrderDetails.adapter = adapter
                var glm2 = GridLayoutManager(requireContext(), 1)
                rvOrderDetails.layoutManager = glm2

            }
        }
    }

    override fun onItemClicked(view: View, position: Int) {
        if(view.id==R.id.llLocation){
            startActivity(Intent(requireActivity(), ActivityMap::class.java))
        }
    }
}