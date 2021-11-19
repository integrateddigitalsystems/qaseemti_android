package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseMainCategories(

    @SerializedName("result")
    @Expose
    var result : String ?="" ,

    @SerializedName("message")
    @Expose
    var message : String ?="" ,

    @SerializedName("categories")
    @Expose
    var categories : ArrayList<ResponseCategories> = arrayListOf()
) {
}