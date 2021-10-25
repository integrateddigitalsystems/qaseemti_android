package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FirebaseWebData (

    @SerializedName("links")
    @Expose
    var links : ArrayList<WebItem> = arrayListOf()
){
}