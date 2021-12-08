package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MarkNotification(
    @SerializedName("mobile_number")
    @Expose
    var mobileNumber : String ?="" ,
    @SerializedName("mobile_notification_id")
    @Expose
    var mobileNotificationId : Int ?=1
) {
}