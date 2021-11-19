package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCategories(

    @SerializedName("id")
    @Expose
    var id : String ?="" ,

    @SerializedName("value_en")
    @Expose
    var valEn : String ?="" ,

    @SerializedName("value_ar")
    @Expose
    var valAr : String ?=""
) {
}