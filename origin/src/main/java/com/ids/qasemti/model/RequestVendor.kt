package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestVendor(
    @SerializedName("vendor_id")
    @Expose
    var vendorId : Int ?=0
) {


}