package com.ids.qasemti.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestUpdateService(
    @SerializedName("product_id")
    @Expose
    var product_id : Int ?=0,

    @SerializedName("stock_quantity")
    @Expose
    var stock_quantity : Int ?=0,

    @SerializedName("stock_status")
    @Expose
    var stock_status : String ?=""
) {
}