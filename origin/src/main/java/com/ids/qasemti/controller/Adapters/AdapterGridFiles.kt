package com.ids.qasemti.controller.Adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.FilesSelected
import com.ids.qasemti.utils.loadImagesUrl
import com.ids.qasemti.utils.loadLocalImage

import java.util.ArrayList

class AdapterGridFiles(
    val items: ArrayList<FilesSelected>,
    private val itemClickListener: RVOnItemClickListener,
    context: Context
) :
    RecyclerView.Adapter<AdapterGridFiles.VHItem>() {

    var con = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(
            LayoutInflater.from(parent.context).inflate(R.layout.item_grid_files, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {
        if(items[position].file == null) {
            try{holder.ivSelectedFile.loadImagesUrl(items[position].name!!)}catch (e:Exception){}
        }
        else
            holder.ivSelectedFile.loadLocalImage(items[position].file!!)
        /*holder.btRemove.setOnClickListener{
            if(!MyApplication.isEditService){
               items.removeAt(position)
               notifyDataSetChanged()
            }
        }*/

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var ivSelectedFile = itemView.findViewById<ImageView>(R.id.ivSelectedFile)
        var btRemove = itemView.findViewById<LinearLayout>(R.id.btRemove)

        init {
          //  itemView.setOnClickListener(this)
            btRemove.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}