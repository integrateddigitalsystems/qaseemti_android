package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.type.LatLng

class AddressGeometry(

   @SerializedName("bounds")
   @Expose
   var bounds : NSLatLng ?=null ,

   @SerializedName("location")
   @Expose
   var location : LatLng ?=null ,

   @SerializedName("location_type")
   @Expose
   var locationType : String ?="" ,

   @SerializedName("viewport")
   @Expose
   var viewport : NSLatLng ?=null ,

   @SerializedName("place_id")
   @Expose
   var placeId : String ?=""




) {
}