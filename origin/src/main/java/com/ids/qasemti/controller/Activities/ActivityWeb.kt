package com.ids.qasemti.controller.Activities

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.*
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.RequestContactUs
import com.ids.qasemti.model.ResponseUpdate
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityWeb: ActivityBase() {

    var selectedUrl : String ?=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        init()

    }



    fun resultContact(req:Int){
        if(req==1){
            AppHelper.createDialog(this,AppHelper.getRemoteString("success",this))
        }else{
            AppHelper.createDialog(this,AppHelper.getRemoteString("failure",this))
        }
        try {
            loading.hide()
        }catch (ex: Exception){

        }
        etFullNameContact.text.clear()
        etEmailContact.text.clear()
        etMessageContact.text.clear()
        etPhoneContact.text.clear()
        etSubjectContact.text.clear()
    }
    fun sendContact(){
        try {
            loading.show()
        }catch (ex: Exception){

        }
        var newReq = RequestContactUs(etFullNameContact.text.toString(),etPhoneContact.text.toString(),etEmailContact.text.toString(),etSubjectContact.text.toString(),etMessageContact.text.toString())
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.contactUs(
                newReq
            )?.enqueue(object : Callback<ResponseUpdate> {
                override fun onResponse(call: Call<ResponseUpdate>, response: Response<ResponseUpdate>) {
                    try{
                        resultContact(response.body()!!.result!!.toInt())
                    }catch (E: java.lang.Exception){
                        resultContact(0)
                    }
                }
                override fun onFailure(call: Call<ResponseUpdate>, throwable: Throwable) {
                    resultContact(0)
                }
            })
    }
    fun init(){
        rlToolBar.setBackgroundColor(AppHelper.getColor(this,R.color.gray_background))
        btBackTool.show()
        tvPageTitle.show()
        AppHelper.setLogoTint(btBackTool,this,R.color.primary)
        var title = intent.getStringExtra("webTitle")
        var id = intent.getIntExtra("webId",0)
        tvPageTitle.setColorTypeface(this,R.color.primary,title!!,true)
        btBackTool.setOnClickListener {
            super.onBackPressed()
        }
        try{
        if(MyApplication.languageCode==AppConstants.LANG_ENGLISH){
            selectedUrl = MyApplication.webLinks!!.links.find { it.idNo ==id  }!!.urlEn
        }else{
            selectedUrl = MyApplication.webLinks!!.links.find { it.idNo ==id  }!!.urlAr
        }}catch (e:Exception){}
        if(id==4){
            linearContact.show()
            wvData.hide()
        }

        loadContent(selectedUrl!!,if(id==4) wvData2 else wvData)

        btContactWeb.onOneClick {

            if(etFullNameContact.text.isNullOrEmpty()||etEmailContact.text.isNullOrEmpty()||etPhoneContact.text.isNullOrEmpty()||etMessageContact.text.isNullOrEmpty()||etSubjectContact.text.isNullOrEmpty()){
                AppHelper.createDialog(this,AppHelper.getRemoteString("fill_all_field",this))
            }else if(!AppHelper.isEmailValid(etEmailContact.text.toString())){
                AppHelper.createDialog(this,AppHelper.getRemoteString("email_valid_error",this))
            }else{
                sendContact()
            }
        }

    }
    fun loadContent(content:String,webView: WebView){
        loading.show()
        webView.settings.javaScriptEnabled=true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = false
        webView.settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.settings.builtInZoomControls = false
        webView.settings.displayZoomControls = false


        webView.webViewClient = object : WebViewClient() {
            private var running = 0
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                running++
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                running = Math.max(running, 1)
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if(--running == 0) {
                    loading.hide()
                }
            }
        }

        webView.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {

                var prog = newProgress
                /*if(prog ==100)
                    loading.visibility= View.GONE*/
                var x = view.url
                var y = 1

            }

            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                Log.wtf("console_message", consoleMessage.message())
                return true
            }


        }
        )

        webView.loadUrl(content)
    }
}