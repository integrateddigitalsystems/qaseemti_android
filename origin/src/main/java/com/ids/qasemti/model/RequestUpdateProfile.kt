package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestUpdateProfile(

    @SerializedName("user_id")
    @Expose
    var userId : Int ?=0 ,

    @SerializedName("first_name")
    @Expose
    var firstName : String ="" ,

    @SerializedName("middle_name")
    @Expose
    var middleName : String ?="" ,

    @SerializedName("last_name")
    @Expose
    var lastName : String ?="" ,

    @SerializedName("email")
    @Expose
    var email : String ?="" ,

    @SerializedName("mobile_number")
    @Expose
    var mobileNumber : String ?="" ,

    @SerializedName("civil_id")
    @Expose
    var civilId : String ?="" ,


) {
}