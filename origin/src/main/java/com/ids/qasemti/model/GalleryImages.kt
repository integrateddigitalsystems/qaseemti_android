package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GalleryImages {
    @SerializedName("id")
    @Expose
    var id: Int? = 0

    @SerializedName("url")
    @Expose
    var url: String? = ""
}