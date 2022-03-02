package com.ids.qasemti.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestUpdate(
    @SerializedName("device_id")
    @Expose
    var deviceId : Int ?=0,
    @SerializedName("mobile_number")
    @Expose
    var mobileNumber : String ?="",
    @SerializedName("model")
    @Expose
    var model : String ?="",
    @SerializedName("os_version")
    @Expose
    var osVersion : String ?="",
    @SerializedName("device_token")
    @Expose
    var deviceToken : String ?="",
    @SerializedName("device_type_id")
    @Expose
    var deviceTypeId : Int ?=0,
    @SerializedName("IMEI")
    @Expose
    var imei : String ?="",
    @SerializedName("general_notifications_enables")
    @Expose
    var generalNotificationEnabled : Int ?=0,
    @SerializedName("application_version_number")
    @Expose
    var applicationVersionNumber : String ?="",
    @SerializedName("badge")
    @Expose
    var badge : Int ?=0,
    @SerializedName("language")
    @Expose
    var language : String ?="",
    @SerializedName("user_id")
    var userId : Int ?=0,
    @SerializedName("is_service_provider")
    @JsonIgnore
    var  isServiceProvider : Int ?=null  ,
    @SerializedName("is_client")
    @JsonIgnore
    var is_client : Int ?=null,
    @SerializedName("ip_address")
    @Expose
    var ipAddress : String ?="" ,

    @SerializedName("latitude")
    @Expose
    var lat : Double ?=0.0 ,

    @SerializedName("longitude")
    @Expose
    var long : Double ?=0.0
) {
    fun sameUpdate(req:RequestUpdate):Boolean{
        if(req.deviceId==this.deviceId && req.mobileNumber.equals(this.mobileNumber) && req.model.equals(this.model) && req.osVersion.equals(this.osVersion) && req.deviceToken.equals(this.deviceToken) && req.deviceTypeId == this.deviceTypeId && req.imei.equals(this.imei) && req.generalNotificationEnabled==this.generalNotificationEnabled&& req.applicationVersionNumber.equals(this.applicationVersionNumber) && req.badge!!.equals(this.badge) && req.language!!.equals(this.language) && req.userId==this.userId && req.isServiceProvider == this.isServiceProvider)
            return true
        return false
    }
}