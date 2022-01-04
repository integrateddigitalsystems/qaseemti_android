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
import com.ids.qasemti.model.RequestServices
import com.ids.qasemti.model.RequestUpdateLanguage
import com.ids.qasemti.model.ResponseMainServices
import com.ids.qasemti.model.ResponseMessage
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.bottom_sheet_language.*
import kotlinx.android.synthetic.main.bottom_sheet_language.rootLayout
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.loading.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.util.*


class FragmentBottomSeetLanguage : BottomSheetDialogFragment(),ApiListener{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.bottom_sheet_language, container, false)


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL,R.style.MyBottomSheetDialog)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayout,requireContext())
        if(MyApplication.languageCode == AppConstants.LANG_ARABIC){
            tvEnglish.setBackgroundResource(R.drawable.rounded_white_red_border)
            AppHelper.setTextColor(requireActivity(),tvEnglish,R.color.primary)
            AppHelper.setTextColor(requireActivity(),tvArabic,R.color.white)
            tvArabic.setBackgroundResource(R.drawable.rounded_red_background)
        }else{
            tvEnglish.setBackgroundResource(R.drawable.rounded_red_background)
            AppHelper.setTextColor(requireActivity(),tvArabic,R.color.primary)
            AppHelper.setTextColor(requireActivity(),tvEnglish,R.color.white)
            tvArabic.setBackgroundResource(R.drawable.rounded_white_red_border)
        }
        tvEnglish.onOneClick{
            if(MyApplication.languageCode == AppConstants.LANG_ARABIC)
                setEnglish()
        }
        tvArabic.onOneClick{
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
        //sendUpdateLanguage()
        CallAPIs.updateDevice(requireContext(),this)
      //  AppHelper.updateDevice(requireContext(),MyApplication.selectedPhone!!)
        reloadActivity()

    }


    private fun setEnglish(){
        //   Toast.makeText(activity!!,"bbbbb",Toast.LENGTH_LONG).show()
        MyApplication.languageCode = AppConstants.LANG_ENGLISH
        MyApplication.selectedFragment=FragmentAccount()
        MyApplication.selectedFragmentTag=AppConstants.FRAGMENT_ACCOUNT
        LocaleUtils.setLocale(Locale("en"))
       // sendUpdateLanguage()
        CallAPIs.updateDevice(requireContext(),this)
       // AppHelper.updateDevice(requireContext(),MyApplication.selectedPhone!!)
        reloadActivity()
    }

    fun reloadActivity(){


        val bundle = ActivityOptionsCompat.makeCustomAnimation(
            requireActivity(),
            android.R.anim.fade_in, android.R.anim.fade_out
        ).toBundle()

        var intent = Intent(requireActivity(),ActivityHome::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        requireActivity().finish()
        requireActivity().startActivity(intent, bundle)



    }


    fun sendUpdateLanguage(){
        var newReq = RequestUpdateLanguage(MyApplication.userId,MyApplication.languageCode)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.updateLanguage(newReq)?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                    try{

                    }catch (E: java.lang.Exception){

                    }
                }
                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                }
            })
    }

    override fun onDataRetrieved(success: Boolean, response: Any, apiId: Int) {

    }

}
