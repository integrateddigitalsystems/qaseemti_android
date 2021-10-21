package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PaymentMethod {
    @SerializedName("id")
    @Expose
    var id: Int? = 0

    @SerializedName("title")
    @Expose
    var title: String? = ""

    @SerializedName("slug")
    @Expose
    var slug: String? = ""

    var selected=false
}