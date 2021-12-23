package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponsePaymentMethod {
    @SerializedName("result")
    @Expose
    var result: Int? = 0

    @SerializedName("payment_methods")
    @Expose
    var paymentMethods: ArrayList<PaymentMethod>? = arrayListOf()
}