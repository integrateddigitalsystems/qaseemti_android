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
import com.ids.qasemti.controller.Fragments.FragmentServices
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.AppHelper
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
        defaultFragment()

    }

    fun defaultFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.homeContainer, FragmentServices(), "Fragment Services")
            .commit()
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

