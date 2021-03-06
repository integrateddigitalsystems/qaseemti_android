package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestAddAddress {
    @SerializedName("user_id")
    @Expose
    var userId: Int? = 0

    @SerializedName("latitude")
    @Expose
    var latitude: Double? = 0.0

    @SerializedName("longitude")
    @Expose
    var longitude: Double? = 0.0

    @SerializedName("address_id")
    @Expose
    var addressId: Int? = 0

    @SerializedName("address_name")
    @Expose
    var addressName: String? = ""

    @SerializedName("street")
    @Expose
    var street: String? = ""

    @SerializedName("building")
    @Expose
    var building: String? = ""

    @SerializedName("floor")
    @Expose
    var floor: String? = ""

    @SerializedName("description")
    @Expose
    var description: String? = ""

    @SerializedName("city")
    @Expose
    var city : String ?=""

    @SerializedName("province")
    @Expose
    var province : String ?=""

    @SerializedName("area")
    @Expose
    var area:String ?=""

    @SerializedName("block")
    @Expose
    var block :String ?=""

    @SerializedName("avenu")
    @Expose
    var avenue : String ?=""

    constructor(
        userId: Int?,
        latitude: Double?,
        longitude: Double?,
        addressId: Int?,
        addressName: String?,
        street: String?,
        building: String?,
        floor: String?,
        description: String?,
        city:String?,
        province:String?,
        area:String?,
        block:String?
    ) {
        this.userId = userId
        this.latitude = latitude
        this.longitude = longitude
        this.addressId = addressId
        this.addressName = addressName
        this.street = street
        this.building = building
        this.floor = floor
        this.description = description
        this.city = city
        this.province = province
        this.area = area
        this.block = block
        this.avenue = avenue
    }
}