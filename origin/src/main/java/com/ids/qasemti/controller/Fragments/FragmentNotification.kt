package com.ids.qasemti.controller.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.controller.Adapters.AdapterNotification
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.model.Notification
import com.ids.qasemti.utils.AppHelper
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.android.synthetic.main.fragment_checkout.rootLayoutCheckout
import kotlinx.android.synthetic.main.fragment_notifications.*

class FragmentNotifications : Fragment() , RVOnItemClickListener {
    override fun onItemClicked(view: View, position: Int) {
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.qasemti.R.layout.fragment_notifications, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutNotifications)
        init()
    }

    fun init(){

        var array : ArrayList<Notification> = arrayListOf()
        array.add(Notification("Notification 1","","https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Image_created_with_a_mobile_phone.png/1200px-Image_created_with_a_mobile_phone.png"))
        array.add(Notification("Notification 2 ","This is your lates Notification","https://helpx.adobe.com/content/dam/help/en/photoshop/using/convert-color-image-black-white/jcr_content/main-pars/before_and_after/image-before/Landscape-Color.jpg"))
        array.add(Notification("Notification 2 ","This notification is here for you to test whether or not you receive a notificaiton , please contact use if you receive this notificaiton , Thank you!",""))

        rvNotifications.layoutManager = LinearLayoutManager(context)
        rvNotifications.adapter = AdapterNotification(array,this,requireContext())
    }
}