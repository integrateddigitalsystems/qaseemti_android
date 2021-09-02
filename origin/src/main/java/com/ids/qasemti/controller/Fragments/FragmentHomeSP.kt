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
import com.ids.qasemti.controller.Adapters.AdapterOrders
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.show
import kotlinx.android.synthetic.main.layout_home_orders.*

class FragmentHomeSP : Fragment() , RVOnItemClickListener {

    private var ordersArray: ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.layout_home_orders, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutOrders)
        init()

    }

    fun init(){
        (activity as ActivityHome?)!!.showLogout(false)
        (activity as ActivityHome?)!!.setTintLogo(R.color.redPrimary)
        rlActive.setOnClickListener {
            MyApplication.selectedFragment = FragmentOrders()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.homeContainer, FragmentOrders(), AppConstants.FRAGMENT_ORDER)
                .commit()
            MyApplication.typeSelected = 0

        }
        rlUpcoming.setOnClickListener {
            MyApplication.selectedFragment = FragmentOrders()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.homeContainer, FragmentOrders(), AppConstants.FRAGMENT_ORDER)
                .commit()

            MyApplication.typeSelected = 1
        }
        setOrders()

        swAvailable.setOnCheckedChangeListener { compoundButton, b ->
            if(swAvailable.isChecked){
                rvOrders.show()
                llNodata.hide()
            }else{
                rvOrders.hide()
                llNodata.show()
            }
        }

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

        if(ordersArray.size==0){
            rvOrders.hide()
            llNodata.show()
        }
    }

    override fun onItemClicked(view: View, position: Int) {

        if(view.id==R.id.llLocation){
            startActivity(Intent(requireActivity(),ActivityMap::class.java))
        }
    }
}