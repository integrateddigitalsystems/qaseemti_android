package com.ids.qasemti.controller.Activities

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterChat
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_code_verification.*
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.fragment_addresses.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.internal.wait
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class ActivityChat : ActivityBase(), RVOnItemClickListener {

    var chats: ArrayList<ChatItem> = arrayListOf()
    var FAILURE = 0
    var timer: CountDownTimer? = null
    var fromTimer : Boolean ?=false
    var atBottom = true
    var adapter: AdapterChat? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        init()
    }

    fun init() {
        loading.show()
        getChat()
        listeners()
        if (MyApplication.isClient) {
            MyApplication.selectedUser = User()
            MyApplication.selectedUser!!.userId = (2345).toString()
            MyApplication.selectedUser!!.firstName = "Jad"
            MyApplication.selectedUser!!.email = "emailer@hotmail.com"
        } else {
            MyApplication.selectedUser = User()
            MyApplication.selectedUser!!.userId = (2500).toString()
            MyApplication.selectedUser!!.firstName = "Service Person"
            MyApplication.selectedUser!!.email = "email@email.com"
        }
        tvPageTitle.setColorTypeface(
            this,
            R.color.white,
            MyApplication.selectedUser!!.firstName!!,
            true
        )
        tvPageTitle.hide()
        btBackTool.show()
        btBackTool.onOneClick {
            super.onBackPressed()
        }
        AppHelper.setLogoTint(btBackTool,this,R.color.gray_send)

    }

    fun listeners() {
        ivSendChat.onOneClick {
            if (etMessage.text.isNullOrEmpty()) {
                AppHelper.createDialog(this, AppHelper.getRemoteString("fill_all_field", this))
            } else {
                sendChat()
            }
        }

        rvChat.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    atBottom = true
                } else {
                    atBottom = false
                }
            }
        })


        timer = object : CountDownTimer(5000, 5000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                fromTimer = true
                getChat()
            }
        }
    }

    fun setChatData() {
        adapter = AdapterChat(chats, this, this)
        rvChat.layoutManager = LinearLayoutManager(this)
        rvChat.adapter = adapter
        rvChat.isNestedScrollingEnabled = false
        rvChat.scrollToPosition(chats.size - 1)

        timer!!.start()
        loading.hide()
    }

    fun nextStep(res: Int) {
        if (res == 1) {
            getChat()
        } else {
            getChat()
            AppHelper.createDialog(this, AppHelper.getRemoteString("failure", this))
        }
    }

    fun timeSetData(newest: ArrayList<ChatItem>) {
        fromTimer = false
        if (chats.size < newest.size) {
            loading.show()
            chats.clear()
            chats.addAll(newest)
            adapter!!.notifyDataSetChanged()
            if (atBottom)
                rvChat.scrollToPosition(chats.size - 1)
            else
                Toast.makeText(this, "New Messages!", Toast.LENGTH_SHORT).show()
            loading.hide()

        }
        timer!!.start()
    }

    fun sendChat() {

        try {
            loading.show()
        } catch (ex: Exception) {

        }
        var xx = MyApplication.selectedUser
        var newReq = RequestSendChat(
            MyApplication.selectedUser!!.firstName,
            MyApplication.selectedUser!!.email,
            etMessage.text.toString(),
            15,
            MyApplication.selectedUser!!.userId!!.toInt()
        )
        var cal = Calendar.getInstance()
        var date = AppHelper.formatDate(cal.time, "yyyy/mm/dd hh:mm:ssss")
        chats.add(
            ChatItem(
                MyApplication.selectedUser!!.firstName,
                etMessage.text.toString(),
                "",
                MyApplication.selectedUser!!.firstName,
                date,
                MyApplication.selectedUser!!.userId,
                true
            )
        )
        etMessage.text.clear()
        adapter!!.notifyDataSetChanged()
        rvChat.scrollToPosition(chats.size - 1)

        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.sendChats(
                newReq
            )?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(
                    call: Call<ResponseMessage>,
                    response: Response<ResponseMessage>
                ) {
                    timer!!.cancel()
                    nextStep(response.body()!!.result!!)

                }

                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    nextStep(FAILURE)
                    loading.hide()
                }
            })
    }

    fun getChat() {
        var newReq = RequestChat(15)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getChats(
                newReq
            )?.enqueue(object : Callback<ResponseMainChat> {
                override fun onResponse(
                    call: Call<ResponseMainChat>,
                    response: Response<ResponseMainChat>
                ) {

                    if (!fromTimer!!) {
                        chats.clear()
                        try {
                            chats.addAll(response.body()!!.chats)
                        } catch (ex: Exception) {

                        }
                        setChatData()
                    } else {
                        try {
                            timeSetData(response.body()!!.chats)
                        } catch (ex: Exception) {
                            timeSetData(arrayListOf())
                        }

                    }
                }

                override fun onFailure(call: Call<ResponseMainChat>, throwable: Throwable) {
                    loading.hide()
                }
            })
    }

    override fun onItemClicked(view: View, position: Int) {
    }
}