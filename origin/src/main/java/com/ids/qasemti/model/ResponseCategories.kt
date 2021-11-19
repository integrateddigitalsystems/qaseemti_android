package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppConstants

class ResponseCategories(

    @SerializedName("id")
    @Expose
    var id : String ?="" ,

    @SerializedName("value_en")
    @Expose
    var valEn : String ?="" ,

    @SerializedName("value_ar")
    @Expose
    var valAr : String ?=""


) {
    fun getName():String{
        if(MyApplication.languageCode == AppConstants.LANG_ENGLISH) return  valEn!! else return  valAr!!
    }
}