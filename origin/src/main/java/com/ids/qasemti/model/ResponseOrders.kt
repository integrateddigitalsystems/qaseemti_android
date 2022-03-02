package com.ids.qasemti.model

import android.location.Address
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ids.qasemti.controller.Adapters.com.ids.qasemti.controller.Adapters.com.ids.qasemti.model.TimeDuration
import com.ids.qasemti.controller.Adapters.com.ids.qasemti.model.ServiceAvailableDateTime

class ResponseOrders(
    @SerializedName("date")
    @Expose
    var date : String ?="",

    @SerializedName("order_id")
    @Expose
    var orderId : String ?="",

    @SerializedName("order_status")
    @Expose
    var orderStatus : String ?="",

    @SerializedName("currency")
    @Expose
    var currency : String ?="",

    @SerializedName("shipping_total")
    @Expose
    var shippingTotal : String ?="",

    @SerializedName("shipping_latitude")
    @Expose
    var shipping_latitude : String ?="",

    @SerializedName("shipping_longitude")
    @Expose
    var shipping_longitude : String ?="",

    @SerializedName("shipping_province")
    @Expose
    var shipping_province : String ?="",

    @SerializedName("shipping_area")
    @Expose
    var shipping_area : String ?="",

    @SerializedName("shipping_block")
    @Expose
    var shipping_block : String ?="",

    @SerializedName("total")
    @Expose
    var total : String ?="",

    @SerializedName("customer_id")
    @Expose
    var customerid : String ?= "",

    @SerializedName("additional_cost")
    @Expose
    var additional : String ?="",



    @SerializedName("payment_method")
    @Expose
    var paymentMethod : String ?="",



    @SerializedName("customer_location")
    @Expose
    var customerLocation : String ?="",

    @SerializedName("delivery_latitude")
    @Expose
    var customerLat : String ?="",

    @SerializedName("delivery_longitude")
    @Expose
    var customerLong : String ?="",

    @SerializedName("delivery_date")
    @Expose
    var deliveryDate : String ?="",

    @SerializedName("actual_delivery_date")
    @Expose
    var actual_delivery_date : String ?="",


    @SerializedName("earnings")
    @Expose
    var earnings : String ?="",

    @SerializedName("admin_fees")
    @Expose
    var adminFees : String ?="",

    @SerializedName("paid")
    @Expose
    var paid : Boolean ?=false,

    @SerializedName("grand_total")
    @Expose
    var grand_total : String ?="",

    @SerializedName("on_track")
    @Expose
    var onTrack : Boolean ?=false,

    @SerializedName("delivered")
    @Expose
    var delivered : Boolean ?=false,

    @SerializedName("product")
    @Expose
    var product : OrderProduct?=OrderProduct(),


    @SerializedName("vendor")
    @Expose
    var vendor : OrderVendor?= OrderVendor(),

    @SerializedName("customer")
    @Expose
    var customer : OrderCustomer ?= OrderCustomer(),

    @SerializedName("user_latitude")
    @Expose
    var userLat : String ?="",

    @SerializedName("user_longitude")
    @Expose
    var userLong : String ?="",

    @SerializedName("cancellation_date")
    @Expose
    var cancellationDate : String ?="",

    @SerializedName("dfg_address_building")
    @Expose
    var addressBuilding : String ?="",


    @SerializedName("dfg_address_description")
    @Expose
    var addressDescription : String ?="",

    @SerializedName("cancellation_reason")
    @Expose
    var cancellationReason : String ?="",

    @SerializedName("cancelled_by_id")
    @Expose
    var cancelledById : String ?="",

    @SerializedName("cancelled_by_name")
    @Expose
    var cancelledByName : String ?="",

    @SerializedName("cancelled_by_role")
    @Expose
    var cancelledByRole : String ?="",

    @SerializedName("dfg_address_floor")
    @Expose
    var addressFloor : String ?="",

    @SerializedName("dfg_address_latitude")
    @Expose
    var addressLat : String ?="",

    @SerializedName("dfg_address_longitude")
    @Expose
    var addressLong : String ?="",

    @SerializedName("dfg_address_name")
    @Expose
    var addressname : String ?="",

    @SerializedName("dfg_address_street")
    @Expose
    var addressStreet : String ?="",

    @SerializedName("dfg_email")
    @Expose
    var dfgEmail : String ?="",

    @SerializedName("dfg_first_name")
    @Expose
    var dfgFirstName : String ?="",

    @SerializedName("dfg_last_name")
    @Expose
    var dfgLastName : String ?="",

    @SerializedName("dfg_phone")
    @Expose
    var dfgPhone : String ?="",

    @SerializedName("failed")
    @Expose
    var failed :Boolean?=false,

    @SerializedName("payment_status")
    @Expose
    var paymentStatus : String ?="",

    @SerializedName("type")
    @Expose
    var type : String ?="",

    @SerializedName("type_id")
    @Expose
    var typeId : Int  ?=0,

    @SerializedName("addresses")
    @Expose
    var addresses : ArrayList<ResponseAddress> = arrayListOf(),

    @SerializedName("shipping_address_building")
    @Expose
    var shipping_address_building : String ?="",
    @SerializedName("shipping_building")
    @Expose
    var shipping_building : String ?="",
    @SerializedName("shipping_address_description")
    @Expose
    var shipping_address_description : String ?="",
    @SerializedName("shipping_description")
    @Expose
    var shipping_description : String ?="",
    @SerializedName("shipping_id")
    @Expose
    var shipping_id : String ?="",
    @SerializedName("shipping_address_floor")
    @Expose
    var shipping_address_floor : String ?="",
    @SerializedName("shipping_address_latitude")
    @Expose
    var shipping_address_latitude : String ?="",
    @SerializedName("shipping_address_longitude")
    @Expose
    var shipping_address_longitude : String ?="",
    @SerializedName("shipping_address_name")
    @Expose
    var shipping_address_name : String ?="",
    @SerializedName("shipping_street")
    @Expose
    var shipping_address_street : String ?="",
    @SerializedName("shipping_floor")
    @Expose
    var shipping_floor  : String ?="",


    @SerializedName("shipping_email")
    @Expose
    var shipping_email : String ?="",
    @SerializedName("shipping_first_name")
    @Expose
    var shipping_first_name : String ?="",
    @SerializedName("shipping_last_name")
    @Expose
    var shipping_last_name : String ?="",

    @SerializedName("shipping_phone")
    @Expose
    var shipping_phone : String ?="",


    @SerializedName("vendor_rate")
    @Expose
    var vendorRate : String ?="",

    @SerializedName("client_rate")
    @Expose
    var clientRate : String ?="",

    @SerializedName("new_delivery_date")
    @Expose
    var newDeliveryDate : String ?="",

    @SerializedName("new_time_slot_to")
    @Expose
    var newTimeSlotTo : String ?="",

    @SerializedName("new_time_slot_from")
    @Expose
    var newTimeSlotFrom : String ?="",

    @SerializedName("time")
    @Expose
    var time : TimeDuration?=null,

    @SerializedName("old_total")
    @Expose
    var oldTotal : String ?="",

    @SerializedName("coupon_code")
    @Expose
    var couponCode : String ?="",

    @SerializedName("discount_amount")
    @Expose
    var discountAmount : String ?="",

    @SerializedName("discount_type")
    @Expose
    var discounType : String ?="",

    @SerializedName("total_discount_amount")
    @Expose
    var totalDiscountAmount : String ?="",

    @SerializedName("types_id")
    @Expose
    var typesId : Int   ?=0,

    @SerializedName("size-capacity_id")
    @Expose
    var sizeCapacityId : Int ?=0,

    @SerializedName("shipping_Apartment")
    @Expose
    var shippingApartment : String ?="",

    @SerializedName("shipping_avenu")
    @Expose
    var shippingAvenu : String ?="",

    var done : Boolean = false,

    @SerializedName("service_reason_id")
    @Expose
    var reasonId : String ?="",

    @SerializedName("service_reason_other")
    @Expose
    var serviceReasonOther : String ?="",

    @SerializedName("service_reason")
    @Expose
    var serviceReason : String ?=""




) {
}