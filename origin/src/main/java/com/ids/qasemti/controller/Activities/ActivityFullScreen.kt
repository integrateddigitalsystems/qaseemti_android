package com.ids.qasemti.controller.Activities

import android.os.Bundle
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.loadImagesUrl
import com.ids.qasemti.utils.onOneClick
import kotlinx.android.synthetic.main.activity_full_screen.*

class ActivityFullScreen : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)



        btBackImage.onOneClick {
            super.onBackPressed()
        }
        ivOnlyImage.loadImagesUrl(MyApplication.selectedImageShow!!)
    }
}