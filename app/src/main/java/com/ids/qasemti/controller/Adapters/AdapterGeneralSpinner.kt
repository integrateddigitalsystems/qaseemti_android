package com.ids.qasemti.controller.Adapters

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.ids.qasemti.R
import com.ids.sampleapp.model.ItemSpinner

class AdapterGeneralSpinner(
    context: Context, textViewResourceId: Int,
    private val values: ArrayList<ItemSpinner>,
    private val type:Int
) : ArrayAdapter<ItemSpinner>(context, textViewResourceId, values) {

    override fun getCount(): Int {
        return values.size
    }

    override fun getItem(position: Int): ItemSpinner? {
        return values[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        // label.setTextColor(Color.BLACK)
        /* if(type==AppConstants.LEFT_SECONDARY) {
             AppHelper.setTextColor(context, label, R.color.secondary)
             label.gravity=Gravity.START
         }else if(type==AppConstants.CENTER_WHITE) {
             AppHelper.setTextColor(context, label, R.color.white)
             label.gravity=Gravity.CENTER
         }else if(type==AppConstants.LEFT_WHITE){
             AppHelper.setTextColor(context, label, R.color.white)
             label.gravity=Gravity.START
             label.setPadding(40,0,40,0)
             //AppHelper.setMargins(context,label,12,0,12,0)
         }
         else if(type==AppConstants.LEFT_BLACK){
             AppHelper.setTextColor(context, label, R.color.black)
             label.gravity=Gravity.START
             label.setPadding(40,0,40,0)
             //AppHelper.setMargins(context,label,12,0,12,0)
         }*/
        label.text = values[position].name

        label.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.resources.getDimension(R.dimen.small_font_2))

        return label
    }

    override fun getDropDownView(
        position: Int, convertView: View?,
        parent: ViewGroup
    ): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK)
        label.text = values[position].name
      /*  if(values[position].selected) {
            label.setBackgroundResource(R.drawable.selected_spinner)
        }else{
            label.setBackgroundColor(ContextCompat.getColor(context,R.color.white))
        }*/

        return label
    }
}