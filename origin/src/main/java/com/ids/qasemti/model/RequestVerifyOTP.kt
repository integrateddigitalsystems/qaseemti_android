package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestVerifyOTP(
    @SerializedName("code")
    @Expose
    var code : String ?="" ,

    @SerializedName("device_id")
    @Expose
    var deviceId : Int ? =0
) {
}