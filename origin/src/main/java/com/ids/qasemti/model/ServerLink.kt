package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ServerLink(

    @SerializedName("title")
    @Expose
    var title : String ?="" ,

    @SerializedName("urlLink")
    @Expose
    var urlLink: String ?="" ,

    @SerializedName("password")
    @Expose
    var password : String ?=""
) {

}
