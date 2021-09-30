package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseUserStatus(

    @SerializedName("result")
    @Expose
    var result : Int ?=1 ,

    @SerializedName("suspended")
    @Expose
    var enabled : Int ?= 3 ,

    @SerializedName("active")
    @Expose
    var online : Int ?=3
 ) {
}