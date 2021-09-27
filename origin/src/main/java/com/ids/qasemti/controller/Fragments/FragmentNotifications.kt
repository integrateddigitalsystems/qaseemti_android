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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
        loading.hide()
    }

    fun getData(){
        loading.show()
        var newReq = RequestNotifications(MyApplication.languageCode,1,MyApplication.deviceId,0,10,1)
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
                        loading.hide()
                    }
                }
                override fun onFailure(call: Call<ArrayList<ResponseNotification>>, throwable: Throwable) {
                    loading.hide()
                }
            })
    }

    fun init() {
        array.clear()
        (activity as ActivityHome).showLogout(false)

        AppHelper.setTitle(
            requireActivity(),
            AppHelper.getRemoteString("notifications", requireContext()),
            "notifications"
        )

        getData()

        val animator: DefaultItemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }


    }

    fun markNotification(notfId : Int ){
        var newReq = MarkNotification(MyApplication.deviceId,1,notfId)
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
            adapter!!.notifyDataSetChanged()
        }
    }
}