package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ServiceCommission(

    @SerializedName("commission_fixed")
    @Expose
    var comissionFixed : String ?="" ,

    @SerializedName("commission_mode")
    @Expose
    var comissionMode :String ?="" ,

    @SerializedName("commission_percent")
    @Expose
    var comissionPercent : String ?="" ,

    @SerializedName("tax_name")
    @Expose
    var taxName : String ?="" ,

    @SerializedName("tax_percent")
    @Expose
    var taxPercent : String ?=""
) {
}