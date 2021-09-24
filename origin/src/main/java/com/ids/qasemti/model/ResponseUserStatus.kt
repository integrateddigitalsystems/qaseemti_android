package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseUserStatus(

    @SerializedName("result")
    @Expose
    var result : Int ?=1 ,

    @SerializedName("User Status Enable ")
    @Expose
    var enabled : Int ?= 3 ,

    @SerializedName("User Status Online ")
    @Expose
    var online : Int ?=3
 ) {
}