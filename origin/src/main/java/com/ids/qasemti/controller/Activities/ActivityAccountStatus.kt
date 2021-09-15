package com.ids.qasemti.controller.Activities


import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import androidx.core.widget.ImageViewCompat
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.onOneClick
import kotlinx.android.synthetic.main.activity_account_status.*

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
               AppHelper.goHome(this)
            }
        }

        llNotificationStatus.onOneClick {
            if(notfSelected==0 || notfSelected==-1){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ImageViewCompat.setImageTintList(
                        ivNotificationStatus,
                        ColorStateList.valueOf(getResources().getColor(R.color.redPrimary, getTheme()))
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
                            getResources().getColor(R.color.redPrimary)
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
            notfSelected = 1
        }

        llMessageStatus.onOneClick {
            if(notfSelected==1 || notfSelected==-1){

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ImageViewCompat.setImageTintList(
                        ivNotificationStatus,
                        ColorStateList.valueOf(getResources().getColor(R.color.gray_tint, getTheme()))
                    )
                    ImageViewCompat.setImageTintList(
                        ivMessageStatus,
                        ColorStateList.valueOf(getResources().getColor(R.color.redPrimary, getTheme()))
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
                            getResources().getColor(R.color.redPrimary)
                        )
                    )
                }
            }
            notfSelected=0
        }

    }
}