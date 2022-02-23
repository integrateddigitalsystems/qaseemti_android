package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ids.qasemti.controller.Adapters.com.ids.qasemti.model.PaymentData
import com.ids.qasemti.controller.Adapters.com.ids.qasemti.model.ServiceAvailableDateTime

class ResponseService(

    @SerializedName("id")
    @Expose
    var id : String ?="" ,

    @SerializedName("type_id")
    @Expose
    var typeId : Int ?=0 ,

    @SerializedName("parent_id")
    @Expose
    var parentId : Int ?=0 ,

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

    @SerializedName("gallery")
    @Expose
    var gallery : ArrayList<GalleryItem> = arrayListOf() ,


    @SerializedName("gallery_of_images")
    @Expose
    var gallery_of_images : ArrayList<String> = arrayListOf() ,

    @SerializedName("files")
    @Expose
    var files : ArrayList<ServiceFiles> = arrayListOf() ,

    @SerializedName("featured_image")
    @Expose
    var featuredImage : String ?="" ,

    @SerializedName("available_payment_methods")
    @Expose
    var availablePaymentMethods : ArrayList<PaymentData> = arrayListOf() ,

    @SerializedName("available_date_time")
    @Expose
    var availableDates : ArrayList<ServiceAvailableDateTime> = arrayListOf(),

    @SerializedName("service_reason")
    @Expose
    var serviceReasons : ArrayList<RequiredFiles> = arrayListOf()




) {

}
