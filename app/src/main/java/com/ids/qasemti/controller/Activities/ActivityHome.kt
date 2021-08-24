package com.ids.qasemti.controller.Activities


import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.navigation.NavigationView

import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterOrders
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.controller.Fragments.FragmentOrders
import com.ids.qasemti.controller.Fragments.FragmentProfile
import com.ids.qasemti.controller.Fragments.FragmentServices
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.AppHelper
import kotlinx.android.synthetic.main.footer.*
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
        defaultFragment()


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


         llFooterCart.setOnClickListener {
            if (MyApplication.selectedFragment != AppConstants.FRAGMENT_CART) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.homeContainer, FragmentOrders(), AppConstants.FRAGMENT_CART)
                    .commit()
                MyApplication.selectedFragment = AppConstants.FRAGMENT_CART
                AppHelper.setLogoTint(btDrawer, this, R.color.redPrimary)
                AppHelper.setUpFooter(this, MyApplication.selectedFragment!!)
            }

        }

        llFooterOrders.setOnClickListener {
            if (MyApplication.selectedFragment != AppConstants.FRAGMENT_ORDER) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.homeContainer, FragmentOrders(), AppConstants.FRAGMENT_ORDER)
                    .commit()
                MyApplication.selectedFragment = AppConstants.FRAGMENT_ORDER
                AppHelper.setLogoTint(btDrawer, this, R.color.redPrimary)
                AppHelper.setUpFooter(this, MyApplication.selectedFragment!!)
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
                    AppHelper.setUpFooter(this, MyApplication.selectedFragment!!)
                    AppHelper.setLogoTint(btDrawer, this, R.color.white)
                }
            }else{
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
                }
            }

        }
        llFooterProfile.setOnClickListener {
            if (MyApplication.selectedFragment != AppConstants.FRAGMENT_PROFILE) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.homeContainer, FragmentProfile(), AppConstants.FRAGMENT_PROFILE)
                    .commit()
                MyApplication.selectedFragment = AppConstants.FRAGMENT_PROFILE
                AppHelper.setUpFooter(this, MyApplication.selectedFragment!!)
                AppHelper.setLogoTint(btDrawer, this, R.color.white)
            }

        }
        llFooterProducts.setOnClickListener {
            if (MyApplication.selectedFragment != AppConstants.FRAGMENT_PROD) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.homeContainer, FragmentProfile(), AppConstants.FRAGMENT_PROD)
                    .commit()
                MyApplication.selectedFragment = AppConstants.FRAGMENT_PROD
                AppHelper.setUpFooter(this, MyApplication.selectedFragment!!)
                AppHelper.setLogoTint(btDrawer, this, R.color.white)
            }

        }
        llFooterNotifications.setOnClickListener {
            if (MyApplication.selectedFragment != AppConstants.FRAGMENT_NOTFICATIONS) {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.homeContainer,
                        FragmentProfile(),
                        AppConstants.FRAGMENT_NOTFICATIONS
                    )
                    .commit()
                MyApplication.selectedFragment = AppConstants.FRAGMENT_NOTFICATIONS
                AppHelper.setUpFooter(this, MyApplication.selectedFragment!!)
                AppHelper.setLogoTint(btDrawer, this, R.color.white)
            }

        }


    }

    fun setTitleAc(title:String){
        tvPageTitle.visibility = View.VISIBLE
        tvPageTitle.text = title
    }

    fun defaultFragment() {

        if(MyApplication.isClient){
            supportFragmentManager.beginTransaction()
                .replace(R.id.homeContainer, FragmentServices(), AppConstants.FRAGMENT_SERVICE)
                .commit()
            MyApplication.selectedFragment = AppConstants.FRAGMENT_SERVICE
            AppHelper.setUpFooter(this, MyApplication.selectedFragment!!)
        }else {

            supportFragmentManager.beginTransaction()
                .replace(R.id.homeContainer, FragmentOrders(), AppConstants.FRAGMENT_SERVICE)
                .commit()
            MyApplication.selectedFragment = AppConstants.FRAGMENT_SERVICE
            AppHelper.setUpFooter(this, MyApplication.selectedFragment!!)
        }
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


}

