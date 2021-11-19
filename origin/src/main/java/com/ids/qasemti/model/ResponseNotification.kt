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

    @SerializedName("title_en",alternate = ["title_ar","title"])
    @Expose
    var title : String ?="" ,

    @SerializedName("body_en",alternate = ["body_ar","body"])
    @Expose
    var body : String ?="",

    @SerializedName("data_fields")
    @Expose
    var dataField : String ?="" ,

    @SerializedName("is_viewed")
    @Expose
    var isViewed : String ?="" ,

    var open : Boolean = false
) {
}