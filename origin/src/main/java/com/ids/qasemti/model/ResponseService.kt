package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseService(

    @SerializedName("id")
    @Expose
    var id : String ?="" ,

    @SerializedName("type")
    @Expose
    var type : String ?="",

    @SerializedName("booking_min")
    @Expose
    var bookingMin : String ?="" ,

    @SerializedName("booking_max")
    @Expose
    var bookingMax : String ?="" ,

    /*@SerializedName("gallery_of_images")
    @Expose
    var gallery : ArrayList<String> = arrayListOf() ,*/


    @SerializedName("eta")
    @Expose
    var eta : String ?="" ,

    @SerializedName("_additional_price_if_distance__10km")
    @Expose
    var additionalPrice10 : String ?="" ,

    @SerializedName("_additional_price_if_distance__20km")
    @Expose
    var additionalPrice20 : String ?="" ,

    @SerializedName("_additional_price_if_distance__30km")
    @Expose
    var additionalPrice30 : String ?="" ,

    @SerializedName("_additional_price_if_distance__40km")
    @Expose
    var additionalPrice40 : String ?="" ,

    @SerializedName("_additional_price_if_distance__50km")
    @Expose
    var additionalPrice50 : String ?="" ,

    @SerializedName("description")
    @Expose
    var desc : String ?="" ,

    @SerializedName("name")
    @Expose
    var name : String ?="" ,

    @SerializedName("variations")
    @Expose
    var variations : ArrayList<ServiceVariation> = arrayListOf() ,

    @SerializedName("featured_image")
    @Expose
    var featuredImage : String ?=""




) {

}
