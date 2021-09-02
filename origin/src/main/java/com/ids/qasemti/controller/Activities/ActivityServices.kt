package com.ids.qasemti.controller.Activities



import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterMyServices
import com.ids.qasemti.controller.Adapters.AdapterServices
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.model.ServiceItem
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.show
import kotlinx.android.synthetic.main.activity_services.*
import kotlinx.android.synthetic.main.no_logo_layout.*


class ActivityServices : ActivityBase(),RVOnItemClickListener {
    var array : ArrayList<ServiceItem> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)
        AppHelper.setAllTexts(rootLayout)
        init()
        listeners()
        setData()

    }

    private fun init(){
        btBck.show()
    }

    private fun listeners(){
        btBck.setOnClickListener{super.onBackPressed()}
        btAdd.setOnClickListener{startActivity(Intent(this,ActivityServiceInformation::class.java))}
    }

    private fun setData(){

        array.add(ServiceItem("Water Tank","", R.drawable.icon_water))
        array.add(ServiceItem("Sand Truck","", R.drawable.icon_truck))
        array.add(ServiceItem("Rubble Truck for Long Term","", R.drawable.icon_rubble_truck))


        var adapter = AdapterMyServices(array,this,this)
        rvServices.layoutManager = LinearLayoutManager(this)
        rvServices.adapter = adapter
        rvServices.isNestedScrollingEnabled = false
    }

    override fun onItemClicked(view: View, position: Int) {

    }
}