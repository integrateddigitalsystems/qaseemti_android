package com.ids.qasemti.controller.Activities

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.controller.Fragments.FragmentOrders
import com.ids.qasemti.controller.Fragments.FragmentProfile
import com.ids.qasemti.controller.Fragments.FragmentServices
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.AppHelper
import kotlinx.android.synthetic.main.activity_place_order.*
import kotlinx.android.synthetic.main.footer.*
import kotlinx.android.synthetic.main.toolbar.*

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
        btClose.visibility=View.VISIBLE

        tvPageTitle.text = getString(R.string.place_order)
        AppHelper.setTextColor(this,tvPageTitle,R.color.redPrimary)


    }

    fun setTintLogo(color:Int){
        AppHelper.setLogoTint(btDrawer, this, color)
        AppHelper.setTextColor(this,tvPageTitle,color)
    }
    fun setListners() {


        llFooterCart.setOnClickListener {
            if (MyApplication.selectedFragment != AppConstants.FRAGMENT_CART) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.homeContainer, FragmentOrders(), AppConstants.FRAGMENT_CART)
                    .commit()
                MyApplication.selectedFragment = AppConstants.FRAGMENT_CART
                setTintLogo(R.color.redPrimary)
                AppHelper.setUpFooter(this, MyApplication.selectedFragment!!)
            }

        }

        llFooterOrders.setOnClickListener {
            if (MyApplication.selectedFragment != AppConstants.FRAGMENT_ORDER) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.homeContainer, FragmentOrders(), AppConstants.FRAGMENT_ORDER)
                    .commit()
                MyApplication.selectedFragment = AppConstants.FRAGMENT_ORDER
                setTintLogo(R.color.redPrimary)
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
                    setTintLogo(R.color.white)
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
                    setTintLogo(R.color.white)
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
                setTintLogo(R.color.white)
            }

        }
        llFooterProducts.setOnClickListener {
            if (MyApplication.selectedFragment != AppConstants.FRAGMENT_PROD) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.homeContainer, FragmentProfile(), AppConstants.FRAGMENT_PROD)
                    .commit()
                MyApplication.selectedFragment = AppConstants.FRAGMENT_PROD
                AppHelper.setUpFooter(this, MyApplication.selectedFragment!!)
                setTintLogo(R.color.white)
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
                setTintLogo(R.color.white)
                MyApplication.selectedFragment = AppConstants.FRAGMENT_NOTFICATIONS
                AppHelper.setUpFooter(this, MyApplication.selectedFragment!!)

            }

        }


    }

    fun init(){

        fragMang = supportFragmentManager

        rlNotService.visibility = View.GONE
        llMain.visibility = View.VISIBLE
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
            llFooterProducts.visibility =View.GONE
            llFooterCart.visibility=View.VISIBLE
        }else{
            llFooterProducts.visibility =View.VISIBLE
            llFooterCart.visibility=View.GONE

        }

        setListners()

        AppHelper.setLogoTint(btDrawer,this,R.color.redPrimary)
    }
}