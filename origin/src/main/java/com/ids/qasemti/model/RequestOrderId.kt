package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestOrderId(
    @SerializedName("order_id")
    @Expose
    var orderId : Int ?=0
) {
}