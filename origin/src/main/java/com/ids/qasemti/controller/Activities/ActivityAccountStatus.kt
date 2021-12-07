package com.ids.qasemti.controller.Activities


import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.widget.ImageViewCompat
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.RequestNotificationUpdate
import com.ids.qasemti.model.ResponseCancel
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_account_status.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityAccountStatus : ActivityBase() {

    var notfSelected = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_status)
        AppHelper.setAllTexts(rootLayoutAccountStatus,this)
        btSave.onOneClick {

            if(notfSelected==-1){
                var msg = AppHelper.getRemoteString("select_get_notified",this)
                AppHelper.createDialog(this,msg)
            }else{
                MyApplication.phoneNumber = MyApplication.selectedPhone
                MyApplication.isSignedIn = true
                MyApplication.firstTime = false
               AppHelper.goHome(this)
            }
        }

        llNotificationStatus.onOneClick {


            if (AppHelper.isOnline(this)) {
                if(notfSelected==0 || notfSelected==-1){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ImageViewCompat.setImageTintList(
                            ivNotificationStatus,
                            ColorStateList.valueOf(getResources().getColor(R.color.primary, getTheme()))
                        )
                        ImageViewCompat.setImageTintList(
                            ivMessageStatus,
                            ColorStateList.valueOf(getResources().getColor(R.color.gray_tint, getTheme()))
                        )
                    }
                    else {
                        ImageViewCompat.setImageTintList(
                            ivNotificationStatus,
                            ColorStateList.valueOf(
                                getResources().getColor(R.color.primary)
                            )
                        )
                        ImageViewCompat.setImageTintList(
                            ivMessageStatus,
                            ColorStateList.valueOf(
                                getResources().getColor(R.color.gray_tint)
                            )
                        )
                    }
                }
                if(btSave.visibility == View.GONE)
                    btSave.show()
                notfSelected = 1
                setNotificationType(1)
            }else{
                AppHelper.createDialog(this,getString(R.string.no_internet))
            }
        }

        llMessageStatus.onOneClick {
            if (AppHelper.isOnline(this)) {
                if(notfSelected==1 || notfSelected==-1){

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ImageViewCompat.setImageTintList(
                            ivNotificationStatus,
                            ColorStateList.valueOf(getResources().getColor(R.color.gray_tint, getTheme()))
                        )
                        ImageViewCompat.setImageTintList(
                            ivMessageStatus,
                            ColorStateList.valueOf(getResources().getColor(R.color.primary, getTheme()))
                        )
                    }
                    else {
                        ImageViewCompat.setImageTintList(
                            ivNotificationStatus,
                            ColorStateList.valueOf(
                                getResources().getColor(R.color.gray_tint)
                            )
                        )
                        ImageViewCompat.setImageTintList(
                            ivMessageStatus,
                            ColorStateList.valueOf(
                                getResources().getColor(R.color.primary)
                            )
                        )
                    }
                }
                if(btSave.visibility == View.GONE)
                    btSave.show()
                notfSelected = 0
                setNotificationType(0)
            }else{
                AppHelper.createDialog(this,getString(R.string.no_internet))
            }

        }

    }

    fun setNotificationType(available : Int ){
        var newReq = RequestNotificationUpdate(MyApplication.userId,available)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.updateNotification(newReq)?.enqueue(object : Callback<ResponseCancel> {
                override fun onResponse(call: Call<ResponseCancel>, response: Response<ResponseCancel>) {
                    try{
                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ResponseCancel>, throwable: Throwable) {

                }
            })
    }
}