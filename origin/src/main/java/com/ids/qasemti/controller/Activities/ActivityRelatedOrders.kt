package com.ids.qasemti.controller.Activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterRelatedOrder
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.RelatedOrder
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_place_order.*
import kotlinx.android.synthetic.main.activity_related_orders.*
import kotlinx.android.synthetic.main.activity_related_orders.rootLayout
import kotlinx.android.synthetic.main.toolbar.*

class ActivityRelatedOrders : AppCompactBase() , RVOnItemClickListener  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_related_orders)
        AppHelper.setAllTexts(rootLayout,this)


        rvRelatedOrders.layoutManager = LinearLayoutManager(this)
        rvRelatedOrders.adapter = AdapterRelatedOrder(MyApplication.relatedOrders,this,this)
        var title = intent.getStringExtra("settelmentId")
        var titleWord = AppHelper.getRemoteString("Related_orders_settlement",this)
        tvPageTitle.setColorTypeface(this,R.color.white,titleWord+"\n"+title,true)
        btBackTool.show()
        btBackTool.onOneClick {
            super.onBackPressed()
        }
        tvTitleDues.setColorTypeface(this,R.color.gray_font_title,"",true)
        tvTitleEarnings.setColorTypeface(this,R.color.gray_font_title,"",true)
        tvTitleOrders.setColorTypeface(this,R.color.gray_font_title,"",true)

        if(MyApplication.relatedOrders.size ==0 ){
            tvNoData.show()
        }else{
            tvNoData.hide()
        }




    }

    override fun onItemClicked(view: View, position: Int) {

    }
}