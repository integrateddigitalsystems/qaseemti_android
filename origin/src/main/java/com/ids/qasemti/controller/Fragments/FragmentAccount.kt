package com.ids.qasemti.controller.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Activities.ActivityServices
import com.ids.qasemti.controller.Activities.ActivitySettlements
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.AppHelper
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.layout_profile.*


class FragmentAccount : Fragment(), RVOnItemClickListener {
    override fun onItemClicked(view: View, position: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.qasemti.R.layout.fragment_account, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        listeners()
    }

    fun init(){

    }

    fun listeners(){
        btMyServices.setOnClickListener{
            startActivity(Intent(requireActivity(),ActivityServices::class.java))
        }

        btSettelments.setOnClickListener{
            startActivity(Intent(requireActivity(),ActivitySettlements::class.java))
        }

        btMyProfile.setOnClickListener{
            (requireActivity() as ActivityHome?)!!.addFrag(FragmentProfile(),AppConstants.FRAGMENT_PROFILE)
        }

        btLanguage.setOnClickListener{
            val bottom_fragment = FragmentBottomSeetLanguage()
            bottom_fragment.show(requireActivity().supportFragmentManager,"fragment_change_language")
        }
    }
}