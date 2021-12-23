package com.ids.qasemti.controller.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.CountryCodes
import com.ids.qasemti.utils.AppHelper
import java.util.ArrayList


class AdapterCountryCodes(
    val items: ArrayList<CountryCodes>,
    private val itemClickListener: RVOnItemClickListener
) :
    RecyclerView.Adapter<AdapterCountryCodes.VHItem>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(
            LayoutInflater.from(parent.context).inflate(R.layout.item_popup, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {
        holder.tvName.text = items[position].code + " "+ items[position].name

        if(MyApplication.selectedItemDialog.replace("+","").trim()==items[position].code!!.replace("+","").trim())
            holder.tvName.typeface= AppHelper.getTypeFaceBold(holder.itemView.context)
        else
            holder.tvName.typeface= AppHelper.getTypeFace(holder.itemView.context)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvName = itemView.findViewById<TextView>(R.id.tvName)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}