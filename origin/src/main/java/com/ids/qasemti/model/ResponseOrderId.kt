package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseOrderId(

    @SerializedName("result")
    @Expose
    var result : String ?=" " ,

    @SerializedName("message")
    @Expose
    var message : String ?="" ,

    @SerializedName("order_id")
    @Expose
    var orderId : String ?=""
) {
}