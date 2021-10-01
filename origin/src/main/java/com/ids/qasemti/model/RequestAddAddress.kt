package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestAddAddress {
    @SerializedName("user_id")
    @Expose
    var userId: Int? = 0

    @SerializedName("latitude")
    @Expose
    var latitude: Int? = 0

    @SerializedName("longitude")
    @Expose
    var longitude: Int? = 0

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

    constructor(
        userId: Int?,
        latitude: Int?,
        longitude: Int?,
        addressId: Int?,
        addressName: String?,
        street: String?,
        building: String?,
        floor: String?,
        description: String?
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
    }
}