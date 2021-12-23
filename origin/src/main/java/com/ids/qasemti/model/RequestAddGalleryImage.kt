package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestAddGalleryImage(

    @SerializedName("product_id")
    @Expose
    var productId : Int
) {
}