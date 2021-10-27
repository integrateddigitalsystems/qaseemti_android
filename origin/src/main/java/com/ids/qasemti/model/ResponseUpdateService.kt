package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseUpdateService (
    @SerializedName("result")
    @Expose
    var result : String ?="",

    @SerializedName("product_id")
    @Expose
    var product_id : String ?=""
){
}