package com.ids.qasemti.controller.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.AppHelper

import java.util.ArrayList

class AdapterOrders(val items: ArrayList<String>, private val itemClickListener: RVOnItemClickListener, context: Context) :
    RecyclerView.Adapter<AdapterOrders.VHItem>() {

    var con = context
      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(LayoutInflater.from(parent.context).inflate(R.layout.item_orders, parent, false))
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {
        if(MyApplication.languageCode == AppConstants.LANG_ARABIC){
            holder.title.text = holder.title.text.toString()+": " + "شراء"
        }else{
            holder.title.text = holder.title.text.toString()+": " + "Purchase"
        }

        holder.expected.typeface = AppHelper.getTypeFaceBold(con)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var title = itemView.findViewById<TextView>(R.id.tvCategoryOrder)
        var expected = itemView.findViewById<TextView>(R.id.tvDateExpected)
        var location = itemView.findViewById<LinearLayout>(R.id.llLocation)
        init {
            itemView.setOnClickListener(this)
            location.setOnClickListener(this)
         }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}