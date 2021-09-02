package com.ids.qasemti.controller.Activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterOrderData
import com.ids.qasemti.controller.Adapters.AdapterOtherOrderData
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.OrderData
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.MyExceptionHandler
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.show
import kotlinx.android.synthetic.main.activity_mobile_registration.*
import kotlinx.android.synthetic.main.activity_mobile_registration.rootLayoutMobileRegister
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.curve_layout_home.*
import kotlinx.android.synthetic.main.layout_border_data.*
import kotlinx.android.synthetic.main.layout_order_contact_tab.*
import kotlinx.android.synthetic.main.layout_request_new_time.*
import kotlinx.android.synthetic.main.toolbar.*

class ActivityOrderDetails: ActivityBase() , RVOnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        btDrawer.hide()
        AppHelper.setAllTexts(rootLayoutOrderDetails)
        tvPageTitle.typeface = AppHelper.getTypeFace(this)
        if(MyApplication.typeSelected==0) {
            tvPageTitle.text = getString(R.string.active_order_det)
            llEditOrderTime.show()
            llActualDelivery.hide()
        }else{
            tvPageTitle.text = getString(R.string.completed_order_details)
            llRatingOrder.visibility=View.VISIBLE
            llEditOrderTime.hide()
            llActualDelivery.show()
            llOrderSwitches.hide()
            btCancelOrder.hide()
        }


        var array:ArrayList<OrderData> = arrayListOf()
        array.add(OrderData("Category","Purchase"))
        array.add(OrderData("Service","Water Tank"))
        array.add(OrderData("Type","Fresh"))
        array.add(OrderData("Size/Capacity","200 Gallons"))
        array.add(OrderData("Quantity","1 Trip"))

        rvDataBorder.layoutManager = LinearLayoutManager(this)
        rvDataBorder.adapter = AdapterOtherOrderData(array,this,this)

    }

    override fun onItemClicked(view: View, position: Int) {


    }
}