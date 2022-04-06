package com.ids.qasemti.controller.Activities

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.webkit.*
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.Base.AppCompactBase
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
import kotlin.math.log

class ActivityWeb: ActivityBase() {

    var selectedUrl : String ?=""
    var id : Int ?=0
    var doneOnce = false ;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        init()

    }


    override fun onBackPressed() {
      //  if (!MyApplication.fromSplash) {
            super.onBackPressed()
     //   }
       /* else {
            //Can't go back without accepting terms
        }*/
    }

    fun resultContact(req:Int){
        if(req==1){
            toast(AppHelper.getRemoteString("success",this))
        }else{
            toast(AppHelper.getRemoteString("failure",this))
        }
        try {
            loading.hide()
        }catch (ex: Exception){

        }
        /*etFullNameContact.text.clear()
        etEmailContact.text.clear()
        etMessageContact.text.clear()
        etPhoneContact.text.clear()
        etSubjectContact.text.clear()*/
        super.onBackPressed()
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
        id = intent.getIntExtra("webId",0)
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




        loadContent(selectedUrl!!,if(id==4) wvData2 else wvData)

        btContactWeb.onOneClick {

            if (AppHelper.isOnline(this)) {
                if(etFullNameContact.text.isNullOrEmpty()||etEmailContact.text.isNullOrEmpty()||etPhoneContact.text.isNullOrEmpty()||etMessageContact.text.isNullOrEmpty()||etSubjectContact.text.isNullOrEmpty()){
                    AppHelper.createDialog(this,AppHelper.getRemoteString("fill_all_field",this))
                }else if(!AppHelper.isEmailValid(etEmailContact.text.toString())){
                    AppHelper.createDialog(this,AppHelper.getRemoteString("email_valid_error",this))
                }else{
                    sendContact()
                }
            }else{
                AppHelper.createDialog(this,AppHelper.getRemoteString("no_internet",this))
            }


        }

    }
    fun loadContent(content:String,webView: WebView){
        if(!doneOnce) {
            loading.show()
            webView.settings.javaScriptEnabled = true
            webView.settings.loadWithOverviewMode = true
            webView.settings.useWideViewPort = false
            webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
            webView.settings.builtInZoomControls = false
            webView.settings.displayZoomControls = false
            webView.settings.domStorageEnabled = true


            webView.webViewClient = object : WebViewClient() {
                private var running = 0
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                   // running++
                    logw("OVERRIDE","TRUE")
                    return true
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                   // super.onReceivedError(view, request, error)
                }

                override fun onReceivedSslError(
                    view: WebView?,
                    handler: SslErrorHandler?,
                    error: SslError?
                ) {

                   /* val builder = AlertDialog.Builder(this@ActivityWeb)
                    builder
                        .setMessage("Your current device version does not allow to trust the used service , if you proceed you will be at a security risk")
                        .setCancelable(true)
                        .setNegativeButton("Cancel") { dialog, _ ->
                            //doCancel()
                            finish()
                            super.onReceivedSslError(view, handler, error)
                            dialog.cancel()
                        }
                        .setPositiveButton("Proceed") { dialog, _ ->
                            handler!!.proceed()
                            loading.show()
                            loadContent(content,webView)
                        }
                    val alert = builder.create()
                    alert.show()
                    logw("SSL_ERROR", error.toString())*/

                    //handler!!.cancel()

                    llAcceptTerms.hide()
                    loading.hide()
                    logw("OVERRIDE", error.toString())
                    super.onReceivedSslError(view, handler, error)

                }

                override fun onReceivedHttpError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    errorResponse: WebResourceResponse?
                ) {
                    super.onReceivedHttpError(view, request, errorResponse)
                    WebStorage.getInstance().deleteAllData();

                    logw("OVERRIDE", "error")
                    // Clear all the cookies
                    CookieManager.getInstance().removeAllCookies(null);
                    CookieManager.getInstance().flush();

                    webView.clearCache(true);
                    webView.clearFormData();
                    webView.clearHistory();
                    webView.clearSslPreferences();
                    loadContent(content, webView)

                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    running = Math.max(running, 1)
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    doneOnce = true
                    logw("HTTPWORK", "done")
                    if (--running == 0) {
                        loading.hide()

                        if (MyApplication.fromSplash) {

                            llAcceptTerms.show()
                            //  btBackTool.hide()

                            cbTermsConditions.setOnCheckedChangeListener { buttonView, isChecked ->
                                btProceed.isEnabled = cbTermsConditions.isChecked
                            }
                            llCheckTerms.onOneClick {
                                if (cbTermsConditions.isChecked)
                                    cbTermsConditions.isChecked = false
                                else {
                                    cbTermsConditions.isChecked = true
                                }

                                btProceed.isEnabled = cbTermsConditions.isChecked
                            }


                            btProceed.onOneClick {

                                MyApplication.termsCondition = true
                                setResult(RESULT_OK, intent)
                                finish()
                                MyApplication.fromSplash = false
                            }
                        } else if (id == 4) {
                            linearContact.show()
                            wvData.hide()
                            var name = ""
                            name =
                                if (MyApplication.selectedUser!!.firstName != null) MyApplication.selectedUser!!.firstName!! + " " else "" +
                                        if (MyApplication.selectedUser!!.middleName != null) MyApplication.selectedUser!!.middleName!! + " " else "" +
                                                if (MyApplication.selectedUser!!.lastName != null) MyApplication.selectedUser!!.lastName!! + " " else ""

                            etFullNameContact.setText(name)
                            if (MyApplication.selectedUser!!.email != null)
                                etEmailContact.setText(MyApplication.selectedUser!!.email)

                            if (MyApplication.selectedUser!!.mobileNumber != null)
                                etPhoneContact.setText(MyApplication.selectedUser!!.mobileNumber)

                        }
                    }

                }
            }

            webView.setWebChromeClient(object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, newProgress: Int) {

                  /*  var prog = newProgress
                    *//*if(prog ==100)
                    loading.visibility= View.GONE*//*
                    var x = view.url
                    var y = 1*/

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
}