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
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Activities.ActivityMap
import com.ids.qasemti.controller.Activities.ActivityOrderDetails
import com.ids.qasemti.controller.Adapters.AdapterOrderType
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.controller.MyApplication.Companion.typeSelected
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.onOneClick
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.layout_order_contact_tab.*


class FragmentOrders : Fragment() , RVOnItemClickListener {

    var ordersArray : ArrayList<String> = arrayListOf()
    var adapter : AdapterOrderType ?=null
    var mainArray : ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_orders, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutOrderType,requireContext())
        requireActivity().getWindow().setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init()
        setTabs()
        setTabLayout(typeSelected)
        setData(true)

    }

    fun init(){
        ordersArray.clear()
        ordersArray.add("1")
        ordersArray.add("2")
        ordersArray.add("1")
        ordersArray.add("3")
        mainArray.addAll(ordersArray)
        (activity as ActivityHome?)!!.drawColor()
        (activity as ActivityHome?)!!.setTitleAc(AppHelper.getRemoteString("order_type",requireContext()))
        (activity as ActivityHome)!!.showTitle(true)
        (activity as ActivityHome)!!.showLogout(false)
        (activity as ActivityHome)!!.setTintLogo(R.color.redPrimary)
        if(!MyApplication.fromFooterOrder){
            (activity as ActivityHome)!!.showBack(true)
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
                if(s.length>0) {
                    ordersArray.clear()
                    for (item in mainArray) {
                        if (item.contains(s)) {
                            ordersArray.add(item)
                        }
                    }
                    adapter!!.notifyDataSetChanged()
                }else{
                    ordersArray.clear()
                    ordersArray.addAll(mainArray)
                    adapter!!.notifyDataSetChanged()
                }

            }
        })

    }

    private fun setTabs(){
        for (i in 0 until linearTabs.childCount){
            linearTabs.getChildAt(i).onOneClick{
                if(typeSelected!=i){
                    var tv=linearTabs.getChildAt(i) as TextView
                    setTabLayout(i)
                    setData(true)
                }
            }
        }
    }

    override fun onItemClicked(view: View, position: Int) {
        if(view.id==R.id.llLocation){
            AppHelper.onOneClick {
                startActivity(
                    Intent(requireActivity(), ActivityMap::class.java)
                        .putExtra(
                            "mapTitle",
                            AppHelper.getRemoteString("view_address", requireContext())
                        )
                )
            }
        }else if(view.id==R.id.llViewOrderDetails){
            AppHelper.onOneClick {
                startActivity(Intent(requireActivity(), ActivityOrderDetails::class.java))
            }
        }else if(view.id==R.id.ivOrderCall){
            AppHelper.onOneClick {
                val intent = Intent(Intent.ACTION_DIAL)
                startActivity(intent)
            }

        }else if(view.id==R.id.ivOrderMessage){
            AppHelper.onOneClick {
                val uri = Uri.parse("smsto:12346556")
                val it = Intent(Intent.ACTION_SENDTO, uri)
                it.putExtra("sms_body", "Here you can set the SMS text to be sent")
                startActivity(it)
            }
        }
    }

    fun setSelected(position: Int){

        when (position) {
            0 ->{
                tvActive.setBackgroundResource(R.drawable.rounded_red_background)
                AppHelper.setTextColor(requireContext(),tvActive,R.color.white)
            }
            1 ->{
                tvUpcoming.setBackgroundResource(R.drawable.rounded_red_background)
                AppHelper.setTextColor(requireContext(),tvUpcoming,R.color.white)
            }
            2 -> {
                tvCompleted.setBackgroundResource(R.drawable.rounded_red_background)
                AppHelper.setTextColor(requireContext(),tvCompleted,R.color.white)
            }
            3 -> {
                tvCancelled.setBackgroundResource(R.drawable.rounded_red_background)
                AppHelper.setTextColor(requireContext(),tvCancelled,R.color.white)
            }
            else -> {

            }
        }
    }
    fun setTabLayout(position: Int){
        typeSelected=position
        for (i in 0 until linearTabs.childCount){
            if (linearTabs.getChildAt(i) is TextView){
                var tv=linearTabs.getChildAt(i) as TextView
                tv.setBackgroundResource(R.color.transparent)
                AppHelper.setTextColor(requireContext(),tv,R.color.redPrimary)
            }
        }

        setSelected(position)

    }

    private fun setData(type:Boolean){
        adapter = AdapterOrderType(ordersArray, this, requireContext())
        rvOrderDetails.adapter = adapter
        var glm2 = GridLayoutManager(requireContext(), 1)
        rvOrderDetails.layoutManager = glm2
    }
}