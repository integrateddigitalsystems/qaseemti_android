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
import kotlinx.android.synthetic.main.activity_place_order.*
import kotlinx.android.synthetic.main.footer.*
import kotlinx.android.synthetic.main.home_container.*
import kotlinx.android.synthetic.main.layout_border_data.*
import kotlinx.android.synthetic.main.layout_border_red.*
import kotlinx.android.synthetic.main.toolbar.*
import org.androidannotations.annotations.App

class ActivityPlaceOrder : AppCompactBase(), RVOnItemClickListener {

    var fragMang: FragmentManager? = null
    var selected: Int = 0
    override fun onItemClicked(view: View, position: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_order)
        init()
        getSupportActionBar()!!.hide();
        btClose.show()

        btClose.setOnClickListener {
            super.onBackPressed()
        }
        tvPageTitle.text = getString(R.string.place_order)
        tvPageTitle.typeface = AppHelper.getTypeFace(this)
        btPLaceOrder.typeface = AppHelper.getTypeFace(this)
        btDrawer.hide()
        AppHelper.setTextColor(this, tvPageTitle, R.color.redPrimary)


    }

    private fun setSelectedTab(
        index: Int,
        fragment: Fragment,
        tag: String,
        selectedIcon: ImageView,
        drawerColor: Int
    ) {
        replaceFragment(R.id.homeContainer, supportFragmentManager, fragment, tag)
        tablayout.getTabAt(index)!!.select()
        MyApplication.selectedPos = index
        AppHelper.resetIcons(
            this,
            ivCartFooter,
            ivProductFooter,
            ivFooterOrder,
            ivFooterHome,
            ivFooterNotifications,
            ivFooterAccount
        )
        selectedIcon.layoutParams = LinearLayout.LayoutParams(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                30f,
                resources.displayMetrics
            ).toInt(),
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics)
                .toInt()
        )
        AppHelper.setLogoTint(btDrawer, this, drawerColor)
        setTintLogo(drawerColor)
        AppHelper.setUpFooter(this, MyApplication.selectedFragmentTag!!)
    }


    fun setTintLogo(color: Int) {
        AppHelper.setLogoTint(btDrawer, this, color)
        AppHelper.setTextColor(this, tvPageTitle, color)
    }

    fun setListners() {


        llFooterOrders.setOnClickListener {
            if (MyApplication.selectedFragmentTag != AppConstants.FRAGMENT_ORDER && ((MyApplication.isClient && MyApplication.isSignedIn) || !MyApplication.isClient))
                setSelectedTab(
                    1,
                    FragmentOrders(),
                    AppConstants.FRAGMENT_ORDER,
                    ivFooterOrder,
                    R.color.redPrimary
                )
        }

        llFooterNotifications.setOnClickListener {
            if (MyApplication.selectedFragmentTag != AppConstants.FRAGMENT_NOTFICATIONS && ((MyApplication.isClient && MyApplication.isSignedIn) || !MyApplication.isClient))
                setSelectedTab(
                    3,
                    FragmentNotifications(),
                    AppConstants.FRAGMENT_NOTFICATIONS, ivFooterNotifications, R.color.white
                )
        }

        llFooterAccount.setOnClickListener {
            if (MyApplication.selectedFragmentTag != AppConstants.FRAGMENT_ACCOUNT && ((MyApplication.isClient && MyApplication.isSignedIn) || !MyApplication.isClient))
                setSelectedTab(
                    4,
                    FragmentAccount(),
                    AppConstants.FRAGMENT_ACCOUNT,
                    ivFooterAccount,
                    R.color.white
                )
        }

        //other tabs
        llFooterCart.setOnClickListener {
            if (MyApplication.selectedFragmentTag != AppConstants.FRAGMENT_CART && MyApplication.isSignedIn)
                setSelectedTab(
                    0,
                    FragmentCart(),
                    AppConstants.FRAGMENT_CART,
                    ivCartFooter,
                    R.color.redPrimary
                )
        }


        llFooterHome.setOnClickListener {
            if (MyApplication.isClient) {
                if (MyApplication.selectedFragmentTag != AppConstants.FRAGMENT_HOME_CLIENT)
                    setSelectedTab(
                        2,
                        FragmentHomeClient(),
                        AppConstants.FRAGMENT_HOME_CLIENT,
                        ivFooterHome,
                        R.color.white
                    )
            } else {
                if (MyApplication.selectedFragmentTag != AppConstants.FRAGMENT_HOME_SP)
                    setSelectedTab(
                        2,
                        FragmentHomeSP(),
                        AppConstants.FRAGMENT_HOME_SP,
                        ivFooterHome,
                        R.color.white
                    )
            }
        }


        llFooterProducts.setOnClickListener {
            if (MyApplication.selectedFragmentTag != AppConstants.FRAGMENT_PROD)
                setSelectedTab(
                    0,
                    FragmentProducts(),
                    AppConstants.FRAGMENT_PROD,
                    ivProductFooter,
                    R.color.white
                )
        }

    }


    fun init() {

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

        rlNotService.hide()
        llMain.show()
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

        if (MyApplication.isClient) {
            llFooterProducts.hide()
            llFooterCart.show()
        } else {
            llFooterProducts.show()
            llFooterCart.hide()

        }

        setListners()

        AppHelper.setLogoTint(btDrawer, this, R.color.redPrimary)
    }
}