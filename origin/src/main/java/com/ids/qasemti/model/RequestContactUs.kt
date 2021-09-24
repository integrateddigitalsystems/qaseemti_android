package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestContactUs(

    @SerializedName("full_name")
    @Expose
    var fullName : String ?="" ,

    @SerializedName("phone")
    @Expose
    var phone : String ?="",

    @SerializedName("email")
    @Expose
    var email : String ?="",

    @SerializedName("subject")
    @Expose
    var subject : String ?="",

    @SerializedName("message")
    @Expose
    var message : String ?=""
) {
}