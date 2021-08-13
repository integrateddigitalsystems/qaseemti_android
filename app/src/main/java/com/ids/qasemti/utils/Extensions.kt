package com.ids.qasemti.utils

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList

import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatRadioButton
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.ids.qasemti.controller.MyApplication
import java.lang.Exception


/**
 * Removes the listener of a checkbox temporarily to restore the chosen choice without calling the on Text change
 */
fun CompoundButton.setCustomChecked(value: Boolean, listener: CompoundButton.OnCheckedChangeListener) {
    setOnCheckedChangeListener(null)
    isChecked = value
    setOnCheckedChangeListener(listener)
}



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
    return this.substring(0, 1).toUpperCase().plus(this.substring(1))
}

/**
 * Checks if String is numeric
 */
fun String.isNumeric(): Boolean {
    return this.matches("\\d+".toRegex())
}

/**
 * Used for simpler logging
 */
fun Any.wtf(message: String) {
    if(MyApplication.showLogs)
        Log.wtf(this::class.java.simpleName, message)
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

fun Any.logw(key:String,value: String){
    try{
    if(MyApplication.showLogs)
        Log.wtf(key,value)}catch (e:Exception){}
}

fun TextView.textRemote(key:String){
    if(MyApplication.localizeArray!=null){
       try{ this.text = MyApplication.localizeArray!!.messages!!.find { it.localize_Key==key }!!.getMessage() }catch (e:Exception){}
    }

}

@SuppressLint("RestrictedApi")
fun AppCompatCheckBox.setCheckBoxColor(uncheckedColor: Int, checkedColor: Int) {
    val colorStateList = ColorStateList(
        arrayOf(
            intArrayOf(-R.attr.state_checked), // unchecked
            intArrayOf(R.attr.state_checked)  // checked
        ),
        intArrayOf(uncheckedColor, checkedColor)
    )
    this.supportButtonTintList = colorStateList
}

@SuppressLint("RestrictedApi")
fun AppCompatRadioButton.setRadioButtonColor(uncheckedColor: Int, checkedColor: Int) {
    val colorStateList = ColorStateList(
        arrayOf(
            intArrayOf(-R.attr.state_checked), // unchecked
            intArrayOf(R.attr.state_checked)  // checked
        ),
        intArrayOf(uncheckedColor, checkedColor)
    )
    this.supportButtonTintList = colorStateList
}



/**
 * Used for Showing and Hiding views
 */
fun View.showView() {
    visibility = View.VISIBLE
}

fun View.hideView() {
    visibility = View.GONE
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