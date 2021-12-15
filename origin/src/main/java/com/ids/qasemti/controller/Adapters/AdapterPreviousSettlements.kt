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
import com.ids.qasemti.model.ResponseOrders
import com.ids.qasemti.model.ResponseSettlement
import com.ids.qasemti.utils.*
import java.util.ArrayList

class AdapterPreviousSettlements(
    val items: ArrayList<ResponseSettlement>,
    private val itemClickListener: RVOnItemClickListener,
    context: Context
) :
    RecyclerView.Adapter<AdapterPreviousSettlements.VHItem>() {

    var con = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(
            LayoutInflater.from(parent.context).inflate(R.layout.item_settlements, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {


        holder.details.text = AppHelper.getRemoteString("view_related_orders", con)


        holder.idTitle.show()
        holder.dateTitle.textRemote("settlement_date", con)
        holder.amountTitle.textRemote("settlement_amount", con)
        holder.earningsTitle.textRemote("earnings", con)
        holder.dues.hide()

        holder.id.text = items.get(position).reqId
        holder.date.text = AppHelper.formatDate(items.get(position).settlementReqDate!!,"yyyy-MM-dd hh:mm:ss","dd MMMM yyyy hh:mm")
        holder.amount.text = items.get(position).total_amount!!.formatNumber(AppConstants.TwoDecimalThousandsSeparator)+ " "+ items.get(position).relatedOrders.get(0).currency
        holder.earn.text = items.get(position).totalEarnings!!.formatNumber(AppConstants.TwoDecimalThousandsSeparator)+" "+items.get(position).relatedOrders.get(0).currency

        var adds =0
        for(item in items.get(position).relatedOrders) {
            adds = adds + item.addCost!!.toInt()
        }

        if(adds >0){
            holder.addCostLayout.show()
            holder.sep.show()
            holder.additionCost.text = adds.toString()
        }else{
            holder.addCostLayout.hide()
            holder.sep.hide()
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
        var viewOrder = itemView.findViewById<LinearLayout>(R.id.llViewOrderDetails)
        var id = itemView.findViewById<TextView>(R.id.tvSettlementId)
        var date = itemView.findViewById<TextView>(R.id.tvOrderDateSett)
        var amount = itemView.findViewById<TextView>(R.id.tvSettAmount)
        var earn = itemView.findViewById<TextView>(R.id.tvEarnSett)
        var duesText = itemView.findViewById<TextView>(R.id.tvSettDues)
        var addCostLayout = itemView.findViewById<LinearLayout>(R.id.llAdditionalfees)
        var additionCost = itemView.findViewById<TextView>(R.id.tvAdditionalCost)
        var sep = itemView.findViewById<LinearLayout>(R.id.llAdditionalSep)


        init {
            details.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}