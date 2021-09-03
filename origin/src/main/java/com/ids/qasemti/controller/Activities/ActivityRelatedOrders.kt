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
import kotlinx.android.synthetic.main.activity_related_orders.*
import kotlinx.android.synthetic.main.toolbar.*

class ActivityRelatedOrders : AppCompatActivity() , RVOnItemClickListener  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_related_orders)

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

        tvPageTitle.text = "Related orders of settlement"
        tvPageTitle.typeface = AppHelper.getTypeFace(this)




    }

    override fun onItemClicked(view: View, position: Int) {

    }
}