package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OrderVendor(
    @SerializedName("altr_numb")
    @Expose
    var altNum : String ?="" ,

    @SerializedName("available")
    @Expose
    var available : String ?="" ,

    @SerializedName("birthday")
    @Expose
    var birthday : String ?="" ,

    @SerializedName("civil_id")
    @Expose
    var civilId : String ?="" ,

    @SerializedName("civil_id_attach")
    @Expose
    var civilIdAtt : String ?="" ,

    @SerializedName("country")
    @Expose
    var country : String ?="" ,

    @SerializedName("driving_license_File")
    @Expose
    var drivLiscFile : String ?="",

    @SerializedName("first_name")
    @Expose
    var firstName : String ?="",

    @SerializedName("last_name")
    @Expose
    var lastName : String ?="",

    @SerializedName("gender")
    @Expose
    var gender : String ?="",

    @SerializedName("latitude")
    @Expose
    var latitude : String ?="",

    @SerializedName("location")
    @Expose
    var location : String ?="",

    @SerializedName("longitude")
    @Expose
    var longitude : String ?="",

    @SerializedName("middle_name")
    @Expose
    var middleName : String ?="",

    @SerializedName("profile_pic_url")
    @Expose
    var profilePic : String ?="",

    @SerializedName("proof_of_ownership_File")
    @Expose
    var proofOwnership : String ?="",

    @SerializedName("rate")
    @Expose
    var rate : String ?="",

    @SerializedName("state")
    @Expose
    var state : String ?="",

    @SerializedName("store_name")
    @Expose
    var storeName : String ?="",

    @SerializedName("user_id")
    @Expose
    var userId : String ?="",

    @SerializedName("type")
    @Expose
    var type : String ?=""
) {
}