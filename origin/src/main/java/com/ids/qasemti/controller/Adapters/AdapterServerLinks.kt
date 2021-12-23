package com.ids.qasemti.controller.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.CountryCodes
import com.ids.qasemti.model.ServerLink
import com.ids.qasemti.utils.AppHelper
import java.util.ArrayList

class AdapterServerLinks(
    val items: ArrayList<ServerLink>,
    private val itemClickListener: RVOnItemClickListener
) :
    RecyclerView.Adapter<AdapterServerLinks.VHItem>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(
            LayoutInflater.from(parent.context).inflate(R.layout.item_link_server, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {
        holder.title.text = items.get(position).title
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var title = itemView.findViewById<TextView>(R.id.tvLinkTitle)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}