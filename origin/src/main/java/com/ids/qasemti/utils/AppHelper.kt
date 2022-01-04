package com.ids.qasemti.utils


import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Outline
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.Editable
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import me.grantland.widget.AutofitHelper
import okhttp3.internal.and
import org.apache.commons.codec.binary.Hex
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import androidx.core.content.ContextCompat.getSystemService
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.layout_profile.*
import kotlinx.android.synthetic.main.loading.*


/**
 * Created by Ibrahim on 8/23/2017.
 */

class AppHelper {


    fun setRoundImageResize(context: Context, img: ImageView, ImgUrl: String, isLocal: Boolean) {
        Log.wtf("image_rounded_1", ImgUrl)
        if (isLocal) {
            Glide.with(context).asBitmap().load(File(ImgUrl)).centerCrop()
                .dontAnimate()
                .into(object : BitmapImageViewTarget(img) {
                    override fun setResource(resource: Bitmap?) {

                        val circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.resources, resource)
                        circularBitmapDrawable.isCircular = true
                        img.setImageDrawable(circularBitmapDrawable)
                    }
                })


        } else {
            Glide.with(context).asBitmap().load(ImgUrl).centerCrop().dontAnimate().override(
                500,
                500
            )
                .into(object : BitmapImageViewTarget(img) {
                    override fun setResource(resource: Bitmap?) {
                        val circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.resources, resource)
                        circularBitmapDrawable.isCircular = true
                        img.setImageDrawable(circularBitmapDrawable)
                    }
                })


        }
    }


    fun setbackgroundImage(context: Context, view: View, ImgUrl: String) {

        Glide.with(context)
            .asBitmap()
            .load(ImgUrl)

            .diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    view.background = BitmapDrawable(context.resources, resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }


    companion object {

        var fragmentAvailable: Int? = null
        private const val TAG = "MainActivity"
        private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

        fun getTypeFace(context: Context): Typeface {
            return if (Locale.getDefault().language == "ar")
                Typeface.createFromAsset(
                    context.applicationContext.assets,
                    "fonts/DroidKufiRegular.ttf"
                )//"fonts/NeoTech-Medium.otf"
            else
                Typeface.createFromAsset(
                    context.applicationContext.assets,
                    "fonts/Raleway-Regular.ttf"
                )//"fonts/NeoTech-Medium.otf"

            // return Typeface.DEFAULT

        }

        fun getAndroidVersion(): String {

            val release = Build.VERSION.RELEASE
            val sdkVersion = Build.VERSION.SDK_INT
            return "Android:$sdkVersion ($release)"
        }


/*        fun updateDevice(context: Context,userId:String){
            val imei = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
          //  val imei ="11111122222"
            val osVersion = AppHelper.getAndroidVersion()


            Log.wtf("json_request",Gson().toJson(request))

            RetrofitClient.client?.create(RetrofitInterface::class.java)
                ?.updateDevice(
                    request
                )
                ?.enqueue(object : Callback<ResponseResult> {


                    override fun onResponse(
                        call: Call<ResponseResult>,
                        response: Response<ResponseResult>
                    ) {
                   Log.wtf("response_update",response.body()!!.result.toString()+".....")
                    }

                    override fun onFailure(call: Call<ResponseResult>, t: Throwable) {
                        Log.wtf("response_update", "$t.....")
                    }

                })
        }*/


        fun startService(act: Activity) {

            if (ActivityCompat.checkSelfPermission(
                    act,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    act,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    act,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 111
                )
                //requestForegroundPermissions(act)
                return
            }
            MyApplication.saveLocationTracking = true
            val intent = Intent(act, CurrentLocationService::class.java)
            act.startService(intent)

        }

        fun stopService(act: Context) {
            val intent = Intent(act, CurrentLocationService::class.java)
            act.stopService(intent)
        }

        fun getTypeFaceItalic(context: Context): Typeface {
            return if (Locale.getDefault().language == "ar")
                Typeface.createFromAsset(
                    context.applicationContext.assets,
                    "fonts/DroidKufi-Bold.ttf"
                )//fonts/NeoTech-Bold.otf

            else
                Typeface.createFromAsset(
                    context.applicationContext.assets,
                    "fonts/Raleway-Italic-VariableFont_wght.ttf"
                )//fonts/NeoTech-Bold.otf

            // return Typeface.DEFAULT_BOLD
        }

        fun getTypeFaceBold(context: Context): Typeface {
            return if (Locale.getDefault().language == "ar")
                Typeface.createFromAsset(
                    context.applicationContext.assets,
                    "fonts/DroidKufi-Bold.ttf"
                )//fonts/NeoTech-Bold.otf

            else
                Typeface.createFromAsset(
                    context.applicationContext.assets,
                    "fonts/Raleway-Bold.ttf"
                )//fonts/NeoTech-Bold.otf

            // return Typeface.DEFAULT_BOLD
        }


        fun setAllTexts(v: View, context: Context) {
            if (MyApplication.localizeArray != null) {
                try {
                    if (v is ViewGroup) {
                        val vg = v as ViewGroup
                        for (i in 0 until vg.childCount) {
                            val child = vg.getChildAt(i)
                            // recursively call this method
                            setAllTexts(child, context)
                        }
                    } else if (v is TextView && v !is EditText) {
                        v.textRemote(v.tag.toString(), context)
                    } else if (v is Button) {
                        v.textRemote(v.tag.toString(), context)
                    } else if (v is EditText) {
                        setHintTag(v, v.tag.toString(), context)
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun isOnline(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val nw      = connectivityManager.activeNetwork ?: return false
                val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
                return when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    //for other device how are able to connect with Ethernet
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    //for check internet over Bluetooth
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                    else -> false
                }
            } else {
                return connectivityManager.activeNetworkInfo?.isConnected ?: false
            }
        }
        fun updateStatus(orderId: Int, onTrack: Boolean, delivered: Boolean, paid: Boolean,rel : ReloadData,loading : LinearLayout) {
            loading.show()
            var newReq = RequestUpdateOrder(orderId, onTrack, delivered, paid)
            RetrofitClient.client?.create(RetrofitInterface::class.java)
                ?.updateOrderCustomStatus(newReq)?.enqueue(object : Callback<ResponseUpdate> {
                    override fun onResponse(
                        call: Call<ResponseUpdate>,
                        response: Response<ResponseUpdate>
                    ) {
                        try {
                            rel.reload()
                        } catch (E: java.lang.Exception) {
                            Log.wtf("Exp", E.toString())
                        }
                    }

                    override fun onFailure(call: Call<ResponseUpdate>, throwable: Throwable) {
                        Log.wtf("Exp", throwable.toString())
                    }
                })
        }


        fun setHintTag(view: View, tag: String, context: Context) {
            var edit = view as EditText
            if (MyApplication.localizeArray != null) {
                try {
                    edit.hint =
                        MyApplication.localizeArray!!.messages!!.find { it.localize_Key == tag }!!
                            .getMessage()
                } catch (e: Exception) {
                    try {
                        val resId =
                            context.resources.getIdentifier(tag, "string", context.packageName)
                        edit.hint = context.resources.getString(resId)
                    } catch (e: Exception) {
                    }
                }

            }
        }


        fun getIdFromUserId(Id: Int) {


        }

        fun clearTabs(tablayout: TabLayout, context: Context) {
            try {
                //repeat(5) { tablayout.addTab(tablayout.newTab()) }
                val tabStrip = tablayout.getChildAt(0) as LinearLayout
                for (i in 0 until tabStrip.childCount) {
                    tabStrip.getChildAt(i).setOnTouchListener { v, _ -> true }
                    val tab = tabStrip.getChildAt(i)
                    val layoutParams = tab.layoutParams as LinearLayout.LayoutParams
                    layoutParams.marginEnd = 8.toPx()
                    layoutParams.marginStart = 8.toPx()
                    layoutParams.width = 12.toPx()
                    tab.layoutParams = layoutParams
                    tablayout.requestLayout()
                }
            }catch (ex:Exception){
                logw("frame_error",ex.toString())

            }
        }

        fun setTabs(tablayout: TabLayout, context: Context) {
            try {
                repeat(5) { tablayout.addTab(tablayout.newTab()) }
                val tabStrip = tablayout.getChildAt(0) as LinearLayout
                for (i in 0 until tabStrip.childCount) {
                    tabStrip.getChildAt(i).setOnTouchListener { v, _ -> true }
                    val tab = tabStrip.getChildAt(i)
                    val layoutParams = tab.layoutParams as LinearLayout.LayoutParams
                    layoutParams.marginEnd = 8.toPx()
                    layoutParams.marginStart = 8.toPx()
                    layoutParams.width = 12.toPx()
                    tab.layoutParams = layoutParams
                    tablayout.requestLayout()
                    /*val v: View = LayoutInflater.from(context).inflate(R.layout.footer_top, null)
                v.layoutParams =
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                tablayout.getTabAt(i)!!.setCustomView(v)*/
                }
            }catch (ex:Exception){
                logw("frame_error",ex.toString())

            }
        }


        fun appDead(context: Context): Boolean {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val runningAppProcesses = activityManager.runningAppProcesses ?: return false
            return runningAppProcesses.any { it.processName == context.packageName && it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE }
        }


        fun resetIcons(context: Context, vararg images: ImageView?) {
            try {
                for (element in images) {
                    element!!.setPadding(0, 0, 0, 0)
                    element!!.layoutParams = LinearLayout.LayoutParams(
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            30f,
                            context.resources.displayMetrics
                        ).toInt(),
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            20f,
                            context.resources.displayMetrics
                        ).toInt()
                    )
                }

            }catch (ex:Exception){
                logw("frame_error",ex.toString())

            }
        }

        fun getVersionNumber(): Int {

            val pInfo: PackageInfo
            var version = -1
            try {
                pInfo = MyApplication.instance.packageManager
                    .getPackageInfo(MyApplication.instance.packageName, 0)
                version = pInfo.versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

            return version
        }

        fun formatDate(date: Date, toFormat: String): String {
            var sdf2 = SimpleDateFormat(toFormat, Locale.US)

            return sdf2.format(date)
        }

        fun formatDate(date: String, format: String, toFormat: String): String {

            var sdf =
                SimpleDateFormat(format, Locale.ENGLISH)
            var date = sdf.parse(date)
            var sdf2 = SimpleDateFormat(toFormat, Locale.US)

            return sdf2.format(date)
        }

   /*     fun getArrayCarts() {

            var array: ArrayList<RequestPlaceOrder> = arrayListOf()
            for (item in MyApplication.arrayCart.indices) {
                val getrow: Any = MyApplication.arrayCart.get(item)
                val t: LinkedTreeMap<Any, Any> = getrow as LinkedTreeMap<Any, Any>

                var userId = 0.0
                var x = t["user_id"].toString()
                try {
                    userId = t["user_id"] as Double
                } catch (ex: Exception) {
                }
                var prodId = 0.0
                try {
                    prodId = t["product_id"] as Double
                } catch (ex: Exception) {
                }
                array.add(
                    RequestPlaceOrder(
                        userId.toInt(),
                        t["product_categroy"].toString(),
                        prodId.toInt(),
                        t["types"].toString(),
                        t["size_capacity"].toString(),
                        t["delivery_date"].toString(),
                        t["address_name"].toString(),
                        t["address_latitude"].toString(),
                        t["address_longitude"].toString(),
                        t["address_street"].toString(),
                        t["address_building"].toString(),
                        t["address_floor"].toString(),
                        t["address_description"].toString(),
                        t["first_name"].toString(),
                        t["last_name"].toString(),
                        t["company"].toString(),
                        t["email"].toString(),
                        t["phone"].toString(),
                        t["title"].toString(),
                        t["price"].toString()
                    )
                )
            }


            var x = array
            MyApplication.arrayCart.clear()
            MyApplication.arrayCart.addAll(array)
        }*/

        fun updateDevice(context: Context, phone: String) {

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

            val imei =
                Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

            val registrationDate = dateFormat.format(cal.time)
            val appVersion = getVersionNumber()

            val generalNotification = 1
            val isProduction = 1


            val lang = MyApplication.languageCode
            var isService = 1
            if (MyApplication.isClient)
                isService = 0

                    FirebaseMessaging.getInstance().token.addOnCompleteListener(
                OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("firebase_messaging", "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }
                    deviceToken = task.result

                Log.wtf("myToken",deviceToken)

            var newReq = RequestUpdate(
                MyApplication.deviceId,
                phone,
                model,
                osVersion,
                deviceToken,
                2,
                imei,
                generalNotification,
                appVersion.toString(),
                0,
                lang,
                MyApplication.userId,
                isService
            )

            logw("aaaaa",Gson().toJson(newReq))


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
                        } catch (E: java.lang.Exception) {
                        }
                    }

                    override fun onFailure(call: Call<ResponseUpdate>, throwable: Throwable) {
                    }
                })


                })





        }

        fun setImageHeight(context: Context, icon: ImageView) {
            try {
                icon.layoutParams = LinearLayout.LayoutParams(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        36f,
                        context.resources.displayMetrics
                    ).toInt(),
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        36f,
                        context.resources.displayMetrics
                    ).toInt()
                )
            }catch (ex:Exception){
                logw("frame_error",ex.toString())

            }
        }


        fun handleCrashes(context: Activity) {
            //   if (!MyApplication.isDebug)
            Thread.setDefaultUncaughtExceptionHandler(MyExceptionHandler(context))
        }


        fun getDrawable(context: Context, id: Int): Drawable {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return context.getDrawable(id)!!
            } else {
                return context.resources.getDrawable(id)
            }
        }

        private fun getStringResourceByName(aString: String, context: Context): String? {
            val packageName: String = context.getPackageName()
            val resId: Int = context.getResources().getIdentifier(aString, aString, packageName)
            return context.getString(resId)
        }


        fun getSha256Hash(password: String): String? {
            return try {
                var digest: MessageDigest? = null
                try {
                    digest = MessageDigest.getInstance("SHA-256")
                } catch (e1: NoSuchAlgorithmException) {
                    e1.printStackTrace()
                }
                digest!!.reset()
                bin2hex(digest.digest(password.toByteArray()))
            } catch (ignored: java.lang.Exception) {
                null
            }
        }

        private fun bin2hex(data: ByteArray): String? {
            val hex = StringBuilder(data.size * 2)
            for (b in data) hex.append(String.format("%02x", b and 0xFF))
            return hex.toString()
        }
        fun formatNumber(num: Double, format: String): String {
            try {
                val formatter = DecimalFormat(format, setInEnglish())
                return formatter.format(num)
            } catch (ex: Exception) {

                return ""
            }
        }




        fun formatDate(
            c: Context,
            dateString: String,
            oldDateFormat: String,
            newDateFormat: String
        ): String? {
            var format = SimpleDateFormat(oldDateFormat, Locale.US)
            val newDate = format.parse(dateString)
            format = SimpleDateFormat(newDateFormat, Locale.US)
            val date = format.format(newDate)
            return date
        }

        fun setTextColor(context: Context, view: TextView, color: Int) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.setTextColor(ContextCompat.getColor(context, color))
            } else {
                view.setTextColor(context.resources.getColor(color))
            }
        }

        fun shareAppIntent(activity: Activity){
            val appPackageName: String = activity.packageName
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out the App at: https://play.google.com/store/apps/details?id=$appPackageName"
            )
            sendIntent.type = "text/plain"
            activity.startActivity(sendIntent)
        }

        fun imageLoading(view:ImageView,loading:LinearLayout , url : String , context: Context){

            loading.show()
            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.color.gray_medium_2)
                .error(R.color.gray_medium_2)
            loading.show()
            Glide.with(context).load(url).apply(options)
                .listener(object : RequestListener<Drawable?> {
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        dataSource: com.bumptech.glide.load.DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        loading.hide()
                        return true
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        isFirstResource: Boolean
                    ): Boolean {

                        return false

                    }

                })
                .into(view)
        }


        fun setTextColor(context: Context, view: EditText, color: Int) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.setTextColor(ContextCompat.getColor(context, color))
            } else {
                view.setTextColor(context.resources.getColor(color))
            }
        }


        fun setUpFooter(context: Activity, selected: String) {
            var imgPro = context.findViewById<ImageView>(R.id.ivProductFooter)
            var tvPro = context.findViewById<TextView>(R.id.tvProductFooter)
            var imgOrd = context.findViewById<ImageView>(R.id.ivFooterOrder)
            var tvOrd = context.findViewById<TextView>(R.id.tvFooterOrder)
            var imgHom = context.findViewById<ImageView>(R.id.ivFooterHome)
            var tvHom = context.findViewById<TextView>(R.id.tvFooterHome)
            var imgNot = context.findViewById<ImageView>(R.id.ivFooterNotifications)
            var tvNot = context.findViewById<TextView>(R.id.tvFooterNotifications)
            var imgAcc = context.findViewById<ImageView>(R.id.ivFooterAccount)
            var tvAcc = context.findViewById<TextView>(R.id.tvFooterAccount)
            var imgCart = context.findViewById<ImageView>(R.id.ivCartFooter)
            var tvCart = context.findViewById<TextView>(R.id.tvCartFooter)

            setLogoTint(imgPro, context, R.color.gray_font)
            setLogoTint(imgHom, context, R.color.gray_font)
            setLogoTint(imgNot, context, R.color.gray_font)
            setLogoTint(imgOrd, context, R.color.gray_font)
            setLogoTint(imgAcc, context, R.color.gray_font)
            setLogoTint(imgCart, context, R.color.gray_font)
            setTextColor(context, tvPro, R.color.gray_font)
            setTextColor(context, tvOrd, R.color.gray_font)
            setTextColor(context, tvHom, R.color.gray_font)
            setTextColor(context, tvNot, R.color.gray_font)
            setTextColor(context, tvAcc, R.color.gray_font)
            setTextColor(context, tvCart, R.color.gray_font)


            when (selected) {
                AppConstants.FRAGMENT_ACCOUNT -> {
                    MyApplication.selectedTitle = getRemoteString("Account", context)
                    setLogoTint(imgAcc, context, R.color.primary)
                    setTextColor(context, tvAcc, R.color.primary)
                }
                AppConstants.FRAGMENT_HOME_CLIENT, AppConstants.FRAGMENT_HOME_SP , AppConstants.FRAGMENT_SERVICE_DETAILS -> {
                    MyApplication.selectedTitle = getRemoteString("Services", context)
                    setLogoTint(imgHom, context, R.color.primary)
                    setTextColor(context, tvHom, R.color.primary)
                }
                AppConstants.FRAGMENT_ORDER -> {
                    MyApplication.selectedTitle = getRemoteString("Orders", context)
                    setLogoTint(imgOrd, context, R.color.primary)
                    setTextColor(context, tvOrd, R.color.primary)
                }
                AppConstants.FRAGMENT_NOTFICATIONS -> {
                    MyApplication.selectedTitle = getRemoteString("notifications", context)
                    setLogoTint(imgNot, context, R.color.primary)
                    setTextColor(context, tvNot, R.color.primary)
                }
                AppConstants.FRAGMENT_MY_SERVICES -> {
                    MyApplication.selectedTitle = getRemoteString("MyServices", context)
                    setLogoTint(imgPro, context, R.color.primary)
                    setTextColor(context, tvPro, R.color.primary)
                }
                AppConstants.FRAGMENT_CART -> {
                    MyApplication.selectedTitle = getRemoteString("Cart", context)
                    setLogoTint(imgCart, context, R.color.primary)
                    setTextColor(context, tvCart, R.color.primary)
                }
            }

        }

        fun openAppInPlayStore(activity: Activity) {
            val appPackageName = activity.packageName
            try {
                activity.finish()
                activity.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appPackageName")
                    )
                )



            } catch (anfe: android.content.ActivityNotFoundException) {
                activity.finish()
                activity.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )


            }
        }


        fun shareApp(activity: Activity) {



            val appPackageName = activity.packageName
            try {
                activity.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appPackageName")
                    )
                )


            } catch (anfe: android.content.ActivityNotFoundException) {
                activity.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )


            }
        }

        fun getRemoteString(key: String, con: Context): String {
            if (MyApplication.localizeArray != null) {
                try {
                    return MyApplication.localizeArray!!.messages!!.find { it.localize_Key == key }!!
                        .getMessage()!!
                } catch (e: Exception) {
                    try {
                        val resId = con.resources.getIdentifier(key, "string", con.packageName)
                        return con.resources.getString(resId)
                    } catch (e: Exception) {
                        return ""
                    }
                }

            } else {
                val resId = con.resources.getIdentifier(key, "string", con.packageName)
                return con.resources.getString(resId)
            }


        }

        fun createYesNoDialog(
            c: Activity,
            positiveButton: String,
            negativeButton: String,
            message: String,
            doAction: () -> Unit,
            doCancel: () -> Unit
        ) {


            val builder = AlertDialog.Builder(c)
            builder
                .setMessage(message)
                .setCancelable(true)
                .setNegativeButton(negativeButton) { dialog, _ ->
                    doCancel()
                    dialog.cancel()
                }
                .setPositiveButton(positiveButton) { dialog, _ ->
                    doAction()
                }
            val alert = builder.create()
            alert.show()

        }

        fun createSwitchDialog(
            c: Activity,
            positiveButton: String,
            negativeButton: String,
            message: String,
            view: SwitchCompat,
            doAction: () -> Unit
        ) {


            val builder = AlertDialog.Builder(c)
            builder.setMessage(
                message
            )
                .setCancelable(true)
                .setNegativeButton(negativeButton) { dialog, _ ->
                    view.isChecked = !view.isChecked
                    dialog.cancel()
                }
                .setPositiveButton(positiveButton) { dialog, _ ->
                    doAction()
                }
            val alert = builder.create()
            alert.show()

        }



        fun createYesNoDialog(
            c: Activity,
            positiveButton: String,
            negativeButton: String,
            message: String,
            doAction: () -> Unit
        ) {


            val builder = AlertDialog.Builder(c)
            builder
                .setMessage(message)
                .setCancelable(true)
                .setNegativeButton(negativeButton) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(positiveButton) { dialog, _ ->
                    doAction()
                }

            val alert = builder.create()
            alert.show()
/*            val messageView = alert.findViewById(android.R.id.message) as TextView
            messageView.textDirection=View.TEXT_DIRECTION_LTR
            messageView.gravity=Gravity.CENTER
            messageView.layoutDirection=View.TEXT_ALIGNMENT_TEXT_START*/

        }

        fun createFixedDialog(c: Activity, message: String,doAction: () -> Unit) {


            var ok = getRemoteString("ok", c)

            val builder = AlertDialog.Builder(c)
            builder
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(ok) { dialog, _ ->
                    doAction()
                }
            val alert = builder.create()
            alert.show()

        }


        fun createDialog(c: Activity, message: String,doAction: () -> Unit) {


            var ok = getRemoteString("ok", c)

            val builder = AlertDialog.Builder(c)
            builder
                .setMessage(message)
                .setCancelable(true)
                .setNegativeButton(ok) { dialog, _ ->
                    doAction()
                }
            val alert = builder.create()
            alert.show()

        }


        fun setLogoTint(img: ImageView, con: Context, color: Int) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ImageViewCompat.setImageTintList(
                        img,
                        ColorStateList.valueOf(con.getResources().getColor(color, con.getTheme()))
                    )
                } else {
                    ImageViewCompat.setImageTintList(
                        img,
                        ColorStateList.valueOf(
                            con.getResources().getColor(color)
                        )
                    )
                }
            } catch (e: Exception) {
            }
        }

        fun goHome(context: Context) {
            context.startActivity(Intent(context, ActivityHome::class.java))
        }

        fun onOneClick(doAction: () -> Unit) {
            if (MyApplication.clickable!!) {
                MyApplication.clickable = false
                doAction()
                Handler(Looper.getMainLooper()).postDelayed({
                    MyApplication.clickable = true
                }, 500)
            }
        }

        fun setUpCornerRadius(image: ImageView, context: Context) {

            val curveRadius = context.resources.getDimensionPixelSize(R.dimen.big_radius).toFloat()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val provider: ViewOutlineProvider = object : ViewOutlineProvider() {
                    override fun getOutline(view: View, outline: Outline) {
                        outline.setRoundRect(
                            0,
                            0,
                            view.width,
                            view.height,
                            curveRadius.toFloat()
                        )
                    }
                }
                image.setOutlineProvider(provider)
                image.setClipToOutline(true)
            }
        }


        fun toGsonArrString(){
           MyApplication.trackOrderIdList = Gson().toJson(MyApplication.listOrderTrack)
        }

        fun GsontoArrString(){
            val gson = GsonBuilder().create()
            MyApplication.listOrderTrack = gson.fromJson<ArrayList<String>>(MyApplication.trackOrderIdList, object : TypeToken<ArrayList<String>>(){}.type)
        }

        fun sha256(str:String):String{
            var byte = org.apache.commons.codec.digest.DigestUtils.sha256(str)
            return String(Hex.encodeHex(byte))


        }

        fun isAppRunning(context: Context) : Boolean {
            var activityManager =  context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
           var procInfos = activityManager.getRunningAppProcesses();
            if (procInfos != null)
            {
                for (processInfo in procInfos) {
                if (processInfo.processName.equals(context.packageName)) {
                    return true;
                }
            }
            }
            return false;
        }


        fun setSwitchColor(sw: SwitchCompat, con: Context) {


            if (sw.isChecked) {
                var colorInt = AppHelper.getColor(con, R.color.green_switch)
                var csl = ColorStateList.valueOf(colorInt)
                sw.thumbTintList = csl
                var colorInt2 = AppHelper.getColor(con, R.color.green_track_switch)
                var csl2 = ColorStateList.valueOf(colorInt2)
                sw.trackTintList = csl2
            }else{
                var colorInt = AppHelper.getColor(con, R.color.gray_switch)
                var csl3 = ColorStateList.valueOf(colorInt)
                sw.thumbTintList = csl3
                var colorInt2 = AppHelper.getColor(con, R.color.gray_track_switch)
                var csl4 = ColorStateList.valueOf(colorInt2)
                sw.trackTintList = csl4
            }


        }

        fun isAppForground(mContext: Context): Boolean {
            val am: ActivityManager =
                mContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val tasks: List<ActivityManager.RunningTaskInfo> = am.getRunningTasks(1)
            if (!tasks.isEmpty()) {
                val topActivity: ComponentName = tasks[0].topActivity!!
                if (!topActivity.getPackageName().equals(mContext.packageName)) {
                    return false
                }
            }
            return true
        }

        fun getFloorRatingBar(rate: Double): Float {
            var db = rate % 1
            var intPart = rate.toInt()

            if (db < 0.5) {
                return intPart.toFloat()
            } else {
                return (intPart + 0.5).toFloat()
            }
        }

        fun getAddressNames (res : ResponseGeoAddress):ResponseAddress{
            var street = ""
            var block = ""
            var avenue = ""
            var kadaa = ""
            var city = ""
            var mohafaza = ""

            res.results.forEach {
                for(item in it.addressComponents){
                    for(ty in item.types){
                        if(street.isEmpty() && ty.contains("route")){
                            street = item.longName!!.trim()

                            if(street.equals("Unnamed Road")){
                                street = ""
                            }
                        }

                        if(block.isEmpty() && ty.contains("sublocality")){
                            block = item.longName!!.trim()
                        }

                        if(avenue.isEmpty() && ty.contains("locality")){
                            avenue = item.longName!!.trim()
                        }

                        if(city.isEmpty() && ty.contains("administrative_area_level_3")){
                            city = item.longName!!.trim()
                        }

                        if(kadaa.isEmpty() && ty.contains("administrative_area_level_2")){
                            kadaa = item.longName!!.trim()
                        }

                        if(mohafaza.isEmpty() && ty.contains("administrative_area_level_1")){
                            mohafaza = item.longName!!.trim()
                        }

                        if(city.lowercase().equals("bayrut")) {
                            city = "Beirut"
                        }
                        if(kadaa.lowercase().equals("bayrut")) {
                            kadaa = "Beirut"
                        }

                        if( kadaa.isEmpty()) {
                            kadaa = mohafaza
                        }
                        if (city.isEmpty()) {
                            city = kadaa
                        }


                    }
                }
            }

            return ResponseAddress("","",MyApplication.selectedLatLngCall!!.latitude.toString(),MyApplication.selectedLatLngCall!!.longitude.toString(),street,"","","",city,avenue,"",block,"",kadaa)

        }

        fun getAddressText(add : ResponseAddress): String{
            var str = ""
            if(!add.province.isNullOrEmpty())
                str += add.province + ","
            if(!add.area.isNullOrEmpty())
                str += add.area+","
            if(!add.block.isNullOrEmpty())
                str+= add.block+","
            if(!add.street.isNullOrEmpty())
                str+=add.street


            return str

        }

        fun getAddressLoc(lat: Double, long: Double, con: Context): Address {


            var address : Address ?=null
            try {
                val myLocation = Geocoder(con, Locale.getDefault())
                val myList = myLocation.getFromLocation(lat, long, 1)
                address = myList[0]
            }catch (ex:Exception){
                Log.wtf("LOCEX",ex.toString())

            }

            return address!!
        }

        fun fromGSon(): ArrayList<RequestPlaceOrder> {
            val gson = Gson()
            val array = gson.fromJson(
                MyApplication.cartItems,
                ArrayList<RequestPlaceOrder>().javaClass
            )

            return array

        }

        fun toGSOn(array: ArrayList<RequestPlaceOrder>) {
            val gson = Gson()
            val jsonText = gson.toJson(array)
            MyApplication.cartItems = jsonText
        }

        fun getAddress(lat: Double, long: Double, con: Context): String {
            var addressStr = ""
            try {
                val myLocation = Geocoder(con, Locale.getDefault())
                val myList = myLocation.getFromLocation(lat, long, 1)
                val address = myList[0]

                // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                val city: String =address.getLocality()
                val state: String =address.getAdminArea()
                val country: String =address.getCountryName()
                val postalCode: String =address.getPostalCode()
                val knownName: String =address.getFeatureName()
                addressStr += address.getAddressLine(0).toString()
            }catch (ex:Exception){
                logw("locEx",ex.toString())
                return ""
            }

            return addressStr!!
        }

        fun addressFromOrder(res:ResponseOrders,type:Int,con:Context): String {

            var str = ""

            try {
                if (type == 0 || !MyApplication.isClient) {

                    if (!res.shipping_province!!.isNullOrEmpty())
                        str += res.shipping_province!! + ","
                    if (!res.shipping_area!!.isNullOrEmpty())
                        str += res.shipping_area!! + ","
                    if (!res.shipping_block!!.isNullOrEmpty())
                        str += res.shipping_block!! + ","
                    if (!res.shipping_address_street!!.isNullOrEmpty())
                        str += res.shipping_address_street!!
                } else {
                    if (!res.vendor!!.addresses.get(0).province!!.isNullOrEmpty())
                        str += res.vendor!!.addresses.get(0).province!! + ","
                    if (!res.vendor!!.addresses.get(0).area!!.isNullOrEmpty())
                        str += res.vendor!!.addresses.get(0).area!! + ","
                    if (!res.vendor!!.addresses.get(0).block!!.isNullOrEmpty())
                        str += res.vendor!!.addresses.get(0).block!! + ","
                    if (!res.vendor!!.addresses.get(0).street!!.isNullOrEmpty())
                        str += res.vendor!!.addresses.get(0).street!!
                }
            }catch (ex:java.lang.Exception){}


            if(str.isEmpty()){
                str = AppHelper.getRemoteString("no_data",con)
            }

            return str

        }

        fun triggerRebirth(context: Context) {
            val packageManager = context.packageManager
            val intent = packageManager.getLaunchIntentForPackage(context.packageName)
            val componentName = intent!!.component
            val mainIntent = Intent.makeRestartActivityTask(componentName)
            context.startActivity(mainIntent)
            Runtime.getRuntime().exit(0)
        }

        fun changeLanguage(context: Context, language: String) {

            when (language) {
                AppConstants.LANG_ARABIC -> Locale.setDefault(Locale("ar"))
                AppConstants.LANG_ENGLISH -> Locale.setDefault(Locale.ENGLISH)
                "0" -> {
                    Locale.setDefault(Locale.ENGLISH)
                }
            }

            val configuration = Configuration()
            var x = Locale.getDefault()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLocale(Locale.getDefault())
                configuration.setLayoutDirection(Locale.getDefault())

            } else
                configuration.locale = Locale.getDefault()


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                context.createConfigurationContext(configuration);
            } else {
                context.resources.updateConfiguration(
                    configuration,
                    context.resources.displayMetrics
                )
            }
            MyApplication.languageCode = language


        }



        fun setUpDoc(order: ResponseOrders) {
            MyApplication.selectedOrderTrack = order
            var doc = MyApplication.db!!.collection("table_order")
                .document(order.orderId!!)

            doc!!.get().addOnSuccessListener { documentSnapshot ->
                val orderLoc = documentSnapshot.toObject<OrderLocation>()
                var ny: LatLng? = null
                if (orderLoc != null) {
                    Log.wtf("there", "already")
                  /*  ny = LatLng(
                        orderLoc.order_laltitude!!.toDouble(),
                        orderLoc.order_longitude!!.toDouble()
                    )*/
                } else {
                    try {
                        val user: MutableMap<String, String> = HashMap()
                        user["oder_id"] = order.orderId!!
                        try {
                            user["order_laltitude"] =
                                MyApplication.selectedCurrentAddress!!.lat.toString()
                        } catch (ex: Exception) {
                            user["order_laltitude"] =
                                ""

                        }
                        try {
                            user["order_longitude"] =
                                MyApplication.selectedCurrentAddress!!.long.toString()
                        } catch (ex: Exception) {
                            user["order_longitude"] =
                                ""

                        }
                        doc!!.set(user)
                    } catch (ex: Exception) {
                        logw("error_database",ex.toString())
                    }
                }

            }

        }

        fun setInEnglish(): DecimalFormatSymbols {
            val custom = DecimalFormatSymbols(Locale.ENGLISH)
            custom.decimalSeparator = '.'
            return custom
        }


        fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

        fun AddFragment(
            fragmentManager: FragmentManager,
            selectedFragment: Int,
            myFragment: Fragment,
            myTag: String,
            id: Int
        ) {
            fragmentAvailable = selectedFragment
            fragmentManager.beginTransaction()
                .add(id, myFragment, myTag)
                .addToBackStack(null)
                .commit()
        }

        fun setLocal(context: Context) {
            if (MyApplication.languageCode == AppConstants.LANG_ENGLISH) {
                LocaleUtils.setLocale(Locale("en"))
            } else if (MyApplication.languageCode == AppConstants.LANG_ARABIC) {
                LocaleUtils.setLocale(Locale("ar", "LB"))
            }

        }


        fun setTitle(context: Context, text: String, tag: String, color: Int) {
            if (tag.isNotEmpty()) {
                try {
                    (context as ActivityHome?)!!.setTitleAc(
                        MyApplication.localizeArray!!.messages!!.find { it.localize_Key == tag }!!
                            .getMessage()!!, color
                    )
                } catch (e: java.lang.Exception) {
                    (context as ActivityHome?)!!.setTitleAc(text, color)
                }
            } else {
                (context as ActivityHome?)!!.setTitleAc(text, color)
            }
        }

        fun isEmailValid(email: String?): Boolean {
            val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher: Matcher = pattern.matcher(email)
            return matcher.matches()
        }

        fun isValidEmail(target: String): Boolean {

            return if (target.isEmpty()) {
                false
            } else {
                return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
            }
        }


        fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
                for (permission in permissions) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            permission
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return false
                    }
                }
            }
            return true
        }


        fun isProbablyArabic(s: String): Boolean {
            var i = 0
            while (i < s.length) {
                val c = s.codePointAt(i)
                if (c in 0x0600..0x06E0)
                    return true
                i += Character.charCount(c)
            }
            return false
        }

        fun share(context: Context, subject: String, text: String) {
            val intent = Intent(Intent.ACTION_SEND)

            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, text)

            context.startActivity(Intent.createChooser(intent, "share"))
        }


        /*     fun AddFragment(
                 fragmentManager: FragmentManager,
                 selectedFragment: Int,
                 myFragment: Fragment,
                 myTag: String
             ){
                 fragmentAvailable = selectedFragment
                 fragmentManager.beginTransaction()
     *//*                .setCustomAnimations(
                    com.ids.qasemti.R.anim.enter_from_right,
                    com.ids.qasemti.R.anim.exit_to_left,
                    com.ids.qasemti.R.anim.enter_from_left,
                    com.ids.qasemti.R.anim.exit_to_right
                )*//*
                .add(com.ids.qasemti.R.id.container, myFragment, myTag)
                .addToBackStack(null)
                .commit()
        }



        fun ReplaceFragment(
            fragmentManager: FragmentManager,
            selectedFragment: Int,
            myFragment: Fragment,
            myTag: String
        ){
            fragmentAvailable = selectedFragment
            fragmentManager.beginTransaction()

                .replace(com.ids.qasemti.R.id.container, myFragment, myTag)
                *//*              .setCustomAnimations(
                                  com.ids.qasemti.R.anim.enter_from_right,
                                  com.ids.qasemti.R.anim.exit_to_left,
                                  com.ids.qasemti.R.anim.enter_from_left,
                                  com.ids.qasemti.R.anim.exit_to_right
                              )*//*
                .commit()

        }
*/


        fun setMargins(context: Context, view: View, left: Int, top: Int, right: Int, bottom: Int) {
            if (view.layoutParams is ViewGroup.MarginLayoutParams) {
                val leftInDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, left.toFloat(), context.resources
                        .displayMetrics
                ).toInt()
                val topInDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, top.toFloat(), context.resources
                        .displayMetrics
                ).toInt()

                val rightInDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, right.toFloat(), context.resources
                        .displayMetrics
                ).toInt()

                val bottomInDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, bottom.toFloat(), context.resources
                        .displayMetrics
                ).toInt()

                val p = view.layoutParams as ViewGroup.MarginLayoutParams
                p.setMargins(leftInDp, topInDp, rightInDp, bottomInDp)
                view.requestLayout()
            }
        }


        fun setPaddings(
            context: Context,
            view: View,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int
        ) {
            if (view.layoutParams is ViewGroup.MarginLayoutParams) {
                val leftInDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, left.toFloat(), context.resources
                        .displayMetrics
                ).toInt()
                val topInDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, top.toFloat(), context.resources
                        .displayMetrics
                ).toInt()

                val rightInDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, right.toFloat(), context.resources
                        .displayMetrics
                ).toInt()

                val bottomInDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, bottom.toFloat(), context.resources
                        .displayMetrics
                ).toInt()

                val p = view.layoutParams as ViewGroup.MarginLayoutParams
                p.setMargins(leftInDp, topInDp, rightInDp, bottomInDp)
                view.requestLayout()
            }
        }


        fun createDialog(c: Activity, message: String) {


            var ok = getRemoteString("ok", c)

            val builder = AlertDialog.Builder(c)
            builder
                .setMessage(message)
                .setCancelable(true)
                .setNegativeButton(ok) { dialog, _ -> dialog.cancel() }
            val alert = builder.create()
            alert.show()

        }

        fun getScreenSize(context: Context): String {
            when (context.resources.displayMetrics.densityDpi) {
                DisplayMetrics.DENSITY_MEDIUM -> return AppConstants.MDPI
                DisplayMetrics.DENSITY_HIGH -> return AppConstants.HDPI
                DisplayMetrics.DENSITY_XHIGH -> return AppConstants.XHDPI
                DisplayMetrics.DENSITY_XXHIGH -> return AppConstants.XXHDPI
                DisplayMetrics.DENSITY_XXXHIGH -> return AppConstants.XXXHDPI
                else -> return AppConstants.XXXHDPI
            }
        }


        private fun capitalize(model: String): String {
            if (model.length == 0) {
                return ""
            }
            val first = model.get(0)
            return if (Character.isUpperCase(first)) {
                model
            } else {
                Character.toUpperCase(first) + model.substring(1)
            }
        }

        fun getDeviceName(): String {

            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.startsWith(manufacturer)) {
                capitalize(model)
            } else {
                capitalize(manufacturer) + " " + model
            }
        }


        fun getImageId(context: Context, imageName: String): Int {
            return context.resources.getIdentifier("drawable/$imageName", null, context.packageName)
        }


        fun setImageResize(
            context: Context,
            img: ImageView,
            ImgUrl: String,
            height: Int,
            width: Int
        ) {


            Glide
                .with(context)
                .load(ImgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(width, height)
                .centerCrop()
                .into(img);

        }

        fun getColor(context: Context, color: Int): Int {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return ContextCompat.getColor(context, color)
            } else {
                return context.resources.getColor(color)
            }
        }

        fun setRoundImageResize(
            context: Context,
            img: ImageView,
            ImgUrl: String,
            isLocal: Boolean
        ) {
            Log.wtf("image_rounded", ImgUrl)
            if (isLocal) {
                Glide.with(context).asBitmap().load(File(ImgUrl)).centerCrop()
                    .dontAnimate()
                    .into(object : BitmapImageViewTarget(img) {
                        override fun setResource(resource: Bitmap?) {

                            val circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.resources, resource)
                            circularBitmapDrawable.isCircular = true
                            img.setImageDrawable(circularBitmapDrawable)
                        }
                    })


            } else {
                Glide.with(context).asBitmap().load(ImgUrl).centerCrop().dontAnimate().override(
                    500,
                    500
                )
                    .into(object : BitmapImageViewTarget(img) {
                        override fun setResource(resource: Bitmap?) {
                            val circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.resources, resource)
                            circularBitmapDrawable.isCircular = true
                            img.setImageDrawable(circularBitmapDrawable)
                        }
                    })


            }
        }


        fun setImageResizePost(context: Context, img: ImageView, ImgUrl: String) {


            Glide
                .with(context)
                .load(ImgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(700, 500)
                .centerCrop()
                .into(img);

        }


        fun setImage(context: Context, img: ImageView, ImgUrl: String, isLocal: Boolean) {
            try {
                if (isLocal) {
                    Glide.with(context)
                        .load(File(ImgUrl))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .fitCenter()
                        .dontTransform()
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .into(img)
                } else {
                    Glide.with(context)
                        .load(ImgUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .fitCenter()
                        .dontTransform()
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .into(img)
                }


            } catch (e: Exception) {
            }

        }

        fun getFragmentCount(fragmentManager: FragmentManager): Int {
            var count = 0
            try {
                for (entry in 0 until fragmentManager.backStackEntryCount) {
                    count++
                }
            } catch (e: java.lang.Exception) {
            }
            Log.wtf("count_frag", count.toString() + "aaa")
            return count
        }


        fun getUserInfo() {
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
                        } catch (e: Exception) {
                        }
                    }

                    override fun onFailure(call: Call<ResponseUser>, throwable: Throwable) {
                    }
                })
        }


        private fun isLocationPermissionGranted(context: Activity, requestcode: Int): Boolean {
            return if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    requestcode
                )
                false
            } else {
                true
            }
        }


        fun setRoundImage(context: Context, img: ImageView, ImgUrl: String, isLocal: Boolean) {
            Log.wtf("image_rounded", ImgUrl)
            if (isLocal) {
                Glide.with(context).asBitmap().load(File(ImgUrl)).centerCrop().dontAnimate()
                    .into(object : BitmapImageViewTarget(img) {
                        override fun setResource(resource: Bitmap?) {

                            val circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.resources, resource)
                            circularBitmapDrawable.isCircular = true
                            img.setImageDrawable(circularBitmapDrawable)
                        }
                    })


            } else {
                Glide.with(context).asBitmap().load(ImgUrl).centerCrop().dontAnimate()
                    .into(object : BitmapImageViewTarget(img) {
                        override fun setResource(resource: Bitmap?) {

                            val circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.resources, resource)
                            circularBitmapDrawable.isCircular = true
                            img.setImageDrawable(circularBitmapDrawable)
                        }
                    })


            }
        }


        fun autofitText(vararg texts: TextView?) {
            for (element in texts) AutofitHelper.create(element)
        }

        @Throws(IOException::class)
        fun getFile(context: Activity, uri: Uri): File {
            try {
                val destinationFilename =
                    File(context.filesDir.path + File.separatorChar + queryName(context, uri))
                try {
                    context.contentResolver.openInputStream(uri).use { ins ->
                        createFileFromStream(
                            ins!!,
                            destinationFilename
                        )
                    }
                } catch (ex: java.lang.Exception) {
                    Log.e("Save File", ex.message!!)
                    ex.printStackTrace()
                }
                return destinationFilename
            }
            catch (ex:java.lang.Exception){
                Log.wtf("createFile","error")
                return File("")
            }
        }

        fun createFileFromStream(ins: InputStream, destination: File?) {
            try {
                FileOutputStream(destination).use { os ->
                    val buffer = ByteArray(4096)
                    var length: Int
                    while (ins.read(buffer).also { length = it } > 0) {
                        os.write(buffer, 0, length)
                    }
                    os.flush()
                }
            } catch (ex: java.lang.Exception) {
                Log.e("Save File", ex.message!!)
                ex.printStackTrace()
            }
        }

        private fun queryName(context: Context, uri: Uri): String {
            val returnCursor: Cursor = context.contentResolver.query(uri, null, null, null, null)!!
            val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            val name: String = returnCursor.getString(nameIndex)
            returnCursor.close()
            return name
        }

    }


}
