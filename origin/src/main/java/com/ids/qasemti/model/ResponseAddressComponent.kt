package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseAddressComponent(

    @SerializedName("long_name")
    @Expose
    var longName : String ?="" ,

    @SerializedName("short_name")
    @Expose
    var shortName : String ?="" ,

    @SerializedName("types")
    @Expose
    var types : ArrayList<String> = arrayListOf()
) {
}