package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseGeoAddress (

    @SerializedName("plus_code")
    @Expose
    var plusCode : AddressPlusCode?=null,

    @SerializedName("results")
    @Expose
    var results : ArrayList<AddressResult> = arrayListOf(),

    @SerializedName("status")
    @Expose
    var status : String ?=""


)
{
}