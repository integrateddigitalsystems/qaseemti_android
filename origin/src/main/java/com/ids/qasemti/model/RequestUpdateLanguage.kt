package com.ids.qasemti.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestUpdateLanguage(

    @SerializedName("user_id")
    @Expose
    var userId : Int ?=1,
    @SerializedName("language")
    @Expose
    var language : String ?=""
) {
}