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
import com.ids.qasemti.model.ResponseNominatim
import com.ids.qasemti.model.ResponseOrders
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.setColorTypeface
import java.util.ArrayList

class AdapterMapLocations(val items: ArrayList<ResponseNominatim>, private val itemClickListener: RVOnItemClickListener, context: Context) :
    RecyclerView.Adapter<AdapterMapLocations.VHItem>() {

    var con = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(LayoutInflater.from(parent.context).inflate(R.layout.item_map_location, parent, false))
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {

        holder.location.text = items.get(position).displayName
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var location = itemView.findViewById<TextView>(R.id.tvSearchedLocation)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}