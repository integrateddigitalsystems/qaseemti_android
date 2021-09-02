package com.ids.qasemti.controller.Fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Adapters.AdapterServices
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.ServiceItem
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.show
import kotlinx.android.synthetic.main.fragment_home_client.*

class FragmentHomeClient : Fragment(), RVOnItemClickListener {

    var dialog: Dialog? = null
    var array: ArrayList<ServiceItem> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(com.ids.qasemti.R.layout.fragment_home_client, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutServices)
        init()

    }

    fun init() {


        if (MyApplication.isSignedIn)
            (activity as ActivityHome?)!!.showLogout(true)
        else
            (activity as ActivityHome?)!!.showLogout(false)

        (activity as ActivityHome?)!!.showBack(false)

        llFilter.setOnClickListener {
            showPopupSocialMedia()
        }

        AppHelper.setTitle(requireActivity(), getString(R.string.our_services), "services")

        array.add(ServiceItem("Gravel Truck", "", R.drawable.icon_truck))
        array.add(ServiceItem("Water Tank", "", R.drawable.icon_water))
        array.add(ServiceItem("Rubble Truck for Long Term", "", R.drawable.icon_rubble_truck))
        array.add(ServiceItem("Water Pump", "", R.drawable.icon_water_pump))
        array.add(ServiceItem("Rubble Truck", "", R.drawable.icon_rubble))
        array.add(ServiceItem("Water Pump", "", R.drawable.icon_water))
        array.add(ServiceItem("Gravel Truck", "", R.drawable.icon_truck))

        var adapter = AdapterServices(array, this, requireContext())
        rvServices.layoutManager = LinearLayoutManager(requireContext())
        rvServices.adapter = adapter
        rvServices.isNestedScrollingEnabled = false

        if (array.size == 0) {
            rvServices.hide()
            llFilter.hide()
            llNodata.show()
        }


    }

    private fun showPopupSocialMedia() {


        dialog = Dialog(requireContext(), R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.setContentView(R.layout.dialog_service)
        dialog!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialog!!.setCancelable(true)


        dialog!!.setOnCancelListener {
            llFilter.show()
            dialog!!.cancel()
        }
        dialog!!.setOnDismissListener {
            llFilter.show()
            dialog!!.cancel()
        }
        llFilter.hide()
        var close = dialog!!.findViewById<ImageView>(R.id.btClose)
        close.setOnClickListener {
            llFilter.show()
            dialog!!.cancel()
        }
        //btCancell!!.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()

    }

    override fun onItemClicked(view: View, position: Int) {

        if (view.id == R.id.linearService) {
            MyApplication.selectedService = array.get(position)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.homeContainer,
                    FragmentServiceDetails(),
                    AppConstants.FRAGMENT_SERVICE_DETAILS
                )
                .commit()
        }

    }
}