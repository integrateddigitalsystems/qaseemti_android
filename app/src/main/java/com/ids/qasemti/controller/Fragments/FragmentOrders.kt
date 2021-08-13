package com.ids.qasemti.controller.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.ids.qasemti.controller.Adapters.AdapterOrders
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import kotlinx.android.synthetic.main.layout_home_orders.*

class FragmentOrders : Fragment() , RVOnItemClickListener {

    private var ordersArray: ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.qasemti.R.layout.layout_home_orders, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

    }

    fun init(){

        setOrders()
    }

    private fun setOrders() {
        ordersArray.clear()
        ordersArray.add("1")
        ordersArray.add("1")
        ordersArray.add("1")
        var adapter = AdapterOrders(ordersArray, this, requireContext())
        rvOrders.adapter = adapter
        var glm2 = GridLayoutManager(requireContext(), 1)
        rvOrders.layoutManager = glm2
    }

    override fun onItemClicked(view: View, position: Int) {

    }
}