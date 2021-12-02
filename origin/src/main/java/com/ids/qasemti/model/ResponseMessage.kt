package com.ids.qasemti.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseMessage {
    @SerializedName("result")
    @Expose
    var result: Int? = 0

    @SerializedName("message")
    @Expose
    var message: String? = ""

    @SerializedName("address_id")
    @Expose
    var address_id: String? = ""

    @SerializedName("product_id")
    @Expose
    var product_id: String? = ""

    @SerializedName("token")
    @Expose
    var token : String ?=""

    @SerializedName("gallery")
    @Expose
    var gallery : ArrayList<GalleryItem> = arrayListOf()
}