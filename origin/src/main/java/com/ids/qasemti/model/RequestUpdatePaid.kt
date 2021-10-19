package com.ids.qasemti.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestUpdatePaid(
    @SerializedName("order_id")
    @Expose
    var order_id : Int ?=0,

    @SerializedName("paid")
    @Expose
    var paid : Int ?=0
) {
}