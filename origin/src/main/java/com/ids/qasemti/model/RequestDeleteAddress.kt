package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestDeleteAddress(
    @SerializedName("user_id")
    @Expose
    var userId : Int ?=0 ,

    @SerializedName("address_id")
    @Expose
    var addressId : Int ?=0
) {
}