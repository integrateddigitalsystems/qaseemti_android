package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RelatedOrder(
    @SerializedName("date")
    @Expose
    var orderDate:String ?="" ,

    @SerializedName("total")
    @Expose
    var total:String ?="" ,

    @SerializedName("earnings")
    @Expose
    var orderEarning:String ?="" ,

    @SerializedName("delivery_date")
    @Expose
    var deliveryDate:String ?="" ,

    @SerializedName("admin_fees")
    @Expose
    var adminFees: String ?="" ,

    @SerializedName("currency")
    @Expose
    var currency : String ?="" ,

    @SerializedName("customer_id")
    @Expose
    var customerId : Int ?= 0 ,

    @SerializedName("delivered")
    @Expose
    var delivered : String ?="" ,

    @SerializedName("delivery_latitude")
    @Expose
    var deliveryLat : String ?="" ,

    @SerializedName("delivery_longitude")
    @Expose
    var deliveryLong : String ?="" ,

    @SerializedName("new_order_email_sent")
    @Expose
    var newOrderEmailSent : String ?="" ,

    @SerializedName("on_track")
    @Expose
    var onTrack : String ?="" ,

    @SerializedName("order_id")
    @Expose
    var orderId : String ?="" ,

    @SerializedName("order_status")
    @Expose
    var orderStatus : String ?="" ,

    @SerializedName("paid")
    @Expose
    var paid : String ?="" ,

    @SerializedName("payment_method")
    @Expose
    var paymentMethod : String ?="" ,

    @SerializedName("payment_method_title")
    @Expose
    var paymentMethodTitle : String ?="" ,

    @SerializedName("product")
    @Expose
    var responseProduct : OrderProduct ?=null ,

    @SerializedName("shipping_total")
    @Expose
    var shippingTotal : String ?="" ,


    @SerializedName("user_latitude")
    @Expose
    var userLat : String ?="" ,

    @SerializedName("user_longitude")
    @Expose
    var userLong : String ?="" ,

    @SerializedName("vendor")
    @Expose
    var vendor : OrderVendor ?=null ,

    @SerializedName("wcfmmp_order_email_triggered")
    @Expose
    var orderEmailTriggered : String ?="" ,

    @SerializedName("customer")
    @Expose
    var customer : OrderCustomer ?=null ,

    @SerializedName("grand_total")
    @Expose
    var grandTotal : String ?=""




) {
}