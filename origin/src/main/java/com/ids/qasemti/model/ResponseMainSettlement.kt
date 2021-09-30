package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseMainSettlement(
    @SerializedName("result")
    @Expose
    var result : Int ?=0 ,

    @SerializedName("Settlements")
    @Expose
    var settlements: ArrayList<ResponseSettlement> ,

    @SerializedName("number of orders")
    @Expose
    var numberOfOrders: Int ?=0 ,

    @SerializedName("total")
    @Expose
    var total : String ?=""
) {
}