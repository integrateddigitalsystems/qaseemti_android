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
import java.util.ArrayList


class AdapterInterval(
    val items: ArrayList<TimeDuration>,
    private val itemClickListener: RVOnItemClickListener
) :
    RecyclerView.Adapter<AdapterInterval.VHItem>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(
            LayoutInflater.from(parent.context).inflate(R.layout.item_popup, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {
        holder.tvName.text = items.get(position).from + "-" + items.get(position).to

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvName = itemView.findViewById<TextView>(R.id.tvName)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}