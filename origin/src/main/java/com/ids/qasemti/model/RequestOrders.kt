package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestOrders(

    @SerializedName("user_id")
    @Expose
    var userId : Int ?=0 ,

    @SerializedName("language")
    @Expose
    var lang : String ?="" ,

    @SerializedName("order_status")
    @Expose
    var orderStatus : String  ?=""
) {
}