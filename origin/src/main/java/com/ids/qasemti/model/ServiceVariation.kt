package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ServiceVariation(

    @SerializedName("earnings")
    @Expose
    var earnings : String ?="" ,

    @SerializedName("fees")
    @Expose
    var fees : String ?=""  ,

    @SerializedName("images")
    @Expose
    var images : ArrayList<String> = arrayListOf() ,

    @SerializedName("is_in_stock")
    @Expose
    var isInStock : Boolean ?= true ,

    @SerializedName("price")
    @Expose
    var price  : String ?=""  ,

    @SerializedName("size-capacity")
    @Expose
    var sizeCapacity : String ?="" ,

    @SerializedName("stock_quantity")
    @Expose
    var stockQuantity  : String ?=""  ,

    @SerializedName("types")
    @Expose
    var types : String ?=""

 ) {

}
