package com.ids.qasemti.controller.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener

import com.ids.qasemti.model.Address
import com.ids.qasemti.utils.AppHelper


class FragmentNotifications : Fragment() , RVOnItemClickListener {

    var array : ArrayList<Address> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.qasemti.R.layout.fragment_cart, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun init(){
        AppHelper.setTitle(requireActivity(),getString(R.string.notifications),"notifications")
    }


    override fun onItemClicked(view: View, position: Int) {

    }
}