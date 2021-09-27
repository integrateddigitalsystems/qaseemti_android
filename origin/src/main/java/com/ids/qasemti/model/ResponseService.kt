package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseService(

    @SerializedName("id")
    @Expose
    var id : Int ,

    @SerializedName("type")
    @Expose
    var type : String ?="",

    @SerializedName("regular_price")
    @Expose
    var regularPrice : ArrayList<String> = arrayListOf() ,

    @SerializedName("stock_status")
    @Expose
    var stockStatus : ArrayList<String> = arrayListOf() ,

    @SerializedName("price")
    @Expose
    var price : ArrayList<String> = arrayListOf() ,

    @SerializedName("booking_min")
    @Expose
    var bookingMin : ArrayList<String> = arrayListOf() ,

    @SerializedName("booking_max")
    @Expose
    var bookingMax : ArrayList<String> = arrayListOf() ,

    @SerializedName("views")
    @Expose
    var views : ArrayList<String> = arrayListOf() ,

    @SerializedName("gallery_of_images")
    @Expose
    var gallery : ArrayList<String> = arrayListOf() ,

    @SerializedName("commission")
    @Expose
    var commission : ArrayList<ServiceCommission> = arrayListOf() ,

    @SerializedName("types")
    @Expose
    var types : ArrayList<String> ?= arrayListOf() ,

    @SerializedName("size-capacity")
    @Expose
    var sizeCapacity : ArrayList<String>?= arrayListOf() ,

    @SerializedName("eta")
    @Expose
    var eta : ArrayList<String> = arrayListOf() ,

    @SerializedName("_additional_price_if_distance__10km")
    @Expose
    var additionalPrice10 : ArrayList<String> = arrayListOf() ,

    @SerializedName("_additional_price_if_distance__20km")
    @Expose
    var additionalPrice20 : ArrayList<String> = arrayListOf() ,

    @SerializedName("_additional_price_if_distance__30km")
    @Expose
    var additionalPrice30 : ArrayList<String> = arrayListOf() ,

    @SerializedName("_additional_price_if_distance__40km")
    @Expose
    var additionalPrice40 : ArrayList<String> = arrayListOf() ,

    @SerializedName("_additional_price_if_distance__50km")
    @Expose
    var additionalPrice50 : ArrayList<String> = arrayListOf() ,

    @SerializedName("description")
    @Expose
    var desc : String ?="" ,

    @SerializedName("name")
    @Expose
    var name : String ?="" ,

    @SerializedName("date")
    @Expose
    var date : String ?=""




) {

}
