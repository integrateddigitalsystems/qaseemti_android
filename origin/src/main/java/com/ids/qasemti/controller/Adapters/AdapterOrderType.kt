package com.ids.qasemti.controller.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.recyclerview.widget.RecyclerView
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.ResponseOrders
import com.ids.qasemti.utils.*

import java.util.ArrayList

class AdapterOrderType(
    val items: ArrayList<ResponseOrders>,
    private val itemClickListener: RVOnItemClickListener,
    context: Context
) :
    RecyclerView.Adapter<AdapterOrderType.VHItem>() {

    var con = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_order_not_cancelled, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {

        try {
            holder.name.text = items.get(position).customer!!.first_name + " " + items.get(position).customer!!.last_name
            holder.name.typeface = AppHelper.getTypeFaceBold(con)
            holder.locationText.setColorTypeface(con,R.color.redPrimary,items.get(position).customerLocation!!,false) }
        catch (ex:Exception){ holder.name.text = "" }
        try{
            holder.orderDate.text = AppHelper.formatDate(items.get(position).date!!,"yyyy-MM-dd HH:mm:ss.SSSSSS","dd MMMM yyyy")
        }catch (ex:java.lang.Exception){holder.orderDate.text = ""}
        try{
            holder.expectedDate.text = items.get(position).deliveryDate
        }catch (ex:java.lang.Exception){holder.orderDate.text=""}
        try{
            holder.orderId.text = "#"+items.get(position).orderId.toString()
        }catch (ex:java.lang.Exception){holder.orderId.text =""}
        try{
            holder.ratingBar.rating = items.get(position).vendor!!.rate!!.toFloat()
        }catch (ex:java.lang.Exception){holder.ratingBar.rating = 0f}
        try{
            holder.paymentMethod.text = items.get(position).paymentMethodTitle
        }catch (ex:java.lang.Exception){holder.paymentMethod.text = ""}
        try {
            holder.orderCost.text = items.get(position).total+" "+items.get(position).currency
        }catch (ex:Exception){ holder.orderCost.text =""}
        try{
            holder.cancelReasonDetails.text = items.get(position).cancellationDate
        } catch (ex:Exception){
                holder.cancelReasonDetails.text =""
        }




        holder.cancelPerson.hide()
        holder.cancelReason.hide()
        holder.cancelorder.hide()
        holder.cancelBord.hide()
        holder.expectedDel.show()
        holder.orderAmount.show()
        holder.dates.show()
        if (MyApplication.isClient && MyApplication.typeSelected == 0) {
            holder.credit.show()
            holder.switch.hide()
            holder.sepActive.show()
            holder.titelOrderDate.text = AppHelper.getRemoteString("order_date",con)
            holder.rating.hide()
            holder.canSep.hide()
            holder.location.hide()
            holder.border.show()
            holder.track.show()
        } else if ((MyApplication.typeSelected == 0 || MyApplication.typeSelected == 1) && !MyApplication.isClient) {
            holder.switch.show()
            holder.sepActive.show()
            holder.canSep.hide()
            holder.rating.hide()
            holder.bottomBorder.hide()
            holder.credit.hide()
        } else if (MyApplication.typeSelected == 1 && MyApplication.isClient) {
            holder.credit.hide()
            holder.rating.hide()
            holder.upcomingSeperator.hide()
            holder.switch.hide()
            holder.location.show()
            holder.bottomBorder.hide()
            holder.border.hide()
            holder.canSep.hide()
            holder.track.hide()
        } else if(MyApplication.typeSelected ==2) {
            holder.phoneChat.hide()
            holder.credit.hide()
            holder.rating.show()
            holder.location.show()
            holder.canSep.hide()
            holder.cancelorder.show()
            holder.switch.hide()
            holder.titelOrderDate.text = AppHelper.getRemoteString("Actual_delivery",con)
        }else{
            holder.titelOrderDate.text = AppHelper.getRemoteString("cancelDate",con)
            holder.orderDate.text = items.get(position).cancellationDate
            holder.phoneChat.hide()
            holder.rating.hide()
            holder.cancelPerson.show()
            holder.cancelReason.show()
            holder.cancelorder.show()
            holder.credit.hide()
            holder.expectedDel.hide()
            holder.switch.hide()
            holder.orderAmount.hide()
            holder.cancelBord.show()
        }
        holder.phoneChat.show()

        holder.expected.typeface = AppHelper.getTypeFaceBold(con)
        holder.cancelReasonDetails.typeface = AppHelper.getTypeFace(con)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var name = itemView.findViewById<TextView>(R.id.tvNameOrder)
        var expected = itemView.findViewById<TextView>(R.id.tvDateExpected)
        var location = itemView.findViewById<LinearLayout>(R.id.llLocation)
        var locationText = itemView.findViewById<TextView>(R.id.tvOrderLocation)
        var track = itemView.findViewById<LinearLayout>(R.id.llTrackOrder)
        var upcomingSeperator = itemView.findViewById<LinearLayout>(R.id.llSeperatorUpcoming)
        var sepActive = itemView.findViewById<LinearLayout>(R.id.llSepActive)
        var cancelReason = itemView.findViewById<LinearLayout>(R.id.llCancelReason)
        var cancelPerson = itemView.findViewById<LinearLayout>(R.id.llPersonCancel)
        var orderAmount = itemView.findViewById<LinearLayout>(R.id.llOrderAmount)
        var cancelorder = itemView.findViewById<LinearLayout>(R.id.llCancelOrderDate)
        var orderDate = itemView.findViewById<TextView>(R.id.tvOrderDate)
        var expectedDate = itemView.findViewById<TextView>(R.id.tvExpectedDate)
        var titelOrderDate = itemView.findViewById<TextView>(R.id.tvOrderDateTitle)
        var dates = itemView.findViewById<LinearLayout>(R.id.llDates)
        var details = itemView.findViewById<LinearLayout>(R.id.llViewOrderDetails)
        var cancelBord = itemView.findViewById<LinearLayout>(R.id.llCancelBorder)
        var phoneChat = itemView.findViewById<LinearLayout>(R.id.llPhoneChat)
        var border = itemView.findViewById<LinearLayout>(R.id.llBorderCan)
        var rating = itemView.findViewById<LinearLayout>(R.id.llRatings)
        var canSep = itemView.findViewById<LinearLayout>(R.id.llSepCancel)
        var expectedDel = itemView.findViewById<LinearLayout>(R.id.llExpectedDelivery)
        var orderId = itemView.findViewById<TextView>(R.id.tvOrderId)
        var switch = itemView.findViewById<LinearLayout>(R.id.llSwitches)
        var credit = itemView.findViewById<LinearLayout>(R.id.llCredit)
        var call = itemView.findViewById<ImageView>(R.id.ivOrderCall)
        var dateExpected = itemView.findViewById<TextView>(R.id.tvDateExpected)
        var expectedLogo = itemView.findViewById<ImageView>(R.id.ivLogoExpected)
        var ratingBar = itemView.findViewById<AppCompatRatingBar>(R.id.ratingOrder)
        var message = itemView.findViewById<ImageView>(R.id.ivOrderMessage)
        var bottomBorder = itemView.findViewById<LinearLayout>(R.id.llSepBottom)
        var cancelReasonDetails = itemView.findViewById<TextView>(R.id.tvCancelReasonDetails)
        var paymentMethod = itemView.findViewById<TextView>(R.id.tvPaymentMethod)
        var orderCost = itemView.findViewById<TextView>(R.id.tvOrderPrice)

        init {
            itemView.setOnClickListener(this)
            location.setOnClickListener(this)
            details.setOnClickListener(this)
            call.setOnClickListener(this)
            track.setOnClickListener(this)
            message.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}