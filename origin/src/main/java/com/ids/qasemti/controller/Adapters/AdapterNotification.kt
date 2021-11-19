package com.ids.qasemti.controller.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.Notification
import com.ids.qasemti.model.ResponseNotification
import com.ids.qasemti.model.ServiceItem
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.setHTML
import com.ids.qasemti.utils.show
import kotlinx.android.synthetic.main.fragment_checkout.*
import java.util.ArrayList

class AdapterNotification(
    val items: ArrayList<ResponseNotification>,
    private val itemClickListener: RVOnItemClickListener,
    context: Context
) :
    RecyclerView.Adapter<AdapterNotification.VHItem>() {

    var con = context
    var up = 90
    var down = -90

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(
            LayoutInflater.from(parent.context).inflate(R.layout.item_notifications, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {

        var title = items[position].title
        var body = items[position].body

            holder.date.text = items[position].date
            holder.title.text = title
            /*if (!items.get(position).image.isNullOrEmpty())
            Glide.with(con).load(items.get(position).image).into(holder.image);*/
            if (!body.isNullOrEmpty())
                holder.details.setHTML(body)


        if(items.get(position).isViewed.equals("1")){
            holder.viewed.setImageResource(R.drawable.circle_gray)
        }else{
            holder.viewed.setImageResource(R.drawable.circle_unread)
        }

        if (items.get(position).open) {
          /*  if (!items.get(position).image.isNullOrEmpty())
                holder.image.show()
            else
                holder.image.hide()*/

            if (!body.isNullOrEmpty())
                holder.details.show()
            else
                holder.details.hide()

            holder.arrow.animate().rotation(down.toFloat())
        } else {
            holder.details.hide()
            holder.image.hide()
            holder.arrow.animate().rotation(up.toFloat())
        }


    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var title = itemView.findViewById<TextView>(R.id.tvNotificationTitle)
        var image = itemView.findViewById<ImageView>(R.id.ivNotificationImage)
        var arrow = itemView.findViewById<ImageView>(R.id.ivArrowNotf)
        var date = itemView.findViewById<TextView>(R.id.tvNotificationDate)
        var linear = itemView.findViewById<LinearLayout>(R.id.llNotificationTitle)
        var viewed = itemView.findViewById<ImageView>(R.id.ivViewed)
        var details = itemView.findViewById<TextView>(R.id.tvNotificationDetails)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}