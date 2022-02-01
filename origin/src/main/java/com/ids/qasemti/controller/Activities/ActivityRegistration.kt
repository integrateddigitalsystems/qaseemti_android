package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.controller.Fragments.FragmentAccount
import com.ids.qasemti.controller.Fragments.FragmentHomeClient
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
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
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ActivityRegistration : ActivityBase() , ApiListener{

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
        if (!MyApplication.isClient){
          var selectedProfilePic : MultipartBody.Part ?=null
          if (selectedProfilePic == null) {
            var empty = ""
            val attachmentEmpty = empty.toRequestBody("text/plain".toMediaTypeOrNull())

            selectedProfilePic =
              MultipartBody.Part.createFormData("attachment", "", attachmentEmpty)
          }
          CallAPIs.updateProfileServiceProvider(
            this,
            this,
            loading,
            0.0,
            0.0,
            "",
            etFirstName.text.toString(),
            "",
            etLastName.text.toString(),
            etEmail.text.toString(),
            MyApplication.selectedPhone!!,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            selectedProfilePic!!,
            selectedProfilePic!!,
            selectedProfilePic!!
          )
        }else {
          var selectedProfilePic : MultipartBody.Part ?=null
          if (selectedProfilePic == null) {
            var empty = ""
            val attachmentEmpty = empty.toRequestBody("text/plain".toMediaTypeOrNull())

            selectedProfilePic =
              MultipartBody.Part.createFormData("attachment", "", attachmentEmpty)
          }
          CallAPIs.updateProfileClient(
            this,
            this,
            loading,
            etFirstName.text.toString(),
            etLastName.text.toString(),
            etEmail.text.toString(),
            MyApplication.selectedPhone!!,
            selectedProfilePic!!
          )
        }
      }
    }
  }


  fun setNotificationType(available : Int ){
    var newReq = RequestNotificationUpdate(MyApplication.userId,available)
    RetrofitClient.client?.create(RetrofitInterface::class.java)
      ?.updateNotification(newReq)?.enqueue(object : Callback<ResponseCancel> {
        override fun onResponse(call: Call<ResponseCancel>, response: Response<ResponseCancel>) {
          try{
            logw("NOTFRES","succ")
          }catch (E: java.lang.Exception){
            logw("NOTFRES","catch")
          }
        }
        override fun onFailure(call: Call<ResponseCancel>, throwable: Throwable) {
          logw("NOTFRES","failed")
        }
      })
  }


  fun nextStep() {
    setNotificationType(1)
    loading.hide()
    if(!MyApplication.isClient) {
      MyApplication.register = true
      MyApplication.selectedPos = 4
      MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_ACCOUNT
      MyApplication.selectedFragment = FragmentAccount()
     // startActivity(Intent(this, ActivityAccountStatus::class.java))
    }else{
      MyApplication.selectedPos = 2
      if(MyApplication.selectedFragmentTag.isNullOrEmpty() && MyApplication.selectedFragment==null) {
        MyApplication.selectedFragmentTag = AppConstants.FRAGMENT_HOME_CLIENT
        MyApplication.selectedFragment = FragmentHomeClient()
      }
     // startActivity(Intent(this, ActivityAccountStatus::class.java))
    }

    MyApplication.phoneNumber = MyApplication.selectedPhone
    MyApplication.isSignedIn = true
    MyApplication.firstTime = false
    AppHelper.goHome(this)
  }


  override fun onDataRetrieved(success: Boolean, response: Any, apiId: Int) {
    if(apiId==AppConstants.UPDATE_PROFILE_CLIENT || apiId==AppConstants.UPDATE_PROFILE_SERVICE_PROVIDER){
      CallAPIs.getUserInfo(this)
    }else{
      nextStep()
    }


  }
}