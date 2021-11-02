package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ErrorMessage(

    @SerializedName("key")
    @Expose
    var key : String ?="" ,

    @SerializedName("codeEn")
    @Expose
    var codeEn : String ?="" ,

    @SerializedName("codeAr")
    @Expose
    var codeAr : String ?=""
) {

}
