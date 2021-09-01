package com.ids.qasemti.controller.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Activities.ActivityMap
import com.ids.qasemti.controller.Adapters.AdapterOrderCancelled
import com.ids.qasemti.controller.Adapters.AdapterOrderType
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication.Companion.typeSelected
import com.ids.qasemti.utils.AppHelper
import kotlinx.android.synthetic.main.fragment_orders.*

class FragmentOrders : Fragment() , RVOnItemClickListener {

    var ordersArray : ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.qasemti.R.layout.fragment_orders, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutOrderType)
        init()
        setTabs()
        setTabLayout(0,tvActive)
        setData(true)

    }

    fun init(){
        ordersArray.clear()
        repeat(3) { ordersArray.add("1") }
        (activity as ActivityHome?)!!.drawColor()

    }

    private fun setTabs(){
        for (i in 0 until linearTabs.childCount){
            linearTabs.getChildAt(i).setOnClickListener{
                if(typeSelected!=i){
                    var tv=linearTabs.getChildAt(i) as TextView
                    setTabLayout(i,tv)
                    setData(true)
                }
            }
        }
    }

    override fun onItemClicked(view: View, position: Int) {
        if(view.id==R.id.llLocation){
            startActivity(Intent(requireActivity(), ActivityMap::class.java))
        }
    }

    fun setTabLayout(position: Int,tvSelected:TextView){
        typeSelected=position
        for (i in 0 until linearTabs.childCount){
            if (linearTabs.getChildAt(i) is TextView){
                var tv=linearTabs.getChildAt(i) as TextView
                tv.setBackgroundResource(R.color.transparent)
                AppHelper.setTextColor(requireContext(),tv,R.color.redPrimary)
            }
        }
        tvSelected.setBackgroundResource(R.drawable.rounded_red_background)
        AppHelper.setTextColor(requireContext(),tvSelected,R.color.white)

    }

    private fun setData(type:Boolean){
        var adapter = AdapterOrderType(ordersArray, this, requireContext(),type)
        rvOrderDetails.adapter = adapter
        var glm2 = GridLayoutManager(requireContext(), 1)
        rvOrderDetails.layoutManager = glm2
    }
}