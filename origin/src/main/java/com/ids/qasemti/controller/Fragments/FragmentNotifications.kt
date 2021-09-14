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
import com.ids.qasemti.model.Notification
import com.ids.qasemti.utils.AppHelper
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.android.synthetic.main.fragment_notifications.*


class FragmentNotifications : Fragment(), RVOnItemClickListener {

    var array: ArrayList<Notification> = arrayListOf()
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

    fun init() {

        (activity as ActivityHome).showLogout(false)
        AppHelper.setTitle(requireActivity(), getString(R.string.notifications), "notifications")


        array.add(
            Notification(
                "Notification 1",
                "",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Image_created_with_a_mobile_phone.png/1200px-Image_created_with_a_mobile_phone.png"
            )
        )
        array.add(
            Notification(
                "Notification 2 ",
                "This is your lates Notification",
                "https://helpx.adobe.com/content/dam/help/en/photoshop/using/convert-color-image-black-white/jcr_content/main-pars/before_and_after/image-before/Landscape-Color.jpg"
            )
        )
        array.add(
            Notification(
                "Notification 3 ",
                "This notification is here for you to test whether or not you receive a notificaiton , please contact use if you receive this notificaiton , Thank you!",
                ""
            )
        )

        val animator: DefaultItemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }
        //  rvNotifications.setItemAnimator(animator)
        rvNotifications.layoutManager = LinearLayoutManager(context)
        adapter = AdapterNotification(array, this, requireContext())
        rvNotifications.adapter = adapter
    }

    override fun onItemClicked(view: View, position: Int) {

        array.get(position).open = !array.get(position).open


        adapter!!.notifyItemChanged(position)
    }
}