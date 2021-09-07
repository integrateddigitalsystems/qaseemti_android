package com.ids.qasemti.controller.Adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.model.ServiceItem
import com.ids.qasemti.utils.AppHelper
import java.util.ArrayList

class AdapterMyServices(
    val items: ArrayList<ServiceItem>,
    private val itemClickListener: RVOnItemClickListener,
    context: Context
) :
    RecyclerView.Adapter<AdapterMyServices.VHItem>() {

    var con = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(
            LayoutInflater.from(parent.context).inflate(R.layout.item_services, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {

        Glide.with(con).load(items.get(position).testCode).into(holder.image);

        holder.image.setBackgroundResource(items.get(position).testCode!!)
        holder.title.text = items.get(position).title
        holder.qtPrice.typeface = AppHelper.getTypeFace(con)
        holder.qtEarn.typeface = AppHelper.getTypeFace(con)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var title = itemView.findViewById<TextView>(R.id.tvServiceTitle)
        var image = itemView.findViewById<ImageView>(R.id.ivServiceLogo)
        var qtPrice = itemView.findViewById<TextView>(R.id.tvPriceQt)
        var qtEarn = itemView.findViewById<TextView>(R.id.tvEarnQt)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}