package com.ids.qasemti.controller.Adapters.com.ids.qasemti.controller.Adapters.com.ids.qasemti.controller.Adapters.com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ids.qasemti.controller.Adapters.com.ids.qasemti.controller.Adapters.com.ids.qasemti.controller.Adapters.com.ids.qasemti.controller.Adapters.com.ids.qasemti.model.DifferenceDetails

class DifferenceData (

    @SerializedName("distance")
    @Expose
    var distance : DifferenceDetails,

    @SerializedName("duration")
    @Expose
    var duration : DifferenceDetails,

    @SerializedName("status")
    @Expose
    var status : String ?=""



    ){

}
