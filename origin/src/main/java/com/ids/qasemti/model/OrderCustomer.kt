package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OrderCustomer(


    @SerializedName("bank_address")
    @Expose
    var bank_address : String ?="",

    @SerializedName("bank_name")
    @Expose
    var bank_name : String ?="",

    @SerializedName("billing_address_1")
    @Expose
    var billing_address_1 : String ?="",

    @SerializedName("billing_address_2")
    @Expose
    var billing_address_2 : String ?="",

    @SerializedName("billing_city")
    @Expose
    var billing_city : String ?="",

    @SerializedName("billing_company")
    @Expose
    var billing_company : String ?="",

    @SerializedName("billing_country")
    @Expose
    var billing_country : String ?="",

    @SerializedName("email")
    @Expose
    var email : String ?="",

    @SerializedName("billing_first_name")
    @Expose
    var billing_first_name : String ?="",

    @SerializedName("billing_last_name")
    @Expose
    var billing_last_name : String ?="",

    @SerializedName("billing_phone")
    @Expose
    var billing_phone : String ?="",

    @SerializedName("billing_postcode")
    @Expose
    var billing_postcode : String ?="",

    @SerializedName("first_name")
    @Expose
    var first_name : String ?="",

    @SerializedName("gender")
    @Expose
    var gender : String ?="",

    @SerializedName("last_name")
    @Expose
    var last_name : String ?="",

    @SerializedName("middle_name")
    @Expose
    var middle_name : String ?="",

    @SerializedName("shipping_address_1")
    @Expose
    var shipping_address_1 : String ?="",

    @SerializedName("shipping_address_2")
    @Expose
    var shipping_address_2 : String ?="",

    @SerializedName("shipping_city")
    @Expose
    var shipping_city : String ?="",

    @SerializedName("shipping_company")
    @Expose
    var shipping_company : String ?="",

    @SerializedName("shipping_country")
    @Expose
    var shipping_country : String ?="",

    @SerializedName("shipping_first_name")
    @Expose
    var shipping_first_name : String ?="",

    @SerializedName("shipping_last_name")
    @Expose
    var shipping_last_name : String ?="",

    @SerializedName("shipping_phone")
    @Expose
    var shipping_phone : String ?="",

    @SerializedName("shipping_postcode")
    @Expose
    var shipping_postcode : String ?="",

    @SerializedName("user_id")
    @Expose
    var user_id : String ?="",

    @SerializedName("account_number")
    @Expose
    var account_number : String ?="",

    @SerializedName("altr_numb")
    @Expose
    var altr_numb: String ?="",

    @SerializedName("account_name")
    @Expose
    var account_name : String ?="",

    @SerializedName("addresses")
    @Expose
    var addresses : ArrayList<ResponseAddress> ?= arrayListOf(),

    @SerializedName("available")
    @Expose
    var available : String ?="",

    @SerializedName("bank_branch")
    @Expose
    var bankBranch : String ?="",

    @SerializedName("civil_id")
    @Expose
    var civilId : String ?="",

    @SerializedName("civil_id_attach")
    @Expose
    var civil_id_attach : String ?="",

    @SerializedName("date_of_birth")
    @Expose
    var date_of_birth : String ?="",

    @SerializedName("description")
    @Expose
    var description : String ?="",

    @SerializedName("iban")
    @Expose
    var iban : String ?="",

    @SerializedName("mobile_number")
    @Expose
    var mobile_number : String ?="",

    @SerializedName("profile_pic_url")
    @Expose
    var profile_pic_url : String ?=""
    ,

    @SerializedName("rate")
    @Expose
    var rate: String ?="",

    @SerializedName("type")
    @Expose
    var type: String ?=""



) {
}