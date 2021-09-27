package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseConfiguration(

    @SerializedName("id")
    @Expose
    var id : String ?="" ,

    @SerializedName("key")
    @Expose
    var key : String ?="" ,

    @SerializedName("value")
    @Expose
    var value : String ?=""
) {
}