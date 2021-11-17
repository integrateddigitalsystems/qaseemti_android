package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestCouponReview(

    @SerializedName("order_id")
    @Expose
    var orderId : Int ?=null ,

    @SerializedName("coupon")
    @Expose
    var coupon : String ?="" ,

    @SerializedName("language")
    @Expose
    var lang : String ?=""


) {
}