package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseVerification(
    @SerializedName("result")
    @Expose
    var result : String ?="" ,

    @SerializedName("user")
    @Expose
    var user : User ?=null ,


) {
}