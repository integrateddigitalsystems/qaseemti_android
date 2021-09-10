package com.ids.qasemti.controller.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityAddresses
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Activities.ActivityServices
import com.ids.qasemti.controller.Activities.ActivitySettlements
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.show
import kotlinx.android.synthetic.main.activity_place_order.*
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.rootLayout
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
        AppHelper.setAllTexts(rootLayout,requireContext())
        init()
        listeners()
    }

    fun init(){
        (activity as ActivityHome).showBack(false)
        (activity as ActivityHome).showLogout(false)
        (activity as ActivityHome).showTitle(false)
        showData()

    }

    fun showData(){
        if(MyApplication.isClient){
            btShareApp.show()
            btLogoutAccount.show()
            btMyAddresses.show()
            btMyServices.hide()
            btSettelments.hide()
            llLastSeperator.show()
            llLastSeperator2.show()
            llSepTop.hide()

        }else{
            btShareApp.hide()
            btLogoutAccount.hide()
            btMyAddresses.hide()
            btMyServices.show()
            btSettelments.show()
            llLastSeperator.hide()
            llLastSeperator2.hide()
        }
    }

    fun listeners(){

        btMyAddresses.setOnClickListener {
            startActivity(Intent(requireContext(),ActivityAddresses::class.java)
                .putExtra("mapTitle",getString(R.string.address)))
        }
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
        btNotifications.setOnClickListener {
            val bottom_fragment = FragmentBottomSheetPush()
            bottom_fragment.show(requireActivity().supportFragmentManager,"fragment_push_notifications")
        }
    }
}