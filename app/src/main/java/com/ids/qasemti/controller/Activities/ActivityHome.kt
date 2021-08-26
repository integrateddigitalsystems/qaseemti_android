package com.ids.qasemti.controller.Activities


import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.navigation.NavigationView

import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterOrders
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.controller.Fragments.FragmentOrderTypes
import com.ids.qasemti.controller.Fragments.FragmentOrders
import com.ids.qasemti.controller.Fragments.FragmentProfile
import com.ids.qasemti.controller.Fragments.FragmentServices
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.toDp
import com.ids.qasemti.utils.toPx
import kotlinx.android.synthetic.main.footer.*
import kotlinx.android.synthetic.main.home_container.*
import kotlinx.android.synthetic.main.layout_home_orders.*

import kotlinx.android.synthetic.main.toolbar.*


class ActivityHome : AppCompactBase(), NavigationView.OnNavigationItemSelectedListener,
    RVOnItemClickListener {
    private lateinit var fragMang: FragmentManager
    private lateinit var drawerLayout: DrawerLayout
    private var ordersArray: ArrayList<String> = arrayListOf()
    var isClose = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()

    }


    private fun init() {

        btDrawer.visibility = View.VISIBLE
        btBack.visibility = View.GONE
        setMenu()
        fragMang = supportFragmentManager



        if(MyApplication.isClient){
            llFooterProducts.visibility =View.GONE
            llFooterCart.visibility=View.VISIBLE
        }else{
            llFooterProducts.visibility =View.VISIBLE
            llFooterCart.visibility=View.GONE

        }

        setListners()

    }

    fun drawColor(){
        AppHelper.setLogoTint(btDrawer, this, R.color.redPrimary)
    }
    fun setListners() {

        setTabs()

         llFooterCart.setOnClickListener {
            if (MyApplication.selectedFragment != AppConstants.FRAGMENT_CART && MyApplication.isSignedIn) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.homeContainer, FragmentOrders(), AppConstants.FRAGMENT_CART)
                    .commit()
                MyApplication.selectedFragment = AppConstants.FRAGMENT_CART
                tablayout.getTabAt(0)!!.select()
                MyApplication.selectedPos = 0
                resetIcons()
                ivCartFooter.layoutParams = LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt(),   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt())
                AppHelper.setLogoTint(btDrawer, this, R.color.redPrimary)
                AppHelper.setUpFooter(this, MyApplication.selectedFragment!!)
            }else if(!MyApplication.isSignedIn){
                MyApplication.selectedPos = 0
                MyApplication.selectedFragment = AppConstants.FRAGMENT_CART
                MyApplication.theFragment = FragmentOrders()
                startActivity(Intent(this, ActivityMobileRegistration::class.java))

            }

        }

        llFooterOrders.setOnClickListener {
            if (MyApplication.selectedFragment != AppConstants.FRAGMENT_ORDER && MyApplication.isSignedIn) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.homeContainer, FragmentOrderTypes(), AppConstants.FRAGMENT_ORDER)
                    .commit()
                MyApplication.selectedFragment = AppConstants.FRAGMENT_ORDER
                AppHelper.setLogoTint(btDrawer, this, R.color.redPrimary)
                AppHelper.setUpFooter(this, MyApplication.selectedFragment!!)
                tablayout.isSelected=true
                MyApplication.selectedPos =1
                tablayout.getTabAt(1)!!.select()
                resetIcons()
                ivFooterOrder.layoutParams = LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt(),   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt())

            }else if(!MyApplication.isSignedIn){
                MyApplication.selectedPos =1
                MyApplication.selectedFragment = AppConstants.FRAGMENT_ORDER
                MyApplication.theFragment = FragmentOrderTypes()
                startActivity(Intent(this, ActivityMobileRegistration::class.java))
            }

        }
        llFooterHome.setOnClickListener {

            if(MyApplication.isClient) {
                if (MyApplication.selectedFragment != AppConstants.FRAGMENT_SERVICE) {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.homeContainer,
                            FragmentServices(),
                            AppConstants.FRAGMENT_SERVICE
                        )
                        .commit()
                    MyApplication.selectedFragment = AppConstants.FRAGMENT_SERVICE
                    tablayout.getTabAt(2)!!.select()
                    resetIcons()
                    MyApplication.selectedPos =2
                    ivFooterHome.layoutParams = LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt(),   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt())
                    AppHelper.setUpFooter(this, MyApplication.selectedFragment!!)
                    AppHelper.setLogoTint(btDrawer, this, R.color.white)
                }
            }else{
                MyApplication.selectedPos =2
                if (MyApplication.selectedFragment != AppConstants.FRAGMENT_SERVICE) {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.homeContainer,
                            FragmentOrders(),
                            AppConstants.FRAGMENT_SERVICE
                        )
                        .commit()
                    MyApplication.selectedFragment = AppConstants.FRAGMENT_SERVICE
                    AppHelper.setUpFooter(this, MyApplication.selectedFragment!!)
                    AppHelper.setLogoTint(btDrawer, this, R.color.white)
                    tablayout.getTabAt(2)!!.select()
                    resetIcons()
                    ivFooterHome.layoutParams = LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt(),   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt())

                    setTintLogo(R.color.white)
                }
            }

        }
        llFooterProfile.setOnClickListener {
            if (MyApplication.selectedFragment != AppConstants.FRAGMENT_PROFILE && MyApplication.isSignedIn) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.homeContainer, FragmentProfile(), AppConstants.FRAGMENT_PROFILE)
                    .commit()
                MyApplication.selectedFragment = AppConstants.FRAGMENT_PROFILE
                AppHelper.setUpFooter(this, MyApplication.selectedFragment!!)
                tablayout.getTabAt(4)!!.select()
                resetIcons()
                MyApplication.selectedPos =4
                ivFooterAccount.layoutParams = LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt(),   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt())
                setTintLogo(R.color.white)
            }else if(!MyApplication.isSignedIn){
                MyApplication.selectedPos =4
                MyApplication.selectedFragment = AppConstants.FRAGMENT_PROD
                MyApplication.theFragment = FragmentProfile()
                startActivity(Intent(this, ActivityMobileRegistration::class.java))
                AppHelper.setLogoTint(btDrawer, this, R.color.white)


            }

        }
        llFooterProducts.setOnClickListener {
            if (MyApplication.selectedFragment != AppConstants.FRAGMENT_PROD  && MyApplication.isSignedIn) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.homeContainer, FragmentProfile(), AppConstants.FRAGMENT_PROD)
                    .commit()
                MyApplication.selectedFragment = AppConstants.FRAGMENT_PROD
                AppHelper.setUpFooter(this, MyApplication.selectedFragment!!)
                AppHelper.setLogoTint(btDrawer, this, R.color.white)
                tablayout.getTabAt(0)!!.select()
                resetIcons()
                MyApplication.selectedPos =0
                ivProductFooter.layoutParams = LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt(),   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt())

                setTintLogo(R.color.white)
            }else if(!MyApplication.isSignedIn){
                MyApplication.selectedPos =0
                MyApplication.selectedFragment = AppConstants.FRAGMENT_PROD
                MyApplication.theFragment = FragmentProfile()
                startActivity(Intent(this, ActivityMobileRegistration::class.java))
            }

        }
        llFooterNotifications.setOnClickListener {
            if (MyApplication.selectedFragment != AppConstants.FRAGMENT_NOTFICATIONS  && MyApplication.isSignedIn) {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.homeContainer,
                        FragmentProfile(),
                        AppConstants.FRAGMENT_NOTFICATIONS
                    )
                    .commit()
                MyApplication.selectedPos =3
                tablayout.getTabAt(3)!!.select()
                resetIcons()
                ivFooterNotifications.layoutParams = LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt(),   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt())
                MyApplication.selectedFragment = AppConstants.FRAGMENT_NOTFICATIONS
                AppHelper.setUpFooter(this, MyApplication.selectedFragment!!)

            }else if(!MyApplication.isSignedIn){
                MyApplication.selectedPos =3
                MyApplication.selectedFragment = AppConstants.FRAGMENT_NOTFICATIONS
                MyApplication.theFragment = FragmentProfile()
                startActivity(Intent(this, ActivityMobileRegistration::class.java))
            }

        }

        defaultFragment()
    }

    fun setTintLogo(color:Int){
        AppHelper.setLogoTint(btDrawer, this, color)
        AppHelper.setTextColor(this,tvPageTitle,color)
    }

    fun setTitleAc(title:String){
        tvPageTitle.visibility = View.VISIBLE
        tvPageTitle.text = title
    }

    fun setImageHeight(){
        if(MyApplication.selectedPos==0){
            if(MyApplication.isClient){
                ivCartFooter.layoutParams = LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt(),   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt())
            }else{
                ivProductFooter.layoutParams = LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt(),   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt())
            }
        }
        if(MyApplication.selectedPos==1){
            ivFooterOrder.layoutParams = LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt(),   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt())
        }
        if(MyApplication.selectedPos==2){
            ivFooterHome.layoutParams = LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt(),   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt())
        }
        if(MyApplication.selectedPos==3){
            ivFooterNotifications.layoutParams = LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt(),   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt())
        }
        if(MyApplication.selectedPos==4){
            ivFooterAccount.layoutParams = LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt(),   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt())
        }
    }
    fun defaultFragment() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.homeContainer, MyApplication.theFragment!!, MyApplication.selectedFragment)
            .commit()
        resetIcons()
        tablayout.getTabAt(MyApplication.selectedPos)!!.select()
        setImageHeight()
        setTintLogo(R.color.white)
        AppHelper.setUpFooter(this, MyApplication.selectedFragment!!)
    }


    private fun setMenu() {

        drawerLayout = findViewById(R.id.drawer_layout)
        btDrawer.setOnClickListener {
            it?.apply { isEnabled = false; postDelayed({ isEnabled = true }, 400) }
            if (isClose)
                drawerLayout.openDrawer(GravityCompat.START)
            else
                drawerLayout.openDrawer(GravityCompat.START)
            isClose = !isClose
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        // super.onBackPressed()
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer(drawerLayout, true)
        } else {
            super.onBackPressed()
        }
    }

    private fun closeDrawer(drawerLayout: DrawerLayout, animation: Boolean) {
        drawerLayout.closeDrawer(GravityCompat.START, animation)
    }

    override fun onItemClicked(view: View, position: Int) {

    }


    private fun setOrders() {
        ordersArray.clear()
        ordersArray.add("1")
        ordersArray.add("1")
        ordersArray.add("1")
        var adapter = AdapterOrders(ordersArray, this, this)
        rvOrders.adapter = adapter
        var glm2 = GridLayoutManager(this, 1)
        rvOrders.layoutManager = glm2
    }

    private fun setTabs(){
        repeat(5) { tablayout.addTab(tablayout.newTab()) }
        val tabStrip = tablayout.getChildAt(0) as LinearLayout
        for (i in 0 until tabStrip.childCount) {
            tabStrip.getChildAt(i).setOnTouchListener { v, _ -> true }
            val tab = tabStrip.getChildAt(i)
            val layoutParams = tab.layoutParams as LinearLayout.LayoutParams
            layoutParams.marginEnd = 8.toPx()
            layoutParams.marginStart = 8.toPx()
            layoutParams.width = 12.toPx()
            tab.layoutParams = layoutParams
            tablayout.requestLayout()
        }
    }


    private fun resetIcons(){
        ivCartFooter.layoutParams = LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt(),   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt())
        ivProductFooter.layoutParams = LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt(),   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt())
        ivFooterOrder.layoutParams = LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt(),   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt())
        ivFooterHome.layoutParams = LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt(),   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt())
        ivFooterNotifications.layoutParams = LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt(),   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt())
        ivFooterAccount.layoutParams = LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt(),   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt())
    }

}

