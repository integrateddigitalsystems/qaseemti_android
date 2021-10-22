package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WebItem (

   @SerializedName("urlEn")
   @Expose
   var urlEn : String ?="" ,

   @SerializedName("urlAr")
   @Expose
   var urlAr : String ?="" ,

   @SerializedName("id")
   @Expose
   var id : String ?="" ,

   @SerializedName("idNo")
   @Expose
   var idNo : Int ?=0,

){

}


