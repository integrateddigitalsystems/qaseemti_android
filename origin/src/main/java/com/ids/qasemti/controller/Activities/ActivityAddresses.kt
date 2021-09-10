package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterAddress
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.ActivityBase

import com.ids.qasemti.model.Address
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.fragment_addresses.*
import kotlinx.android.synthetic.main.toolbar.*

class ActivityAddresses : ActivityBase() , RVOnItemClickListener {

    var array : ArrayList<Address> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_addresses)
        AppHelper.setAllTexts(rootLayout,this)
        init()
        listeners()
        setData()

    }

    fun init(){
        var title = intent.getStringExtra("mapTitle")
        tvPageTitle.setColorTypeface(this,R.color.white,title!!,false)
        btBackTool.setOnClickListener {
            super.onBackPressed()
        }
        btBackTool.show()
        AppHelper.setLogoTint(btBackTool,this,R.color.white)
     }

    private fun listeners(){
        btAddNew.setOnClickListener{
            startActivityForResult(Intent(this,ActivityAddNewAddress::class.java)
                .putExtra("mapTitle",getString(R.string.new_address)),1)
        }
    }

    private fun setData(){
        array.add(Address(1,"My Home","Jabriya"))
        array.add(Address(1,"My Work","Salmiya"))

        var adapter = AdapterAddress(array,this,this)
        rvAddresses.layoutManager = LinearLayoutManager(this)
        rvAddresses.adapter = adapter
        rvAddresses.isNestedScrollingEnabled = false

        if(array.size==0){
            rvAddresses.hide()
        }

    }


    override fun onItemClicked(view: View, position: Int) {

    }
}