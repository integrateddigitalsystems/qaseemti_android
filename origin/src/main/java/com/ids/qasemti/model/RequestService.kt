package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestService
    (
    @SerializedName("language")
    @Expose
    var language : String ?="" ,

    @SerializedName("product_id")
    @Expose
    var productId : Int ?=0 ,

    @SerializedName("type")
    @Expose
    var type : String ?=""
) {
}