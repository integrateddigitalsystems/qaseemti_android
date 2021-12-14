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
import com.ids.qasemti.utils.*
import java.util.ArrayList

class AdapterSettlements(
    val items: ArrayList<ResponseOrders>,
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


        holder.details.text = AppHelper.getRemoteString("view_order_details", con)
        holder.idTitle.hide()
        holder.viewOrder.show()
        holder.id.text = items.get(position).orderId.toString()
        holder.date.text = AppHelper.formatDate(items.get(position).date!!,"yyyy-MM-dd hh:mm:ss","dd MMMM yyyy hh:mm")
        try{holder.amount.text = items.get(position).grand_total!!.formatNumber(AppConstants.TwoDecimalThousandsSeparator)+" "+items.get(position).currency}catch (e:Exception){}
        try{holder.earn.text = items.get(position).earnings!!.formatNumber(AppConstants.TwoDecimalThousandsSeparator) + " "+ items.get(position).currency}catch (e:Exception){}
        try{holder.addCost.text = items.get(position).additional!!.formatNumber(AppConstants.TwoDecimalThousandsSeparator) + " "+ items.get(position).currency}catch (e:Exception){}
        if(items.get(position).additional!!.isNullOrEmpty() || items.get(position).equals("0")){
            holder.addCostLayout.hide()
        }else{
            holder.addCostLayout.show()
        }
        holder.duesText.text = items.get(position).adminFees!!.formatNumber(AppConstants.TwoDecimalThousandsSeparator) + " "+items.get(position).currency


    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var details = itemView.findViewById<TextView>(R.id.tvViewDetails)
        var idTitle = itemView.findViewById<TextView>(R.id.tvIdSettlementTitle)
        var dateTitle = itemView.findViewById<TextView>(R.id.tvDateTitle)
        var dues = itemView.findViewById<LinearLayout>(R.id.llMyDues)
        var viewOrder = itemView.findViewById<LinearLayout>(R.id.llViewOrderDetails)
        var amountTitle = itemView.findViewById<TextView>(R.id.tvAmountTitle)
        var earningsTitle = itemView.findViewById<TextView>(R.id.tvEarningsTitle)
        var id = itemView.findViewById<TextView>(R.id.tvSettlementId)
        var date = itemView.findViewById<TextView>(R.id.tvOrderDateSett)
        var amount = itemView.findViewById<TextView>(R.id.tvSettAmount)
        var earn = itemView.findViewById<TextView>(R.id.tvEarnSett)
        var addCost = itemView.findViewById<TextView>(R.id.tvAdditionalCost)
        var addCostLayout = itemView.findViewById<LinearLayout>(R.id.llAdditionalfees)
        var duesText = itemView.findViewById<TextView>(R.id.tvSettDues)


        init {
            viewOrder.setOnClickListener(this)
            details.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}