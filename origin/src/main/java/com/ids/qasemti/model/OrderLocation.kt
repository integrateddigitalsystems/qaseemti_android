package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class OrderLocation(
    @SerializedName("latest_udpate_time")
    @Expose
    var latest_udpate_time : String ?="",
    @SerializedName("oder_id")
    @Expose
    var oder_id : String ?="",
    @SerializedName("order_laltitude")
    @Expose
    var order_laltitude : String ?="",
    @SerializedName("order_longitude")
    @Expose
    var order_longitude : String ?=""

) {
}