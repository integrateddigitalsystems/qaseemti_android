package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ChatItem(
    @SerializedName("sent_by")
    @Expose
    var sentBy : String ?="" ,

    @SerializedName("message")
    @Expose
    var message : String ?="" ,

    @SerializedName("files")
    @Expose
    var files : String ?="" ,

    @SerializedName("user")
    @Expose
    var user : String ?="" ,

    @SerializedName("senton")
    @Expose
    var sentOn : String ?="" ,

    @SerializedName("sent_by_id")
    @Expose
    var sendById : String ?="" ,

    var loading : Boolean ?=false


) {

}
