package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.Fragments.FragmentAccount
import com.ids.qasemti.controller.Fragments.FragmentHomeClient
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.RequestOTP
import com.ids.qasemti.model.ResponseConfiguration
import com.ids.qasemti.model.ResponseUpdate
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_mobile_registration.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.layout_profile.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.white_logo_layout.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ActivityRegistration : ActivityBase() {

    var body1: MultipartBody.Part? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        AppHelper.setAllTexts(rootLayoutRegistration, this)
        AppHelper.setLogoTint(logo_main, this, R.color.white)

        if (MyApplication.isClient) {
            rlNoLogo.show()
            llTopService.hide()
            tvRegisterTitle.hide()
        } else {
            rlNoLogo.hide()
            llTopService.show()
            tvRegisterTitle.show()
        }



        btRegister.onOneClick {
            if (etFirstName.text.isNullOrEmpty() || etLastName.text.isNullOrEmpty() || etEmail.text.isNullOrEmpty()) {
                AppHelper.createDialog(this, AppHelper.getRemoteString("fill_all_field", this))
            } else if (!AppHelper.isEmailValid(etEmail.text.toString())) {
                AppHelper.createDialog(this, AppHelper.getRemoteString("email_valid_error", this))
            } else {
                updateProfile()
                //   sendOTP()
                //  startActivity(Intent(this,ActivityCodeVerification::class.java))
            }
        }
    }

    fun nextStep() {
        if (MyApplication.firstTime) {
            MyApplication.register = true
            MyApplication.selectedPos=4
            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_ACCOUNT
            MyApplication.selectedFragment = FragmentAccount()
            startActivity(Intent(this, ActivityAccountStatus::class.java))
        } else {
            MyApplication.isSignedIn = true
            MyApplication.phoneNumber = MyApplication.selectedPhone
            MyApplication.register = true
            MyApplication.selectedPos=4
            MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_ACCOUNT
            MyApplication.selectedFragment = FragmentAccount()
            AppHelper.getUserInfo()
            startActivity(Intent(this, ActivityHome::class.java))
        }
    }

    fun updateProfile() {
        try {
            loading.show()
        } catch (ex: java.lang.Exception) {

        }

        var empty = RequestBody.create("text/plain".toMediaTypeOrNull(), "")
        val user = RequestBody.create("text/plain".toMediaTypeOrNull(), MyApplication.userId.toString())
        val first =
            RequestBody.create("text/plain".toMediaTypeOrNull(), etFirstName.text.toString())
        val last =
            RequestBody.create("text/plain".toMediaTypeOrNull(), etLastName.text.toString())
        val email =
            RequestBody.create("text/plain".toMediaTypeOrNull(), etEmail.text.toString())

        var req =  RequestBody.create("multipart/form-data".toMediaType(), File(""))
        try {
            body1 = MultipartBody.Part.createFormData("file", "", req)
        }catch (ex:Exception){
            body1 = MultipartBody.Part.createFormData("file", "Upload",req)
        }

        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.updateProfileRegister(
                user,
                first,
                last,
                email)?.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    try {
                        nextStep()
                    } catch (E: java.lang.Exception) {

                    }
                }

                override fun onFailure(call: Call<String>, throwable: Throwable) {
                    nextStep()
                }
            })
    }
}