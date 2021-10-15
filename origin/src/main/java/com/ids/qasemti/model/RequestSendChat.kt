package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestSendChat(

    @SerializedName("sent_by")
    @Expose
    var sentBy : String ?="" ,

    @SerializedName("email")
    @Expose
    var email : String ?="" ,

    @SerializedName("message")
    @Expose
    var message : String ?="" ,

    @SerializedName("order_id")
    @Expose
    var orderId : Int ?=0 ,

    @SerializedName("sent_by_id")
    @Expose
    var sendById : Int ?=0
) {
}