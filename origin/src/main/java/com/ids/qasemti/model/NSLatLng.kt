package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.type.LatLng

class NSLatLng(

    @SerializedName("northeast")
    @Expose
    var northEast : LatLng ?=null ,

    @SerializedName("southwest")
    @Expose
    var southWest : LatLng ?=null
) {
}