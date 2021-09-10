package com.ids.qasemti.controller.Activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterRelatedOrder
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.model.RelatedOrder
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.setColorTypeface
import com.ids.qasemti.utils.show
import com.ids.qasemti.utils.textRemote
import kotlinx.android.synthetic.main.activity_place_order.*
import kotlinx.android.synthetic.main.activity_related_orders.*
import kotlinx.android.synthetic.main.activity_related_orders.rootLayout
import kotlinx.android.synthetic.main.toolbar.*

class ActivityRelatedOrders : AppCompatActivity() , RVOnItemClickListener  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_related_orders)
        AppHelper.setAllTexts(rootLayout,this)

        var array : ArrayList<RelatedOrder> = arrayListOf()
        array.add(RelatedOrder("01/03/2020","30 KWD","20 KWD","10KWD"))
        array.add(RelatedOrder("01/03/2020","30 KWD","20 KWD","10KWD"))
        array.add(RelatedOrder("01/03/2020","30 KWD","20 KWD","10KWD"))
        array.add(RelatedOrder("01/03/2020","30 KWD","20 KWD","10KWD"))
        array.add(RelatedOrder("01/03/2020","30 KWD","20 KWD","10KWD"))
        array.add(RelatedOrder("01/03/2020","30 KWD","20 KWD","10KWD"))
        array.add(RelatedOrder("01/03/2020","30 KWD","20 KWD","10KWD"))
        array.add(RelatedOrder("01/03/2020","30 KWD","20 KWD","10KWD"))
        array.add(RelatedOrder("01/03/2020","30 KWD","20 KWD","10KWD"))
        array.add(RelatedOrder("01/03/2020","30 KWD","20 KWD","10KWD"))
        array.add(RelatedOrder("01/03/2020","30 KWD","20 KWD","10KWD"))
        array.add(RelatedOrder("01/03/2020","30 KWD","20 KWD","10KWD"))
        array.add(RelatedOrder("01/03/2020","30 KWD","20 KWD","10KWD"))
        array.add(RelatedOrder("01/03/2020","30 KWD","20 KWD","10KWD"))
        array.add(RelatedOrder("01/03/2020","30 KWD","20 KWD","10KWD"))
        array.add(RelatedOrder("01/03/2020","30 KWD","20 KWD","10KWD"))

        rvRelatedOrders.layoutManager = LinearLayoutManager(this)
        rvRelatedOrders.adapter = AdapterRelatedOrder(array,this,this)
        var title = intent.getStringExtra("settelmentId")
        tvPageTitle.textRemote("relatedOrdersOf",this)
        tvPageTitle.setColorTypeface(this,R.color.white,tvPageTitle.text.toString()+title,false)
        btBackTool.show()
        btBackTool.setOnClickListener {
            super.onBackPressed()
        }
        tvTitleDues.setColorTypeface(this,R.color.gray_font_title,"",true)
        tvTitleEarnings.setColorTypeface(this,R.color.gray_font_title,"",true)
        tvTitleOrders.setColorTypeface(this,R.color.gray_font_title,"",true)




    }

    override fun onItemClicked(view: View, position: Int) {

    }
}