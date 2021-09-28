package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCancel (
    @SerializedName("result")
    @Expose
    var result : Int ?=0
        ){
}