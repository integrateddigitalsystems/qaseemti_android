package com.ids.qasemti.model

import com.google.gson.annotations.SerializedName

class CountryArray {
    @SerializedName("countries")
    var countries: ArrayList<CountryCodes>? = arrayListOf()
}