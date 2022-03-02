package com.ids.qasemti.utils

import android.app.Application
import android.content.Context
import android.provider.Settings
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.com.ids.qasemti.model.ResponseDistance
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import kotlinx.android.synthetic.main.layout_home_orders.*
import kotlinx.android.synthetic.main.layout_profile.*
import kotlinx.android.synthetic.main.loading.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CallAPIs {

    companion object {
        lateinit var apiListener: ApiListener
        var retro = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        var retroMap = RetroFitMap.client!!.create(RetrofitInterface::class.java)
        var curr: Call<ArrayList<ResponseNominatim>>? = null
        var mapWorking = false


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
                var lang = MyApplication.languageCode.toRequestBody("text/plain".toMediaTypeOrNull())

                retro.updateClientProfile(
                    user,
                    phone,
                    first,
                    last,
                    email,
                    selectedProfilePic,
                    typeReq,
                    lang


                )?.enqueue(object : Callback<ResponseUser> {
                    override fun onResponse(
                        call: Call<ResponseUser>,
                        response: Response<ResponseUser>
                    ) {
                        try {
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


        fun getDistance(
            from : LatLng,
            to : LatLng,
            con : Application ,
            listener: ApiListener
        ){
            /*var latLng = LatLng(33.872525264390575, 35.49364099233594)*/
            var dest = from.latitude.toString() + ","+from.longitude.toString()
            var origin  = to.latitude.toString() + ","+to.longitude.toString()
            RetroFitMap2.client?.create(RetrofitInterface::class.java)
                ?.getDistance(dest,origin,con.getString(R.string.googleKey))?.enqueue(object : Callback<ResponseDistance> {
                    override fun onResponse(
                        call: Call<ResponseDistance>,
                        response: Response<ResponseDistance>
                    ) {

                        try {
                            listener.onDataRetrieved(
                                true,
                                response.body()!!,
                                AppConstants.DISTANCE_GEO
                            )
                            //nextStep(response.body()!!.result!!)
                        } catch (E: java.lang.Exception) {

                            listener.onDataRetrieved(
                                true,
                                response.body()!!,
                                AppConstants.DISTANCE_GEO
                            )
                        }
                    }

                    override fun onFailure(call: Call<ResponseDistance>, throwable: Throwable) {
                        listener.onDataRetrieved(
                            false,
                            ResponseGeoAddress(),
                            AppConstants.DISTANCE_GEO
                        )
                    }
                })
        }

        fun getAddressName(
            locationLatLng : LatLng ,
            con : Context ,
            listener : ApiListener
        ){
            /*var latLng = LatLng(33.872525264390575, 35.49364099233594)*/
            var latLngStr = locationLatLng.latitude.toString() + ","+locationLatLng.longitude.toString()
            RetroFitMap2.client?.create(RetrofitInterface::class.java)
                ?.getLocationNames(latLngStr,con.getString(R.string.googleKey),true,MyApplication.languageCode)?.enqueue(object : Callback<ResponseGeoAddress> {
                    override fun onResponse(
                        call: Call<ResponseGeoAddress>,
                        response: Response<ResponseGeoAddress>
                    ) {
                        MyApplication.selectedLatLngCall = locationLatLng
                        try {
                            listener.onDataRetrieved(
                                true,
                                response.body()!!,
                                AppConstants.ADDRESS_GEO
                            )
                            //nextStep(response.body()!!.result!!)
                        } catch (E: java.lang.Exception) {

                            listener.onDataRetrieved(
                                true,
                                response.body()!!,
                                AppConstants.ADDRESS_GEO
                            )
                        }
                    }

                    override fun onFailure(call: Call<ResponseGeoAddress>, throwable: Throwable) {
                        listener.onDataRetrieved(
                            false,
                            ResponseGeoAddress(),
                            AppConstants.ADDRESS_GEO
                        )
                    }
                })
        }
        fun updateProfileServiceProvider(
            context: Context,
            listener: ApiListener,
            loading: View,
            lat: Double,
            long: Double,
            gender: String,
            firstName: String,
            middleName: String,
            lastName: String,
            emaill: String,
            mobile: String,
            altNum: String,
            civilIdNb: String,
            date: String,
            Address: String,
            accountNum: String,
            bankName: String,
            branchName: String,
            descrp: String,
            ibann: String,
            selectedFile: MultipartBody.Part,
            selectedProf: MultipartBody.Part,
            selectedFile2:MultipartBody.Part

        ) {
            try {
                loading.show()
            } catch (ex: Exception) {

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
            val iban = ibann.toRequestBody("text/plain".toMediaTypeOrNull())
            var lang = MyApplication.languageCode.toRequestBody("text/plain".toMediaTypeOrNull())
            var x = retro.updateProfile(
                user,
                first,
                middle,
                last,
                email,
                phone,
                alt,
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
                description,
                lang,selectedFile2!!
            )

            retro.updateProfile(
                user,
                first,
                middle,
                last,
                email,
                phone,
                alt,
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
                description,
                lang,
                selectedFile2

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
                    } catch (E: Exception) {
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

        fun getCategories(
            context: Context,
            listener: ApiListener,
        ) {
            retro.getCategories()?.enqueue(object : Callback<ResponseMainCategories> {
                override fun onResponse(
                    call: Call<ResponseMainCategories>,
                    response: Response<ResponseMainCategories>
                ) {
                    try {
                        //var res = response as ResponseMainCategories
                        MyApplication.categories.clear()
                        MyApplication.categories.addAll(response.body()!!.categories)
                        MyApplication.purchaseId =
                            MyApplication.categories.find { it.valEn.equals("purchase") }!!.id!!.toInt()
                        MyApplication.rentalId =
                            MyApplication.categories.find { it.valEn.equals("rental") }!!.id!!.toInt()
                    } catch (ex: Exception) {
                    }
                    try {
                        listener.onDataRetrieved(
                            true,
                            response.body()!!,
                            AppConstants.GET_CATEGORIES
                        )
                    } catch (e: Exception) {
                        try {
                            listener.onDataRetrieved(
                                false,
                                ResponseUserStatus(),
                                AppConstants.GET_CATEGORIES
                            )
                        } catch (ex: Exception) {

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseMainCategories>, throwable: Throwable) {
                    try {
                        listener.onDataRetrieved(
                            false,
                            ResponseUserStatus(),
                            AppConstants.GET_CATEGORIES
                        )
                    } catch (ex: Exception) {

                    }
                }
            })
        }


        fun getUserInfo(
            listener: ApiListener,
        ) {
            var newReq = RequestUpdateLanguage(MyApplication.userId, MyApplication.languageCode)
            retro.getUser(
                newReq
            )?.enqueue(object : Callback<ResponseUser> {
                override fun onResponse(
                    call: Call<ResponseUser>,
                    response: Response<ResponseUser>
                ) {
                    try {
                        MyApplication.selectedUser = response.body()!!.user
                        //MyApplication.generalNotificaiton = MyApplication.selectedUser.notificationType
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

        fun getUserInfo() {
            var newReq = RequestUpdateLanguage(MyApplication.userId, MyApplication.languageCode)
            retro.getUser(
                newReq
            )?.enqueue(object : Callback<ResponseUser> {
                override fun onResponse(
                    call: Call<ResponseUser>,
                    response: Response<ResponseUser>
                ) {
                    try {
                        MyApplication.selectedUser = response.body()!!.user
                        logw("MY_USER_ID",MyApplication.selectedUser!!.userId.toString())
                        //MyApplication.generalNotificaiton = MyApplication.selectedUser.notificationType
                    } catch (e: Exception) {
                        try {
                            logw("excNotf",e.toString())
                        } catch (ex: Exception) {

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseUser>, throwable: Throwable) {
                    try {
                        logw("excNotf",throwable.toString())
                    } catch (ex: Exception) {

                    }
                }
            })
        }

        fun getMapLocations(
            str: String,
            listener: ApiListener,
            loading: View
        ) {


            loading.show()
            if (curr != null)
                curr!!.cancel()
            curr = retroMap!!.getMapLocations(
                str,
                "json",
                MyApplication.countryNameCodes!!,
                MyApplication.languageCode
            )
            curr!!.enqueue(object : Callback<ArrayList<ResponseNominatim>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseNominatim>>,
                    response: Response<ArrayList<ResponseNominatim>>
                ) {
                    try {
                        listener.onDataRetrieved(
                            true,
                            response.body()!!,
                            AppConstants.MAP_SEARCH
                        )

                        loading.hide()
                    } catch (e: Exception) {
                        try {
                            listener.onDataRetrieved(
                                false,
                                arrayListOf<ResponseNominatim>(),
                                AppConstants.MAP_SEARCH
                            )
                        } catch (ex: Exception) {

                        }
                        loading.hide()
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseNominatim>>,
                    throwable: Throwable
                ) {
                    try {
                        listener.onDataRetrieved(
                            false,
                            arrayListOf<ResponseNominatim>(),
                            AppConstants.MAP_SEARCH
                        )
                        if(!throwable.message.equals("Canceled"))
                            loading.hide()
                    } catch (ex: Exception) {

                    }
                }

            })


        }

        fun updateDevice(context: Context){

            val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
            val cal = Calendar.getInstance()

            val model = AppHelper.getDeviceName()
            val osVersion = AppHelper.getAndroidVersion()

            var deviceToken = ""
            val deviceTypeId = ""
            var android_id = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID
            );

            var pre = if(MyApplication.isClient) "cl_" else "sp_"
            val imei =
                pre+Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

            val registrationDate = dateFormat.format(cal.time)
            val appVersion = AppHelper.getVersionNumber()

            val isProduction = 1


            val lang = MyApplication.languageCode
            var isService = 1
            if (MyApplication.isClient)
                isService = 0

            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(
                            "firebase_messaging",
                            "Fetching FCM registration token failed",
                            task.exception
                        )
                        return@OnCompleteListener
                    }
                    deviceToken = task.result


                    var newReq = RequestUpdate(
                        MyApplication.deviceId,
                        "0",
                        model,
                        osVersion,
                        "",
                        2,
                        imei,
                        MyApplication.generalNotificaiton,
                        appVersion.toString(),
                        0,
                        lang,
                        0
                    )

                    if(MyApplication.isClient){
                        newReq.is_client = 1
                    }else{
                        newReq.isServiceProvider = 1
                    }

                    var jsonString = Gson().toJson(newReq)
                    logw("UPDATE_JSON",jsonString)

                    RetrofitClient.client?.create(RetrofitInterface::class.java)
                        ?.updateDevice(
                            newReq
                        )?.enqueue(object : Callback<ResponseUpdate> {
                            override fun onResponse(
                                call: Call<ResponseUpdate>,
                                response: Response<ResponseUpdate>
                            ) {
                                try {

                                    //sendOTP()
                                } catch (E: Exception) {

                                }
                            }

                            override fun onFailure(call: Call<ResponseUpdate>, throwable: Throwable) {

                            }
                        })
                })




        }

        fun getIP(con:Context,listener: ApiListener,ll:LatLng){
            RetroFitIP.client?.create(RetrofitInterface::class.java)
                ?.getIpAddress(
                )?.enqueue(object : Callback<ResponseIP> {
                    override fun onResponse(
                        call: Call<ResponseIP>,
                        response: Response<ResponseIP>
                    ) {
                        if(!response.body()!!.origin.isNullOrEmpty()){
                            updateDevice(con,listener,response.body()!!.origin!!,ll)
                        }else{
                            updateDevice(con,listener,"",ll)
                        }
                    }

                    override fun onFailure(call: Call<ResponseIP>, t: Throwable) {
                        updateDevice(con,listener,"",LatLng(0.0,0.0))
                    }
                }
                )

        }

        fun updateDevice(context: Context,
        listener: ApiListener,ip:String , ll : LatLng){

            val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
            val cal = Calendar.getInstance()

            val model = AppHelper.getDeviceName()
            val osVersion = AppHelper.getAndroidVersion()

            var deviceToken = ""
            val deviceTypeId = ""
            var android_id = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID
            );

            var pre = if(MyApplication.isClient) "cl_" else "sp_"
            val imei =
                pre+Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

            val registrationDate = dateFormat.format(cal.time)
            val appVersion = AppHelper.getVersionNumber()

            val isProduction = 1


            val lang = MyApplication.languageCode
            var isService = 1
            if (MyApplication.isClient)
                isService = 0

            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(
                            "firebase_messaging",
                            "Fetching FCM registration token failed",
                            task.exception
                        )
                        return@OnCompleteListener
                    }
                    deviceToken = task.result


                    var newReq = RequestUpdate(
                        MyApplication.deviceId,
                        MyApplication.selectedPhone,
                        model,
                        osVersion,
                        deviceToken,
                        2,
                        imei,
                        MyApplication.generalNotificaiton,
                        appVersion.toString(),
                        0,
                        lang,
                        MyApplication.userId

                    )

                    if(MyApplication.isClient){
                        newReq.is_client = 1
                    }else{
                        newReq.isServiceProvider = 1
                    }

                    newReq.ipAddress = ip
                    newReq.lat = ll.latitude
                    newReq.long = ll.longitude


                    var jsonString = Gson().toJson(newReq)
                    logw("UPDATE_JSON",jsonString)

                    RetrofitClient.client?.create(RetrofitInterface::class.java)
                        ?.updateDevice(
                            newReq
                        )?.enqueue(object : Callback<ResponseUpdate> {
                            override fun onResponse(
                                call: Call<ResponseUpdate>,
                                response: Response<ResponseUpdate>
                            ) {
                                try {
                                    MyApplication.deviceId = response.body()!!.deviceId!!
                                    listener.onDataRetrieved(
                                        true,
                                        arrayListOf<ResponseNominatim>(),
                                        AppConstants.UPDATE_DEVICE
                                    )
                                    //sendOTP()
                                } catch (E: Exception) {

                                }
                            }

                            override fun onFailure(call: Call<ResponseUpdate>, throwable: Throwable) {

                            }
                        })
                })




        }







        fun getOrderByOrderIdBroad(
            orderId: Int,
            userId : Int ,
            listener: ApiListener,
        ) {

            var doneOnce = 0

            var newReq = RequestServices(MyApplication.userId, MyApplication.languageCode)
            retro.getBroadcastedOrders(newReq)?.enqueue(object : Callback<ResponseMainOrder> {
                    override fun onResponse(
                        call: Call<ResponseMainOrder>,
                        response: Response<ResponseMainOrder>
                    ) {
                        var res = response.body()!!.orders
                        for(item in res){
                            if(item.orderId!!.toInt() == orderId) {
                                doneOnce=1
                                listener.onDataRetrieved(
                                    true,
                                    item,
                                    AppConstants.ORDER_BY_ORDER_ID_BROAD
                                )
                                break
                            }
                        }

                        if(doneOnce == 0 ){
                           getOrderByOrderId(orderId,listener)
                        }
                    }



                    override fun onFailure(call: Call<ResponseMainOrder>, throwable: Throwable) {
                        listener.onDataRetrieved(
                            false,
                            ResponseOrders(),
                            AppConstants.ORDER_BY_ORDER_ID_BROAD
                        )
                    }
            })


        }


        fun getOrderByOrderId(
            orderId: Int,
            listener: ApiListener,
        ) {
            var req = RequestOrderIdL(orderId, MyApplication.languageCode)
            retro.getOrderById(
                req
            ).enqueue(object : Callback<ResponseMainOrderById> {
                override fun onResponse(
                    call: Call<ResponseMainOrderById>,
                    response: Response<ResponseMainOrderById>
                ) {
                    try {
                        listener.onDataRetrieved(
                            true,
                            response.body()!!.order,
                            AppConstants.ORDER_BY_ID
                        )
                    } catch (e: Exception) {
                        try {
                            listener.onDataRetrieved(
                                false,
                                ResponseOrders(),
                                AppConstants.ORDER_BY_ID
                            )
                        } catch (ex: Exception) {

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseMainOrderById>, throwable: Throwable) {
                    try {
                        listener.onDataRetrieved(
                            false,
                            ResponseOrders(),
                            AppConstants.ORDER_BY_ID
                        )
                    } catch (ex: Exception) {

                    }
                }
            })

        }


    }
}