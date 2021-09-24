package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestNotificationUpdate(

    @SerializedName("user_id")
    @Expose
    var userId : Int ?=0 ,

    @SerializedName("type")
    @Expose
    var available : Int?=0
) {
}