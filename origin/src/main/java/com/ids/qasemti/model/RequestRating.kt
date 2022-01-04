package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestRating {
    @SerializedName("vendor_id")
    @Expose
    var vendorId: Int? = 0

    @SerializedName("client_id")
    @Expose
    var authorId: Int? = 0

    @SerializedName("client_name")
    @Expose
    var authorName: String? = ""

    @SerializedName("client_email")
    @Expose
    var authorEmail: String? = ""

    @SerializedName("review_title")
    @Expose
    var reviewTitle: String? = ""

    @SerializedName("review_description")
    @Expose
    var reviewDescription: String? = ""

    @SerializedName("review_rating")
    @Expose
    var reviewRating: Int? = 0

    @SerializedName("order_id")
    @Expose
    var orderId : Int ?=0

    constructor(
        vendorId: Int?,
        authorId: Int?,
        authorName: String?,
        authorEmail: String?,
        reviewTitle: String?,
        reviewDescription: String?,
        reviewRating: Int? ,
        orderId : Int ?
    ) {
        this.vendorId = vendorId
        this.authorId = authorId
        this.authorName = authorName
        this.authorEmail = authorEmail
        this.reviewTitle = reviewTitle
        this.reviewDescription = reviewDescription
        this.reviewRating = reviewRating
        this.orderId = orderId
    }
}