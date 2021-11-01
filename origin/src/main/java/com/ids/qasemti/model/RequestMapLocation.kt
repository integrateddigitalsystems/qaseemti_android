package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestMapLocation(

    @SerializedName("query")
    @Expose
    var query : String ?="" ,

    @SerializedName("key")
    @Expose
    var key : String ?=""
) {
}