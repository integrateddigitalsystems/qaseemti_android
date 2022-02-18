package com.ids.qasemti.controller.Adapters.com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PaymentData(

    @SerializedName("id")
    @Expose
    var id : String ?="" ,

    @SerializedName("value_en")
    @Expose
    var valueEn : String ?="" ,

    @SerializedName("value_ar")
    @Expose
    var valueAr : String ?="" ,

    var selected : Boolean = false
) {
}