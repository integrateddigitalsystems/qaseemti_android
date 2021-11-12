package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestNewDeliveryDate(

    @SerializedName("order_id")
    @Expose
    var orderId : Int ?=0 ,

    @SerializedName("new_delivery_date")
    @Expose
    var new_delivery_date : String ?=""
) {
}