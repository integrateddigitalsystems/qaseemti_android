package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.ids.qasemti.R

import com.ids.qasemti.controller.Adapters.AdapterGeneralSpinner
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import com.ids.sampleapp.model.ItemSpinner
import kotlinx.android.synthetic.main.activity_mobile_registration.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.white_logo_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

class ActivityMobileRegistration : ActivityBase() {

    var selectedCode = ""

    override fun onBackPressed() {
        if (MyApplication.fromLogout) {
            AppHelper.createYesNoDialog(
                this,
                AppHelper.getRemoteString("exit", this),
                AppHelper.getRemoteString("cancel", this),
                AppHelper.getRemoteString("sureExit", this)
            ) {
                finishAffinity()
                exitProcess(0)
            }
        } else {
            super.onBackPressed()
        }

        /*logo_main.onOneClick {
            Toast.makeText(this,"Howdy Gov",Toast.LENGTH_SHORT).show()
        }*/
        /*if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer(drawerLayout, true)
        } else {
            checkBack()
            super.onBackPressed()
        }*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_registration)
        AppHelper.setAllTexts(rootLayoutMobileRegister, this)
        AppHelper.setLogoTint(logo_main, this, R.color.white)


        if (MyApplication.isClient) {
            btLogin.hide()
            llNewMember.show()
        } else {
            btLogin.show()
            llNewMember.hide()
        }

        tvRegisterNewMember.onOneClick {
            startActivity(Intent(this, ActivityRegistration::class.java))
        }


        var items: ArrayList<ItemSpinner> = arrayListOf()
        items.add(ItemSpinner(0, "+961", ""))
        items.add(ItemSpinner(0, "+965", ""))
        items.add(ItemSpinner(0, "+1", ""))
        items.add(ItemSpinner(0, "+31", ""))


        val adapterMobileCode = AdapterGeneralSpinner(
            this,
            R.layout.spinner_layout,
            items,
            AppConstants.LEFT_BLACK
        )

        spMobileCode.adapter = adapterMobileCode
        adapterMobileCode.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spMobileCode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedCode = items.get(position).name!!
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }


        }

        btLoginClient.onOneClick {
            if (etPhone.text.isNullOrEmpty()) {
                AppHelper.createDialog(this, AppHelper.getRemoteString("fill_all_field", this))
            } else {
                //  MyApplication.isSignedIn = true
                  updateDevice()
                startActivity(Intent(this, ActivityCodeVerification::class.java))
            }

        }

        btLogin.onOneClick {
            if (etPhone.text.isNullOrEmpty()) {
                AppHelper.createDialog(this, AppHelper.getRemoteString("fill_all_field", this))
            } else {
                getUserStatus()
                MyApplication.selectedPhone = etPhone.text.toString()

            }

        }
    }

    fun updateDevice(){
        val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
        val cal = Calendar.getInstance()

        val model = AppHelper.getDeviceName()
        val osVersion = AppHelper.getAndroidVersion()

        val deviceToken = ""
        val deviceTypeId = ""
        var android_id = Settings.Secure.getString(
            getContentResolver(),
            Settings.Secure.ANDROID_ID
        );

        val imei =
            Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        val registrationDate = dateFormat.format(cal.time)
        val appVersion = AppHelper.getVersionNumber()

        val generalNotification = 1
        val isProduction = 1


        val lang = MyApplication.languageCode
        var isService = 1
        if (MyApplication.isClient)
            isService = 0

        var newReq = RequestUpdate(
            MyApplication.deviceId,
            etPhone.text.toString(),
            model,
            osVersion,
            deviceToken,
            2,
            imei,
            generalNotification,
            appVersion.toString(),
            0,
            lang,
            MyApplication.userId,
            isService
        )


        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.updateDevice(
                newReq
            )?.enqueue(object : Callback<ResponseUpdate> {
                override fun onResponse(
                    call: Call<ResponseUpdate>,
                    response: Response<ResponseUpdate>
                ) {
                    try {
                        MyApplication.deviceId = response.body()!!.deviceId!!
                        sendOTP()
                    } catch (E: java.lang.Exception) {
                    }
                }

                override fun onFailure(call: Call<ResponseUpdate>, throwable: Throwable) {
                }
            })

    }

    fun sendOTP() {
        var req = RequestOTP(etPhone.text.toString(), MyApplication.deviceId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.sendOTP(
                req
            )?.enqueue(object : Callback<ResponseUpdate> {
                override fun onResponse(
                    call: Call<ResponseUpdate>,
                    response: Response<ResponseUpdate>
                ) {
                    try {

                    } catch (E: java.lang.Exception) {
                    }
                }

                override fun onFailure(call: Call<ResponseUpdate>, throwable: Throwable) {
                }
            })
    }

    fun nextStep() {
       updateDevice()
        if(MyApplication.userStatus!!.enabled!=0)
            startActivity(Intent(this, ActivityCodeVerification::class.java))
    }

    fun getUserStatus() {
        try {
            loading.show()
        } catch (ex: Exception) {

        }
        var newReq = RequestUserStatus(MyApplication.userId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getUserStatus(
                newReq
            )?.enqueue(object : Callback<ResponseUserStatus> {
                override fun onResponse(
                    call: Call<ResponseUserStatus>,
                    response: Response<ResponseUserStatus>
                ) {
                    try {
                        MyApplication.userStatus = response.body()
                        loading.hide()
                        nextStep()
                    } catch (E: java.lang.Exception) {
                        try {
                            loading.hide()
                        } catch (ex: Exception) {

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseUserStatus>, throwable: Throwable) {
                    try {
                        loading.hide()
                    } catch (ex: Exception) {

                    }
                }
            })
    }
}