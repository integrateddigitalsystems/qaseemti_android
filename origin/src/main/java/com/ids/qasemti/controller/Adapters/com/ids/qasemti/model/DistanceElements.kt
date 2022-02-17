package com.ids.qasemti.controller.Adapters.com.ids.qasemti.controller.Adapters.com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ids.qasemti.controller.Adapters.com.ids.qasemti.controller.Adapters.com.ids.qasemti.controller.Adapters.com.ids.qasemti.model.DifferenceData

class DistanceElements (

    @SerializedName("elements")
    @Expose
    var elements : ArrayList<DifferenceData>
        ){

}
