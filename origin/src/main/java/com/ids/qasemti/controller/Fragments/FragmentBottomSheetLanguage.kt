package com.ids.qasemti.controller.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import android.content.Intent
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.DialogFragment
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.LocaleUtils
import kotlinx.android.synthetic.main.bottom_sheet_language.*
import kotlinx.android.synthetic.main.bottom_sheet_language.rootLayout
import kotlinx.android.synthetic.main.fragment_account.*

import java.util.*


class FragmentBottomSeetLanguage : BottomSheetDialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.bottom_sheet_language, container, false)


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL,R.style.MyBottomSheetDialog)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayout,requireContext())
        tvEnglish.setOnClickListener{
            if(MyApplication.languageCode == AppConstants.LANG_ARABIC)
                setEnglish()
        }
        tvArabic.setOnClickListener{
            if(MyApplication.languageCode == AppConstants.LANG_ENGLISH)
                setArabic()
        }
    }

    private fun setArabic(){
        //  Toast.makeText(activity!!,"aaaaa", Toast.LENGTH_LONG).show()
        MyApplication.languageCode = AppConstants.LANG_ARABIC
        MyApplication.selectedFragment=FragmentAccount()
        MyApplication.selectedFragmentTag=AppConstants.FRAGMENT_ACCOUNT
        LocaleUtils.setLocale(Locale("ar"))
        reloadActivity()

    }


    private fun setEnglish(){
        //   Toast.makeText(activity!!,"bbbbb",Toast.LENGTH_LONG).show()
        MyApplication.languageCode = AppConstants.LANG_ENGLISH
        MyApplication.selectedFragment=FragmentAccount()
        MyApplication.selectedFragmentTag=AppConstants.FRAGMENT_ACCOUNT
        LocaleUtils.setLocale(Locale("en"))
        reloadActivity()
    }

    fun reloadActivity(){


        val bundle = ActivityOptionsCompat.makeCustomAnimation(
            requireActivity(),
            android.R.anim.fade_in, android.R.anim.fade_out
        ).toBundle()

        var intent = Intent(requireActivity(),ActivityHome::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        requireActivity().startActivity(intent, bundle)
        requireActivity().finish()


    }

}
