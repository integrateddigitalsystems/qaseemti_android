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


class FragmentAccount : Fragment(), RVOnItemClickListener {
    override fun onItemClicked(view: View, position: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            btShareApp.hide()
            btLogoutAccount.show()
            btMyAddresses.hide()
            btMyServices.show()
            btSettelments.show()
            llLastSeperator.hide()
            llLastSeperator2.hide()
        }
    }

    fun listeners() {

        MyApplication.fromProfile = true
        btMyAddresses.onOneClick {
            MyApplication.typeSelected = 1 
            startActivity(
                Intent(requireContext(), ActivityAddresses::class.java)
                    .putExtra("mapTitle", AppHelper.getRemoteString("address", requireContext()))
                    .putExtra("from","account")
            )
        }
        btMyServices.onOneClick {
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
        btContactAdministrator.setOnClickListener {
            startActivity(Intent(requireActivity(), ActivityContactUs::class.java))
        }
        btLogoutAccount.onOneClick {
            AppHelper.createYesNoDialog(
                requireActivity(),
                AppHelper.getRemoteString("logout", requireContext()),
                AppHelper.getRemoteString("cancel", requireContext()),
                AppHelper.getRemoteString("sureLogout", requireContext())
            ) {
                MyApplication.isSignedIn = false
                MyApplication.fromLogout = true
                MyApplication.deviceId = 0
                MyApplication.phoneNumber = ""
                MyApplication.userId = 0
                if (MyApplication.isClient) {
                    MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_CLIENT
                    MyApplication.selectedFragment = FragmentHomeClient()
                } else {
                    MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_SP
                    MyApplication.selectedFragment = FragmentHomeSP()
                }
                MyApplication.selectedPos = 2
                requireActivity().finishAffinity()
                startActivity(
                    Intent(
                        requireContext(),
                        ActivityMobileRegistration::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }

        }
        btPrivacy.setOnClickListener {
            startActivity(
                Intent(requireContext(), ActivityWeb::class.java)
                    .putExtra("webTitle", "Privacy Policy")
            )
        }

        btRateUs.onOneClick {
            AppHelper.openAppInPlayStore(requireActivity())
        }
    }
}