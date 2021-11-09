package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseMainOrderById(

    @SerializedName("result")
    @Expose
    var result : String ?="" ,

    @SerializedName("order")
    @Expose
    var order : ResponseOrders
) {
}