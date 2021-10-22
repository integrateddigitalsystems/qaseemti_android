package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestProductId(
    @SerializedName("product_id")
    @Expose
    var product_id : Int ?=0
) {
}