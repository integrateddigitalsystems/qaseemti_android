package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResonseMainCancel
    (
    @SerializedName("result")
    @Expose
    var result: String? = "",

    @SerializedName("message")
    @Expose
    var message: String? = "",

    @SerializedName("cancellation_reasons")
    @Expose
    var banks: ArrayList<BankItem>
) {
}
