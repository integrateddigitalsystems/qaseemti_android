package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OrderProduct(

    @SerializedName("booking_max")
    @Expose
    var bookingMax: String? = "",

    @SerializedName("booking_end_date")
    @Expose
    var booking_end_date: String? = "",

    @SerializedName("booking_start_date")
    @Expose
    var booking_start_date: String? = "",

    @SerializedName("booking_min")
    @Expose
    var bookingMin: ArrayList<String>? = arrayListOf(),

    @SerializedName("date")
    @Expose
    var date: String? = "",

    @SerializedName("description")
    @Expose
    var desc: String? = "",

    @SerializedName("end_date")
    @Expose
    var endDate: String? = "",


    @SerializedName("id")
    @Expose
    var id: Int? = 0,

    @SerializedName("line_total")
    @Expose
    var lineTotal: String? = "",

    @SerializedName("name")
    @Expose
    var name: String? = "",

    @SerializedName("price")
    @Expose
    var price: ArrayList<String> = arrayListOf(),

    @SerializedName("regular_price")
    @Expose
    var regularPrice: ArrayList<String> = arrayListOf(),

    @SerializedName("product_id")
    @Expose
    var productId: String? = "",

    @SerializedName("qty")
    @Expose
    var qty: String? = "",

    @SerializedName("sizes")
    @Expose
    var sizes: ArrayList<String> = arrayListOf(),

    @SerializedName("start_date")
    @Expose
    var startDate: String? = "",

    @SerializedName("stock_status")
    @Expose
    var stockStatus: ArrayList<Any> = arrayListOf(),

    @SerializedName("type")
    @Expose
    var type: String? = "",

    @SerializedName("types")
    @Expose
    var types: String = "",

    @SerializedName("variation_id")
    @Expose
    var variationId: String? = "",

    @SerializedName("vendor_id")
    @Expose
    var vendorId: String = "",

    @SerializedName("views")
    @Expose
    var views: ArrayList<String> = arrayListOf(),

    @SerializedName("commission")
    @Expose
    var commission: ArrayList<OrderCommission> = arrayListOf(),

    @SerializedName("gallery_of_images")
    @Expose
    var gallery: ArrayList<Any> = arrayListOf(),

    @SerializedName("size-capacity")
    @Expose
    var sizeCapacity: String? = "",

    @SerializedName("size-capacity_id")
    @Expose
    var sizeCapacityId: String? = "",

    @SerializedName("types_id")
    @Expose
    var typesId: String? = ""
) {
}