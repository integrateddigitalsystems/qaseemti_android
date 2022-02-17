package com.ids.qasemti.controller.Adapters.com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ids.qasemti.controller.Adapters.com.ids.qasemti.controller.Adapters.com.ids.qasemti.model.DistanceElements

class ResponseDistance(

    @SerializedName("destination_addresses")
    @Expose
    var destAddress : String ?="" ,

    @SerializedName("origin_addresses")
    @Expose
    var originsAddress : String ?="" ,

    @SerializedName("rows")
    @Expose
    var rows : ArrayList<DistanceElements> = arrayListOf()


) {
}