package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestLanguage(

    @SerializedName("language")
    @Expose
    var lang : String ?=""
) {
}