package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseVerification(
    @SerializedName("result")
    @Expose
    var result : String ?="" ,

    @SerializedName("user_basic")
    @Expose
    var basicUser : VerificationUserBasic ?=null ,

    @SerializedName("user_meta_data")
    @Expose
    var MetaDataUser : VerificationUserMeta ?=null


) {
}