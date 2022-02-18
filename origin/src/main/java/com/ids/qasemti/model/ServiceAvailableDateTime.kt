package com.ids.qasemti.controller.Adapters.com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ids.qasemti.controller.Adapters.com.ids.qasemti.controller.Adapters.com.ids.qasemti.model.TimeDuration

class ServiceAvailableDateTime(

    @SerializedName("day")
    @Expose
    var day : String ?="" ,

    @SerializedName("time")
    @Expose
    var time : ArrayList<TimeDuration> = arrayListOf()
) {
}