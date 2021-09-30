package com.ids.qasemti.utils


import com.google.protobuf.Api
import com.ids.qasemti.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface RetrofitInterface {

    @POST("update-device")
    fun updateDevice(@Body param: RequestUpdate): Call<ResponseUpdate>

    @POST("notifications")
    fun getNotifications(
        @Body param: RequestNotifications
    ): Call<ArrayList<ResponseNotification>>

    @POST("mark-notification")
    fun markNotification(
        @Body param: MarkNotification
    ): Call<ResponseUpdate>

    @POST("mobile_configuration")
    fun getMobileConfiguration(): Call<ArrayList<ResponseConfiguration>>

    @POST("send_otp")
    fun sendOTP(@Body param: RequestOTP): Call<ResponseUpdate>

    @POST("verify_otp")
    fun verifyOTP(@Body param: RequestVerifyOTP): Call<ResponseVerification>

    @POST("contact_us")
    fun contactUs(@Body param: RequestContactUs): Call<ResponseUpdate>

    @POST("sp_get_orders")
    fun getOrders(@Body param: RequestOrders): Call<ResponseMainOrder>

    @POST("cancel_order")
    fun cancelOrder(@Body param: RequestCancelOrder): Call<ResponseCancel>

    @POST("get_user_status")
    fun getUserStatus(@Body param: RequestUserStatus): Call<ResponseUserStatus>

    @Multipart
    @POST("sp_update_profile")
    fun updateProfile(
        @Part(ApiParameters.USER_ID) userId: RequestBody,
        @Part(ApiParameters.FIRST_NAME) firstName: RequestBody,
        @Part(ApiParameters.MIDDLE_NAME) middlename: RequestBody,
        @Part(ApiParameters.LAST_NAME) lastName: RequestBody,
        @Part(ApiParameters.EMAIL) email: RequestBody,
        @Part(ApiParameters.MOBILE_NUMBER) mobileNumber: RequestBody,
        @Part(ApiParameters.CIVIL_ID) civilid: RequestBody,
        @Part civil_id_attach: MultipartBody.Part,
        @Part(ApiParameters.GENDER) gender: RequestBody,
        @Part(ApiParameters.DATE_OF_BIRTH) dateOfBirth: RequestBody,
        @Part(ApiParameters.ROLE) role: RequestBody,
        @Part file: MultipartBody.Part,
        @Part(ApiParameters.ADDRESS) address: RequestBody,
        @Part(ApiParameters.LATITUDE) latitude: RequestBody,
        @Part(ApiParameters.LONGITUDE) longitude: RequestBody,
        @Part(ApiParameters.ACCOUNT_NUMBER) accNumber: RequestBody,
        @Part(ApiParameters.BANK_NAME) bankName: RequestBody,
        @Part(ApiParameters.BANK_BRANCH) bankBranch: RequestBody,
        @Part(ApiParameters.IBAN) iban: RequestBody
    ): Call<String>

    @POST("sp_update_availability")
    fun updateAvailability(
        @Body param: RequestAvailability
    ): Call<ResponseCancel>

    @POST("update_notification_type")
    fun updateNotification(
        @Body param: RequestNotificationUpdate
    ): Call<ResponseCancel>

    /*@POST("sp_get_ratings")
    fun getRatings(
        @Body
    )*/

    @POST("sp_get_orders_count")
    fun getOrdersCount(
        @Body param:RequestUserStatus
    ): Call<ResponeOrderCount>

    @POST("sp_update_order_custom_status")
    fun updateOrderCustomStatus(
        @Body param:RequestUpdateOrder
    ): Call<ResponseUpdate>

    @POST("sp_get_services")
    fun getServicesProduct(
        @Body param:RequestService
    ): Call<ResponeMainService>
    @POST("sp_get_services")
    fun getServices(
        @Body param:RequestServices
    ): Call<ResponseMainServices>


    @POST("sp_post_ratings")
    fun setRating(
        @Body param:RequestRating
    ): Call<ResponseMessage>



    @Multipart
    @POST("sp_post_services")
    fun addService(
        @Part(ApiParameters.USER_ID) userId: RequestBody,
        @Part(ApiParameters.CATEGORY) category: RequestBody,
        @Part(ApiParameters.PRODUCT_NAME) prod_name: RequestBody,
        @Part(ApiParameters.SIZE_CAPACITY) size_cap: RequestBody,
        @Part(ApiParameters.TYPE) type: RequestBody,
        @Part(ApiParameters.STOCK_STATUS) stock_status: RequestBody,
        @Part(ApiParameters.PRODUCT_DESC) product_desc: RequestBody,
        @Part gallery: ArrayList<MultipartBody.Part>,
        @Part(ApiParameters.LANGUAGE) language: RequestBody,
        @Part(ApiParameters.PRODUCT_ID) product_id: RequestBody

    ): Call<ResponseMessage>
}
