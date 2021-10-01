package com.ids.qasemti.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MediaFile {
    @SerializedName("Id")
    @Expose
    var id: Int? = 0

   @SerializedName("imagePath")
    @Expose
    var imagePath: String? = ""

    @SerializedName("YouTubePath")
    @Expose
    var youTubePath: String? = ""


    constructor(id: Int?, imagePath: String?, youTubePath: String?) {
        this.id = id
        this.imagePath = imagePath
        this.youTubePath = youTubePath
    }
}