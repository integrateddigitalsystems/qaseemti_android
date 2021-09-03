package com.ids.qasemti.controller.Activities

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterMyServices
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.model.ServiceItem
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.show
import kotlinx.android.synthetic.main.actiivity_service_information.*
import kotlinx.android.synthetic.main.activity_services.*
import kotlinx.android.synthetic.main.activity_services.rootLayout
import kotlinx.android.synthetic.main.no_logo_layout.*
import kotlinx.android.synthetic.main.no_logo_layout.btBck
import kotlinx.android.synthetic.main.service_tab_1.*
import kotlinx.android.synthetic.main.service_tab_2.*
import kotlinx.android.synthetic.main.service_tab_3.*

class ActivityServiceInformation : ActivityBase(), RVOnItemClickListener {
    var array : ArrayList<ServiceItem> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actiivity_service_information)
        AppHelper.setAllTexts(rootLayout,this)
        init()


    }

    private fun init(){
        btBck.show()
    }

    private fun listeners(){
        btBck.setOnClickListener{super.onBackPressed()}
    }

    private fun setTabs(){
        btTab1.setOnClickListener{
            linearProgress1.setBackgroundColor(ContextCompat.getColor(this,R.color.gray_progress))
            linearProgress2.setBackgroundColor(ContextCompat.getColor(this,R.color.gray_progress))
            linearService1.show()
            linearService2.hide()
            linearService3.hide()

            btTab2.setBackgroundResource(R.drawable.circle_gray)
            btTab3.setBackgroundResource(R.drawable.circle_gray)


        }
    }



    override fun onItemClicked(view: View, position: Int) {

    }
}