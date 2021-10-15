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
import com.ids.qasemti.model.ResponseService
import com.ids.qasemti.model.ServiceItem
import com.ids.qasemti.utils.AppHelper
import java.util.ArrayList

class AdapterServices(
    val items: ArrayList<ResponseService>,
    private val itemClickListener: RVOnItemClickListener,
    context: Context
) :
    RecyclerView.Adapter<AdapterServices.VHItem>() {

    var con = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(
            LayoutInflater.from(parent.context).inflate(R.layout.item_service, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {

        Glide.with(con).load(R.drawable.icon_truck).into(holder.image);

       // holder.image.setBackgroundResource(items.get(position).testCode!!)
        holder.title.text = items.get(position).name
        if(position==0){
            AppHelper.setPaddings(con,holder.linear,0,14,0,0)
        } else if(position == items.size-1){
            AppHelper.setPaddings(con,holder.linear,0,0,0,70)
        }else{
            AppHelper.setPaddings(con,holder.linear,0,0,0,0)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var title = itemView.findViewById<TextView>(R.id.tvServiceTitle)
        var image = itemView.findViewById<ImageView>(R.id.ivServiceLogo)
        var linear = itemView.findViewById<LinearLayout>(R.id.linearService)

        init {
            itemView.setOnClickListener(this)
            linear.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}