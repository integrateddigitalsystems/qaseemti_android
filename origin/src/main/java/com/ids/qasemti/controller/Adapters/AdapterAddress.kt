package com.ids.qasemti.controller.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.model.ResponseAddress
import com.ids.qasemti.utils.AppHelper

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
        /*if(!items[position].desc.equals("null")&&!items[position].desc.isNullOrEmpty()){
            holder.tvAddressBody.text = items[position].desc
        }
        if(!items[position].street.equals("null")&&!items[position].street.isNullOrEmpty()){
            holder.tvAddressBody.text =  holder.tvAddressBody.text.toString()+","+items[position].street
        }
        if(!items[position].bldg.equals("null")&&!items[position].bldg.isNullOrEmpty()){
            holder.tvAddressBody.text =  holder.tvAddressBody.text.toString()+","+items[position].bldg
        }
        if(!items[position].floor.equals("null")&&!items[position].floor.isNullOrEmpty()){
            holder.tvAddressBody.text =  holder.tvAddressBody.text.toString()+","+items[position].floor
        }*/
        holder.tvAddressBody.text = AppHelper.getAddressText(items[position])

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvAddressTitle = itemView.findViewById<TextView>(R.id.tvAddressTitle)
        var tvAddressBody = itemView.findViewById<TextView>(R.id.tvAddressBody)
        var deleteAddress = itemView.findViewById<ImageView>(R.id.btDeleteAddress)

        init {
            deleteAddress.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}