package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestServices(

    @SerializedName("vendor_id")
    @Expose
    var vendorId : Int ?=0 ,

    @SerializedName("language")
    @Expose
    var lang : String ?=""

) {
}