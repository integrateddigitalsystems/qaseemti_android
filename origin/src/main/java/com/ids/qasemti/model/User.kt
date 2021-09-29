package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("user_id")
    @Expose
    var userId: String? = null

    @SerializedName("middle_name")
    @Expose
    var middleName: String? = null

    @SerializedName("last_name")
    @Expose
    var lastName: String? = null

    @SerializedName("gender")
    @Expose
    var gender: String? = null

    @SerializedName("mobile_number")
    @Expose
    var mobileNumber: String? = null

    @SerializedName("altr_numb")
    @Expose
    var altrNumb: String? = null

    @SerializedName("bank_name")
    @Expose
    var bankName: String? = null

    @SerializedName("bank_address")
    @Expose
    var bankAddress: String? = null

    @SerializedName("account_name")
    @Expose
    var accountName: String? = null

    @SerializedName("account_number")
    @Expose
    var accountNumber: String? = null

    @SerializedName("billing_first_name")
    @Expose
    var billingFirstName: String? = null

    @SerializedName("billing_last_name")
    @Expose
    var billingLastName: String? = null

    @SerializedName("billing_company")
    @Expose
    var billingCompany: String? = null

    @SerializedName("billing_address_1")
    @Expose
    var billingAddress1: String? = null

    @SerializedName("billing_address_2")
    @Expose
    var billingAddress2: String? = null

    @SerializedName("billing_city")
    @Expose
    var billingCity: String? = null

    @SerializedName("billing_postcode")
    @Expose
    var billingPostcode: String? = null

    @SerializedName("billing_country")
    @Expose
    var billingCountry: String? = null

    @SerializedName("billing_email")
    @Expose
    var billingEmail: String? = null

    @SerializedName("billing_phone")
    @Expose
    var billingPhone: String? = null

    @SerializedName("shipping_first_name")
    @Expose
    var shippingFirstName: String? = null

    @SerializedName("shipping_last_name")
    @Expose
    var shippingLastName: String? = null

    @SerializedName("shipping_company")
    @Expose
    var shippingCompany: String? = null

    @SerializedName("shipping_address_1")
    @Expose
    var shippingAddress1: String? = null

    @SerializedName("shipping_address_2")
    @Expose
    var shippingAddress2: String? = null

    @SerializedName("shipping_city")
    @Expose
    var shippingCity: String? = null

    @SerializedName("shipping_postcode")
    @Expose
    var shippingPostcode: String? = null

    @SerializedName("shipping_country")
    @Expose
    var shippingCountry: String? = null

    @SerializedName("shipping_phone")
    @Expose
    var shippingPhone: String? = null
}