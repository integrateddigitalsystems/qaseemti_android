package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestUpdatePayment(

    @SerializedName("order_id")
    @Expose
    var orderId : Int ?=0 ,

    @SerializedName("status")
    @Expose
    var status : String ?=""  ,

    @SerializedName("error_code")
    @Expose
    var error_code: String ?="" ,

    @SerializedName("payment_type")
    @Expose
    var paymentType : String ?="" ,

    @SerializedName("payment_id")
    @Expose
    var paymentId : String ?="" ,

    @SerializedName("result")
    @Expose
    var result : String ?=" " ,

    @SerializedName("post_date")
    @Expose
    var postDate : String ?="" ,

    @SerializedName("tran_id")
    @Expose
    var trainId : String ?="" ,

    @SerializedName("ref")
    @Expose var ref : String ?="" ,

    @SerializedName("track_id")
    @Expose
    var trackId : String ?="" ,

    @SerializedName("auth")
    @Expose
    var auth : String ?=""
) {
}