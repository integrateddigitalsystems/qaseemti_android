package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestRating {
    @SerializedName("vendor_id")
    @Expose
    var vendorId: Int? = 0

    @SerializedName("author_id")
    @Expose
    var authorId: Int? = 0

    @SerializedName("author_name")
    @Expose
    var authorName: String? = ""

    @SerializedName("author_email")
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

    constructor(
        vendorId: Int?,
        authorId: Int?,
        authorName: String?,
        authorEmail: String?,
        reviewTitle: String?,
        reviewDescription: String?,
        reviewRating: Int?
    ) {
        this.vendorId = vendorId
        this.authorId = authorId
        this.authorName = authorName
        this.authorEmail = authorEmail
        this.reviewTitle = reviewTitle
        this.reviewDescription = reviewDescription
        this.reviewRating = reviewRating
    }
}