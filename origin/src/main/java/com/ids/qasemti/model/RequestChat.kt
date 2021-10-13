package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestChat(
    @SerializedName("order_id")
    @Expose
    var orderID : Int ?=0
){
}