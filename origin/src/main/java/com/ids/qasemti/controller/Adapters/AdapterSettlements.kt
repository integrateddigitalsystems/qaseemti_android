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
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.show
import com.ids.qasemti.utils.textRemote
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


        holder.details.text = AppHelper.getRemoteString("view_order_details",con)

      if(!MyApplication.upcoming!!){
          holder.idTitle.hide()

      }else{
          //holder.details.text = AppHelper.getRemoteString("viewRelatedOrders",con)
          holder.idTitle.show()
          holder.dateTitle.textRemote("settlement_date",con)
          holder.amountTitle.textRemote("settlement_amount",con)
          holder.earningsTitle.textRemote("earnings",con)
          holder.dues.hide()
      }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var details = itemView.findViewById<TextView>(R.id.tvViewDetails)
        var idTitle = itemView.findViewById<TextView>(R.id.tvIdSettlementTitle)
        var dateTitle = itemView.findViewById<TextView>(R.id.tvDateTitle)
        var dues = itemView.findViewById<LinearLayout>(R.id.llMyDues)
        var amountTitle = itemView.findViewById<TextView>(R.id.tvAmountTitle)
        var earningsTitle = itemView.findViewById<TextView>(R.id.tvEarningsTitle)


        init {
            details.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}