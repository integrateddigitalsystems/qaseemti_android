package com.ids.qasemti.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class ResponseNominatim (

    @SerializedName("place_id")
    @Expose
    val placeId: Int? = 0 ,

    @SerializedName("licence")
    @Expose
    val licence: String? = "" ,

    @SerializedName("osm_type")
    @Expose
    val osmType: String? = "" ,

    @SerializedName("osm_id")
    @Expose
    val osmId: String? = "" ,

    @SerializedName("boundingbox")
    @Expose
    val boundingbox: ArrayList<String>? = arrayListOf() ,

    @SerializedName("lat")
    @Expose
     val lat: String? = "" ,

    @SerializedName("lon")
    @Expose
     val lon: String? = "" ,

    @SerializedName("display_name")
    @Expose
     val displayName: String? = "" ,

    @SerializedName("class")
    @Expose
     val _class: String? = "" ,

    @SerializedName("type")
    @Expose
     val type: String? = ""  ,

    @SerializedName("importance")
    @Expose
     val importance: Double? = 0.0 ,

    @SerializedName("icon")
    @Expose
     val icon: String? = "" ,

    @SerializedName("address")
    @Expose
     val address: Address? = null ,

    @SerializedName("geojson")
    @Expose
     val geojson: Geojson? = null

){
}