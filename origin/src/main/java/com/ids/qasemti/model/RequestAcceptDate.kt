package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestAcceptDate(

    @SerializedName("order_id")
    @Expose
    var orderId : Int ?=0 ,

    @SerializedName("accept")
    @Expose
    var accept : Int ?=0 ,

    @SerializedName("language")
    @Expose
    var lang : String ?=""

) {
}