package com.ids.qasemti.model

import com.google.gson.annotations.SerializedName

class FirebaseConfItem {

    @SerializedName("version")
    var version: Double?=0.0

    @SerializedName("isForceUpdate")
    var isForceUpdate: Boolean?=false

    @SerializedName("isClient")
    var isClient: Boolean?=false

}