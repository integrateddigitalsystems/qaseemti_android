package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestRenewOrder(

    @SerializedName("user_id")
    @Expose
    var userId : Int ?=0 ,

    @SerializedName("order_id")
    @Expose
    var orderId : Int ?=0 ,

    @SerializedName("vendor_id")
    @Expose
    var vendorId : Int ?=0 ,

    @SerializedName("product_category")
    @Expose
    var productCategory : String ?="" ,

    @SerializedName("product_id")
    @Expose
    var productId : Int ?=0 ,

    @SerializedName("types")
    @Expose
    var types : String ?="" ,

    @SerializedName("size_capacity")
    @Expose
    var sizeCap : String ?="" ,

    @SerializedName("delivery_date")
    @Expose
    var delDate : String ?="" ,

    @SerializedName("date_from")
    @Expose
    var dateFrom : String ?="" ,

    @SerializedName("date_to")
    @Expose
    var dateTo : String ?="" ,

    @SerializedName("address_name")
    @Expose
    var addressName : String ?="" ,

    @SerializedName("address_latitude")
    @Expose
    var addressLat : Double ?=0.0 ,

    @SerializedName("address_longitude")
    @Expose
    var addressLong : Double ?=0.0 ,

    @SerializedName("address_street")
    @Expose
    var addressStreet : String ?="" ,

    @SerializedName("address_building")
    @Expose
    var addressBldg : String ?="" ,

    @SerializedName("address_floor")
    @Expose
    var addressFloor : String ?="" ,

    @SerializedName("address_description")
    @Expose
    var addressDesc : String ?="" ,

    @SerializedName("first_name")
    @Expose
    var firstName : String ?="" ,

    @SerializedName("last_name")
    @Expose
    var lastName : String ?="" ,

    @SerializedName("company")
    @Expose
    var company : String ?="" ,

    @SerializedName("email")
    @Expose
    var email : String ?="" ,

    @SerializedName("phone")
    @Expose
    var phone : String ?=""




) {
}