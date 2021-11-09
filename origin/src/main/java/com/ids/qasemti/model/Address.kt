package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Address
     (
    @SerializedName("village")
    @Expose
    var village : String ?="" ,
    @SerializedName("municipality")
    @Expose
    var municipality : String ?="" ,
    @SerializedName("county")
    @Expose
    var county : String ?="" ,
    @SerializedName("state")
    @Expose
    var state : String ?="" ,
    @SerializedName("country")
    @Expose
    var country : String ?="" ,
    @SerializedName("postcode")
    @Expose
    var postcode : String ?="" ,
    @SerializedName("country_code")
    @Expose
    var country_code : String ?="" ,
){}