package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MarkNotification(
    @SerializedName("device_id")
    @Expose
    var deviceId : Int ?=1,
    @SerializedName("user_id")
    @Expose
    var userId : Int ?=1,
    @SerializedName("mobile_notification_id")
    @Expose
    var mobileNotificationId : Int ?=1
) {
}