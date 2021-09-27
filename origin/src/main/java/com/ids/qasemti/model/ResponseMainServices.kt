package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseMainServices (

        @SerializedName("result")
        @Expose
        var result : Int ,

        @SerializedName("service")
        @Expose
        var responseService : ArrayList<ResponseService> ?=null ,

        @SerializedName("count")
        @Expose
        var count : Int ?=0
    )

{
}