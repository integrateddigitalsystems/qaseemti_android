package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class User (
    @SerializedName("user_id")
    @Expose
    var userId: String? = "" ,

    @SerializedName("notification_type")
    @Expose
    var notificationType : String ?="" ,

    @SerializedName("middle_name")
    @Expose
    var middleName: String? = "" ,

    @SerializedName("last_name")
    @Expose
    var lastName: String? = "" ,

    @SerializedName("gender")
    @Expose
    var gender: String? = "" ,

    @SerializedName("mobile_number")
    @Expose
    var mobileNumber: String? = "" ,

    @SerializedName("altr_numb")
    @Expose
    var altrNumb: String? = "" ,

    @SerializedName("bank_name")
    @Expose
    var bankName: String? = "" ,

    @SerializedName("bank_address")
    @Expose
    var bankAddress: String? = "" ,

    @SerializedName("account_name")
    @Expose
    var accountName: String? = "" ,

    @SerializedName("account_number")
    @Expose
    var accountNumber: String? = "" ,

    @SerializedName("billing_first_name")
    @Expose
    var billingFirstName: String? = "" ,

    @SerializedName("billing_last_name")
    @Expose
    var billingLastName: String? = "" ,

    @SerializedName("billing_company")
    @Expose
    var billingCompany: String? = "" ,

    @SerializedName("billing_address_1")
    @Expose
    var billingAddress1: String? = "" ,

    @SerializedName("billing_address_2")
    @Expose
    var billingAddress2: String? = "" ,

    @SerializedName("billing_city")
    @Expose
    var billingCity: String? = "" ,

    @SerializedName("billing_postcode")
    @Expose
    var billingPostcode: String? = "" ,

    @SerializedName("billing_country")
    @Expose
    var billingCountry: String? = "" ,

    @SerializedName("billing_email")
    @Expose
    var billingEmail: String? = "" ,

    @SerializedName("billing_phone")
    @Expose
    var billingPhone: String? = "" ,

    @SerializedName("shipping_first_name")
    @Expose
    var shippingFirstName: String? = "" ,

    @SerializedName("shipping_last_name")
    @Expose
    var shippingLastName: String? = "" ,

    @SerializedName("shipping_company")
    @Expose
    var shippingCompany: String? = "" ,

    @SerializedName("shipping_address_1")
    @Expose
    var shippingAddress1: String? = "" ,

    @SerializedName("shipping_address_2")
    @Expose
    var shippingAddress2: String? = "" ,

    @SerializedName("shipping_city")
    @Expose
    var shippingCity: String? = "" ,

    @SerializedName("shipping_postcode")
    @Expose
    var shippingPostcode: String? = "" ,

    @SerializedName("shipping_country")
    @Expose
    var shippingCountry: String? = "" ,

    @SerializedName("shipping_phone")
    @Expose
    var shippingPhone: String? = "" ,


    @SerializedName("available")
    @Expose
    var available: String? = "" ,
    @SerializedName("birthday")
    @Expose
    var birthday: String? = "" ,

    @SerializedName("country")
    @Expose
    var country: String? = "" ,

    

    @SerializedName("first_name")
    @Expose
    var firstName: String? = "" ,



    @SerializedName("email")
    @Expose
    var email: String? = "" ,

    @SerializedName("store_name")
    @Expose
    var storeName: String? = "" ,


    @SerializedName("state")
    @Expose
    var state: String? = "" ,

    @SerializedName("latitude")
    @Expose
    var latitude: String? = "" ,

    @SerializedName("longitude")
    @Expose
    var longitude: String? = "" ,

    @SerializedName("location")
    @Expose
    var location: String? = "",



    @SerializedName("civil_id")
    @Expose
    var civilId: String? = "" ,

    @SerializedName("civil_id_attach")
    @Expose
    var civilIdAttach: String? = "" ,

    @SerializedName("profile_pic_url")
    @Expose
    var profilePicUrl: String? = "" ,

    @SerializedName("date_of_birth")
    @Expose
    var dob: String? = "" ,

    @SerializedName("type")
    @Expose
    var type: Int? = 0 ,

    @SerializedName("files")
    @Expose
    var files: Files? = null ,

    @SerializedName("rate")
    @Expose
    var rate: Double? = 0.0

    ,

    @SerializedName("suspended")
    @Expose
    var suspended: Int? = 0 ,
    @SerializedName("active")
    @Expose
    var active: Int? = 0 ,
    @SerializedName("approved")
    @Expose
    var approved: Int? = 0 ,

    @SerializedName("description")
    @Expose
    var desc: String? = "" ,

    @SerializedName("iban")
    @Expose
    var IBAN : String ?="" ,

    @SerializedName("bank_branch")
    @Expose
    var bankBranch : String ?="" ,

    @SerializedName("bank_id")
    @Expose
    var bankId : String ?="" ,


    @SerializedName("addresses")
    @Expose
    var addresses : ArrayList<ResponseAddress> ?= arrayListOf()
){
    
    

}