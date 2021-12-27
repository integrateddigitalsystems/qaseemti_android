package com.ids.qasemti.controller.Adapters.com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ids.qasemti.model.ResponseNotification

class ResponeMainNotification(

    @SerializedName("count")
    @Expose
    var count : String ?="" ,

    @SerializedName("notifications")
    @Expose
    var notf : ArrayList<ResponseNotification> = arrayListOf()
) {
}