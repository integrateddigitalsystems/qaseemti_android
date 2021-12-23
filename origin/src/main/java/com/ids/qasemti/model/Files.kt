package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Files {
    @SerializedName("proof of ownership File ")
    @Expose
    var proofOfOwnershipFile: String? = null

    @SerializedName("driving licenseFile ")
    @Expose
    var drivingLicenseFile: String? = null
}