package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseSettlement (

    @SerializedName("request_id")
    @Expose
    var reqId : String ?="" ,

    @SerializedName("withdraw_request_id")
    @Expose
    var withdrawReqId : String ?="" ,

    @SerializedName("reverse_withdraw_request-id")
    @Expose
    var withdrawReverseId : String ?="" ,

    @SerializedName("settlement_request_date")
    @Expose
    var settlementReqDate : String ?="" ,

    @SerializedName("status")
    @Expose
    var status : String ?="" ,

    @SerializedName("status_change_date")
    @Expose
    var statusChangeDate : String ?="" ,

    @SerializedName("timestamp")
    @Expose
    var timeStamp : String ?="" ,

    @SerializedName("total_amount")
    @Expose
    var total_amount : String ?="" ,

    @SerializedName("vendor_id")
    @Expose
    var vendorId : String ?="" ,

    @SerializedName("earnings")
    @Expose
    var totalEarnings : String ?="" ,

    @SerializedName("fees")
    @Expose
    var fees : String ?="" ,

    @SerializedName("related_orders")
    @Expose
    var relatedOrders : ArrayList<RelatedOrder>
        ){

}
