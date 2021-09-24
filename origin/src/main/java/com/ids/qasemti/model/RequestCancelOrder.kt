package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestCancelOrder(
    @SerializedName("order_id")
    @Expose
    var orderId : Int ?=0 ,

    @SerializedName("user_id")
    @Expose
    var userId : Int ?=0 ,

    @SerializedName("cancellation_date")
    @Expose
    var cancellationDate: String ?="" ,

    @SerializedName("cancellation_reason")
    @Expose
    var cancellationReason : String ?=""

) {
}