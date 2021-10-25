package com.ids.qasemti.controller.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Adapters.AdapterNotification
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class FragmentNotifications : Fragment(), RVOnItemClickListener {

    var array: ArrayList<ResponseNotification> = arrayListOf()
    var adapter: AdapterNotification? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(com.ids.qasemti.R.layout.fragment_notifications, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutNotifications, requireContext())
        init()
    }

    fun setData(){
        rvNotifications.layoutManager = LinearLayoutManager(context)
        adapter = AdapterNotification(array, this, requireContext())
        rvNotifications.adapter = adapter
        if(array.size==0){
            tvNoData.show()
        }else{
            tvNoData.hide()
        }
        try {
            loading.hide()
        }catch (ex: Exception){

        }
    }

    fun getData(){
        try {
            try {
                loading.show()
            }catch (ex: Exception){

            }
        }catch (ex:Exception){

        }
        var newReq = RequestNotifications(MyApplication.languageCode,MyApplication.userId,MyApplication.deviceId,0,10,1)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getNotifications(
                newReq
            )?.enqueue(object : Callback<ArrayList<ResponseNotification>> {
                override fun onResponse(call: Call<ArrayList<ResponseNotification>>, response: Response<ArrayList<ResponseNotification>>) {
                    try{
                        array.clear()
                        array.addAll(response.body()!!)
                        setData()
                    }catch (E: java.lang.Exception){
                       try {
                           try {
                               loading.hide()
                           }catch (ex: Exception){

                           }
                       }catch (ex:Exception){

                       }
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseNotification>>, throwable: Throwable) {
                    try {
                        loading.hide()
                    }catch (ex: Exception){

                    }
                }
            })
    }

    fun init() {
        array.clear()
        (activity as ActivityHome).showLogout(false)

        AppHelper.setTitle(requireActivity(), MyApplication.selectedTitle!!, "",R.color.white)

        getData()

        val animator: DefaultItemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }


    }

    fun markNotification(notfId : Int ){
        var newReq = MarkNotification(MyApplication.deviceId,MyApplication.userId,notfId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.markNotification(
                newReq
            )?.enqueue(object : Callback<ResponseUpdate> {
                override fun onResponse(
                    call: Call<ResponseUpdate>,
                    response: Response<ResponseUpdate>
                ) {
                }

                override fun onFailure(call: Call<ResponseUpdate>, t: Throwable) {

                }

            })
    }
    override fun onItemClicked(view: View, position: Int) {

        AppHelper.onOneClick {
            array.get(position).open = !array.get(position).open
            if(array.get(position).isViewed.equals("0")){
                markNotification(array.get(position).id!!.toInt())
                array.get(position).isViewed = "1"
            }
            adapter!!.notifyItemChanged(position)
        }
    }
}