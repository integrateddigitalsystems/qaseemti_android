package com.ids.sampleapp.model

import com.google.gson.annotations.SerializedName
class ItemSpinner {
    var id: Int? = null
    var name: String? = null
    var icon: String? = null
    var selected:Boolean=false


    constructor(id: Int?, name: String?,selected:Boolean) {
        this.name = name
        this.id = id
        this.selected=selected
    }

    constructor(id: Int?, name: String?, icon: String?) {
        this.id = id
        this.name = name
        this.icon = icon
    }


}