package com.ids.qasemti.controller.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.utils.AppHelper
import kotlinx.android.synthetic.main.curve_layout_home.*
import kotlinx.android.synthetic.main.layout_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.white_logo_layout.*
import org.androidannotations.annotations.App

class FragmentProfile : Fragment(), RVOnItemClickListener {
    override fun onItemClicked(view: View, position: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.qasemti.R.layout.layout_profile, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutProfile)
        init()

    }

    fun init(){

        (activity as ActivityHome?)!!.showLogout(false)
        tvToolbarCurveTitle.visibility = View.GONE


    }
}