package com.ids.qasemti.custom


import android.content.Context
import android.graphics.Typeface
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet
import com.ids.qasemti.utils.AppHelper
import me.grantland.widget.AutofitHelper
import java.util.*


class CustomTextViewBold : AppCompatTextView {
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        if (!isInEditMode)
            typeface = AppHelper.getTypeFaceBold(context)

        AutofitHelper.create(this)
            //setTypeface(typeface, Typeface.BOLD)
    }
}
