package com.ids.qasemti.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseGalleryImages {
    @SerializedName("result")
    @Expose
    var result: Int? = 0

    @SerializedName("message")
    @Expose
    var message: String? = ""

    @SerializedName("gallery")
    @Expose
    var gallery: ArrayList<GalleryImages>? = arrayListOf()
}