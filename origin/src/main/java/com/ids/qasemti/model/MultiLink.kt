package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MultiLink(

    @SerializedName("enableMultiLinks_ios")
    @Expose
    var enableIOS : Boolean ?= false ,

    @SerializedName("enableMultiLinks_android")
    @Expose
    var enableAndroid : Boolean ?=false ,

    @SerializedName("serversLink")
    @Expose
    var serverLink : ArrayList<ServerLink> = arrayListOf()
) {
}