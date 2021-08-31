package com.ids.qasemti.custom


import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet

import com.ids.qasemti.utils.AppHelper


class CustomTextViewMedium : AppCompatTextView {
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
            typeface = AppHelper.getTypeFace(context)

    }

}
