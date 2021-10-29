package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppConstants
import okhttp3.MultipartBody

class RequiredFiles {
    @SerializedName("id")
    @Expose
    var id: String? = ""

    @SerializedName("meta_key")
    @Expose
    var metaKey: String? = ""

    @SerializedName("meta_value_en")
    @Expose
    var metaValueEn: String? = ""

    @SerializedName("meta_value_ar")
    @Expose
    var metaValueAr: String? = ""

    var multipart : MultipartBody.Part ?=null
    var selectedFileName: String? = ""

    constructor(
        id: String?,
        metaKey: String?,
        metaValueEn: String?,
        metaValueAr: String?,
        multipart: MultipartBody.Part?,
        selectedFileName: String?
    ) {
        this.id = id
        this.metaKey = metaKey
        this.metaValueEn = metaValueEn
        this.metaValueAr = metaValueAr
        this.multipart = multipart
        this.selectedFileName = selectedFileName
    }

    fun getFileTitle():String?{
        return if (MyApplication.languageCode == AppConstants.LANG_ENGLISH)
            return metaValueEn
        else
            metaValueAr
    }




}