package com.ids.qasemti.utils


import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.widget.ImageViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.youtube.player.internal.v
import com.ids.qasemti.R
import com.ids.qasemti.controller.MyApplication
import kotlinx.android.synthetic.main.white_logo_layout.*
import me.grantland.widget.AutofitHelper
import java.io.File
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


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

        fun getTypeFace(context: Context): Typeface {
                      return if (Locale.getDefault().language == "ar")
                          Typeface.createFromAsset(
                              context.applicationContext.assets,
                              "fonts/Cairo-Regular.ttf"
                          )//"fonts/NeoTech-Medium.otf"
                      else
                          Typeface.createFromAsset(
                              context.applicationContext.assets,
                              "fonts/Cairo-Regular.ttf"
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

        fun getTypeFaceBold(context: Context): Typeface {
            return if (Locale.getDefault().language == "ar")
                Typeface.createFromAsset(
                    context.applicationContext.assets,
                    "fonts/Cairo-Bold.ttf"
                )//fonts/NeoTech-Bold.otf

            else
                Typeface.createFromAsset(
                    context.applicationContext.assets,
                    "fonts/Cairo-Bold.ttf"
                )//fonts/NeoTech-Bold.otf

         //   return Typeface.DEFAULT_BOLD
        }


        fun setAllTexts(v:View){
            if(MyApplication.localizeArray != null){
            try {
                if (v is ViewGroup) {
                    val vg = v as ViewGroup
                    for (i in 0 until vg.childCount) {
                        val child = vg.getChildAt(i)
                        // recursively call this method
                        setAllTexts(child)
                    }
                } else if (v is TextView) {
                    v.textRemote(v.tag.toString())
                }
                else if (v is Button) {
                    v.textRemote(v.tag.toString())
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }}
        }



        fun getIdFromUserId(Id: Int){



        }


        fun handleCrashes(context: Activity) {
            if (!MyApplication.isDebug)
                Thread.setDefaultUncaughtExceptionHandler(MyExceptionHandler(context))
        }


        private fun getStringResourceByName(aString: String, context: Context): String? {
            val packageName: String = context.getPackageName()
            val resId: Int = context.getResources().getIdentifier(aString, aString, packageName)
            return context.getString(resId)
        }


        fun formatNumber(num: Double, format: String): String {
            val formatter = DecimalFormat(format, setInEnglish())
            return formatter.format(num)
        }

        fun setInEnglish(): DecimalFormatSymbols {
            val custom = DecimalFormatSymbols(Locale.ENGLISH)
            custom.decimalSeparator = '.'
            return custom
        }



        fun formatDate(c: Context, dateString: String, oldDateFormat: String, newDateFormat: String):String?{
            var format = SimpleDateFormat(oldDateFormat, Locale.US)
            val newDate = format.parse(dateString)
            format = SimpleDateFormat(newDateFormat, Locale.US)
            val date = format.format(newDate)
            return date
        }


        fun setLogoTint(img:ImageView,con:Context){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ImageViewCompat.setImageTintList(img,
                    ColorStateList.valueOf(con.getResources().getColor(android.R.color.white, con.getTheme())))
            }else {
                ImageViewCompat.setImageTintList(img,
                    ColorStateList.valueOf(
                        con.getResources().getColor(android.R.color.white)))
            }
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



        fun setLocal(context: Context) {

            if (MyApplication.languageCode == AppConstants.LANG_ENGLISH) {
               LocaleUtils.setLocale(Locale("en"))
            } else if (MyApplication.languageCode == AppConstants.LANG_ARABIC) {
                LocaleUtils.setLocale(Locale("ar", "LB"))
            }

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
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
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


        fun setPaddings(context: Context, view: View, left: Int, top: Int, right: Int, bottom: Int) {
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

            var ok = c.getString(R.string.ok)

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




        fun setImageResize(
            context: Context,
            img: ImageView,
            ImgUrl: String,
            height: Int,
            width: Int
        ){


            Glide
                .with(context)
                .load(ImgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(width, height)
                .centerCrop()
                .into(img);

        }

        fun setRoundImageResize(context: Context, img: ImageView, ImgUrl: String, isLocal: Boolean) {
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

    }





}
