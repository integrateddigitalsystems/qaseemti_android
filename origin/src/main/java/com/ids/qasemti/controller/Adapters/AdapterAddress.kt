package com.ids.qasemti.controller.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.model.Address
import com.ids.qasemti.model.ResponseAddress

import java.util.ArrayList

class AdapterAddress(
    val items: ArrayList<ResponseAddress>,
    private val itemClickListener: RVOnItemClickListener,
    context: Context
) :
    RecyclerView.Adapter<AdapterAddress.VHItem>() {

    var con = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(
            LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {
        holder.tvAddressTitle.text = items[position].addressName
        holder.tvAddressBody.text = items[position].desc + " ,"+ items[position].street + " ,"+ items[position].bldg + " ,"+items[position].floor
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvAddressTitle = itemView.findViewById<TextView>(R.id.tvAddressTitle)
        var tvAddressBody = itemView.findViewById<TextView>(R.id.tvAddressBody)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}