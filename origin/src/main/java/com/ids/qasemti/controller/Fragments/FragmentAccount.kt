package com.ids.qasemti.controller.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.*
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_place_order.*
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.rootLayout
import kotlinx.android.synthetic.main.layout_profile.*
import kotlinx.android.synthetic.main.toolbar.*


class FragmentAccount : Fragment(), RVOnItemClickListener {
    override fun onItemClicked(view: View, position: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if(MyApplication.isSettle){
            startActivity(Intent(requireActivity(), ActivitySettlements::class.java))
        }
        (activity as ActivityHome).showTitle(false)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(com.ids.qasemti.R.layout.fragment_account, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayout, requireContext())
        init()
        listeners()
    }

    fun init() {
        (activity as ActivityHome).showBack(false)
        (activity as ActivityHome).showLogout(false)
        (activity as ActivityHome).showTitle(false)
        showData()
        //AppHelper.setTitle(requireActivity(), MyApplication.selectedTitle!!, "",R.color.redPrimary)
        if (MyApplication.register) {
            MyApplication.register = false
            (requireActivity() as ActivityHome?)!!.addFrag(
                FragmentProfile(),
                AppConstants.FRAGMENT_PROFILE
            )
        }

    }

    fun showData() {
        if (MyApplication.isClient) {
            btShareApp.show()
            btLogoutAccount.show()
            btMyAddresses.show()
            btMyServices.hide()
            btSettelments.hide()
            llLastSeperator.show()
            llLastSeperator2.show()
            llSepTop.hide()

        } else {
            btShareApp.show()
            btLogoutAccount.show()
            btMyAddresses.hide()
            btMyServices.hide()
            btSettelments.show()
            llLastSeperator.hide()
            llLastSeperator2.show()
        }
    }

    fun listeners() {

        MyApplication.fromProfile = true
        btMyAddresses.onOneClick {
            MyApplication.typeSelected = 1 
            startActivity(
                Intent(requireContext(), ActivityAddresses::class.java)
                    .putExtra("mapTitle", AppHelper.getRemoteString("MyAddresses", requireContext()))
                    .putExtra("from","account")
            )
        }
        btMyServices.onOneClick {
            MyApplication.selectedTitle = AppHelper.getRemoteString("MyServices",requireContext())
            MyApplication.fromAccount = true
            (requireActivity() as ActivityHome?)!!.addFrag(
                FragmentMyServices(),
                AppConstants.FRAGMENT_MY_SERVICES
            )
        }

        btSettelments.onOneClick {
            startActivity(Intent(requireActivity(), ActivitySettlements::class.java))
        }

        btMyProfile.onOneClick {
            MyApplication.tempCivilId = null
            MyApplication.tempCivilIdBack = null
            MyApplication.tempProfilePic = null
            MyApplication.selectedTitle = AppHelper.getRemoteString("Profile",requireContext())
            (requireActivity() as ActivityHome?)!!.addFrag(
                FragmentProfile(),
                AppConstants.FRAGMENT_PROFILE
            )
        }

        btLanguage.onOneClick {
            val bottom_fragment = FragmentBottomSeetLanguage()
            bottom_fragment.show(
                requireActivity().supportFragmentManager,
                "fragment_change_language"
            )
        }
        btNotifications.onOneClick {
            val bottom_fragment = FragmentBottomSheetPush()
            bottom_fragment.show(
                requireActivity().supportFragmentManager,
                "fragment_push_notifications"
            )
        }
        btContactAdministrator.onOneClick {
            startActivity(
                Intent(requireContext(), ActivityWeb::class.java)
                    .putExtra("webTitle", AppHelper.getRemoteString("ContactAdministrator",requireContext()))
                    .putExtra("webId",4)
            )
        }
        btLogoutAccount.onOneClick {
            MyApplication.doneOrders.clear()
            AppHelper.toGSOnDOne(MyApplication.doneOrders)
            (requireActivity() as ActivityHome?)!!.showLogoutDialog(requireActivity())
        }

        btPrivacy.onOneClick {
            startActivity(
                Intent(requireContext(), ActivityWeb::class.java)
                    .putExtra("webTitle", AppHelper.getRemoteString("PrivacyPolicy",requireContext()))
                    .putExtra("webId",3)
            )
        }
        btTermsAndConditions.onOneClick {
            startActivity(
                Intent(requireContext(), ActivityWeb::class.java)
                    .putExtra("webTitle", AppHelper.getRemoteString("TermsAndConditions",requireContext()))
                    .putExtra("webId",2)
            )
        }

        btRateUs.onOneClick {
            AppHelper.shareApp(requireActivity())
        }

        btShareApp.onOneClick {
            AppHelper.shareAppIntent(requireActivity())
        }
    }
}