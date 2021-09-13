package com.ids.qasemti.controller.Activities

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterOrderData
import com.ids.qasemti.controller.Adapters.AdapterOtherOrderData
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.controller.Fragments.*
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.OrderData
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.activity_place_order.*
import kotlinx.android.synthetic.main.activity_place_order.rootLayout
import kotlinx.android.synthetic.main.footer.*
import kotlinx.android.synthetic.main.home_container.*
import kotlinx.android.synthetic.main.layout_border_data.*
import kotlinx.android.synthetic.main.layout_border_red.*
import kotlinx.android.synthetic.main.toolbar.*


class ActivityPlaceOrder : AppCompactBase(), RVOnItemClickListener {

    var fragMang: FragmentManager? = null
    var selected: Int = 0
    override fun onItemClicked(view: View, position: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_order)
        init()
        AppHelper.setAllTexts(rootLayout,this)
        getSupportActionBar()!!.hide();



    }
    fun setData(){
        fragMang = supportFragmentManager


        var array:ArrayList<OrderData> = arrayListOf()
        array.add(OrderData("Category","Purchase"))
        array.add(OrderData("Service","Water Tank"))
        array.add(OrderData("Type","Fresh"))
        array.add(OrderData("Size/Capacity","200 Gallons"))
        array.add(OrderData("Quantity","1 Trip"))
        rvDataBorder.layoutManager = LinearLayoutManager(this)
        rvDataBorder.adapter = AdapterOrderData(array,this,this)

        var array2:ArrayList<OrderData> = arrayListOf()
        array2.add(OrderData("Subtotal","10 KWD"))
        array2.add(OrderData("Additional fees due to delivery address","2 KWD"))
        array2.add(OrderData("Total Amount","12 KWD"))
        rvOtherData.layoutManager = LinearLayoutManager(this)
        rvOtherData.adapter = AdapterOtherOrderData(array2,this,this)
    }

    fun setListeners(){
        rbCash.setOnClickListener {
            if (selected != 0) {
                ivCash.setImageDrawable(
                    getResources().getDrawable(
                        R.drawable.blue_circle_border,
                        theme
                    )
                )
                ivKnet.setImageDrawable(
                    getResources().getDrawable(
                        R.drawable.blue_circle,
                        theme
                    )
                )
                ivVisa.setImageDrawable(
                    getResources().getDrawable(
                        R.drawable.blue_circle,
                        theme
                    )
                )

                selected = 0
            }
        }
        rbKnet.setOnClickListener {
            if (selected != 1) {
                ivCash.setImageDrawable(
                    getResources().getDrawable(
                        R.drawable.blue_circle,
                        theme
                    )
                )
                ivKnet.setImageDrawable(
                    getResources().getDrawable(
                        R.drawable.blue_circle_border,
                        theme
                    )
                )
                ivVisa.setImageDrawable(
                    getResources().getDrawable(
                        R.drawable.blue_circle,
                        theme
                    )
                )
                selected = 1
            }
        }
        rbVisa.setOnClickListener {
            if (selected != 2) {
                ivCash.setImageDrawable(
                    getResources().getDrawable(
                        R.drawable.blue_circle,
                        theme
                    )
                )
                ivKnet.setImageDrawable(
                    getResources().getDrawable(
                        R.drawable.blue_circle,
                        theme
                    )
                )
                ivVisa.setImageDrawable(
                    getResources().getDrawable(
                        R.drawable.blue_circle_border,
                        theme
                    )
                )
                selected = 2
            }
        }
    }
    fun init() {


        tvLocationPlaceOrder.setColorTypeface(this,R.color.redPrimary,"",false)
        tvPageTitle.show()
        tvPageTitle.textRemote("place_order",this)
        tvPageTitle.setColorTypeface(this,R.color.redPrimary,"",true)
        btBackTool.show()
        btBackTool.setOnClickListener {
            super.onBackPressed()
        }
        btPLaceOrder.typeface = AppHelper.getTypeFace(this)
        btClose.hide()
        AppHelper.setLogoTint(btBackTool,this,R.color.redPrimary)

        if(MyApplication.position==1) {
            rlNotService.show()
            llMain.hide()
        }else{
            rlNotService.hide()
            llMain.show()
        }


        if (MyApplication.isClient) {
            llFooterProducts.hide()
            llFooterCart.show()
        } else {
            llFooterProducts.show()
            llFooterCart.hide()

        }
        setListeners()
        setData()
        AppHelper.setLogoTint(btDrawer, this, R.color.redPrimary)
    }
}