package com.ids.qasemti.controller.Adapters.com.ids.qasemti.controller.Adapters.com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TimeDuration(

    @SerializedName("from")
    @Expose
    var from : String ?="" ,

    @SerializedName("to")
    @Expose
    var to : String ?=""
) {

}
