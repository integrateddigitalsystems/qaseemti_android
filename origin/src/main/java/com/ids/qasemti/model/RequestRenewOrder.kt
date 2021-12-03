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
    var types : Int ?=0 ,

    @SerializedName("size_capacity")
    @Expose
    var sizeCap : Int ?=0 ,

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

    @SerializedName("latitude")
    @Expose
    var addressLat : Double ?=0.0 ,

    @SerializedName("longitude")
    @Expose
    var addressLong : Double ?=0.0 ,

    @SerializedName("street")
    @Expose
    var addressStreet : String ?="" ,

    @SerializedName("building")
    @Expose
    var addressBldg : String ?="" ,

    @SerializedName("floor")
    @Expose
    var addressFloor : String ?="" ,

    @SerializedName("description")
    @Expose
    var addressDesc : String ?=""
    ,

    @SerializedName("province")
    @Expose
    var province : String ?=""

    ,

    @SerializedName("area")
    @Expose
    var area : String ?="",

    @SerializedName("block")
    @Expose
    var block : String ?="",

    @SerializedName("avenu")
    @Expose
    var avenue : String ?="",

    @SerializedName("apartment")
    @Expose
    var apt : String ?=""





) {
}