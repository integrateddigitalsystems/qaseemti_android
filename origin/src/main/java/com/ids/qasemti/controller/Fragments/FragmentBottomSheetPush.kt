package com.ids.qasemti.controller.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ids.qasemti.R
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.RequestNotificationUpdate
import com.ids.qasemti.model.ResponseCancel
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.bottom_sheet_language.*
import kotlinx.android.synthetic.main.bottom_sheet_language.rootLayout
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.android.synthetic.main.fragmentbottom_push_notification.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentBottomSheetPush : BottomSheetDialogFragment() , ApiListener{

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragmentbottom_push_notification, container, false)


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialog)


    }

    fun listeners() {

        swAvailable.isChecked = if(MyApplication.generalNotificaiton==1) true else false

        swAvailable.setOnCheckedChangeListener { compoundButton, b ->
            MyApplication.generalNotificaiton = if(swAvailable.isChecked) 1 else 0
            if(AppHelper.isOnline(requireContext())){
                CallAPIs.updateDevice(requireContext(),this)
            }else{
                swAvailable.isChecked = !swAvailable.isChecked
                AppHelper.createDialog(requireActivity(),AppHelper.getRemoteString("no_internet",requireContext()))
            }
        }
        try {

            if ( MyApplication.selectedUser!!.notificationType!!.toInt() == 1) {
                rbNotification.isChecked = true
            } else {
                rbSMS.isChecked = true
            }
        }catch (ex:Exception){
            rbSMS.isChecked = true
        }

        rbNotification.setOnClickListener {
          //  if(MyApplication.selectedUser!!.notificationType!!.toInt()!=1){
                setNotificationType(1)
           // }

        }

        rbSMS.setOnClickListener {
          //  if(MyApplication.selectedUser!!.notificationType!!.toInt()!=2){
                setNotificationType(2)
          //  }
        }
    }

    fun setNotificationType(available: Int) {
        MyApplication.selectedUser!!.notificationType =available.toString()
        var newReq = RequestNotificationUpdate(MyApplication.userId, available)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.updateNotification(newReq)?.enqueue(object : Callback<ResponseCancel> {
                override fun onResponse(
                    call: Call<ResponseCancel>,
                    response: Response<ResponseCancel>
                ) {
                    try {

                      //  AppHelper.getUserInfo()
                    } catch (E: java.lang.Exception) {

                    }
                }

                override fun onFailure(call: Call<ResponseCancel>, throwable: Throwable) {
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayout, requireContext())
        listeners()

    }

    override fun onDataRetrieved(success: Boolean, response: Any, apiId: Int) {

    }
}