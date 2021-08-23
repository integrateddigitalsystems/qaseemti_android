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
import kotlinx.android.synthetic.main.fragment_services.*

class FragmentServices : Fragment() , RVOnItemClickListener {

    var dialog : Dialog ?=null
    var array : ArrayList<ServiceItem> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.qasemti.R.layout.fragment_services, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutServices)
        init()

    }

    fun init(){

        llFilter.setOnClickListener {
            showPopupSocialMedia()
        }

        (activity as ActivityHome?)!!.setTitleAc(getString(R.string.our_services))


        array.add(ServiceItem("Gravel Truck","", R.drawable.icon_truck))
        array.add(ServiceItem("Water Tank","", R.drawable.icon_water))
        array.add(ServiceItem("Rubble Truck for Long Term","", R.drawable.icon_rubble_truck))
        array.add(ServiceItem("Water Pump","", R.drawable.icon_water_pump))
        array.add(ServiceItem("Rubble Truck","", R.drawable.icon_rubble))
        array.add(ServiceItem("Water Pump","", R.drawable.icon_water))
        array.add(ServiceItem("Gravel Truck","", R.drawable.icon_truck))

        var adapter = AdapterServices(array,this,requireContext())
        rvServices.layoutManager = LinearLayoutManager(requireContext())
        rvServices.adapter = adapter
        rvServices.isNestedScrollingEnabled = false

        if(array.size==0){
            rvServices.visibility=View.GONE
            llFilter.visibility=View.GONE
            llNodata.visibility=View.VISIBLE
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
            llFilter.visibility=View.VISIBLE
            dialog!!.cancel()
        }
        dialog!!.setOnDismissListener {
            llFilter.visibility=View.VISIBLE
            dialog!!.cancel()
        }
        llFilter.visibility=View.GONE
        var close = dialog!!.findViewById<ImageView>(R.id.btClose)
        close.setOnClickListener {
            llFilter.visibility=View.VISIBLE
            dialog!!.cancel()
        }
        //btCancell!!.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()

    }

    override fun onItemClicked(view: View, position: Int) {

        if(view.id==R.id.linearService){
            MyApplication.selectedService = array.get(position)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.homeContainer, FragmentServiceDetails(), AppConstants.FRAGMENT_SERVICE_DETAILS)
                .commit()
        }

    }
}