package com.ids.qasemti.controller.Adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppHelper
import java.util.ArrayList

class AdapterSettlements(
    val items: ArrayList<String>,
    private val itemClickListener: RVOnItemClickListener,
    context: Context
) :
    RecyclerView.Adapter<AdapterSettlements.VHItem>() {

    var con = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(
            LayoutInflater.from(parent.context).inflate(R.layout.item_settlements, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {

      if(!MyApplication.upcoming!!){

          holder.details.text = AppHelper.getRemoteString("view_order_details",con)
      }else{
          holder.details.text = AppHelper.getRemoteString("viewRelatedOrders",con)
      }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var details = itemView.findViewById<TextView>(R.id.tvViewDetails)


        init {
            details.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}