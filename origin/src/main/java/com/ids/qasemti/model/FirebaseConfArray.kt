package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FirebaseConfArray {
    @SerializedName("android")
    @Expose
    var android: ArrayList<FirebaseConfItem>? = arrayListOf()

    @SerializedName("ios")
    @Expose
    var ios: List<FirebaseConfItem>? = arrayListOf()
}