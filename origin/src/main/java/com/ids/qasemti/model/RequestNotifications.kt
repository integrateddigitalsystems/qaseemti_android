package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestNotifications(
    @SerializedName("language")
    @Expose
    var lang : String ?="" ,

    @SerializedName("mobile_number")
    @Expose
    var userId : String ?="" ,

    @SerializedName("mobile_notification_id")
    @Expose
    var mobileNotificatonId : Int ?=0,

    @SerializedName("per_page")
    @Expose
    var perPage : Int ?=0,

    @SerializedName("page")
    @Expose
    var page : Int ?=0 ,

    @SerializedName("is_sp")
    @Expose
    var isSp : Int ?=0 ,

    @SerializedName("is_cl")
    @Expose
    var isCl : Int ?=0

) {
}