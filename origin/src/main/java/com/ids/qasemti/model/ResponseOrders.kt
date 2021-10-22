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

    @SerializedName("shipping_latitude")
    @Expose
    var shipping_latitude : String ?="" ,

    @SerializedName("shipping_longitude")
    @Expose
    var shipping_longitude : String ?="" ,

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
    var paid : Boolean ?=false,

    @SerializedName("grand_total")
    @Expose
    var grand_total : String ?="" ,

    @SerializedName("on_track")
    @Expose
    var onTrack : Boolean ?=false ,

    @SerializedName("delivered")
    @Expose
    var delivered : Boolean ?=false,

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
    var cancellationDate : String ?="" ,

    @SerializedName("dfg_address_building")
    @Expose
    var addressBuilding : String ?="" ,


    @SerializedName("dfg_address_description")
    @Expose
    var addressDescription : String ?="" ,

    @SerializedName("cancellation_reason")
    @Expose
    var cancellationReason : String ?="" ,

    @SerializedName("cancelled_by_id")
    @Expose
    var cancelledById : String ?="" ,

    @SerializedName("cancelled_by_name")
    @Expose
    var cancelledByName : String ?="" ,

    @SerializedName("cancelled_by_role")
    @Expose
    var cancelledByRole : String ?="" ,

    @SerializedName("dfg_address_floor")
    @Expose
    var addressFloor : String ?="" ,

    @SerializedName("dfg_address_latitude")
    @Expose
    var addressLat : String ?="" ,

    @SerializedName("dfg_address_longitude")
    @Expose
    var addressLong : String ?="" ,

    @SerializedName("dfg_address_name")
    @Expose
    var addressname : String ?="" ,

    @SerializedName("dfg_address_street")
    @Expose
    var addressStreet : String ?="" ,

    @SerializedName("dfg_email")
    @Expose
    var dfgEmail : String ?="" ,

    @SerializedName("dfg_first_name")
    @Expose
    var dfgFirstName : String ?="" ,

    @SerializedName("dfg_last_name")
    @Expose
    var dfgLastName : String ?="" ,

    @SerializedName("dfg_phone")
    @Expose
    var dfgPhone : String ?="" ,

    @SerializedName("failed")
    @Expose
    var failed :Boolean?=false ,

    @SerializedName("payment_status")
    @Expose
    var paymentStatus : String ?="" ,

    @SerializedName("type")
    @Expose
    var type : String ?="" ,

    @SerializedName("vendor_rate")
    @Expose
    var vendorRate : String ?=""




) {
}