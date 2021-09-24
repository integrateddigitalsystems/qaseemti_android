package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Response

class ResponseMainOrder(

    @SerializedName("result")
    @Expose
    var result : Int ?=0 ,

    @SerializedName("orders")
    @Expose
    var orders : ArrayList<ResponseOrders> = arrayListOf()
 ) {
}