package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OrderCommission(

    @SerializedName("commission_fixed")
    @Expose
    var commissionFixed : String ?="" ,

    @SerializedName("commission_mode")
    @Expose
    var commissionMod : String ?="" ,

    @SerializedName("commission_percent")
    @Expose
    var commissionPercent : String ?="" ,

    @SerializedName("tax_name")
    @Expose
    var taxName:String  ?="" ,

    @SerializedName("tax_percent")
    @Expose
    var taxPercent : String ?=""
) {
}