package com.ids.qasemti.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseUser (
    @SerializedName ("result")
    @Expose
    var result: Int? = 0 ,

    @SerializedName("user")
    @Expose
    var user: User? = null ,

    @SerializedName("user_id")
    @Expose
    var userId : String ?="" ,

    @SerializedName("message")
    @Expose
    var message : String ?="" ,

    @SerializedName("language")
    @Expose
    var lang : String ?=""
){
}