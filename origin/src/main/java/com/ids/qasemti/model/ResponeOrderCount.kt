package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponeOrderCount(

    @SerializedName("result")
    @Expose
    var result : Int ?=0 ,

    @SerializedName("upcoming_orders")
    @Expose
    var upcomingOrders : Int ?=0 ,

    @SerializedName("active_orders")
    @Expose
    var activeOrders : Int ?=0
) {
}