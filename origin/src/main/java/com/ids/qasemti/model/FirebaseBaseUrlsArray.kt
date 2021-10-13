package com.ids.qasemti.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FirebaseBaseUrlsArray {
    @SerializedName("android")
    @Expose
    var android: ArrayList<FirebaseUrlItems>? = arrayListOf()

    @SerializedName("ios")
    @Expose
    var ios: List<FirebaseUrlItems>? = arrayListOf()
}