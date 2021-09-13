package com.ids.qasemti.controller.Adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ids.qasemti.R
import com.ids.qasemti.controller.Fragments.PageFragment


class SampleFragmentPagerAdapter(fm: FragmentManager?, private val context: Context) :
    FragmentPagerAdapter(fm!!) {
    val PAGE_COUNT = 5
    private val tabTitles = arrayOf("Tab1", "Tab2", "Tab3", "Tab4", "Tab5")
    override fun getCount(): Int {
        return PAGE_COUNT
    }

    fun getTabView(position: Int): View? {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        val v: View = LayoutInflater.from(context).inflate(R.layout.footer_top, null)
        return v
    }

    override fun getItem(position: Int): Fragment {
        return PageFragment.newInstance(position + 1)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        // Generate title based on item position
        return tabTitles[position]
    }
}