package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestClientReviews(

    @SerializedName("vendor_id")
    @Expose
    var vendorId : Int ?=0,

    @SerializedName("client_id")
    @Expose
    var clientId : Int ?=0 ,

    @SerializedName("review_title")
    @Expose
    var reviewTitle : String ?="" ,

    @SerializedName("review_description")
    @Expose
    var reviewDesc : String ?="" ,

    @SerializedName("review_rate")
    @Expose
    var reviewRate : Int ?=0
) {
}