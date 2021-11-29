package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestDeleteImage(

    @SerializedName("image_id")
    @Expose
    var imageId : Int ?=0 ,

    @SerializedName("product_id")
    @Expose
    var productId : Int ?=0
) {
}