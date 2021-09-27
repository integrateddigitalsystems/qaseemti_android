package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponeMainService(
    @SerializedName("result")
    @Expose
    var result : Int ,

    @SerializedName("service")
    @Expose
    var responseService : ResponseService ?=null
){

}

