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
import com.ids.qasemti.model.OrderData
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.hide
import java.util.ArrayList

class AdapterOrderData(val items: ArrayList<OrderData>, private val itemClickListener: RVOnItemClickListener, context: Context) :
    RecyclerView.Adapter<AdapterOrderData.VHItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(LayoutInflater.from(parent.context).inflate(R.layout.item_data_border, parent, false))
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {

        holder.title.text = items.get(position).title
        holder.value.text = items.get(position).value
        if(position==items.size-1){
            holder.line.hide()
        }



    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var title = itemView.findViewById<TextView>(R.id.tvDataName)
        var value = itemView.findViewById<TextView>(R.id.tvDataValue)
        var line = itemView.findViewById<LinearLayout>(R.id.llLine)

        init {
            itemView.setOnClickListener(this)
            value.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}