package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseProfile(

    @SerializedName("result")
    @Expose
    var result : Int ?=0 ,

    @SerializedName("user_id")
    @Expose
    var userId : String ?=""
) {
}