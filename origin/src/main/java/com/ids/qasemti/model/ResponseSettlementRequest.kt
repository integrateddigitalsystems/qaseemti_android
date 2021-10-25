package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseSettlementRequest (

    @SerializedName("result")
    @Expose
    var result : String ?="" ,

    @SerializedName("Settlement ID")
    @Expose
    var settlementId : String ?=""
){
}