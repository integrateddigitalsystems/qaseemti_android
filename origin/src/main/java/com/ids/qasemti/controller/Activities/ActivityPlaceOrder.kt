package com.ids.qasemti.controller.Activities

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.controller.Fragments.FragmentHomeSP
import com.ids.qasemti.controller.Fragments.FragmentProfile
import com.ids.qasemti.controller.Fragments.FragmentHomeClient
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.show
import kotlinx.android.synthetic.main.activity_place_order.*
import kotlinx.android.synthetic.main.footer.*
import kotlinx.android.synthetic.main.toolbar.*
import org.androidannotations.annotations.App

class ActivityPlaceOrder : AppCompactBase() , RVOnItemClickListener {

    var fragMang : FragmentManager?=null
    var selected : Int = 0
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
        tvPageTitle.typeface=AppHelper.getTypeFace(this)
        btPLaceOrder.typeface = AppHelper.getTypeFace(this)
        btDrawer.hide()
        AppHelper.setTextColor(this,tvPageTitle,R.color.redPrimary)


    }

    fun setTintLogo(color:Int){
        AppHelper.setLogoTint(btDrawer, this, color)
        AppHelper.setTextColor(this,tvPageTitle,color)
    }
    fun setListners() {


        llFooterCart.setOnClickListener {
            if (MyApplication.selectedFragmentTag != AppConstants.FRAGMENT_CART) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.homeContainer, FragmentHomeSP(), AppConstants.FRAGMENT_CART)
                    .commit()
                MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_CART
                setTintLogo(R.color.redPrimary)
                AppHelper.setUpFooter(this, MyApplication.selectedFragmentTag!!)
            }

        }

        llFooterOrders.setOnClickListener {
            if (MyApplication.selectedFragmentTag != AppConstants.FRAGMENT_ORDER) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.homeContainer, FragmentHomeSP(), AppConstants.FRAGMENT_ORDER)
                    .commit()
                MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_ORDER
                setTintLogo(R.color.redPrimary)
                AppHelper.setUpFooter(this, MyApplication.selectedFragmentTag!!)
            }

        }
        llFooterHome.setOnClickListener {

            if(MyApplication.isClient) {
                if (MyApplication.selectedFragmentTag != AppConstants.FRAGMENT_HOME_CLIENT) {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.homeContainer,
                            FragmentHomeClient(),
                            AppConstants.FRAGMENT_HOME_CLIENT
                        )
                        .commit()
                    MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_CLIENT
                    AppHelper.setUpFooter(this, MyApplication.selectedFragmentTag!!)
                    setTintLogo(R.color.white)
                }
            }else{
                if (MyApplication.selectedFragmentTag != AppConstants.FRAGMENT_HOME_CLIENT) {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.homeContainer,
                            FragmentHomeSP(),
                            AppConstants.FRAGMENT_HOME_CLIENT
                        )
                        .commit()
                    MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_CLIENT
                    AppHelper.setUpFooter(this, MyApplication.selectedFragmentTag!!)
                    setTintLogo(R.color.white)
                }
            }

        }
        llFooterAccount.setOnClickListener {
            if (MyApplication.selectedFragmentTag != AppConstants.FRAGMENT_PROFILE) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.homeContainer, FragmentProfile(), AppConstants.FRAGMENT_PROFILE)
                    .commit()
                MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_PROFILE
                AppHelper.setUpFooter(this, MyApplication.selectedFragmentTag!!)
                setTintLogo(R.color.white)
            }

        }
        llFooterProducts.setOnClickListener {
            if (MyApplication.selectedFragmentTag != AppConstants.FRAGMENT_PROD) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.homeContainer, FragmentProfile(), AppConstants.FRAGMENT_PROD)
                    .commit()
                MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_PROD
                AppHelper.setUpFooter(this, MyApplication.selectedFragmentTag!!)
                setTintLogo(R.color.white)
            }

        }
        llFooterNotifications.setOnClickListener {
            if (MyApplication.selectedFragmentTag != AppConstants.FRAGMENT_NOTFICATIONS) {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.homeContainer,
                        FragmentProfile(),
                        AppConstants.FRAGMENT_NOTFICATIONS
                    )
                    .commit()
                setTintLogo(R.color.white)
                MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_NOTFICATIONS
                AppHelper.setUpFooter(this, MyApplication.selectedFragmentTag!!)

            }

        }


    }

    fun init(){

        fragMang = supportFragmentManager

        rlNotService.hide()
        llMain.show()
        rbCash.setOnClickListener {
            if(selected!=0) {
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
            if(selected!=1) {
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
            if(selected!=2) {
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

        if(MyApplication.isClient){
            llFooterProducts.hide()
            llFooterCart.show()
        }else{
            llFooterProducts.show()
            llFooterCart.hide()

        }

        setListners()

        AppHelper.setLogoTint(btDrawer,this,R.color.redPrimary)
    }
}