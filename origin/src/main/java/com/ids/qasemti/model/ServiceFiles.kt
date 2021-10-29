package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ServiceFiles {
    @SerializedName("id")
    @Expose
    var id: String? =""

    @SerializedName("name")
    @Expose
    var name: String? = ""

    @SerializedName("url")
    @Expose
    var url: String? = ""
}