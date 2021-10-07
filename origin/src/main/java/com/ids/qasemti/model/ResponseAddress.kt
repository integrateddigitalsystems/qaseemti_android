package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseAddress (
    @SerializedName("address_id")
    @Expose
    var addressId : String ?="",

    @SerializedName("address_name")
    @Expose
    var addressName : String ?="" ,

    @SerializedName("latitude")
    @Expose
    var lat : String ?="" ,

    @SerializedName("longitude")
    @Expose
    var long : String ?="" ,

    @SerializedName("street")
    @Expose
    var street : String ?="" ,

    @SerializedName("floor")
    @Expose
    var floor : String ?="" ,

    @SerializedName("description")
    @Expose
    var desc :String ?="" ,

    @SerializedName("building")
    @Expose
    var bldg : String ?=""
        ){

}
