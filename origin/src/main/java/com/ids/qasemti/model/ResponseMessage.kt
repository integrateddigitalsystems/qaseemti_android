package com.ids.qasemti.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseMessage {
    @SerializedName("result")
    @Expose
    var result: Int? = 0

    @SerializedName("message")
    @Expose
    var message: String? = ""

    @SerializedName("address_id")
    @Expose
    var address_id: String? = ""
}