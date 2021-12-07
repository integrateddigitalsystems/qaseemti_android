package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestUpdateOrder(

    @SerializedName("order_id")
    @Expose
    var orderId : Int ?=0 ,

    @SerializedName("on_track")
    @Expose
    var onTrack : Boolean ?=false ,

    @SerializedName("delivered")
    @Expose
    var delivered : Boolean ?=false ,

    @SerializedName("paid")
    @Expose
    var paid : Boolean ?=false

 ) {
}