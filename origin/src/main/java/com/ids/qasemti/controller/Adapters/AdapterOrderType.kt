package com.ids.qasemti.controller.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.show

import java.util.ArrayList

class AdapterOrderType(
    val items: ArrayList<String>,
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
        if (MyApplication.languageCode == AppConstants.LANG_ARABIC) {
            holder.title.text = "سامي سليمان" + ": " + "شراء"
        } else {
            holder.title.text = "Sami Suleiman" + ": " + "Purchase"
        }
        holder.title.typeface = AppHelper.getTypeFace(con)


        if (MyApplication.isClient && MyApplication.typeSelected == 0) {
            holder.credit.show()
            holder.switch.hide()
            holder.sepActive.hide()
            holder.rating.hide()
            holder.location.hide()
            holder.border.show()
            holder.track.show()
        } else if ((MyApplication.typeSelected == 0 || MyApplication.typeSelected == 1) && !MyApplication.isClient) {
            holder.switch.show()
            holder.sepActive.hide()
            holder.rating.hide()
            holder.credit.hide()
        } else if (MyApplication.typeSelected == 1 && MyApplication.isClient) {
            holder.credit.hide()
            holder.rating.hide()
            holder.upcomingSeperator.hide()
            holder.switch.hide()
            holder.location.show()
            holder.border.hide()
            holder.track.hide()
        } else if(MyApplication.typeSelected ==2) {
            holder.phoneChat.hide()
            holder.credit.hide()
            holder.rating.show()
            holder.switch.hide()
        }else{
            holder.phoneChat.hide()
            holder.dates.hide()
            holder.rating.hide()
            holder.cancelPerson.show()
            holder.cancelReason.show()
            holder.cancelorder.show()
            holder.credit.hide()
            holder.switch.hide()
            holder.cancelBord.show()
        }

        holder.expected.typeface = AppHelper.getTypeFaceBold(con)
        holder.cancelReasonDetails.typeface = AppHelper.getTypeFace(con)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var title = itemView.findViewById<TextView>(R.id.tvCategoryOrder)
        var expected = itemView.findViewById<TextView>(R.id.tvDateExpected)
        var location = itemView.findViewById<LinearLayout>(R.id.llLocation)
        var track = itemView.findViewById<LinearLayout>(R.id.llTrackOrder)
        var upcomingSeperator = itemView.findViewById<LinearLayout>(R.id.llSeperatorUpcoming)
        var sepActive = itemView.findViewById<LinearLayout>(R.id.llSepActive)
        var cancelReason = itemView.findViewById<LinearLayout>(R.id.llCancelReason)
        var cancelPerson = itemView.findViewById<LinearLayout>(R.id.llPersonCancel)
        var cancelorder = itemView.findViewById<LinearLayout>(R.id.llCancelOrderDate)
        var dates = itemView.findViewById<LinearLayout>(R.id.llDates)
        var details = itemView.findViewById<LinearLayout>(R.id.llViewOrderDetails)
        var cancelBord = itemView.findViewById<LinearLayout>(R.id.llCancelBorder)
        var phoneChat = itemView.findViewById<LinearLayout>(R.id.llPhoneChat)
        var border = itemView.findViewById<LinearLayout>(R.id.llBorderCan)
        var rating = itemView.findViewById<LinearLayout>(R.id.llRatings)
        var switch = itemView.findViewById<LinearLayout>(R.id.llSwitches)
        var credit = itemView.findViewById<LinearLayout>(R.id.llCredit)
        var call = itemView.findViewById<ImageView>(R.id.ivOrderCall)
        var message = itemView.findViewById<ImageView>(R.id.ivOrderMessage)
        var cancelReasonDetails = itemView.findViewById<TextView>(R.id.tvCancelReasonDetails)

        init {
            itemView.setOnClickListener(this)
            location.setOnClickListener(this)
            details.setOnClickListener(this)
            call.setOnClickListener(this)
            message.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}