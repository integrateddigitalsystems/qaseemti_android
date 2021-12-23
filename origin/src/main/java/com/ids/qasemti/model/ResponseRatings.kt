package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseRatings(

    @SerializedName("result")
    @Expose
    var result : String ? = "" ,

    @SerializedName("rate")
    @Expose
    var rate : Double ?= 0.0

) {
}