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
import kotlinx.android.synthetic.main.activity_code_verification.*
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
    var notfNum: Int? = 0

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

    fun setData() {
        rvNotifications.layoutManager = LinearLayoutManager(context)
        adapter = AdapterNotification(array, this, requireContext())
        rvNotifications.adapter = adapter
        notfNum = array!!.count {
            it.isViewed.equals("0")
        }
        (activity as ActivityHome).setNotNumber(notfNum!!)
        if (array.size == 0) {
            tvNoData.show()
        } else {
            tvNoData.hide()
        }
        try {
            loading.hide()
        } catch (ex: Exception) {

        }
    }

    fun getData() {
        try {
            try {
                loading.show()
            } catch (ex: Exception) {

            }
        } catch (ex: Exception) {

        }
        var newReq = RequestNotifications(
            MyApplication.languageCode,
            MyApplication.selectedUser!!.mobileNumber,
            0,
            10,
            1
        )

        if(MyApplication.isClient){
            newReq.isCl = 1
            newReq.isSp = 0
        }else{
            newReq.isCl = 0
            newReq.isSp = 1
        }
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getNotifications(
                newReq
            )?.enqueue(object : Callback<ArrayList<ResponseNotification>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseNotification>>,
                    response: Response<ArrayList<ResponseNotification>>
                ) {
                    try {
                        array.clear()
                        array.addAll(response.body()!!)
                        setData()
                    } catch (E: java.lang.Exception) {
                        try {
                            try {
                                loading.hide()
                            } catch (ex: Exception) {

                            }
                        } catch (ex: Exception) {

                        }
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseNotification>>,
                    throwable: Throwable
                ) {
                    try {
                        loading.hide()
                    } catch (ex: Exception) {
                    }
                }
            })
    }

    fun init() {
        array.clear()
        (activity as ActivityHome).showLogout(false)

        AppHelper.setTitle(requireActivity(), MyApplication.selectedTitle!!, "", R.color.white)

        getData()

        val animator: DefaultItemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }


    }

    fun markNotification(notfId: Int) {
        var newReq = MarkNotification(MyApplication.phoneNumber, notfId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.markNotification(
                newReq
            )?.enqueue(object : Callback<ResponseUpdate> {
                override fun onResponse(
                    call: Call<ResponseUpdate>,
                    response: Response<ResponseUpdate>
                ) {
                    logw("notfID", notfId.toString())
                }

                override fun onFailure(call: Call<ResponseUpdate>, t: Throwable) {
                    logw("notfID", t.toString())

                }

            })
    }

    override fun onItemClicked(view: View, position: Int) {

        AppHelper.onOneClick {
            if (AppHelper.isOnline(requireContext())) {
                array[position].open = !array[position].open
                if (array[position].isViewed.equals("0")) {
                    markNotification(array[position].id!!.toInt())
                    notfNum = notfNum!!.minus(1)
                    (activity as ActivityHome).setNotNumber(notfNum!!)
                    array[position].isViewed = "1"
                }
                adapter!!.notifyItemChanged(position)
            } else {
                AppHelper.createDialog(
                    requireActivity(),
                    AppHelper.getRemoteString("no_internet", requireContext())
                )
            }

        }
    }
}