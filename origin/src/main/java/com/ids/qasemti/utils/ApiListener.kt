package com.ids.qasemti.utils

interface ApiListener {
    fun  onDataRetrieved(success:Boolean,response: Any,apiId:Int)
}