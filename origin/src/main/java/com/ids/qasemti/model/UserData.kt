package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserData(

    @SerializedName("display_name")
    @Expose
    var displayName : String ?="" ,

    @SerializedName("ID")
    @Expose
    var id : String ?="" ,

    @SerializedName("user_activation_key")
    @Expose
    var userActKey : String ?="" ,

    @SerializedName("user_email")
    @Expose
    var userEmail : String ?=""
    ,

    @SerializedName("user_login")
    @Expose
    var userLogin : String ?=""
    ,

    @SerializedName("user_nicename")
    @Expose
    var userNiceName : String ?=""
    ,

    @SerializedName("user_pass")
    @Expose
    var userPass : String ?=""
    ,

    @SerializedName("user_registered")
    @Expose
    var userRegistered : String ?=""
    ,

    @SerializedName("user_status")
    @Expose
    var userStatus : String ?=""
    ,

    @SerializedName("user_url")
    @Expose
    var userUrl : String ?=""

) {
}