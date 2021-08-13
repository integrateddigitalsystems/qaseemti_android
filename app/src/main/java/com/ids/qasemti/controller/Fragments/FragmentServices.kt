package com.ids.qasemti.controller.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterServices
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.model.ServiceItem
import kotlinx.android.synthetic.main.curve_layout_home.*
import kotlinx.android.synthetic.main.fragment_services.*

class FragmentServices : Fragment() , RVOnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.qasemti.R.layout.fragment_services, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

    }

    fun init(){

        tvToolbarCurveTitle.text = getString(R.string.our_services)
        var array : ArrayList<ServiceItem> = arrayListOf()

        array.add(ServiceItem("Gravel Truck","", R.drawable.icon_truck))
        array.add(ServiceItem("Water Tank","", R.drawable.icon_water))
        array.add(ServiceItem("Rubble Truck for Long Term","", R.drawable.icon_rubble_truck))
        array.add(ServiceItem("Water Pump","", R.drawable.icon_water_pump))
        array.add(ServiceItem("Rubble Truck","", R.drawable.icon_rubble))
        array.add(ServiceItem("Water Pump","", R.drawable.icon_water))
        array.add(ServiceItem("Gravel Truck","", R.drawable.icon_truck))

        var adapter = AdapterServices(array,this,requireContext())
        rvServices.layoutManager = LinearLayoutManager(requireContext())
        rvServices.adapter = adapter
        rvServices.isNestedScrollingEnabled = false



    }

    override fun onItemClicked(view: View, position: Int) {

    }
}