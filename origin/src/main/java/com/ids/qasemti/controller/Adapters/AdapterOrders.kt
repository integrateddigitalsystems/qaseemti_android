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
        try{holder.category.text = items.get(position).product!!.name}catch (e:Exception){}
        try{holder.expected.text = items[position].deliveryDate!!}catch (e:Exception){}
        try{holder.orderDate.text = AppHelper.formatDate(items[position].date!!,"yyyy-mm-dd hh:mm:ssss","dd MMM yyyy hh:mm")}catch (e:Exception){}
        try{holder.tvLocation.text = AppHelper.getAddress(items[position].userLat!!.toDouble(),
            items[position].userLong!!.toDouble(),con)}catch (e:Exception){}
        try{holder.id.text ="# "+ items.get(position).orderId!!.toString()}catch (e:Exception){}
        try{holder.expectedTitle.typeface = AppHelper.getTypeFaceBold(con)}catch (e:Exception){}
        try{holder.tvLocation.setColorTypeface(con,R.color.redPrimary,items.get(position).customerLocation!!,false)}catch (e:Exception){}

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