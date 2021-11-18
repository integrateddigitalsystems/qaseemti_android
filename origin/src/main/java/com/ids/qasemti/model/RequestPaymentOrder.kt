package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestPaymentOrder(

    @SerializedName("order_id")
    @Expose
    var orderId : Int ?=0 ,

    @SerializedName("payment_id")
    @Expose
    var payment_id : String ?="",

    @SerializedName("order_total")
    @Expose
    var orderTotal : String ?="" ,

    @SerializedName("is_test")
    @Expose
    var isTest : Int ?=0 ,

    @SerializedName("status")
    @Expose
    var status : String ?="" ,

    @SerializedName("currency")
    @Expose
    var currency : String ?="" ,

    @SerializedName("reference")
    @Expose
    var reference : String ?="" ,

    @SerializedName("track_id")
    @Expose
    var trackId : String ?="" ,

    @SerializedName("transaction_id")
    @Expose
    var transId : String ?="" ,

    @SerializedName("authorization")
    @Expose
    var auth : String ?="" ,

    @SerializedName("token")
    @Expose
    var token : String ?="" ,

    @SerializedName("payment_date")
    @Expose
    var paymentDate : String ?="" ,

    @SerializedName("customer_reference")
    @Expose
    var custRef : String ?="" ,

    @SerializedName("error_message")
    @Expose
    var errorMessage : String ?="" ,

    @SerializedName("error_code")
    @Expose
    var errorCode : String ?=""




) {
}