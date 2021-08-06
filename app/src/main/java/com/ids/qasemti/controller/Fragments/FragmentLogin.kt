package com.ids.qasemti.controller.Fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment

import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener


class FragmentLogin : Fragment() , RVOnItemClickListener {

    var canClick=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.qasemti.R.layout.fragment_login, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


/*    fun login () {


        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.login(
                etUsername.text.toString(),
                etPassword.text.toString(),
                MyApplication.languageCode
            )?.enqueue(object : Callback<ResponseLoginNew> {


                override fun onResponse(
                    call: Call<ResponseLoginNew>,
                    response: Response<ResponseLoginNew>
                ) {
                    if(response.body()!!.success!!){
                        //MyApplication.responseLogin = response.body()!!.data
                     //   successLogin(response.body().data!!)
                    }
                    else{
                        loading.visibility=View.GONE
                        var log  = AppHelper.getStringFirebase("login_verf_error",requireActivity(), TextView(requireContext()),true,R.string.login_verf_error)
                        if(log.isNullOrEmpty())
                            log = getString(R.string.login_verf_error)
                        AppHelper.createDialog(requireActivity(),log)
                    }


                }

                override fun onFailure(call: Call<ResponseLoginNew>, t: Throwable) {
                    var x = 1
                }

            })
    }*/


    override fun onResume() {
        super.onResume()
        canClick=true
    }

    override fun onItemClicked(view: View, position: Int) {

    }


}

















