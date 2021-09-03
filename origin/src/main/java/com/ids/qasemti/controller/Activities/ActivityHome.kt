package com.ids.qasemti.controller.Activities
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView

import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.controller.Fragments.*
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.*
import com.ids.qasemti.utils.AppConstants.FRAGMENT_ACCOUNT
import com.ids.qasemti.utils.AppConstants.FRAGMENT_CART
import com.ids.qasemti.utils.AppConstants.FRAGMENT_NOTFICATIONS
import com.ids.qasemti.utils.AppConstants.FRAGMENT_ORDER
import com.ids.qasemti.utils.AppHelper.Companion.getFragmentCount
import com.ids.qasemti.utils.AppHelper.Companion.resetIcons
import kotlinx.android.synthetic.main.footer.*
import kotlinx.android.synthetic.main.home_container.*
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


    fun showBack(show:Boolean){
        if(show)
            btBackTool.show()
        else
            btBackTool.hide()
    }

    fun showLogout(show:Boolean){
        if(show)
            btLogout.show()
        else
            btLogout.hide()
    }
    private fun init() {

        if(MyApplication.isSignedIn)
            btLogout.show()
        btDrawer.hide()
        setMenu()
        tvPageTitle.typeface = AppHelper.getTypeFace(this)
        fragMang = supportFragmentManager
        llFooterProducts.hide()
        llFooterCart.show()
        setListners()

    }

    fun drawColor(){
        AppHelper.setLogoTint(btDrawer, this, R.color.redPrimary)
    }

    fun setTintLogo(color:Int){
        AppHelper.setLogoTint(btDrawer, this, color)
        AppHelper.setTextColor(this,tvPageTitle,color)
        AppHelper.setLogoTint(btBackTool,this,color)
        AppHelper.setLogoTint(btLogout,this,color)
    }

    fun setTitleAc(title:String){
        tvPageTitle.show()
        tvPageTitle.text = title
    }

    fun defaultFragment() {
       setSelectedTab(MyApplication.selectedPos,MyApplication.selectedFragment!!,MyApplication.selectedFragmentTag!!,ivFooterHome,MyApplication.tintColor)
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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer(drawerLayout, true)
        } else {
            checkBack()
            super.onBackPressed()
        }
    }

    private fun closeDrawer(drawerLayout: DrawerLayout, animation: Boolean) {
        drawerLayout.closeDrawer(GravityCompat.START, animation)
    }

    override fun onItemClicked(view: View, position: Int) {

    }


    private fun setSelectedTab(index: Int,fragment:Fragment,tag:String,selectedIcon:ImageView,drawerColor: Int){
        replaceFragment(R.id.homeContainer,fragMang,fragment, tag)
        hideBack()
        tvPageTitle.hide()
        tablayout.getTabAt(index)!!.select()
        MyApplication.selectedPos = index
        resetIcons(this,ivCartFooter,ivProductFooter,ivFooterOrder,ivFooterHome,ivFooterNotifications,ivFooterAccount)
        selectedIcon.layoutParams = LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt(),   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt())
        AppHelper.setLogoTint(btDrawer, this, drawerColor)
        setTintLogo(drawerColor)
        AppHelper.setUpFooter(this, MyApplication.selectedFragmentTag!!)
    }


    private fun goRegistration(position: Int,tag: String,fragment: Fragment,color: Int){
        MyApplication.selectedPos =position
        MyApplication.selectedFragmentTag = tag
        MyApplication.selectedFragment = fragment
        MyApplication.tintColor = color
        startActivity(Intent(this, ActivityMobileRegistration::class.java))
    }

    fun setListners() {

        AppHelper.setTabs(tablayout)

        //common tabs

        btBackTool.setOnClickListener {
            getSupportFragmentManager()
                .beginTransaction().
                remove(getSupportFragmentManager().findFragmentById(R.id.container)!!).commit();
        }
        llFooterOrders.setOnClickListener {
            if (MyApplication.selectedFragmentTag != FRAGMENT_ORDER && ((MyApplication.isClient && MyApplication.isSignedIn) || !MyApplication.isClient))
                setSelectedTab(1,FragmentOrders(), FRAGMENT_ORDER,ivFooterOrder,R.color.redPrimary)
            else if(!MyApplication.isSignedIn && MyApplication.isClient)
            goRegistration(1,FRAGMENT_ORDER,FragmentOrders(),R.color.redPrimary)
        }

        llFooterNotifications.setOnClickListener {
            if (MyApplication.selectedFragmentTag != FRAGMENT_NOTFICATIONS && ((MyApplication.isClient && MyApplication.isSignedIn) || !MyApplication.isClient))
                setSelectedTab(3,FragmentNotifications(), FRAGMENT_NOTFICATIONS,ivFooterNotifications,R.color.white)
            else if(!MyApplication.isSignedIn && MyApplication.isClient)
                goRegistration(3,FRAGMENT_NOTFICATIONS,FragmentNotifications(),R.color.white)
        }

        llFooterAccount.setOnClickListener {
            if (MyApplication.selectedFragmentTag != FRAGMENT_ACCOUNT && ((MyApplication.isClient && MyApplication.isSignedIn) || !MyApplication.isClient))
                setSelectedTab(4,FragmentAccount(), FRAGMENT_ACCOUNT,ivFooterAccount,R.color.white)
            else if(!MyApplication.isSignedIn && MyApplication.isClient)
                goRegistration(4,FRAGMENT_ACCOUNT,FragmentAccount(),R.color.white)
        }

        //other tabs
        llFooterCart.setOnClickListener {
            if (MyApplication.selectedFragmentTag != FRAGMENT_CART && MyApplication.isSignedIn)
                setSelectedTab(0,FragmentCart(), FRAGMENT_CART,ivCartFooter,R.color.redPrimary)
            else if(!MyApplication.isSignedIn)
                goRegistration(0,FRAGMENT_CART,FragmentCart(),R.color.redPrimary)
        }


        llFooterHome.setOnClickListener {
            if(MyApplication.isClient){
                if (MyApplication.selectedFragmentTag != AppConstants.FRAGMENT_HOME_CLIENT)
                    setSelectedTab(2,FragmentHomeClient(), AppConstants.FRAGMENT_HOME_CLIENT,ivFooterHome,R.color.white)
            }else{
                if (MyApplication.selectedFragmentTag != AppConstants.FRAGMENT_HOME_SP)
                    setSelectedTab(2,FragmentHomeSP(), AppConstants.FRAGMENT_HOME_SP,ivFooterHome,R.color.white)
            }
        }


        llFooterProducts.setOnClickListener {
            if (MyApplication.selectedFragmentTag != AppConstants.FRAGMENT_PROD)
                setSelectedTab(0,FragmentProducts(), AppConstants.FRAGMENT_PROD,ivProductFooter,R.color.white)
        }

        defaultFragment()

        btBackTool.setOnClickListener{
            super.onBackPressed()
        }
    }


    fun addFrag(fragment: Fragment,tag: String){
        addFragment(R.id.homeContainer,fragMang,fragment, tag)
    }

    fun hideBack(){
        btBackTool.hide()
    }

    fun showBack(color: Int){
        btBackTool.show()
        AppHelper.setLogoTint(btBackTool, this, color)
    }


    fun checkBack(){
        if(getFragmentCount(fragMang)<=1){
            btBackTool.hide()
            tvPageTitle.hide()
        }
        else
            btBackTool.show()

    }

}

