package com.ids.qasemti.utils

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.RequestUpdateLanguage
import com.ids.qasemti.model.RequestUserStatus
import com.ids.qasemti.model.ResponseUser
import com.ids.qasemti.model.ResponseUserStatus
import kotlinx.android.synthetic.main.layout_profile.*
import kotlinx.android.synthetic.main.loading.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class CallAPIs {

    companion object {
        lateinit var apiListener: ApiListener

        fun updateProfileClient(
            context: Context,
            listener: ApiListener,
            loading: View,
            firstName: String,
            lastName: String,
            emaill: String,
            mobile: String,
            selectedProfilePic: MultipartBody.Part
        ) {
            apiListener = listener
            if (MyApplication.isClient) {
                try {
                    loading.show()
                } catch (ex: java.lang.Exception) {

                }

                val user =
                    MyApplication.userId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val first =
                    firstName.toRequestBody("text/plain".toMediaTypeOrNull())
                val last = lastName
                    .toRequestBody("text/plain".toMediaTypeOrNull())
                val email = emaill
                    .toRequestBody("text/plain".toMediaTypeOrNull())
                val phone =
                    mobile.toRequestBody("text/plain".toMediaTypeOrNull())
                var type = "1"
                var typeReq = type.toRequestBody()


                RetrofitClient.client?.create(RetrofitInterface::class.java)
                    ?.updateClientProfile(
                        user,
                        phone,
                        first,
                        last,
                        email,
                        selectedProfilePic,
                        typeReq


                    )?.enqueue(object : Callback<ResponseUser> {
                        override fun onResponse(
                            call: Call<ResponseUser>,
                            response: Response<ResponseUser>
                        ) {
                            try {
                                loading.hide()
                                listener.onDataRetrieved(
                                    true,
                                    response.body()!!,
                                    AppConstants.UPDATE_PROFILE_CLIENT
                                )
                            } catch (E: java.lang.Exception) {
                                listener.onDataRetrieved(
                                    false,
                                    ResponseUser(),
                                    AppConstants.UPDATE_PROFILE_CLIENT
                                )
                            }
                        }

                        override fun onFailure(call: Call<ResponseUser>, throwable: Throwable) {
                            listener.onDataRetrieved(
                                false,
                                ResponseUser(),
                                AppConstants.UPDATE_PROFILE_CLIENT
                            )
                        }
                    })
            }
        }

        fun updateProfileServiceProvider(
            context: Context,
            listener: ApiListener,
            loading: View,
            lat: Double,
            long: Double,
            gender: String,
            firstName: String,
            middleName : String ,
            lastName: String ,
            emaill: String,
            mobile: String,
            altNum : String ,
            civilIdNb : String ,
            date : String ,
            Address : String ,
            accountNum : String ,
            bankName : String ,
            branchName : String ,
            descrp:String,
            ibann : String ,
            selectedFile : MultipartBody.Part ,
            selectedProf: MultipartBody.Part

        ) {
            try {
                loading.show()
            } catch (ex: java.lang.Exception) {

            }
            var userId = MyApplication.userId.toString()
            var rolev = "vendor"
            var latt = lat.toString()
            var longg = long.toString()
            var add = "Sidon, Sidon District, South Governorate, 1600, Lebanon"

            val user = userId.toRequestBody("text/plain".toMediaTypeOrNull())
            val first =
              firstName.toRequestBody("text/plain".toMediaTypeOrNull())
            val middle =
               middleName.toRequestBody("text/plain".toMediaTypeOrNull())
            val last =
                lastName.toRequestBody("text/plain".toMediaTypeOrNull())
            val email =
               emaill.toRequestBody("text/plain".toMediaTypeOrNull())
            val phone =
               mobile.toRequestBody("text/plain".toMediaTypeOrNull())
            val civilId =
                civilIdNb.toRequestBody("text/plain".toMediaTypeOrNull())
            val alt =
                altNum.toRequestBody("text/plain".toMediaTypeOrNull())
            val genderr = gender.toRequestBody("text/plain".toMediaTypeOrNull())
            val dob =
              date.toRequestBody("text/plain".toMediaTypeOrNull())
            val role = rolev.toRequestBody("text/plain".toMediaTypeOrNull())
            val address =
                Address.toRequestBody("text/plain".toMediaTypeOrNull())
            val lat = latt.toRequestBody("text/plain".toMediaTypeOrNull())
            val long = longg.toRequestBody("text/plain".toMediaTypeOrNull())
            val accNum =
                accountNum
                    .toRequestBody("text/plain".toMediaTypeOrNull())
            val bankname =
                bankName.toRequestBody("text/plain".toMediaTypeOrNull())
            val bankBranch =
               branchName.toRequestBody("text/plain".toMediaTypeOrNull())
            val description =
               descrp.toRequestBody("text/plain".toMediaTypeOrNull())
            val iban =ibann.toRequestBody("text/plain".toMediaTypeOrNull())



            RetrofitClient.client?.create(RetrofitInterface::class.java)
                ?.updateProfile(
                    user,
                    first,
                    middle,
                    last,
                    email,
                    phone,
                    alt ,
                    civilId,
                   selectedFile,
                    genderr,
                    dob,
                    role,
                    selectedProf,
                    address,
                    lat,
                    long,
                    accNum,
                    bankname,
                    bankBranch,
                    iban,
                    description

                )?.enqueue(object : Callback<ResponseUser> {
                    override fun onResponse(
                        call: Call<ResponseUser>,
                        response: Response<ResponseUser>
                    ) {
                        try {
                            listener.onDataRetrieved(
                                true,
                                response.body()!!,
                                AppConstants.UPDATE_PROFILE_SERVICE_PROVIDER
                            )
                        } catch (E: java.lang.Exception) {
                            listener.onDataRetrieved(
                                false,
                                ResponseUser(),
                                AppConstants.UPDATE_PROFILE_SERVICE_PROVIDER
                            )
                        }
                    }

                    override fun onFailure(call: Call<ResponseUser>, throwable: Throwable) {
                        listener.onDataRetrieved(
                            true,
                            ResponseUser(),
                            AppConstants.UPDATE_PROFILE_SERVICE_PROVIDER
                        )
                    }
                })

        }

        fun getUserInfo(   context: Context,
                           listener: ApiListener,) {
            var newReq = RequestUpdateLanguage(MyApplication.userId, MyApplication.languageCode)
            RetrofitClient.client?.create(RetrofitInterface::class.java)
                ?.getUser(
                    newReq
                )?.enqueue(object : Callback<ResponseUser> {
                    override fun onResponse(
                        call: Call<ResponseUser>,
                        response: Response<ResponseUser>
                    ) {
                        try {
                            MyApplication.selectedUser = response.body()!!.user
                            listener.onDataRetrieved(
                                true,
                                response.body()!!,
                                AppConstants.API_USER_STATUS
                            )
                        } catch (e: Exception) {
                            try {
                                listener.onDataRetrieved(
                                    false,
                                    ResponseUserStatus(),
                                    AppConstants.API_USER_STATUS
                                )
                            } catch (ex: Exception) {

                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseUser>, throwable: Throwable) {
                        try {
                            listener.onDataRetrieved(
                                false,
                                ResponseUserStatus(),
                                AppConstants.API_USER_STATUS
                            )
                        } catch (ex: Exception) {

                        }
                    }
                })
        }

    }
}