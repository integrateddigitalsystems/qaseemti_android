package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestPlaceOrder {
    @SerializedName("user_id")
    @Expose
    var userId: Int? = null

    @SerializedName("product_category")
    @Expose
    var productCategory: String? = null

    @SerializedName("product_id")
    @Expose
    var productId: Int? = null

    @SerializedName("type")
    @Expose
    var types: String? = null

    @SerializedName("size_capacity")
    @Expose
    var sizeCapacity: String? = null

    @SerializedName("delivery_date")
    @Expose
    var deliveryDate: String? = null

    @SerializedName("address_name")
    @Expose
    var addressName: String? = null

    @SerializedName("address_id")
    @Expose
    var address_id: Int? = null


    @SerializedName("province")
    @Expose
    var province: String? = null


    @SerializedName("area")
    @Expose
    var area: String? = null


    @SerializedName("block")
    @Expose
    var block: String? = null

    @SerializedName("avenu")
    @Expose
    var avenu: String? = null

    @SerializedName("latitude")
    @Expose
    var latitude: String? = null

    @SerializedName("longitude")
    @Expose
    var longitude: String? = null

    @SerializedName("street")
    @Expose
    var street: String? = null


    @SerializedName("apartment")
    @Expose
    var apartment: String? = null


    @SerializedName("building")
    @Expose
    var building: String? = null

    @SerializedName("floor")
    @Expose
    var floor: String? = null

    @SerializedName("description")
    @Expose
    var addressDescription: String? = null

    @SerializedName("first_name")
    @Expose
    var firstName: String? = null

    @SerializedName("last_name")
    @Expose
    var lastName: String? = null

    @SerializedName("company")
    @Expose
    var company: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("coupon")
    @Expose
    var coupon: String? = ""

    @SerializedName("date_from")
    @Expose
    var dateFrom: String? = ""

    @SerializedName("date_to")
    @Expose
    var dateTo: String? = ""

    var title : String ?=""
    var price : String ?=""

    constructor(){

    }


    constructor(
        userId: Int?,
        productCategory: String?,
        productId: Int?,
        types: String?,
        sizeCapacity: String?,
        deliveryDate: String?,
        addressName: String?,
        latitude: String?,
        longitude: String?,
        street: String?,
        building: String?,
        floor: String?,
        addressDescription: String?,
        firstName: String?,
        lastName: String?,
        company: String?,
        email: String?,
        phone: String?,
        title : String ?,
        price : String ?,
        coupon : String ?,
        address_id:Int ,

    ) {
        this.userId = userId
        this.productCategory = productCategory
        this.productId = productId
        this.types = types
        this.sizeCapacity = sizeCapacity
        this.deliveryDate = deliveryDate
        this.addressName = addressName
        this.latitude = latitude
        this.longitude = longitude
        this.street = street
        this.building = building
        this.floor = floor
        this.addressDescription = addressDescription
        this.firstName = firstName
        this.lastName = lastName
        this.company = company
        this.email = email
        this.phone = phone
        this.title = title
        this.price = price
        this.address_id=address_id
        this.coupon=coupon
    }


    constructor(
        userId: Int?,
        productCategory: String?,
        productId: Int?,
        types: String?,
        sizeCapacity: String?,
        deliveryDate: String?,
        addressName: String?,
        latitude: String?,
        longitude: String?,
        street: String?,
        building: String?,
        floor: String?,
        addressDescription: String?,
        firstName: String?,
        lastName: String?,
        company: String?,
        email: String?,
        phone: String?,
        title : String ?,
        price : String ?,
        coupon : String ?,
        address_id:Int ,
        dateFrom : String ?,
        dateTo : String ?
    ) {
        this.userId = userId
        this.productCategory = productCategory
        this.productId = productId
        this.types = types
        this.sizeCapacity = sizeCapacity
        this.deliveryDate = deliveryDate
        this.addressName = addressName
        this.latitude = latitude
        this.longitude = longitude
        this.street = street
        this.building = building
        this.floor = floor
        this.addressDescription = addressDescription
        this.firstName = firstName
        this.lastName = lastName
        this.company = company
        this.email = email
        this.phone = phone
        this.title = title
        this.price = price
        this.address_id=address_id
        this.coupon=coupon
        this.dateFrom = dateFrom
        this.dateTo = dateTo
    }
}