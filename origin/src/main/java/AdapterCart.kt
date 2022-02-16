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

import com.ids.qasemti.model.ResponseOrders
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.logw
import com.ids.qasemti.utils.show
import org.w3c.dom.Text

import java.util.ArrayList

class AdapterCart(
    val items: ArrayList<ResponseOrders>,
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
        try {
            if(items.get(position).vendor!=null){
                holder.delete.hide()
            }else{
                holder.delete.show()
            }
            if (!items.get(position).product!!.name.isNullOrEmpty())
                holder.title.text = items.get(position).product!!.name
            else
                holder.title.text = AppHelper.getRemoteString("no_data", con)
        }catch (ex:Exception){
            logw("errorCart",position.toString())
            holder.title.text = AppHelper.getRemoteString("no_data", con)
        }
        if(!items.get(position).orderId.isNullOrEmpty()){
            holder.id.text = items.get(position).orderId
        }
        holder.cost.text = items.get(position).total+" "+items.get(position).currency



    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var delete = itemView.findViewById<ImageView>(R.id.ivDeleteItem)
        var title = itemView.findViewById<TextView>(R.id.tvCartTitle)
        var cost = itemView.findViewById<TextView>(R.id.tvItemCost)
        var id = itemView.findViewById<TextView>(R.id.tvCartId)

        init {
            itemView.setOnClickListener(this)
            delete.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}