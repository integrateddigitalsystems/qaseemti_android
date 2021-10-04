package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestUserStatus
    (
    @SerializedName("user_id")
    @Expose
    var userId : Int ?=0
            ){
}