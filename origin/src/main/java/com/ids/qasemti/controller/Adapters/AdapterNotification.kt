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
import com.ids.qasemti.model.Notification
import com.ids.qasemti.model.ServiceItem
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.show
import kotlinx.android.synthetic.main.fragment_checkout.*
import java.util.ArrayList

class AdapterNotification(
    val items: ArrayList<Notification>,
    private val itemClickListener: RVOnItemClickListener,
    context: Context
) :
    RecyclerView.Adapter<AdapterNotification.VHItem>() {

    var con = context
    var open = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(
            LayoutInflater.from(parent.context).inflate(R.layout.item_notifications, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {

        if(!items.get(position).image.isNullOrEmpty()) {
            Glide.with(con).load(items.get(position).image).into(holder.image);
        }else {
            holder.image.hide()
        }
        holder.title.text = items.get(position).title
        if(!items.get(position).details.isNullOrEmpty())
            holder.details.text = items.get(position).details
        else
            holder.details.hide()
        holder.linear.setOnClickListener {
            var up = 90
            var down = -90
            if(items.get(position).open){
                holder.arrow.animate().rotation(up.toFloat()).setDuration(300)
               /* var anim = AnimationUtils.loadAnimation(con, R.anim.slide_to_up)
                holder.more.startAnimation(anim)*/
                holder.more.hide()
            }else {
               holder.arrow.animate().rotation(down.toFloat()).setDuration(300)
              /*  var anim = AnimationUtils.loadAnimation(con, R.anim.slide_to_down)
                holder.more.startAnimation(anim)*/
                holder.more.show()
            }

            items.get(position).open = !items.get(position).open

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var title = itemView.findViewById<TextView>(R.id.tvNotificationTitle)
        var image = itemView.findViewById<ImageView>(R.id.ivNotificationImage)
        var arrow = itemView.findViewById<ImageView>(R.id.ivArrowNotf)
        var linear = itemView.findViewById<LinearLayout>(R.id.llNotificationTitle)
        var more = itemView.findViewById<LinearLayout>(R.id.llNotificationMore)
        var details = itemView.findViewById<TextView>(R.id.tvNotificationDetails)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}