package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterMyServices
import com.ids.qasemti.controller.Adapters.AdapterServices
import com.ids.qasemti.controller.Adapters.AdapterSettlements
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.ServiceItem
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.show
import kotlinx.android.synthetic.main.activity_settlement.*
import kotlinx.android.synthetic.main.activity_settlement.linearTabs
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.toolbar.*


class ActivitySettlements : ActivityBase(),RVOnItemClickListener {
    var array : ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settlement)
        AppHelper.setAllTexts(rootLayout,this)
        init()
        listeners()
    }

    private fun init(){
        array.clear()
        repeat(3){array.add("1")}
        btBck.show()
        setTabs()
        setTabLayout(MyApplication.settlementTabSelected,tvToBeSettled)
        setData(MyApplication.settlementTabSelected)
    }

    private fun listeners(){
        btBck.setOnClickListener{super.onBackPressed()}
    }

    private fun setData(position: Int){
        var adapter = AdapterSettlements(array,this,this)
        rvSettlements.layoutManager = LinearLayoutManager(this)
        rvSettlements.adapter = adapter
        rvSettlements.isNestedScrollingEnabled = false

        if(position==0)
            btRequestSettlements.show()
        else
            btRequestSettlements.hide()


    }

    override fun onItemClicked(view: View, position: Int) {

    }

    private fun setTabs(){
        for (i in 0 until linearTabs.childCount){
            linearTabs.getChildAt(i).setOnClickListener{
                if(MyApplication.settlementTabSelected !=i){
                    var tv=linearTabs.getChildAt(i) as TextView
                    setTabLayout(i,tv)
                    setData(i)
                }
            }
        }
    }

    fun setTabLayout(position: Int,tvSelected:TextView){
        MyApplication.settlementTabSelected =position
        for (i in 0 until linearTabs.childCount){
            if (linearTabs.getChildAt(i) is TextView){
                var tv=linearTabs.getChildAt(i) as TextView
                tv.setBackgroundResource(R.color.transparent)
                AppHelper.setTextColor(this,tv,R.color.redPrimary)
            }
        }
        tvSelected.setBackgroundResource(R.drawable.rounded_red_background)
        AppHelper.setTextColor(this,tvSelected,R.color.white)

    }
}