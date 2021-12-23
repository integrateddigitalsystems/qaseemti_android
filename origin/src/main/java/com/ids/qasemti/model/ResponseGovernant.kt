package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseGovernant(

    @SerializedName("gov_id")
    @Expose
    var govId : String ?="" ,

    @SerializedName("gov_ar")
    @Expose
    var govAr : String ?="" ,

    @SerializedName("gov_en")
    @Expose
    var govEn : String ?=""
){
}