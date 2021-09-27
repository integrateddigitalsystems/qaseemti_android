package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseUpdate(
    @SerializedName("result")
    @Expose
    var result: Double? = 0.0,

    @SerializedName("device_id")
    @Expose
    var deviceId: Int? = 0,
    @SerializedName("user_id")
    @Expose
    var userId : String ?= ""
)
{
}