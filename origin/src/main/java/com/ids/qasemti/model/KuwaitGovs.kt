package com.ids.qasemti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class KuwaitGovs(
@SerializedName("listOfGovs")
@Expose
var list : ArrayList<ResponseGovernant> = arrayListOf()
) {
}