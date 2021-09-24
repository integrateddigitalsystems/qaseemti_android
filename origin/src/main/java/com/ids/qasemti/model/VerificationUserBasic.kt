package com.ids.qasemti.model

import com.google.firebase.firestore.auth.User
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.logging.Filter

class VerificationUserBasic(

    @SerializedName("data")
    @Expose
    var data : UserData ?=null ,

    @SerializedName("ID")
    @Expose
    var id : Int ?=0 ,

    @SerializedName("caps")
    @Expose
    var caps : ArrayList<Any> = arrayListOf() ,

    @SerializedName("roles")
    @Expose
    var roles : ArrayList<Any> = arrayListOf(),

    @SerializedName("allcaps")
    @Expose
    var allcaps : ArrayList<Any> = arrayListOf()
    ,

    @SerializedName("filter")
    @Expose
    var filter : Any  ?=null

) {
}