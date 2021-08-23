package com.ids.qasemti.controller.Activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterServices
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.model.ServiceItem
import kotlinx.android.synthetic.main.curve_layout_home.*
import kotlinx.android.synthetic.main.fragment_services.*
import kotlinx.android.synthetic.main.toolbar.*

class ActivityServices : ActivityBase()  , RVOnItemClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_services)

        tvPageTitle.text = getString(R.string.our_services)
        var array : ArrayList<ServiceItem> = arrayListOf()

        array.add(ServiceItem("Gravel Truck","",R.drawable.icon_truck))
        array.add(ServiceItem("Water Tank","",R.drawable.icon_water))
        array.add(ServiceItem("Rubble Truck for Long Term","",R.drawable.icon_rubble_truck))
        array.add(ServiceItem("Water Pump","",R.drawable.icon_water_pump))
        array.add(ServiceItem("Rubble Truck","",R.drawable.icon_rubble))
        array.add(ServiceItem("Water Pump","",R.drawable.icon_water))
        array.add(ServiceItem("Gravel Truck","",R.drawable.icon_truck))

        var adapter = AdapterServices(array,this,this)
        rvServices.layoutManager = LinearLayoutManager(this)
        rvServices.adapter = adapter
        rvServices.isNestedScrollingEnabled = false



    }

    override fun onItemClicked(view: View, position: Int) {

    }
}