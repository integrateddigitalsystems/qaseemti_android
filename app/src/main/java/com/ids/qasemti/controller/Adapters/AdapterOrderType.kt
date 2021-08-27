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
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.AppHelper

import java.util.ArrayList

class AdapterOrderType(val items: ArrayList<String>, private val itemClickListener: RVOnItemClickListener, context: Context, type: Boolean) :
    RecyclerView.Adapter<AdapterOrderType.VHItem>() {

    var con = context
    var type = type
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(LayoutInflater.from(parent.context).inflate(R.layout.item_order_not_cancelled, parent, false))
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {
        if(MyApplication.languageCode == AppConstants.LANG_ARABIC){
            holder.title.text = holder.title.text.toString()+": " + "شراء"
        }else{
            holder.title.text = holder.title.text.toString()+": " + "Purchase"
        }

        if(type){
            if(MyApplication.isClient&&MyApplication.typeSelected==1){
                holder.credit.visibility = View.VISIBLE
                holder.switch.visibility = View.GONE
                holder.location.visibility = View.GONE
                holder.border.visibility = View.VISIBLE
                holder.track.visibility = View.VISIBLE
            }else if(MyApplication.typeSelected==1 && MyApplication.typeSelected ==2 && !MyApplication.isClient){
                holder.switch.visibility = View.VISIBLE
                holder.credit.visibility = View.GONE
            }else if(MyApplication.typeSelected==2 && MyApplication.isClient){
                holder.credit.visibility = View.GONE
                holder.switch.visibility = View.GONE
                holder.location.visibility = View.VISIBLE
                holder.border.visibility = View.GONE
                holder.track.visibility = View.GONE
            }


            holder.rating.visibility=View.GONE

        }else{
            holder.phoneChat.visibility = View.GONE
            holder.credit.visibility = View.GONE
            holder.rating.visibility=View.VISIBLE
            holder.switch.visibility = View.GONE
        }

        holder.expected.typeface = AppHelper.getTypeFaceBold(con)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var title = itemView.findViewById<TextView>(R.id.tvCategoryOrder)
        var expected = itemView.findViewById<TextView>(R.id.tvDateExpected)
        var phoneChat = itemView.findViewById<LinearLayout>(R.id.llPhoneChat)
        var track = itemView.findViewById<LinearLayout>(R.id.llTrackOrder)
        var border = itemView.findViewById<LinearLayout>(R.id.llBorderCan)
        var location = itemView.findViewById<LinearLayout>(R.id.llLocation)
        var rating = itemView.findViewById<LinearLayout>(R.id.llRatings)
        var switch = itemView.findViewById<LinearLayout>(R.id.llSwitches)
        var credit = itemView.findViewById<LinearLayout>(R.id.llCredit)
        init {
            itemView.setOnClickListener(this)
            location.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}