package com.ids.qasemti.controller.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityAddNewAddress
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Adapters.AdapterAddress
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener

import com.ids.qasemti.model.Address
import com.ids.qasemti.utils.AppHelper
import kotlinx.android.synthetic.main.fragment_addresses.*
import kotlinx.android.synthetic.main.fragment_services.llFilter
import kotlinx.android.synthetic.main.fragment_services.llNodata
import kotlinx.android.synthetic.main.fragment_services.rvServices

class FragmentAddresses : Fragment() , RVOnItemClickListener {

    var array : ArrayList<Address> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.qasemti.R.layout.fragment_addresses, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayout)
        init()
        listeners()
        setData()

    }

    fun init(){
        (activity as ActivityHome?)!!.setTitleAc(getString(R.string.address))
     }

    private fun listeners(){
        btAddNew.setOnClickListener{
            startActivityForResult(Intent(requireActivity(),ActivityAddNewAddress::class.java),1)
        }
    }

    private fun setData(){
        array.add(Address(1,"My Home","Jabriya"))
        array.add(Address(1,"My Work","Salmiya"))

        var adapter = AdapterAddress(array,this,requireContext())
        rvAddresses.layoutManager = LinearLayoutManager(requireContext())
        rvAddresses.adapter = adapter
        rvAddresses.isNestedScrollingEnabled = false

        if(array.size==0){
            rvAddresses.visibility=View.GONE
        }
    }


    override fun onItemClicked(view: View, position: Int) {

    }
}