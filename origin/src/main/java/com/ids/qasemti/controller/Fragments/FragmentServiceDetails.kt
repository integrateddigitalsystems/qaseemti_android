package com.ids.qasemti.controller.Fragments

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.material.tabs.TabLayout
import com.ids.makhateer.controller.Adapters.PageAdapterSrvice
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityCheckout
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Adapters.AdapterGeneralSpinner
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.SliderItem
import com.ids.qasemti.utils.AppConstants
import com.ids.qasemti.utils.AppHelper
import com.ids.qasemti.utils.hide
import com.ids.qasemti.utils.show
import com.ids.sampleapp.model.ItemSpinner
import kotlinx.android.synthetic.main.fragment_service_details.*

import java.util.ArrayList

class FragmentServiceDetails : Fragment() ,  com.google.android.exoplayer2.Player.EventListener {


    var selectedPosition: Int = 0
    var selectedC: Drawable? = null
    private var mediaSource: MediaSource? = null
    private var dataSourceFactory: DataSource.Factory? = null
    private var extractorsFactory: ExtractorsFactory? = null
    var notSelectedC: Drawable? = null
    var arrayItems: ArrayList<SliderItem> = arrayListOf()
    var theViews: ArrayList<View>? = arrayListOf()
    var playerView: StyledPlayerView? = null
    var tabs: TabLayout? = null
    var players = java.util.ArrayList<SimpleExoPlayer>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.qasemti.R.layout.fragment_service_details, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutServiceDetails,requireContext())
        init()


    }

    fun initiatePager(URL: String, player: SimpleExoPlayer) {
        extractorsFactory = DefaultExtractorsFactory()
        // trackSelectionFactory  =  new AdaptiveTrackSelection.Factory()
        //trackSelector = new AdaptiveTrackSelection(trackSelectionFactory,bandwidthMeter);
        dataSourceFactory = DefaultDataSourceFactory(
            requireContext(),
            Util.getUserAgent(requireContext(), "mediaPlayerSample")
        )


        var media = MediaItem.fromUri(Uri.parse(URL))
        mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory!!)
            .createMediaSource(media)
        player.addListener(this)
        player.addMediaSource(mediaSource!!)
        player.prepare()
        player.playWhenReady = true
        playerView!!.setPlayer(player)
    }

    fun turnOffVideos() {
        for (item in players) {
            item.playWhenReady = false
        }
    }

    fun setUpMediaPager() {

        for (item in arrayItems) {

            val inflater = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var viewer: View = inflater.inflate(R.layout.item_media_pager, null)
            theViews!!.add(viewer)
            var iv = viewer.findViewById<ImageView>(R.id.theTitleImage)
            playerView = viewer.findViewById(R.id.simpleExoPlayerView4)
            val full =
                viewer.findViewById<ImageView>(R.id.btFullScreen)
            val backgroudImage =
                viewer.findViewById<LinearLayout>(R.id.llBackgroundImage)
            val videoLayout = viewer.findViewById<RelativeLayout>(R.id.llPlayer)
            var player: SimpleExoPlayer


            //   setUpCornerRadius(iv)
            // setUpCornerRadius(playerView!!)
            //   setUpCornerRadius(backgroudImage)
            //  player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            //  player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            player = SimpleExoPlayer.Builder(requireContext()).build()
            players.add(player)

            if (item.type == 1) {

                iv.show()
                Glide.with(iv)
                    .load(item.url)
                    .fitCenter()
                    .into(iv)

                iv.setOnClickListener {
                    turnOffVideos()
                    MyApplication.firstImage = true
                    MyApplication.selectedImage = item.url
                    MyApplication.selectedVideo = "-"
               //     startActivity(Intent(this, ActivityFullScreen::class.java))
                }
                // Glide.with(iv.getContext()).load(multiplePages.get(y).getImage()).into(iv);
                /*Glide.with(iv.getContext()).load(pages.get(i).getImage()).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        iv.setImageBitmap(resource);
                    }
                });*/
                // Glide.with(iv.getContext()).load(multiplePages.get(y).getImage()).into(iv);
                /*Glide.with(iv.getContext()).load(pages.get(i).getImage()).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        iv.setImageBitmap(resource);
                    }
                });*/


            } else {
                initiatePager(item.url!!, player)
                player.playWhenReady = false
                backgroudImage.hide()
                playerView!!.show()
                videoLayout.show()
                iv.hide()
                full.show()
                full.setOnClickListener {
                    turnOffVideos()
                    MyApplication.selectedImage = "-"
                    MyApplication.selectedVideo = item.url
                  //  startActivity(Intent(this, ActivityFullScreen::class.java))
                    /*  MyApplication.selectedImage = ""
                      MyApplication.selectedVideo = pages.get(position).getVideo()
                      startActivity(Intent(this, ActivityImages::class.java))*/
                }
            }
        }



        vpServiceDetails.setAdapter(PageAdapterSrvice(theViews!!, requireActivity()))
        if (theViews!!.size > 1) {

            tbMedias.setupWithViewPager(vpServiceDetails)
            setUpTabs()


            vpServiceDetails.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    for (i in 0 until arrayItems.size) {
                        if (i == position)
                            tbMedias!!.getTabAt(i)!!.setIcon(selectedC)
                        else
                            tbMedias!!.getTabAt(i)!!.setIcon(notSelectedC)

                    }
                    selectedPosition = position
                    //  shareData( arrayItems.get(selectedPosition).url!!)
                    /* MyApplication.menu_position = position
                     MyApplication.PageNbr = position
                     if (MyApplication.PageNbr === 0) {
                         page1.setVisibility(View.VISIBLE)
                         page2.setVisibility(View.GONE)
                     } else {
                         page1.setVisibility(View.GONE)
                         page2.setVisibility(View.VISIBLE)
                     }*/
                }

                override fun onPageScrollStateChanged(state: Int) {
                    turnOffVideos()
                }
            })
        } else {
            tbMedias.hide()
        }

    }

    fun setUpTabs() {
        for (i in 0 until arrayItems.size) {
            if (i == 0)
                tbMedias!!.getTabAt(i)!!.setIcon(selectedC)
            else
                tbMedias!!.getTabAt(i)!!.setIcon(notSelectedC)
        }
    }

  /*  fun setUpCornerRadius() {

       // val image = requireActivity().findViewById<ImageView>(R.id.ivServiceDetail)
        val curveRadius = resources.getDimensionPixelSize(R.dimen.normal_radius).toFloat()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            image.outlineProvider = object : ViewOutlineProvider() {

                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun getOutline(view: View?, outline: Outline?) {
                    outline?.setRoundRect(
                        view!!.left,
                        view.top,
                        view!!.width,
                        (view.height + curveRadius).toInt(),
                        curveRadius
                    )
                }
            }

            image.clipToOutline = true

        }
    }*/


    private fun convertToBitmap(drawable: Drawable): Bitmap? {
        val mutable = Bitmap.createBitmap(30, 30, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mutable)
        drawable.setBounds(0, 0, 30, 30)
        drawable.draw(canvas)
        return mutable
    }


    fun init(){
        (activity as ActivityHome?)!!.showBack(true)
        (activity as ActivityHome?)!!.showLogout(false)


        btServiceCheckout.typeface=AppHelper.getTypeFace(requireContext())
        btServiceCheckout.setOnClickListener {
            /*MyApplication.selectedFragment = FragmentCheckout()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.homeContainer, FragmentCheckout(), AppConstants.CHECKOUT)
                .commit()*/
            startActivity(Intent(requireContext(),ActivityCheckout::class.java))
        }
        if(!MyApplication.selectedService!!.title.isNullOrEmpty()) {
            AppHelper.setTitle(requireActivity(), MyApplication.selectedService!!.title!!, "")
        }
        tbMedias.setTabTextColors(
            AppHelper.getColor(requireContext(), R.color.transparent),
            AppHelper.getColor(requireContext(), R.color.transparent)
        )

        val not_selected_circle = AppHelper.getDrawable(requireContext(), R.drawable.not_selected)
        val selected_circle = AppHelper.getDrawable(requireContext(), R.drawable.selected)
// Scale it to 50 x 50
        // Scale it to 50 x 50
        selectedC = BitmapDrawable(
            resources,
            Bitmap.createScaledBitmap(convertToBitmap(selected_circle)!!, 20, 20, true)
        )
        notSelectedC = BitmapDrawable(
            resources,
            Bitmap.createScaledBitmap(convertToBitmap(not_selected_circle)!!, 20, 20, true)
        )

        arrayItems.add(SliderItem("http://sc04.alicdn.com/kf/He4dba8a9ab794fc0b22a7ae23c246fd4G.jpg",1,""))
        arrayItems.add(SliderItem("https://sc04.alicdn.com/kf/HTB1jpkfl_nI8KJjSszgq6A8ApXaC.jpg",1,""))
        arrayItems.add(SliderItem("https://img.bidorbuy.co.za/image/upload/user_images/999/369999_100819205424_Tank-Sigi.jpg",1,""))

        setUpMediaPager()

        // setUpCornerRadius()


        var arrayType : ArrayList<ItemSpinner> = arrayListOf()

        arrayType.add(ItemSpinner(0,"Fresh",""))
        arrayType.add(ItemSpinner(1,"First Edition",""))
        arrayType.add(ItemSpinner(2,"Used",""))

        var arraySize : ArrayList<ItemSpinner> = arrayListOf()

        arraySize.add(ItemSpinner(0,"Small",""))
        arraySize.add(ItemSpinner(1,"Medium",""))
        arraySize.add(ItemSpinner(2,"Big",""))


        val adapterSerType = AdapterGeneralSpinner(
            requireContext(),
            R.layout.spinner_layout,
            arrayType,
            AppConstants.LEFT_BLACK
        )

        spServiceType.adapter = adapterSerType
        adapterSerType.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spServiceType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }


        }

        val adapterSerSize = AdapterGeneralSpinner(
            requireContext(),
            R.layout.spinner_layout,
            arraySize,
            AppConstants.LEFT_BLACK
        )

        spServiceSize.adapter = adapterSerSize
        adapterSerSize.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spServiceSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }


        }



    }
}