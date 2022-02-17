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
import com.ids.qasemti.model.OrderData
import com.ids.qasemti.model.RelatedOrder
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.formatNumber
import com.ids.qasemti.utils.hide
import java.text.SimpleDateFormat
import java.util.*

class AdapterRelatedOrder(val items: ArrayList<RelatedOrder>, private val itemClickListener: RVOnItemClickListener, context: Context) :
    RecyclerView.Adapter<AdapterRelatedOrder.VHItem>() {

    var done : Boolean ?= false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(
            LayoutInflater.from(parent.context).inflate(R.layout.item_related_order, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {

        holder.value.text = items.get(position).grandTotal!!.formatNumber(AppConstants.TwoDecimalThousandsSeparator)+ " "+items.get(position).currency
        var simp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        var smp = SimpleDateFormat("dd/mm/yyyy",Locale.ENGLISH)
        holder.date.text = smp.format(simp.parse(items.get(position).orderDate))
        holder.due.text = items.get(position).adminFees!!.formatNumber(AppConstants.TwoDecimalThousandsSeparator) + " "+ items.get(position).currency
        holder.earning.text = items.get(position).orderEarning!!.formatNumber(AppConstants.TwoDecimalThousandsSeparator) + " "+ items.get(position).currency
        /*if(position==items.size-1&&done==false){
            holder.line.hide()
            done = true
        }*/


    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var value = itemView.findViewById<TextView>(R.id.tvOrderValue)
        var date = itemView.findViewById<TextView>(R.id.tvOrderDate)
        var due = itemView.findViewById<TextView>(R.id.tvOrderDue)
        var earning = itemView.findViewById<TextView>(R.id.tvOrderEarning)
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