package com.ids.qasemti.controller.Adapters

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.ResponseOrders
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.layout_order_switch.*
import java.util.*

class AdapterOrderType(
    val items: ArrayList<ResponseOrders>,
    private val itemClickListener: RVOnItemClickListener,
    context: Activity
) :
    RecyclerView.Adapter<AdapterOrderType.VHItem>() {

    var con = context
    var delivered = 0
    var onTrack = 0
    var paid = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_order_not_cancelled, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {

        holder.orderDetails.setColorTypeface(con,R.color.button_blue,"",false)
        try {
            holder.name.text = items.get(position).customer!!.first_name + " " + items.get(position).customer!!.last_name
            holder.name.typeface = AppHelper.getTypeFaceBold(con)
            holder.locationText.setColorTypeface(con,R.color.redPrimary,items.get(position).customerLocation!!,false) }
        catch (ex:Exception){ holder.name.text = "" }

        try{holder.locationText.text=items.get(position).shipping_address_name!!}catch (e:Exception){}
        try{
            holder.orderDate.text = AppHelper.formatDate(items[position].date!!,"yyyy-MM-dd HH:mm:ss.SSSSSS","dd MMM yyyy hh:mm")
        }catch (ex:java.lang.Exception){
            if(items[position].date!=null)
               holder.orderDate.text = items[position].date!!
            else
                holder.orderDate.text = ""
        }

        try{
            holder.tvOrderDateValue.text = AppHelper.formatDate(items[position].date!!,"yyyy-MM-dd HH:mm:ss.SSSSSS","dd MMM yyyy hh:mm")
        }catch (ex:java.lang.Exception){
            if(items[position].date!=null)
               holder.tvOrderDateValue.text=items[position].date!!
            else
               holder.tvOrderDateValue.text = ""
        }
        try{
            holder.expectedDate.text = items.get(position).deliveryDate!!
        }catch (ex:java.lang.Exception){holder.expectedDate.text=""}
        try{
            holder.orderId.text = "#"+items.get(position).orderId.toString()
        }catch (ex:java.lang.Exception){holder.orderId.text =""}
        try{
            holder.ratingBar.rating = items.get(position).vendor!!.rate!!.toFloat()
        }catch (ex:java.lang.Exception){holder.ratingBar.rating = 0f}
        try{
            if(items[position].paymentMethod!=null && items[position].paymentMethod!!.isNotEmpty())
                holder.paymentMethod.text = items[position].paymentMethod
        }catch (ex:java.lang.Exception){}
        try {
            holder.orderCost.text = items[position].total!!+" "+items.get(position).currency
        }catch (ex:Exception){ holder.orderCost.text =""}
        try{
            holder.cancelReasonDetails.text = items.get(position).cancellationReason
        } catch (ex:Exception){
                holder.cancelReasonDetails.text =""
        }
        try{
            holder.cancelledname.text = items.get(position).cancelledByName
        }catch (ex:Exception){
            holder.cancelledname.text =""
        }

        try {
            onTrack= if(items.get(position).onTrack!!) 1 else 0
            holder.switchOnTrack.isChecked = items[position].onTrack!!
            AppHelper.setSwitchColor(holder.switchOnTrack,con)
        }catch (ex:Exception){}
        try {
            paid= if(items.get(position).paid!!) 1 else 0
            holder.switchPaid.isChecked = items[position].paid!!
            AppHelper.setSwitchColor(holder.switchPaid,con)
        }catch (ex:java.lang.Exception){}
        try {
            delivered= if(items.get(position).delivered!!) 1 else 0
            holder.switchDelivered.isChecked = items[position].delivered!!
            AppHelper.setSwitchColor(holder.switchDelivered,con)
        }catch (ex:java.lang.Exception){}

        if(paid==1) {
            holder.switchPaid.isChecked = true
            holder.switchPaid.isEnabled = false
            AppHelper.setSwitchColor(holder.switchPaid,con)
        }
        if(delivered==1) {
            holder.switchDelivered.isChecked = true
            holder.switchDelivered.isEnabled = false
            AppHelper.setSwitchColor(holder.switchDelivered,con)
        }

        holder.switchDelivered.setOnClickListener {
            AppHelper.createSwitchDialog(
                con,
                AppHelper.getRemoteString("ok", con),
                AppHelper.getRemoteString("cancel", con),
                con.getString(
                    R.string.are_you_sure_change_status
                ),
                holder.switchDelivered
            ) {
                if (holder.switchDelivered.isChecked) {
                    holder.switchDelivered.isEnabled = false
                    MyApplication.saveLocationTracking = false
                    MyApplication.selectedOrder = items.get(position)
                    (con as ActivityHome).changeState()
                    delivered = 1
                    AppHelper.setSwitchColor(holder.switchDelivered,con)
                } else {
                    delivered = 0
                    AppHelper.setSwitchColor(holder.switchDelivered,con)
                }

                AppHelper.updateStatus(
                    items.get(position).orderId!!.toInt(),
                    onTrack,
                    delivered,
                    paid
                )
            }
        }
        holder.switchOnTrack.setOnClickListener {
            AppHelper.createSwitchDialog(
                con,
                AppHelper.getRemoteString("ok", con),
                AppHelper.getRemoteString("cancel", con),
                con.getString(
                    R.string.are_you_sure_change_status
                ),
                holder.switchOnTrack
            ) {
                if (holder.switchOnTrack.isChecked) {
                    MyApplication.selectedOrder = items.get(position)
                    MyApplication.saveLocationTracking = true
                    (con as ActivityHome).changeState()
                    AppHelper.setSwitchColor(holder.switchOnTrack,con)
                    onTrack = 1
                } else {
                    onTrack = 0
                    AppHelper.setSwitchColor(holder.switchOnTrack,con)
                }
                AppHelper.updateStatus(
                    items.get(position).orderId!!.toInt(),
                    onTrack,
                    delivered,
                    paid
                )

                //AppHelper.setUpDoc(items.get(position))
            }
        }

        holder.switchPaid.setOnClickListener {

            AppHelper.createSwitchDialog(
                con,
                AppHelper.getRemoteString("ok", con),
                AppHelper.getRemoteString("cancel", con),
                con.getString(
                    R.string.are_you_sure_change_status
                ),
                holder.switchPaid
            ) {
                if (holder.switchPaid.isChecked) {
                    holder.switchPaid.isEnabled = false
                    MyApplication.saveLocationTracking = false
                    paid = 1
                    AppHelper.setSwitchColor(holder.switchPaid,con)
                } else {
                    paid= 0
                    AppHelper.setSwitchColor(holder.switchPaid,con)
                }
                AppHelper.updateStatus(
                    items.get(position).orderId!!.toInt(),
                    onTrack,
                    delivered,
                    paid
                )
            }
        }






        holder.cancelPerson.hide()
        holder.cancelReason.hide()
        holder.cancelorder.hide()
        holder.phoneChat.show()
        holder.cancelBord.hide()
        holder.expectedDel.show()
        holder.orderAmount.show()
        holder.dates.show()
        if (MyApplication.isClient && items.get(position).orderStatus.equals(AppConstants.ORDER_TYPE_ACTIVE)) {
            holder.credit.show()
            holder.switch.hide()
            holder.dateBorder.hide()
            holder.sepActive.show()
            holder.titelOrderDate.text = AppHelper.getRemoteString("order_date",con)
            holder.rating.hide()
            holder.canSep.hide()
            holder.location.hide()
            holder.border.show()
            holder.track.show()
        } else if(items.get(position).orderStatus.equals(AppConstants.ORDER_TYPE_ACTIVE)){
            holder.switch.show()
            holder.sepActive.show()
            holder.dateBorder.show()
            holder.bottomBorder.show()
            holder.canSep.hide()
            holder.border.show()
            holder.rating.hide()
            holder.track.show()
            holder.credit.hide()
        }else if ( items.get(position).orderStatus.equals(AppConstants.ORDER_TYPE_UPCOMING) && !MyApplication.isClient) {
            holder.switch.show()
            holder.sepActive.show()
            holder.dateBorder.show()
            holder.bottomBorder.show()
            holder.canSep.hide()
            holder.switch.hide()
            holder.rating.hide()
            holder.track.hide()
            holder.credit.hide()
        } else if (items.get(position).orderStatus.equals(AppConstants.ORDER_TYPE_UPCOMING) && MyApplication.isClient) {
            holder.credit.hide()
            holder.rating.hide()
            holder.dateBorder.show()
            holder.upcomingSeperator.hide()
            holder.switch.hide()
            holder.location.show()
            holder.border.hide()
            holder.canSep.hide()
            holder.track.hide()
        } else if(items.get(position).orderStatus.equals(AppConstants.ORDER_TYPE_COMPLETED)) {
            holder.phoneChat.hide()
            holder.credit.hide()
            holder.rating.show()
            holder.track.hide()
            holder.upcomingSeperator.show()
            holder.location.show()
            holder.canSep.hide()
            holder.cancelorder.show()
            holder.switch.hide()
            holder.titelOrderDate.text = AppHelper.getRemoteString("Actual_delivery",con)
            try{
                if(items[position].actual_delivery_date!=null && items[position].actual_delivery_date!!.isNotEmpty())
                  holder.orderDate.text = items[position].actual_delivery_date
                else
                    holder.orderDate.text = AppHelper.getRemoteString("no_data",con)
            }catch (e:Exception){
                holder.orderDate.text = AppHelper.getRemoteString("no_data",con)
            }
        }else{
            holder.titelOrderDate.text = AppHelper.getRemoteString("cancelDate",con)
            holder.orderDate.text = items[position].cancellationDate
            holder.phoneChat.hide()
            holder.rating.hide()
            holder.canSep.show()
            holder.bottomBorder.hide()
            holder.cancelPerson.show()
            holder.cancelReason.show()
            holder.upcomingSeperator.hide()
            holder.cancelorder.show()
            holder.credit.hide()
            holder.dateBorder.show()
            holder.track.hide()
            holder.expectedDel.hide()
            holder.switch.hide()
            holder.orderAmount.hide()
            holder.cancelBord.show()
        }

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
        var dateBorder = itemView.findViewById<LinearLayout>(R.id.llDateBorder)
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
        var cancelledname = itemView.findViewById<TextView>(R.id.tvCancelledByname)
        var switchOnTrack = itemView.findViewById<SwitchCompat>(R.id.swOnTrack)
        var switchDelivered = itemView.findViewById<SwitchCompat>(R.id.swDelivered)
        var switchPaid = itemView.findViewById<SwitchCompat>(R.id.swPaid)
        var orderDetails = itemView.findViewById<TextView>(R.id.tvViewOrderDetails)
        var tvOrderDateValue = itemView.findViewById<TextView>(R.id.tvOrderDateValue)


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