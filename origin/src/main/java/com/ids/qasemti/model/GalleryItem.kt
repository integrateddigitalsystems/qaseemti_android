package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GalleryItem(

    @SerializedName("id")
    @Expose
    var id : String ?="" ,

    @SerializedName("url")
    @Expose
    var url : String ?=""
) {

}
