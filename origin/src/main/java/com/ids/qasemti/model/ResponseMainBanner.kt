package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseMainBanner(

    @SerializedName("result")
    @Expose
    var result : String ?="" ,

    @SerializedName("banners")
    @Expose
    var banners : ArrayList<ResponseBanner>
) {

}
