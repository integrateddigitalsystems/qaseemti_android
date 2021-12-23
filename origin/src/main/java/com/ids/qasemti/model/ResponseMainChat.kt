package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseMainChat (

    @SerializedName("result")
    @Expose
    var result : String ?="" ,

    @SerializedName("response")
    @Expose
    var chats : ArrayList<ChatItem>

){
}