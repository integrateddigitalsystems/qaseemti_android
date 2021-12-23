package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GatewayRespone (

    @SerializedName("params")
    @Expose
    var params : ArrayList<GatewayParam> = arrayListOf() ,


    @SerializedName("error_codes")
    @Expose
    var errorCode : ArrayList<ErrorMessage> = arrayListOf()
){
}