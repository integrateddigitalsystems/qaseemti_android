package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseOrders(
    @SerializedName("date")
    @Expose
    var date : String ?="" ,

    @SerializedName("order_id")
    @Expose
    var orderId : String ?="" ,

    @SerializedName("order_status")
    @Expose
    var orderStatus : String ?="" ,

    @SerializedName("currency")
    @Expose
    var currency : String ?="" ,

    @SerializedName("shipping_total")
    @Expose
    var shippingTotal : String ?="" ,

    @SerializedName("total")
    @Expose
    var total : String ?="" ,

    @SerializedName("customer_id")
    @Expose
    var customerid : String ?= "" ,

    @SerializedName("payment_method")
    @Expose
    var paymentMethod : String ?="" ,

    @SerializedName("payment_method_title")
    @Expose
    var paymentMethodTitle : String ?="" ,

    @SerializedName("customer_location")
    @Expose
    var customerLocation : String ?="" ,

    @SerializedName("delivery_latitude")
    @Expose
    var customerLat : String ?="" ,

    @SerializedName("delivery_longitude")
    @Expose
    var customerLong : String ?="" ,

    @SerializedName("delivery_date")
    @Expose
    var deliveryDate : String ?="" ,

    @SerializedName("earnings")
    @Expose
    var earnings : String ?="",

    @SerializedName("admin_fees")
    @Expose
    var adminFees : String ?="" ,

    @SerializedName("paid")
    @Expose
    var paid : String ?="" ,

    @SerializedName("on_track")
    @Expose
    var onTrack : String ?="" ,

    @SerializedName("delivered")
    @Expose
    var delivered : String ?="" ,

    @SerializedName("product")
    @Expose
    var product : OrderProduct?=null ,


    @SerializedName("vendor")
    @Expose
    var vendor : OrderVendor?=null ,

    @SerializedName("customer")
    @Expose
    var customer : OrderCustomer ?=null ,

    @SerializedName("user_latitude")
    @Expose
    var userLat : String ?="" ,

    @SerializedName("user_longitude")
    @Expose
    var userLong : String ?="" ,

    @SerializedName("cancellation_date")
    @Expose
    var cancellationDate : String ?=""
) {
}