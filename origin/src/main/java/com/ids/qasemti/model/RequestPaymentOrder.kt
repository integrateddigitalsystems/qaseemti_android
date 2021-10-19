package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestPaymentOrder(

    @SerializedName("order_id")
    @Expose
    var orderId : Int ?=0 ,

    @SerializedName("payment_method")
    @Expose
    var paymentMethod : String ?="" ,

    @SerializedName("payment_method_title")
    @Expose
    var paymentMethodTitle : String ?=""
) {
}