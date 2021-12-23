package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponsePreviewCoupon(

    @SerializedName("result")
    @Expose
    var result : String ?="" ,

    @SerializedName("message")
    @Expose
    var message : String ?="" ,

    @SerializedName("old_total")
    @Expose
    var oldTotal : String ?="" ,

    @SerializedName("new_total")
    @Expose
    var newTotal : String ?="" ,

    @SerializedName("discount_type")
    @Expose
    var discountType : String ?="" ,

    @SerializedName("discount_amount")
    @Expose
    var discountAmount : String ?="" ,

    @SerializedName("total_discount_amount")
    @Expose
    var totalDiscountAmount : String ?=""
) {
}