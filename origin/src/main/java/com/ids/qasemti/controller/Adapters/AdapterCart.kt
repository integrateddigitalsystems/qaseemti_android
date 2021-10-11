package com.ids.qasemti.controller.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.internal.LinkedTreeMap
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.Address
import com.ids.qasemti.model.RequestPlaceOrder
import com.ids.qasemti.utils.AppHelper

import java.util.ArrayList

class AdapterCart(
    val items: ArrayList<RequestPlaceOrder>,
    private val itemClickListener: RVOnItemClickListener,
    context: Context
) :
    RecyclerView.Adapter<AdapterCart.VHItem>() {

    var con = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {
        holder.title.text = items.get(position).title
        holder.cost.text = items.get(position).price



    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var delete = itemView.findViewById<ImageView>(R.id.ivDeleteItem)
        var title = itemView.findViewById<TextView>(R.id.tvCartTitle)
        var cost = itemView.findViewById<TextView>(R.id.tvItemCost)

        init {
            itemView.setOnClickListener(this)
            delete.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}