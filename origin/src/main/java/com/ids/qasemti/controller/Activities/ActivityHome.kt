package com.ids.qasemti.controller.Activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Adapters.SampleFragmentPagerAdapter
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
import kotlinx.android.synthetic.main.layout_footer_shadow.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlin.system.exitProcess


class ActivityHome : AppCompactBase(), NavigationView.OnNavigationItemSelectedListener,
    RVOnItemClickListener {
    private lateinit var fragMang: FragmentManager
    private lateinit var drawerLayout: DrawerLayout
    var selectedPos = 2
    private var ordersArray: ArrayList<String> = arrayListOf()
    var isClose = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()


    }


    fun setCurve(position: Int) {
        ivCurveCartProd.invisible()
        ivCurveAccount.invisible()
        ivCurveHome.invisible()
        ivCurveNotification.invisible()
        ivCurveOrder.invisible()

        if (position == 0) {
            ivCurveCartProd.show()
        } else if (position == 1) {
            ivCurveOrder.show()
        } else if (position == 2) {
            ivCurveHome.show()
        } else if (position == 3) {
            ivCurveNotification.show()
        } else if (position == 4) {
            ivCurveAccount.show()
        }
    }

    fun showTitle(show: Boolean) {
        if (show)
            tvPageTitle.show()
        else
            tvPageTitle.hide()
    }

    fun showBack(show: Boolean) {
        if (show)
            btBackTool.show()
        else
            btBackTool.hide()
    }

    fun showLogout(show: Boolean) {
        if (show)
            btLogout.show()
        else
            btLogout.hide()
    }

    private fun init() {
        /*val pagerAdapter = SampleFragmentPagerAdapter(
            supportFragmentManager, this
        )
        vpPagerTab.adapter = pagerAdapter
        tablayout.setupWithViewPager(vpPagerTab)
        for (i in 0 until tablayout.tabCount) {
            val tab = tablayout.getTabAt(i)
            tab!!.setCustomView(pagerAdapter.getTabView(i))
        }*/



        if (MyApplication.isSignedIn)
            btLogout.show()
        btDrawer.hide()
        setMenu()
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        tvPageTitle.typeface = AppHelper.getTypeFaceBold(this)
        fragMang = supportFragmentManager
        llFooterProducts.hide()
        llFooterCart.show()
        setListners()

    }

    override fun onResume() {
        super.onResume()
        MyApplication.fromFooterOrder = false
    }

    fun drawColor() {
        AppHelper.setLogoTint(btDrawer, this, R.color.redPrimary)
    }

    fun setTintLogo(color: Int) {
        AppHelper.setLogoTint(btDrawer, this, color)
        AppHelper.setTextColor(this, tvPageTitle, color)
        AppHelper.setLogoTint(btBackTool, this, color)
        AppHelper.setLogoTint(btLogout, this, color)
    }

    fun setTitleAc(title: String,color:Int) {
        tvPageTitle.show()
        tvPageTitle.setColorTypeface(this,color,title,true)
    }

    fun defaultFragment() {
        if(MyApplication.defaultIcon==null){
            MyApplication.defaultIcon = ivFooterHome
        }
        setSelectedTab(
            MyApplication.selectedPos,
            MyApplication.selectedFragment!!,
            MyApplication.selectedFragmentTag!!,
            MyApplication.defaultIcon!!,
            MyApplication.tintColor
        )
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
            if (getFragmentCount(fragMang) == 0) {
                AppHelper.createYesNoDialog(
                    this,
                    AppHelper.getRemoteString("exit", this),
                    AppHelper.getRemoteString("cancel", this),
                    AppHelper.getRemoteString("sureExit", this)
                ) {
                    finishAffinity()
                    exitProcess(0)
                }
            } else
                super.onBackPressed()
        }
    }

    private fun closeDrawer(drawerLayout: DrawerLayout, animation: Boolean) {
        drawerLayout.closeDrawer(GravityCompat.START, animation)
    }

    override fun onItemClicked(view: View, position: Int) {

    }

    fun setIconBig(selectedIcon: ImageView){
        selectedIcon.layoutParams = LinearLayout.LayoutParams(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                35f,
                resources.displayMetrics
            ).toInt(),
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35f, resources.displayMetrics)
                .toInt()
        )
        selectedIcon.setPadding(0,-10,0,0)
    }
    fun bigIcon(selectedPosition:Int){
        if (selectedPosition == 0) {
            setIconBig(ivCartFooter)
            setIconBig(ivProductFooter)
        } else if (selectedPosition == 1) {
            setIconBig(ivFooterOrder)
        } else if (selectedPosition == 2) {
           setIconBig(ivFooterHome)
        } else if (selectedPosition == 3) {
            setIconBig(ivFooterNotifications)
        } else if (selectedPosition == 4) {
            setIconBig(ivFooterAccount)
        }
    }
    private fun setSelectedTab(
        index: Int,
        fragment: Fragment,
        tag: String,
        selectedIcon: ImageView,
        drawerColor: Int
    ) {
        replaceFragment(R.id.homeContainer, fragMang, fragment, tag)
        hideBack()
        tvPageTitle.hide()
        tablayout.getTabAt(index)!!.select()
        MyApplication.selectedPos = index
        resetIcons(
            this,
            ivCartFooter,
            ivProductFooter,
            ivFooterOrder,
            ivFooterHome,
            ivFooterNotifications,
            ivFooterAccount
        )
        bigIcon(index)
        AppHelper.setLogoTint(btDrawer, this, drawerColor)
        setTintLogo(drawerColor)
        setCurve(index)
        AppHelper.setUpFooter(this, MyApplication.selectedFragmentTag!!)
    }


    fun goRegistration(position: Int, tag: String, fragment: Fragment, color: Int) {
        MyApplication.selectedPos = position
        MyApplication.selectedFragmentTag = tag
        MyApplication.selectedFragment = fragment
        MyApplication.tintColor = color
        startActivity(Intent(this, ActivityMobileRegistration::class.java))
    }


    fun setListners() {

        AppHelper.setTabs(tablayout, this)

        //common tabs

        tablayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                /*AppHelper.clearTabs(tablayout, this@ActivityHome)
                for (i in 0 until 5)
                    if (tablayout.getTabAt(i) == tab)
                        selectedPos = i


                val v: View =
                    LayoutInflater.from(this@ActivityHome).inflate(R.layout.footer_top, null)
                v.layoutParams =
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                tablayout.getTabAt(selectedPos)!!.setCustomView(v)*/

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        btBackTool!!.onOneClick {
            getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentById(R.id.container)!!).commit();
        }
        llFooterOrders.onOneClick {
            MyApplication.fromFooterOrder = true
            MyApplication.defaultIcon = ivFooterOrder
            if (MyApplication.selectedFragmentTag != FRAGMENT_ORDER && ((MyApplication.isClient && MyApplication.isSignedIn) || !MyApplication.isClient))
                setSelectedTab(
                    1,
                    FragmentOrders(),
                    FRAGMENT_ORDER,
                    ivFooterOrder,
                    R.color.redPrimary
                )
            else if (!MyApplication.isSignedIn && MyApplication.isClient) {
                goRegistration(1, FRAGMENT_ORDER, FragmentOrders(), R.color.redPrimary)
            }
        }

        llFooterNotifications.onOneClick {
            MyApplication.defaultIcon = ivFooterNotifications
            if (MyApplication.selectedFragmentTag != FRAGMENT_NOTFICATIONS && ((MyApplication.isClient && MyApplication.isSignedIn) || !MyApplication.isClient))
                setSelectedTab(
                    3,
                    FragmentNotifications(),
                    FRAGMENT_NOTFICATIONS,
                    ivFooterNotifications,
                    R.color.white
                )
            else if (!MyApplication.isSignedIn && MyApplication.isClient)
                goRegistration(3, FRAGMENT_NOTFICATIONS, FragmentNotifications(), R.color.white)
        }

        llFooterAccount.onOneClick {
            MyApplication.defaultIcon = ivFooterAccount
            if (MyApplication.selectedFragmentTag != FRAGMENT_ACCOUNT && ((MyApplication.isClient && MyApplication.isSignedIn) || !MyApplication.isClient))
                setSelectedTab(
                    4,
                    FragmentAccount(),
                    FRAGMENT_ACCOUNT,
                    ivFooterAccount,
                    R.color.white
                )
            else if (!MyApplication.isSignedIn && MyApplication.isClient)
                goRegistration(4, FRAGMENT_ACCOUNT, FragmentAccount(), R.color.white)
        }

        //other tabs
        llFooterCart.onOneClick {
            MyApplication.defaultIcon = ivCartFooter
            if (MyApplication.selectedFragmentTag != FRAGMENT_CART && MyApplication.isSignedIn)
                setSelectedTab(0, FragmentCart(), FRAGMENT_CART, ivCartFooter, R.color.redPrimary)
            else if (!MyApplication.isSignedIn)
                goRegistration(0, FRAGMENT_CART, FragmentCart(), R.color.redPrimary)
        }


        llFooterHome.onOneClick {
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


        llFooterProducts.onOneClick {
            MyApplication.defaultIcon = ivProductFooter
            if (MyApplication.selectedFragmentTag != AppConstants.FRAGMENT_PROD)
                setSelectedTab(
                    0,
                    FragmentMyServices(),
                    AppConstants.FRAGMENT_MY_SERVICES,
                    ivProductFooter,
                    R.color.white
                )
        }

        defaultFragment()

        btBackTool.onOneClick {
            checkBack()
            super.onBackPressed()
        }

        if(MyApplication.isClient){
            llFooterProducts.hide()
            llFooterCart.show()
            tvFooterHome.textRemote("Services",this)
        }else{
            llFooterProducts.show()
            llFooterCart.hide()
            tvFooterHome.textRemote("Home",this)
        }

        btLogout.setOnClickListener{
            showLogoutDialog(this)
        }
    }

    fun showLogoutDialog(context: Activity){
        AppHelper.createYesNoDialog(
            context,
            AppHelper.getRemoteString("logout", context),
            AppHelper.getRemoteString("cancel", context),
            AppHelper.getRemoteString("sureLogout", context)
        ) {
            MyApplication.isSignedIn = false
            MyApplication.fromLogout = true
            MyApplication.deviceId = 0
            MyApplication.phoneNumber = ""
            MyApplication.userId = 0
            if (MyApplication.isClient) {
                MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_CLIENT
                MyApplication.selectedFragment = FragmentHomeClient()
            } else {
                MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_SP
                MyApplication.selectedFragment = FragmentHomeSP()
            }
            MyApplication.selectedPos = 2
            context.finishAffinity()
            startActivity(
                Intent(
                    context,
                    ActivityMobileRegistration::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }


    fun addFrag(fragment: Fragment, tag: String) {
        addFragment(R.id.homeContainer, fragMang, fragment, tag)
    }

    fun hideBack() {
        btBackTool.hide()
    }

    fun showBack(color: Int) {
        btBackTool.show()
        AppHelper.setLogoTint(btBackTool, this, color)
    }


    fun checkBack() {
        if (getFragmentCount(fragMang) <= 1) {
            btBackTool.hide()
            tvPageTitle.hide()
        } else
            btBackTool.show()
    }

}


