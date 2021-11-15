package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseDeliveryDate(

    @SerializedName("result")
    @Expose
    var result : String ?="" ,

    @SerializedName("new_delivery_date")
    @Expose
    var new_delivery_date : String?=""

   /* @SerializedName("new_delivery_date")
    @Expose
    var new_delivery_date : ResponseOrders*/
) {
}