package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GatewayParam(

    @SerializedName("key")
    @Expose
    var key : String ?="" ,

    @SerializedName("value")
    @Expose
    var value : String ?=""
)