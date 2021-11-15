package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Coordinates(

    @SerializedName("latitude")
    @Expose
    var lat : String ?="" ,

    @SerializedName("longitude")
    @Expose
    var long : String ?=""
) {
}