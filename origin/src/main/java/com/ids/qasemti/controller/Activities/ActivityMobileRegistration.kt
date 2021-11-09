package com.ids.qasemti.controller.Activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterCountryCodes

import com.ids.qasemti.controller.Adapters.AdapterGeneralSpinner
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
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

class ActivityMobileRegistration : ActivityBase() , RVOnItemClickListener{

    var selectedCode = "961"
    var arrayCountries : ArrayList<CountryCodes> = arrayListOf()
    var dialog : Dialog ?=null

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

        tvCountryCode.text = MyApplication.selectedItemDialog
        tvCountryCode.onOneClick {
            if(MyApplication.enableCountryCodes!!)
                showCountires()
        }
        if (MyApplication.isClient) {
            btLogin.hide()
            llNewMember.show()
        } else {
            btLogin.show()
            llNewMember.hide()
        }

        btLoginClient.onOneClick {
            validateRegistration()
       }

        btLogin.onOneClick {
           validateRegistration()
        }
    }

    fun validateRegistration(){
        if (etPhone.text.isNullOrEmpty()) {
            AppHelper.createDialog(this, AppHelper.getRemoteString("fill_all_field", this))
        } else if (etPhone.text.length !=8 && !MyApplication.enableCountryCodes!!) {
            AppHelper.createDialog(this, AppHelper.getRemoteString("check_phone_number", this))
        }
        else {
            AppHelper.createYesNoDialog(this,AppHelper.getRemoteString("confirm",this),AppHelper.getRemoteString("edit",this),AppHelper.getRemoteString("login_alert_title",this).replace("phoned_number",MyApplication.selectedItemDialog+etPhone.text.toString())+"\n"+AppHelper.getRemoteString("login_alert_msg",this)){
                MyApplication.selectedPhone = MyApplication.selectedItemDialog.replace("+","").trim()+etPhone.text.toString()
                loading.show()
                updateDevice()
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
            MyApplication.selectedPhone,
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

    private fun showCountires(){
        arrayCountries.clear()
        arrayCountries.addAll(Gson().fromJson(loadJSONFromAssets("countries.json"), CountryArray::class.java).countries!!)
        dialog = Dialog(this, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_recyler)
        dialog!!.setCancelable(true)
        val rv: RecyclerView = dialog!!.findViewById(R.id.rvData)

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv.layoutManager = layoutManager
        var adapter = AdapterCountryCodes(arrayCountries,this)
        rv.adapter = adapter

        try{
            var item=arrayCountries.find { it.code!!.replace("+","").trim()==MyApplication.selectedItemDialog.replace("+","").trim() }
            var position=arrayCountries.indexOf(item!!)
            rv.scrollToPosition(position)}catch (e:Exception){}

        dialog!!.show()
    }

    fun nextStepCode(){

        startActivity(Intent(this, ActivityCodeVerification::class.java))
        loading.hide()
    }
    fun sendOTP() {
        var req = RequestOTP(  MyApplication.selectedPhone, MyApplication.deviceId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.sendOTP(
                req
            )?.enqueue(object : Callback<ResponseUpdate> {
                override fun onResponse(
                    call: Call<ResponseUpdate>,
                    response: Response<ResponseUpdate>
                ) {
                    try {
                        nextStepCode()
                    } catch (E: java.lang.Exception) {
                    }
                }

                override fun onFailure(call: Call<ResponseUpdate>, throwable: Throwable) {
                }
            })
    }



    override fun onItemClicked(view: View, position: Int) {


            dialog!!.dismiss()
            MyApplication.selectedItemDialog=arrayCountries[position].code!!
            tvCountryCode.text = arrayCountries[position].code!!

    }
}