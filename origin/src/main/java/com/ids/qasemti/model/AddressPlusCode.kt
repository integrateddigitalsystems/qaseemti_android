package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AddressPlusCode(

    @SerializedName("compound_code")
    @Expose
    var compoundCode : String ?="" ,

    @SerializedName("global_code")
    @Expose
    var globalCode : String ?=""
) {
}