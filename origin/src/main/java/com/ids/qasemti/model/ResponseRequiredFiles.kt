package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseRequiredFiles {
    @SerializedName("result")
    @Expose
    var result: Int? = 0

    @SerializedName("files")
    @Expose
    var files: ArrayList<RequiredFiles>? = null
}