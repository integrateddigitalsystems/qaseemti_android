package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestOTP(
    @SerializedName("mobile_number")
    @Expose
    var mobileNumber : String ?="" ,

    @SerializedName("device_id")
    @Expose
    var deviceId : Int ?=0

) {
}