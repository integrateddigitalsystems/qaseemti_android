package com.ids.qasemti.model

import com.google.android.libraries.places.api.model.AddressComponent
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AddressResult(

    @SerializedName("address_components")
    @Expose
    var addressComponents: ArrayList<ResponseAddressComponent> = arrayListOf(),

    @SerializedName("formatted_address")
    @Expose
    var formattedAddress: String,


    @SerializedName("geometry")
    @Expose
    var geometry: AddressGeometry? = null,
    @SerializedName("place_id")
    @Expose
    var placeId: String? = "",
    @SerializedName("plus_code")
    @Expose
    var plusCode: AddressPlusCode? = null,
    @SerializedName("types")
    @Expose
   var  types : ArrayList<String> = arrayListOf()

) {
}