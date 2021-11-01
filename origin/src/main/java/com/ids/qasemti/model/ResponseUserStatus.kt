package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseUserStatus(

    @SerializedName("result")
    @Expose
    var result : Int ?=0 ,

    @SerializedName("suspended")
    @Expose
    var suspended : Int ?= 0 ,

    @SerializedName("active")
    @Expose
    var active : Int ?=0
 ) {
}