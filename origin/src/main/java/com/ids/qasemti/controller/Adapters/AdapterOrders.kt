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
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.setColorTypeface
import org.w3c.dom.Text

import java.util.ArrayList

class AdapterOrders(val items: ArrayList<ResponseOrders>, private val itemClickListener: RVOnItemClickListener, context: Context) :
    RecyclerView.Adapter<AdapterOrders.VHItem>() {

    var con = context
      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(LayoutInflater.from(parent.context).inflate(R.layout.item_orders, parent, false))
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {
        if(MyApplication.languageCode == AppConstants.LANG_ARABIC){
            holder.titleTop.text = holder.titleTop.text.toString()+": " + "شراء"
        }else{
            holder.titleTop.text = holder.titleTop.text.toString()+": " + "Purchase"
        }
        holder.category.text = items.get(position).product!!.name
        holder.expected.text = items.get(position).deliveryDate
        holder.orderDate.text = items.get(position).date
        try {
            holder.tvLocation.text = AppHelper.getAddress(
                items.get(position).userLat!!.toDouble(),
                items.get(position).userLong!!.toDouble(),
                con
            )
        }catch (ex:Exception){
            holder.tvLocation.text =items.get(position).addressname
        }
        holder.id.text ="# "+ items.get(position).orderId!!.toString()
        holder.expectedTitle.typeface = AppHelper.getTypeFaceBold(con)
        holder.tvLocation.setColorTypeface(con,R.color.redPrimary,"",false)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var category = itemView.findViewById<TextView>(R.id.tvOrderTitle)
        var orderDate = itemView.findViewById<TextView>(R.id.tvOrderDate)
        var expected = itemView.findViewById<TextView>(R.id.tvExpectedDate)
        var titleTop = itemView.findViewById<TextView>(R.id.tvCategoryOrder)
        var expectedTitle = itemView.findViewById<TextView>(R.id.tvDateExpected)
        var location = itemView.findViewById<LinearLayout>(R.id.llLocation)
        var id = itemView.findViewById<TextView>(R.id.tvOrderId)
        var tvLocation = itemView.findViewById<TextView>(R.id.tvLocationOrder)
        var layoutOrder = itemView.findViewById<LinearLayout>(R.id.llViewOrderDetails)
        var acceptOrder = itemView.findViewById<LinearLayout>(R.id.btAcceptOrder)
        init {
            itemView.setOnClickListener(this)
            location.setOnClickListener(this)
            layoutOrder.setOnClickListener(this)
            acceptOrder.setOnClickListener(this)
         }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}