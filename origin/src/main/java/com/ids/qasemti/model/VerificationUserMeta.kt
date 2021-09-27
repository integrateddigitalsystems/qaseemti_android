package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VerificationUserMeta(
    @SerializedName("altr_numb")
    @Expose
    var alternateNumbers : ArrayList<String> = arrayListOf()
) {
}