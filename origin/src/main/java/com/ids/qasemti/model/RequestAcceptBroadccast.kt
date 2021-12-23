package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestAcceptBroadccast(
    @SerializedName("vendor_id")
    @Expose
    var vendorId : Int ?= 0,

    @SerializedName("order_id")
    @Expose
    var orderId : Int ?=0
 ) {
}