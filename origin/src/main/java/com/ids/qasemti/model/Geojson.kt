package com.ids.qasemti.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Geojson (

        @SerializedName("type")
        @Expose
        var type: String? = "" ,

        @SerializedName("coordinates")
        @Expose
        var coordinates: ArrayList<ArrayList<ArrayList<Double>>>? = ArrayList<ArrayList<ArrayList<Double>>>()
)

{
}
