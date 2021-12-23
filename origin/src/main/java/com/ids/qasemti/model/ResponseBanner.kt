package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseBanner(

    @SerializedName("banner_id")
    @Expose
    var bannerId : String ?="" ,

    @SerializedName("banner_image_url")
    @Expose
    var bannerImageURL : String ?="" ,

    @SerializedName("product_id")
    @Expose
    var productId : String ?="" ,

    @SerializedName("site_url")
    @Expose
    var siteUrl : String ?=""
) {
}