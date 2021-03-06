package com.ids.qasemti.controller.Adapters

import android.widget.TextView
import com.ids.qasemti.model.PaymentMethod

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.model.FilesSelected
import com.ids.qasemti.utils.AppHelper

import java.util.ArrayList

class AdapterPaymentMethods(
    val items: ArrayList<PaymentMethod>,
    private val itemClickListener: RVOnItemClickListener,
    context: Context
) :
    RecyclerView.Adapter<AdapterPaymentMethods.VHItem>() {

    var con = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(
            LayoutInflater.from(parent.context).inflate(R.layout.item_payment_methods, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {
        holder.tvPaymentTitle.text = items[position].title
        if(!items[position].selected)
            holder.ivRadioPayment.setImageResource(R.drawable.blue_circle)
        else
            holder.ivRadioPayment.setImageResource(R.drawable.blue_circle_border)

        try{holder.ivPayment.setImageResource(AppHelper.getImageId(con,items[position].slug!!))}catch (e:Exception){}
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var ivRadioPayment = itemView.findViewById<ImageView>(R.id.ivRadioPayment)
        var tvPaymentTitle = itemView.findViewById<TextView>(R.id.tvPaymentTitle)
        var ivPayment = itemView.findViewById<ImageView>(R.id.ivPayment)
        init {
              itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}