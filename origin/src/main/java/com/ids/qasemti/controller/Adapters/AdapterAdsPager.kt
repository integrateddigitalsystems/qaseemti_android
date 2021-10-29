package com.ids.qasemti.controller.Adapters


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager.widget.PagerAdapter
import com.ids.qasemti.R
import com.ids.qasemti.controller.Fragments.FragmentBottomSheetImage
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.ResponseBanner
import com.ids.qasemti.utils.*
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


internal class AdapterAdsPager(
    private val context: Context,
    private val arrayList: ArrayList<ResponseBanner>,
    lifecycle: Lifecycle,
    private var sfm: FragmentManager
) :
    PagerAdapter() {
    var lifecycle = lifecycle
    var supportFragmentManager = sfm
    lateinit var myYouTubePlayer: YouTubePlayer
    private val layoutInflater: LayoutInflater =
        this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o as View
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View
        var item = arrayList[position]
        view = layoutInflater.inflate(R.layout.item_media_pager, container, false)
        val ivFile: ImageView = view.findViewById(R.id.ivFile) as ImageView
        val youTubePlayerView: YouTubePlayerView =
            view.findViewById(R.id.youtube_player_view) as YouTubePlayerView

        youTubePlayerView.hide()
        ivFile.show()
        ivFile.loadImagesUrlResize(arrayList[position].bannerImageURL!!)
        if (!arrayList[position].bannerImageURL!!.isNullOrEmpty()) {
            ivFile.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(arrayList.get(position).siteUrl))
                context.startActivity(browserIntent)
                /*MyApplication.selectedImage=arrayList[position].bannerImageURL!!
                val bottom_fragment = FragmentBottomSheetImage()
                bottom_fragment.show(supportFragmentManager,"frag_image")*/
            }
        }


        container.addView(view)
        return view
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }


    fun stopPlayer() {
        myYouTubePlayer.pause()
    }


}