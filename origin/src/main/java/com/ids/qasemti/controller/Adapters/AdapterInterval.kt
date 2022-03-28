package com.ids.qasemti.controller.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Adapters.com.ids.qasemti.controller.Adapters.com.ids.qasemti.model.TimeDuration
import com.ids.qasemti.controller.Adapters.com.ids.qasemti.model.ServiceAvailableDateTime
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.CountryCodes
import com.ids.qasemti.utils.AppHelper
import java.text.SimpleDateFormat
import java.util.*


class AdapterInterval(
    val items: ArrayList<TimeDuration>,
    private val itemClickListener: RVOnItemClickListener
) :
    RecyclerView.Adapter<AdapterInterval.VHItem>() {

    var fullTimeFormatter = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
    var smallTimeFormatter = SimpleDateFormat("HH:mm",Locale.ENGLISH)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(
            LayoutInflater.from(parent.context).inflate(R.layout.item_time_period, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {
        try {
            var dateF = fullTimeFormatter.parse(items.get(position).from)
            var dateT = fullTimeFormatter.parse(items.get(position).to)
            holder.tvName.text = smallTimeFormatter.format(dateF) + "-" + smallTimeFormatter.format(dateT)
        }catch (ex:Exception){
            holder.tvName.text = items.get(position).from +"-"+items.get(position).to
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvName = itemView.findViewById<TextView>(R.id.tvTimePeriod)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}