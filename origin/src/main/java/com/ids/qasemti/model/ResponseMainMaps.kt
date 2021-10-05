package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseMainMaps(
    @SerializedName("result")
    @Expose
    var result : String ?="" ,

    @SerializedName("addresses")
    @Expose
    var addresses : ArrayList<ResponseMap> = arrayListOf()
) {
}