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

    @SerializedName("types")
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

    @SerializedName("address_latitude")
    @Expose
    var addressLatitude: String? = null

    @SerializedName("address_longitude")
    @Expose
    var addressLongitude: String? = null

    @SerializedName("address_street")
    @Expose
    var addressStreet: String? = null

    @SerializedName("address_building")
    @Expose
    var addressBuilding: String? = null

    @SerializedName("address_floor")
    @Expose
    var addressFloor: String? = null

    @SerializedName("address_description")
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

    var title : String ?=""
    var price : String ?=""


    constructor(
        userId: Int?,
        productCategory: String?,
        productId: Int?,
        types: String?,
        sizeCapacity: String?,
        deliveryDate: String?,
        addressName: String?,
        addressLatitude: String?,
        addressLongitude: String?,
        addressStreet: String?,
        addressBuilding: String?,
        addressFloor: String?,
        addressDescription: String?,
        firstName: String?,
        lastName: String?,
        company: String?,
        email: String?,
        phone: String?,
        title : String ?,
        price : String ?
    ) {
        this.userId = userId
        this.productCategory = productCategory
        this.productId = productId
        this.types = types
        this.sizeCapacity = sizeCapacity
        this.deliveryDate = deliveryDate
        this.addressName = addressName
        this.addressLatitude = addressLatitude
        this.addressLongitude = addressLongitude
        this.addressStreet = addressStreet
        this.addressBuilding = addressBuilding
        this.addressFloor = addressFloor
        this.addressDescription = addressDescription
        this.firstName = firstName
        this.lastName = lastName
        this.company = company
        this.email = email
        this.phone = phone
        this.title = title
        this.price = price
    }
}