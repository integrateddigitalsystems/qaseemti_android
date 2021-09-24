package com.ids.qasemti.controller.Activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import com.bumptech.glide.Glide.init
import com.ids.qasemti.R
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.setColorTypeface
import com.ids.qasemti.utils.show
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.toolbar.*

class ActivityWeb: ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        init()

    }

    fun init(){
        rlToolBar.setBackgroundColor(AppHelper.getColor(this,R.color.redPrimary))
        btBackTool.show()
        tvPageTitle.show()
        AppHelper.setLogoTint(btBackTool,this,R.color.white)
        var title = intent.getStringExtra("webTitle")
        tvPageTitle.setColorTypeface(this,R.color.white,title!!,true)
        btBackTool.setOnClickListener {
            super.onBackPressed()
        }
        loadContent("https://mokhtar-fund.gov.lb/")
    }
    fun loadContent(content:String){
        wvData.settings.javaScriptEnabled=true
        wvData.settings.loadWithOverviewMode = true
        wvData.settings.useWideViewPort = false
        wvData.settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvData.settings.builtInZoomControls = false
        wvData.settings.displayZoomControls = false


        wvData.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }
        }

        wvData.setWebChromeClient(object : WebChromeClient() {
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

        wvData.loadUrl(content!!);
    }
}