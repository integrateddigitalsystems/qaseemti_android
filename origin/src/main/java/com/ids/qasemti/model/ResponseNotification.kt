package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseNotification(
    @SerializedName("id")
    @Expose
    var id : String?="",

    @SerializedName("date")
    @Expose
    var date : String ?="",

    @SerializedName("title_ar")
    @Expose
    var title_ar : String ?="" ,

    @SerializedName("body_ar")
    @Expose
    var body_ar : String ?="",

    @SerializedName("title_en")
    @Expose
    var title_en : String ?="" ,

    @SerializedName("body_en")
    @Expose
    var body_en : String ?="",

    @SerializedName("data_fields")
    @Expose
    var dataField : String ?="" ,

    @SerializedName("is_viewed")
    @Expose
    var isViewed : String ?="" ,

    var open : Boolean = false
) {
}