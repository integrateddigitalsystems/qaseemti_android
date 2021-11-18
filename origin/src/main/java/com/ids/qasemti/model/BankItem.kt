package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BankItem(

    @SerializedName("id")
    @Expose
    var id : String ?="" ,

    @SerializedName("code")
    @Expose
    var code : String ?="" ,

    @SerializedName("value")
    @Expose
    var value : String ?=""
) {

}
