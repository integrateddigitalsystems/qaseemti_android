package com.ids.qasemti.controller.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Adapters.AdapterNotification
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Adapters.com.ids.qasemti.model.ResponeMainNotification
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_code_verification.*
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.layout_home_orders.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class FragmentNotifications : Fragment(), RVOnItemClickListener , ApiListener {

    var array: ArrayList<ResponseNotification> = arrayListOf()
    var adapter: AdapterNotification? = null
    var notfNum: Int? = 0
    var isLoading = true
    var page = 1
    var perPage = 10
    var finishScrolling=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onResume() {
        super.onResume()
        (activity as ActivityHome).showTitle(true)
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
        page = 1
        init()
    }

    fun setData() {
        if (adapter != null)
            adapter!!.notifyDataSetChanged()
        else {
            rvNotifications.layoutManager = LinearLayoutManager(context)
            adapter = AdapterNotification(array, this, requireContext())
            rvNotifications.adapter = adapter
        }
           (activity as ActivityHome).setNotNumber(notfNum!!.toString())
            //  }
            /*try {
            if (page > 1)
                rvNotifications.getLayoutManager()!!.scrollToPosition(page*10)
        }catch (ex:Exception){}*/


            if (array.size == 0) {
                tvNoData.show()
            } else {
                tvNoData.hide()
            }
            try {
                loading.hide()
            } catch (ex: Exception) {

            }

        rvNotifications.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var x = recyclerView.scrollState
                if (!recyclerView.canScrollVertically(dy) && dy > 0 && !isLoading && !finishScrolling)
                {
                        isLoading=true
                        getData()

                }
            }
        })
        }


    fun getData() {
        swipeNotifications.isRefreshing = false
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
            perPage,
            page
        )

        if(MyApplication.isClient){
            newReq.isCl = 1
            newReq.isSp = 0
        }else{
            newReq.isCl = 0
            newReq.isSp = 1
        }

        logw("NOTFREQ",Gson().toJson(newReq))


        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getNotifications(
                newReq
            )?.enqueue(object : Callback<ResponeMainNotification> {
                override fun onResponse(
                    call: Call<ResponeMainNotification>,
                    response: Response<ResponeMainNotification>
                ) {
                    try {
                        if(page==1)
                            array.clear()
                        array.addAll(response.body()!!.notf)
                        notfNum = response.body()!!.count!!.toInt()
                        setData()
                        isLoading=false
                        page++
                        if(response.body()!!.notf.size==0)
                            finishScrolling=true
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
                    call: Call<ResponeMainNotification>,
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

        if(MyApplication.selectedUser !=null )
            getData()
        else{
            loading.show()
            CallAPIs.getUserInfo(this)
        }
        val animator: DefaultItemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }

        swipeNotifications.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            page = 1
            finishScrolling=false
            getData()
        })


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
                try {
                    var pos = array.indexOf(array.find { it.open }!!)
                    if(pos !=position) {
                        array.get(pos).open = false
                        adapter!!.notifyItemChanged(pos)
                    }
                }catch (ex:Exception){

                }

                if (array[position].isViewed.equals("0")) {
                    markNotification(array[position].id!!.toInt())
                    if(notfNum!! > 0) {
                        notfNum = notfNum!!.minus(1)
                    }

                    if(notfNum == 0)
                    (activity as ActivityHome).setNotNumber(notfNum!!.toString())
                    array[position].isViewed = "1"
                }
                if(array.get(position).open)
                    array.get(position).open = false
                else
                    array.get(position).open = true
                adapter!!.notifyItemChanged(position)
            } else {
                AppHelper.createDialog(
                    requireActivity(),
                    AppHelper.getRemoteString("no_internet", requireContext())
                )
            }

        }
    }

    override fun onDataRetrieved(success: Boolean, response: Any, apiId: Int) {

        if(success) {
            getData()
        }

    }
}