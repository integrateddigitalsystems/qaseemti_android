package com.ids.qasemti.utils


import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.location.Location
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.ids.qasemti.R
import com.ids.qasemti.controller.Fragments.FragmentHomeClient
import com.ids.qasemti.controller.Fragments.FragmentServiceDetails
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppHelper.Companion.setInEnglish
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*


/**
 * Removes the listener of a checkbox temporarily to restore the chosen choice without calling the on Text change
 */
fun CompoundButton.setCustomChecked(
    value: Boolean,
    listener: CompoundButton.OnCheckedChangeListener
) {
    setOnCheckedChangeListener(null)
    isChecked = value
    setOnCheckedChangeListener(listener)
}

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

/**
 * Calculate the occurrences of a certain string
 */
fun String.occurrencesOf(sub: String): Int {
    var count = 0
    var last = 0
    while (last != -1) {
        last = this.indexOf(sub, last)
        if (last != -1) {
            count++
            last += sub.length
        }
    }
    return count
}

/**
 * Set the first character as upper case
 */
fun String.upperCaseFirstLetter(): String {
    return this.substring(0, 1).uppercase().plus(this.substring(1))
}

/**
 * Checks if String is numeric
 */
fun String.isNumeric(): Boolean {
    return this.matches("\\d+".toRegex())
}

fun Location?.toText(): String {
    return if (this != null) {
        "($latitude, $longitude)"
    } else {
        "Unknown location"
    }
}

fun ImageView.loadImagesUrl(url: String) {
    val options: RequestOptions = RequestOptions()
        .centerCrop()
        .placeholder(R.color.gray_medium_2)
        .error(R.color.gray_medium_2)
    Glide.with(this).load(url).apply(options)
        .into(this)

}
fun ImageView.loadImagesUrlResize(url: String) {
    val options: RequestOptions = RequestOptions()
        .centerCrop()
        .placeholder(R.color.gray_medium_2)
        .error(R.color.gray_medium_2)
        .override(500, 200)
        Glide.with(this).load(url).apply(options)
        .into(this)

}

fun ImageView.loadLocalImage(file: File) {
    val options: RequestOptions = RequestOptions()
        .centerCrop()
        .placeholder(R.color.gray_medium_2)
        .error(R.color.gray_medium_2)
    Glide.with(this).load(file).apply(options)
        .into(this)

}

fun ImageView.loadRoundedImage(url: String) {
    val options: RequestOptions = RequestOptions()
        .centerCrop()
        .placeholder(R.drawable.circle_gray)
        .error(R.drawable.circle_gray)
        .circleCrop()
         Glide.with(this).load(url).apply(options)
        .into(this)

}

fun ImageView.loadRoundedLocalImage(file: File) {
    val options: RequestOptions = RequestOptions()
        .centerCrop()
        .circleCrop()
        .placeholder(R.drawable.circle_gray)
        .error(R.drawable.circle_gray)
          Glide.with(this).load(file).apply(options)
        .into(this)

}
/**
 * Used for simpler logging
 */
fun Any.wtf(message: String) {
    if (MyApplication.showLogs)
        Log.wtf(this::class.java.simpleName, message)
}

fun Activity.loadJSONFromAssets(fileName: String): String {
    return this.assets.open(fileName).bufferedReader().use { reader ->
        reader.readText()
    }
}


fun Any.addFragment(
    container: Int,
    fragmentManager: FragmentManager,
    myFragment: Fragment,
    myTag: String
) {
    MyApplication.selectedFragmentTag = myTag
    fragmentManager.beginTransaction()
        .add(container, myFragment, myTag)
        .addToBackStack(null)
        .commit()
}

fun Any.replaceFragment(
    container: Int,
    fragmentManager: FragmentManager,
    myFragment: Fragment,
    myTag: String
) {
    for (i in 0 until fragmentManager.backStackEntryCount) {
        fragmentManager.popBackStack()
    }
    MyApplication.selectedFragmentTag = myTag
    MyApplication.selectedFragment = myFragment

    if(myTag == AppConstants.FRAGMENT_SERVICE_DETAILS){
        fragmentManager.beginTransaction()
            .replace(container, FragmentHomeClient(), AppConstants.FRAGMENT_HOME_CLIENT)
            .commit()
        addFragment(container,fragmentManager,myFragment,myTag)
    }else{
        fragmentManager.beginTransaction()
            .replace(container, myFragment, myTag)
            .commit()
    }
}
/**
 * Used for Images Loading
 */


/**
 * Used for Showing toasts
 */
fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Activity.snackbar(message: String){
    val snack: Snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
    val view = snack.view
    view.setBackgroundColor(AppHelper.getColor(this,R.color.primary))
    snack.show()
}

fun Any.logw(key: String, value: String) {
    try {
        if (MyApplication.showLogs)
            Log.wtf(key, value)
    } catch (e: Exception) {
    }
}

fun View.onOneClick(doAction: () -> Unit){
    this.setOnClickListener {
        if(MyApplication.clickable!!){
            MyApplication.clickable = false
            doAction()
            Handler(Looper.getMainLooper()).postDelayed({
                MyApplication.clickable = true
            }, 500)
        }
    }

}
fun TextView.setHTML(html:String){
    this.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(html)
    }
}
fun TextView.textRemote(key: String, con:Context) {
    if (MyApplication.localizeArray != null) {
        try {
            this.text = MyApplication.localizeArray!!.messages!!.find { it.localize_Key == key }!!
                .getMessage()
        } catch (e: Exception) {
            try {
                val resId = resources.getIdentifier(key, "string", con.packageName)
                this.text = resources.getString(resId)
            } catch (e: Exception) {
            }
        }

    }

}

fun String.capitalized(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase())
            it.titlecase(Locale.getDefault())
        else it.toString()
    }
}

fun String.format(number:Double,format:String): String {
    return this.replaceFirstChar {
        if (it.isLowerCase())
            it.titlecase(Locale.getDefault())
        else it.toString()
    }






}

fun String.formatNumber(format: String): String {
    return if(this==null || this == "null" ) {
        ""
    }
    else if(this=="0.0" ) {
        "0"
    }else if(this.isEmpty()){
        ""
    }
    else{
        var formatter = DecimalFormat(format, setInEnglish())
        formatter.format(this.toDouble())
    }
}



fun TextView.setColorTypeface(context: Context, color: Int,text:String,bold:Boolean) {

    this.show()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.setTextColor(ContextCompat.getColor(context, color))
    } else {
        this.setTextColor(context.resources.getColor(color))
    }
    if(!bold) {
        this.typeface = AppHelper.getTypeFace(context)
    }else{
        this.typeface = AppHelper.getTypeFaceBold(context)
    }
    if(!text.isNullOrEmpty()){
        this.text = text
    }

}

fun View.show() {
    try {
        visibility = View.VISIBLE
    } catch (e: Exception) {
    }
}


fun View.setWeight(weight:Float){
    val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT
    )
    params.weight = weight
    this.layoutParams = params
}


fun View.invisible() {
    try {
        visibility = View.INVISIBLE
    } catch (e: Exception) {
    }
}

fun View.hide() {
    try {
        visibility = View.GONE
    } catch (e: Exception) {
        Log.wtf("visibilityEx",e)
    }
}

/**
 * Used for Showing and Hiding keyboard
 */
fun View.showKeyboard(show: Boolean) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (show) {
        if (requestFocus()) imm.showSoftInput(this, 0)
    } else {
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}