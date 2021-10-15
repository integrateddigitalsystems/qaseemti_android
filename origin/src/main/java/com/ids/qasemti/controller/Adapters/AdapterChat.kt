package com.ids.qasemti.controller.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.internal.LinkedTreeMap
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.Address
import com.ids.qasemti.model.ChatItem
import com.ids.qasemti.model.RequestPlaceOrder
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.show

import java.util.ArrayList

class AdapterChat(
    val items: ArrayList<ChatItem>,
    private val itemClickListener: RVOnItemClickListener,
    context: Context
) :
    RecyclerView.Adapter<AdapterChat.VHItem>() {

    var con = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {
        var chat = items.get(position)
        var sender = false
        if(chat.sendById==MyApplication.selectedUser!!.userId){
            sender = true
        }

        if(sender){
            holder.replyLayout.hide()
            holder.messageLayout.show()
            holder.messageText.text = chat.message
            holder.messageDate.text = chat.sentOn
        }else{
            holder.messageLayout.hide()
            holder.replyLayout.show()
            holder.replyText.text =chat.message
            holder.replyDate.text = chat.sentOn
        }

        if(chat.loading!!){
            holder.loading.show()
            holder.messageDate.hide()
        }else{
            holder.loading.hide()
            holder.messageDate.show()
        }


    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var replyLayout = itemView.findViewById<LinearLayout>(R.id.llReplyMessage)
        var messageLayout = itemView.findViewById<LinearLayout>(R.id.llUserMessage)
        var replyText = itemView.findViewById<TextView>(R.id.tvReply)
        var replyDate = itemView.findViewById<TextView>(R.id.tvReplydate)
        var messageText = itemView.findViewById<TextView>(R.id.tvMessage)
        var messageDate = itemView.findViewById<TextView>(R.id.tvMessageDate)
        var loading = itemView.findViewById<ImageView>(R.id.ivLoading)

        init {
            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}