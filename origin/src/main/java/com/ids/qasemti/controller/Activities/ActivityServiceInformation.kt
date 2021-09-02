package com.ids.qasemti.controller.Activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterMyServices
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.model.ServiceItem
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.show
import kotlinx.android.synthetic.main.activity_services.*
import kotlinx.android.synthetic.main.no_logo_layout.*

class ActivityServiceInformation : ActivityBase(), RVOnItemClickListener {
    var array : ArrayList<ServiceItem> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)
        AppHelper.setAllTexts(rootLayout)
        init()


    }

    private fun init(){
        btBck.show()
    }

    private fun listeners(){
        btBck.setOnClickListener{super.onBackPressed()}
    }



    override fun onItemClicked(view: View, position: Int) {

    }
}