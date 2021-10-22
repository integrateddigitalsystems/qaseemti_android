package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterMyServices
import com.ids.qasemti.controller.Adapters.AdapterPreviousSettlements
import com.ids.qasemti.controller.Adapters.AdapterServices
import com.ids.qasemti.controller.Adapters.AdapterSettlements
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_settlement.*
import kotlinx.android.synthetic.main.activity_settlement.linearTabs
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class ActivitySettlements : ActivityBase(), RVOnItemClickListener {
    var array: ArrayList<ResponseOrders> = arrayListOf()
    var arraySett: ArrayList<ResponseSettlement> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settlement)
        AppHelper.setAllTexts(rootLayout, this)
        init()
        listeners()
    }

    private fun init() {
        //   array.clear()
        // repeat(3){array.add("1")}
        btBck.show()
        setTabs()
        setTabLayout(MyApplication.settlementTabSelected, tvToBeSettled)
        getOrders(MyApplication.settlementTabSelected)
    }

    fun nextStep(res: Int) {
        if (res == 1) {
            AppHelper.createDialog(this, AppHelper.getRemoteString("success", this))
            getOrders(MyApplication.settlementTabSelected)
        } else {
            AppHelper.createDialog(this, AppHelper.getRemoteString("failure", this))
        }
        loading.hide()
    }

    fun postSettlement() {

        loading.show()

        var newReq = RequestUserStatus(MyApplication.userId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.postSettlement(
                newReq
            )?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(
                    call: Call<ResponseMessage>,
                    response: Response<ResponseMessage>
                ) {
                    try {
                        nextStep(response.body()!!.result!!)
                    } catch (E: java.lang.Exception) {
                        nextStep(AppConstants.FAILURE_REQUEST)
                    }
                }

                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    nextStep(AppConstants.FAILURE_REQUEST)
                }
            })
    }

    private fun listeners() {
        btBck.onOneClick { super.onBackPressed() }

        btRequestSettlements.onOneClick {
            postSettlement()
        }
    }

    fun getOrders(position: Int) {

            loading.show()

        var newReq = RequestUserStatus(MyApplication.userId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getToBeSettled(
                newReq
            )?.enqueue(object : Callback<ResponseMainOrder> {
                override fun onResponse(
                    call: Call<ResponseMainOrder>,
                    response: Response<ResponseMainOrder>
                ) {
                    try {
                        array.clear()
                        array.addAll(response.body()!!.orders)
                        setData(position)
                    } catch (E: java.lang.Exception) {
                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponseMainOrder>, throwable: Throwable) {
                    loading.hide()
                }
            })
    }

    fun getSettlements(position: Int) {

            loading.show()

        var newReq = RequestServices(MyApplication.userId, MyApplication.languageCode)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getSettlements(
                newReq
            )?.enqueue(object : Callback<ResponseMainSettlement> {
                override fun onResponse(
                    call: Call<ResponseMainSettlement>,
                    response: Response<ResponseMainSettlement>
                ) {
                    try {
                        arraySett.clear()
                        arraySett.addAll(response.body()!!.settlements)
                        setData(position)
                    } catch (E: java.lang.Exception) {
                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponseMainSettlement>, throwable: Throwable) {
                    loading.hide()
                }
            })
    }

    private fun setData(position: Int) {

        if (array.size == 0) {
            btRequestSettlements.show()
            MyApplication.upcoming = false
            var adapter = AdapterSettlements(array, this, this)
            rvSettlements.layoutManager = LinearLayoutManager(this)
            rvSettlements.adapter = adapter
            rvSettlements.isNestedScrollingEnabled = false
            if(array.size>0){
                btRequestSettlements.hide()
            }
        } else {
            btRequestSettlements.hide()
            MyApplication.upcoming = true
            var adapter = AdapterPreviousSettlements(arraySett, this, this)
            rvSettlements.layoutManager = LinearLayoutManager(this)
            rvSettlements.adapter = adapter
            rvSettlements.isNestedScrollingEnabled = false
        }


        loading.hide()


    }

    override fun onItemClicked(view: View, position: Int) {
        if (view.id == R.id.tvViewDetails) {
            AppHelper.onOneClick {
                if (MyApplication.upcoming!!) {
                    startActivity(
                        Intent(this, ActivityRelatedOrders::class.java)
                            .putExtra("settelmentId", "#70007070")
                    )
                }
            }
        }
    }

    private fun setTabs() {
        for (i in 0 until linearTabs.childCount) {
            linearTabs.getChildAt(i).onOneClick {
                if (MyApplication.settlementTabSelected != i) {
                    var tv = linearTabs.getChildAt(i) as TextView
                    setTabLayout(i, tv)
                    // setData(i)
                    if (i == 0)
                        getOrders(i)
                    else
                        getSettlements(i)
                }
            }
        }
    }

    fun setTabLayout(position: Int, tvSelected: TextView) {
        MyApplication.settlementTabSelected = position
        for (i in 0 until linearTabs.childCount) {
            if (linearTabs.getChildAt(i) is TextView) {
                var tv = linearTabs.getChildAt(i) as TextView
                tv.setBackgroundResource(R.color.transparent)
                AppHelper.setTextColor(this, tv, R.color.gray_font_title)
            }
        }
        tvSelected.setBackgroundResource(R.drawable.rounded_red_background)
        AppHelper.setTextColor(this, tvSelected, R.color.white)

    }
}