package com.ids.qasemti.controller.Adapters



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.FilesSelected
import com.ids.qasemti.model.RequiredFiles
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.loadLocalImage
import com.ids.qasemti.utils.show

import java.util.ArrayList

class AdapterRequiredFiles(
    val items: ArrayList<RequiredFiles>,
    private val itemClickListener: RVOnItemClickListener,
    context: Context
) :
    RecyclerView.Adapter<AdapterRequiredFiles.VHItem>() {

    var con = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(
            LayoutInflater.from(parent.context).inflate(R.layout.item_required_files, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {
        try{holder.tvFileTitle.text=items[position].getFileTitle()}catch (e:Exception){}
        try{holder.tvPickedFile.text=items[position].selectedFileName}catch (e:Exception){}

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var btPickFile = itemView.findViewById<Button>(R.id.btPickFile)
        var tvPickedFile = itemView.findViewById<TextView>(R.id.tvPickedFile)

        var tvFileTitle = itemView.findViewById<TextView>(R.id.tvFileTitle)
        var etLicenceNumber = itemView.findViewById<EditText>(R.id.etLicenceNumber)

        init {
              btPickFile.setOnClickListener(this)
            /*if(MyApplication.isEditService)
                btPickFile.hide()
            else*/
                btPickFile.show()
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(v, layoutPosition)
        }
    }
}