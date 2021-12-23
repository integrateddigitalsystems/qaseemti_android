package com.ids.qasemti.model
import android.net.Uri
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import java.io.File

class FilesSelected(
    var name : String ?="" ,
    var file : File?=null ,
    var multipart : MultipartBody.Part ?=null,
    var id : Int ?=0

) {
}