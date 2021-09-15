package com.ids.qasemti.controller.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Activities.ActivityMapLocation
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.utils.AppHelper
import kotlinx.android.synthetic.main.curve_layout_home.*
import kotlinx.android.synthetic.main.layout_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.white_logo_layout.*


class FragmentProfile : Fragment(), RVOnItemClickListener {
    override fun onItemClicked(view: View, position: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.layout_profile, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutProfile,requireContext())
        init()

    }

    fun init(){
        //tvToolbarCurveTitle.setText(getString(R.string.profile))
        (activity as ActivityHome?)!!.showBack(R.color.white)
         AppHelper.setTitle(requireActivity(),getString(R.string.Profile),"profile")

       // (activity as ActivityHome?)!!.showLogout(false)
        tvToolbarCurveTitle.visibility = View.GONE
        ivMapButton.setOnClickListener {
            startActivity(Intent(requireContext(),ActivityMapLocation::class.java))
        }


    }
}