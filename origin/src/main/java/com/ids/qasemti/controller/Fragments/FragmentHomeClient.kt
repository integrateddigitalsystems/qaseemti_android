package com.ids.qasemti.controller.Fragments

import android.app.ActionBar
import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Adapters.AdapterAdsPager
import com.ids.qasemti.controller.Adapters.AdapterGeneralSpinner
import com.ids.qasemti.controller.Adapters.AdapterServices
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import com.ids.sampleapp.model.ItemSpinner
import kotlinx.android.synthetic.main.fragment_home_client.*
import kotlinx.android.synthetic.main.loading.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.youtube.player.internal.v

import com.google.android.youtube.player.internal.r

import com.google.android.youtube.player.internal.t

import com.google.android.youtube.player.internal.l
import kotlinx.android.synthetic.main.fragment_home_client.tbMedia
import kotlinx.android.synthetic.main.fragment_service_details.*
import androidx.viewpager.widget.ViewPager





class FragmentHomeClient : Fragment(), RVOnItemClickListener,ApiListener {

    var dialog: Dialog? = null
    var arrayAllServices: ArrayList<ResponseService> = arrayListOf()
    var arrayFiltered: ArrayList<ResponseService> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    var adapter : AdapterServices ?=null

    var arraySpinnerServices: ArrayList<ItemSpinner> = arrayListOf()
    var arraySpinnerTypes: ArrayList<ItemSpinner> = arrayListOf()
    var arraySpinnerSizes: ArrayList<ItemSpinner> = arrayListOf()
    private var selectedCategoryId = 0
    private var selectedCategoryName = ""
    private var selectedServiceId = 0
    private var selectedServiceName = ""
    private var selectedTypeId = 0
    private var selectedTypeName = ""
    private var selectedSizeId = 0
    private var selectedSizeName = ""
    private var selectedQtyId = 0
    private var selectedQtyName = ""
    var timer : CountDownTimer?=null
    var isTimer = false

    lateinit var spServices: Spinner
    lateinit var spType: Spinner
    lateinit var spServiceCapactity: Spinner
    lateinit var rgCategory:RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(com.ids.qasemti.R.layout.fragment_home_client, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutServices, requireContext())
        init()


    }

    fun setUpTimer(){
        timer = object : CountDownTimer(30000, 1015) {
            override fun onTick(millisUntilFinished: Long) {
                //logw("tick","second")
            }

            override fun onFinish() {
                isTimer = true
                if (AppHelper.isOnline(requireContext())) {
                    getServices(isTimer)
                }else{
                    AppHelper.createDialog(requireActivity(),AppHelper.getRemoteString("no_internet",requireContext()))
                }

            }
        }.start()
    }

    fun init() {
/*
        try {
            MyApplication.arrayCart = AppHelper.fromGSon()
            AppHelper.getArrayCarts()
        }catch (ex:Exception){
        }*/

        /*   linearProfileInfo.setOnClickListener {
               startActivity(Intent( requireActivity(),ActivitySelectAddress::class.java))
           }*/
        if (MyApplication.isSignedIn) {
            (activity as ActivityHome?)!!.showLogout(true)
            btRegisterLogin.hide()
        } else {
            btRegisterLogin.show()
            (activity as ActivityHome?)!!.showLogout(false)
        }



        btRegisterLogin.onOneClick {
            (activity as ActivityHome)!!.goRegistration(
                2,
                AppConstants.FRAGMENT_HOME_CLIENT,
                FragmentHomeClient(),
                R.color.white
            )
        }


        (activity as ActivityHome?)!!.showBack(false)

        btFilter.onOneClick {
            showPopupSocialMedia()
        }

        rvServices.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val topRowVerticalPosition =
                    if (recyclerView == null || recyclerView.childCount == 0) 0 else recyclerView.getChildAt(
                        0
                    ).top
                listHomeClient.setEnabled(topRowVerticalPosition >= 0)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })



        listHomeClient.setOnRefreshListener {
            listHomeClient.isRefreshing = false
            getBanners()
            getServices(false)
        }



        AppHelper.setTitle(requireActivity(), MyApplication.selectedTitle!!, "", R.color.white)

        if (AppHelper.isOnline(requireContext())) {
            getServices(false)

            getBanners()

            setUpTimer()
        }else{
            AppHelper.createDialog(requireActivity(),AppHelper.getRemoteString("no_internet",requireContext()))
        }


    }

    fun setBannerData(arrayItems: ArrayList<ResponseBanner>) {
        if (arrayItems.size > 0) {
            var array =
                arrayItems.filter { it.bannerImageURL != "false" && !it.bannerImageURL.isNullOrEmpty() } as ArrayList
            if(array.size >0) {
                linearHomeClient.show()
                var adapterPager = AdapterAdsPager(
                    requireActivity(),
                    array,
                    lifecycle,
                    requireActivity().supportFragmentManager
                )
                tbMedia.setupWithViewPager(vpAdsClient)
                if(MyApplication.languageCode == AppConstants.LANG_ARABIC) {
                    vpAdsClient.setRotationY(180f)
                    vpAdsClient.setPageTransformer(false,
                        ViewPager.PageTransformer { page, position -> page.rotationY = 180f })
                }
                vpAdsClient.adapter = adapterPager

            }else{
                linearHomeClient.hide()
                val p = listHomeClient.getLayoutParams() as ViewGroup.MarginLayoutParams
                p.setMargins(0, -130, 0, 0)
                listHomeClient.requestLayout()
            }
        } else {
            linearHomeClient.hide()
            val p = listHomeClient.getLayoutParams() as ViewGroup.MarginLayoutParams
            p.setMargins(0, 65, 0, 0)
            listHomeClient.requestLayout()
        }
    }

    fun getBanners() {

        loading.show()
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getBanners(
            )?.enqueue(object : Callback<ResponseMainBanner> {
                override fun onResponse(
                    call: Call<ResponseMainBanner>,
                    response: Response<ResponseMainBanner>
                ) {
                    try {
                        setBannerData(response.body()!!.banners)

                    } catch (E: java.lang.Exception) {
                        if(loading!=null)
                            loading.hide()
                        try{setBannerData(arrayListOf())}catch (e:Exception){}


                    }
                }

                override fun onFailure(call: Call<ResponseMainBanner>, throwable: Throwable) {
                    if(loading!=null)
                        loading.hide()
                    try{setBannerData(arrayListOf())}catch (e:Exception){}
                }
            })
    }

    private fun showPopupSocialMedia() {


        dialog = Dialog(requireContext(), R.style.Base_ThemeOverlay_AppCompat_Dialog)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.setContentView(R.layout.dialog_service)
        dialog!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialog!!.window!!.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
        dialog!!.setCancelable(true)


        dialog!!.setOnCancelListener {
            btFilter.show()
            dialog!!.cancel()
        }
        dialog!!.setOnDismissListener {
            btFilter.show()
            dialog!!.cancel()
        }
        btFilter.hide()
        var close = dialog!!.findViewById<ImageView>(R.id.btClose)

        spServices = dialog!!.findViewById<Spinner>(R.id.spServices)
        spType = dialog!!.findViewById<Spinner>(R.id.spType)
        spServiceCapactity = dialog!!.findViewById<Spinner>(R.id.spServiceCapactity)
        var btResetFilter = dialog!!.findViewById<Button>(R.id.btResetFilter)
        var btApplyFilter = dialog!!.findViewById<Button>(R.id.btApplyFilter)
/*        var rbPurchase = dialog!!.findViewById<RadioButton>(R.id.rbPurchase)
        var rbRental = dialog!!.findViewById<RadioButton>(R.id.rbRental)*/
        rgCategory = dialog!!.findViewById<RadioGroup>(R.id.rgCategory)

        if(MyApplication.categories.size>0){
           setCategoriesDialog()
        }else
            CallAPIs.getCategories(requireActivity(),this)


        if (arrayAllServices.size > 0)
            setServiceSpinner()

        close.onOneClick {
            btFilter.show()
            dialog!!.cancel()
        }

        btApplyFilter.onOneClick {
            arrayFiltered.clear()
            arrayFiltered.addAll(arrayAllServices.filter { it.id == selectedServiceId.toString() })
            setData()
            btFilter.show()
            dialog!!.cancel()
        }

        btResetFilter.onOneClick {
            arrayFiltered.clear()
            arrayFiltered.addAll(arrayAllServices)
            setData()
            btFilter.show()
            dialog!!.cancel()
        }


        //btCancell!!.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()

    }

    private fun setCategoriesDialog(){
        val params =
            LinearLayout.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT, 1f)

        for (i in MyApplication.categories.indices) {
            val rbn = RadioButton(requireActivity())
            rbn.id = MyApplication.categories[i].id!!.toInt()
            rbn.text = MyApplication.categories[i].getName().capitalized()
            rbn.layoutParams = params
            rgCategory.addView(rbn)
        }
        if(selectedCategoryId ==0) {
            selectedCategoryId = MyApplication.categories[0].id!!.toInt()
            selectedCategoryName = MyApplication.categories[0].getName().capitalized()
        }
        if (arrayAllServices.size > 0)
            setServiceSpinner()
        rgCategory.check(selectedCategoryId)
        rgCategory.setOnCheckedChangeListener { group, checkedId ->
            val rb = dialog!!.findViewById<View>(checkedId) as RadioButton
            selectedCategoryId = checkedId
            selectedCategoryName = rb.text.toString()
            if (arrayAllServices.size > 0)
                setServiceSpinner()
        }
    }


    private fun setServiceSpinner() {
        arraySpinnerServices.clear()
        var arrayFiltered =
            arrayAllServices.filter {
                it.type!!.lowercase() == selectedCategoryName.lowercase()
            }
        for (i in arrayFiltered.indices) {
            if (arrayFiltered[i].name != null && arrayFiltered[i].name!!.isNotEmpty())
                arraySpinnerServices.add(
                    ItemSpinner(
                        arrayFiltered[i].id!!.toInt(),
                        arrayFiltered[i].name,
                        ""
                    )
                )
        }
        val adapterServices = AdapterGeneralSpinner(
            requireActivity(),
            R.layout.spinner_layout,
            arraySpinnerServices,
            0
        )
        spServices.adapter = adapterServices
        adapterServices.setDropDownViewResource(R.layout.item_spinner_drop_down)
        spServices.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedServiceId = arraySpinnerServices[position].id!!
                selectedServiceName = arraySpinnerServices[position].name!!
                setTypeSpinner()
                setSizeCapacitySpinner()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }


    private fun setTypeSpinner() {

        var selectedArray = arrayAllServices.find { it.id == selectedServiceId.toString() }
        var arrayTypes = selectedArray!!.variations.distinctBy { it.types }
        arraySpinnerTypes.clear()
        for (i in arrayTypes.indices) {
            if (arrayTypes[i].types != null && arrayTypes[i].types!!.isNotEmpty())
                arraySpinnerTypes.add(ItemSpinner(i, arrayTypes[i].types, ""))
        }
        val adapterTypes =
            AdapterGeneralSpinner(requireActivity(), R.layout.spinner_layout, arraySpinnerTypes, 0)
        spType.adapter = adapterTypes
        adapterTypes.setDropDownViewResource(R.layout.item_spinner_drop_down)
        spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedTypeId = arraySpinnerTypes[position].id!!
                selectedTypeName = arraySpinnerTypes[position].name!!
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }


    private fun setSizeCapacitySpinner() {
        var selectedArray = arrayAllServices.find { it.id == selectedServiceId.toString() }
        var arrayTypes = selectedArray!!.variations.distinctBy { it.sizeCapacity }
        arraySpinnerSizes.clear()
        for (i in arrayTypes.indices) {
            if (arrayTypes[i].sizeCapacity != null && arrayTypes[i].sizeCapacity!!.isNotEmpty())
                arraySpinnerSizes.add(ItemSpinner(i, arrayTypes[i].sizeCapacity, ""))
        }
        val adapterSize =
            AdapterGeneralSpinner(requireActivity(), R.layout.spinner_layout, arraySpinnerSizes, 0)
        spServiceCapactity.adapter = adapterSize
        adapterSize.setDropDownViewResource(R.layout.item_spinner_drop_down)
        spServiceCapactity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedSizeId = arraySpinnerSizes[position].id!!
                selectedSizeName = arraySpinnerSizes[position].name!!

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }


    fun getServices(timer:Boolean) {
        if(!timer)
            loading.show()

        var newReq = RequestLanguage(MyApplication.languageCode)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getClServices(
                newReq
            )?.enqueue(object : Callback<ResponseMainServices> {
                override fun onResponse(
                    call: Call<ResponseMainServices>,
                    response: Response<ResponseMainServices>
                ) {
                    try {
                        loading.hide()
                        if(arrayAllServices.size >0 && rvServices.canScrollVertically(1)){
                            if(arrayAllServices.size < response.body()!!.responseService!!.size)
                                Toast.makeText(requireContext(),"New Data Added",Toast.LENGTH_SHORT).show()
                        }
                        arrayAllServices.clear()
                        arrayFiltered.clear()
                        arrayAllServices.addAll(response.body()!!.responseService!!)
                        arrayFiltered.addAll(response.body()!!.responseService!!)
                        setData()
                    } catch (E: java.lang.Exception) {
                        arrayAllServices.clear()
                        arrayFiltered.clear()
                        setData()
                    }
                }

                override fun onFailure(call: Call<ResponseMainServices>, throwable: Throwable) {
                    try {
                        loading.hide()
                        arrayAllServices.clear()
                        arrayFiltered.clear()
                        setData()
                    }catch (ex:Exception){

                    }
                }
            })
    }

    private fun setData() {
        try {
            /* if (arrayFiltered.size == 0) {
                rvServices.hide()
                btFilter.hide()
                tvNoDataClient.show()




            } else {*/

            /* var data = "{\"id\":202,\"featured_image\":\"https:\\/\\/dev.qasemti.com\\/wp-content\\/uploads\\/2021\\/06\\/a-Horizontal-and-b-vertical-roof-top-Polyethylene-Water-Storage-Tanks-available-in-the.png\",\"type\":\"purchase\",\"name\":\"خزان مياه  \",\"description\":\"خزان مياه بالمياه لمواقع البناء\",\"eta\":\"1 Day\",\"variations\":[{\"size-capacity\":\"1000 " +
                    "Gallons\",\"types\":\"مياه نقية\",\"price\":10,\"earnings\":9,\"fees\":1,\"is_in_stock\":true,\"images\":[\"https:\\/\\/dev.qasemti.com\\/wp-content\\/uploads\\/2021\\/07\\/download-8-1.jpg\",\"https:\\/\\/dev.qasemti.com\\/wp-content\\/uploads\\/2021\\/07\\/download-3-1.jpg\",\"https:\\/\\/dev.qasemti.com\\/wp-content\\/uploads\\/2021\\/07\\/liebherr-kelly-drilling-bohren-verrohrt-cased-lb-28-rotary-drilling-rig-hydraulic-casing-oscillators.jpg\"],\"stock_quantity\":10},{\"size-capacity\":\"3000 " +
                    "Gallons\",\"types\":\"Fresh " +
                    "Water\",\"price\":20,\"earnings\":18,\"fees\":2,\"is_in_stock\":true,\"images\":[\"https:\\/\\/dev.qasemti.com\\/wp-content\\/uploads\\/2021\\/06\\/a-Horizontal-and-b-vertical-roof-top-Polyethylene-Water-Storage-Tanks-available-in-the.png\"],\"stock_quantity\":0},{\"size-capacity\":\"5000 " +
                    "Gallons\",\"types\":\"Fresh " +
                    "Water\",\"price\":30,\"earnings\":27,\"fees\":3,\"is_in_stock\":true,\"images\":[\"https:\\/\\/dev.qasemti.com\\/wp-content\\/uploads\\/2021\\/06\\/a-Horizontal-and-b-vertical-roof-top-Polyethylene-Water-Storage-Tanks-available-in-the.png\"],\"stock_quantity\":0},{\"size-capacity\":\"1000 " +
                    "Gallons\",\"types\":\"Not Fresh " +
                    "Water\",\"price\":10,\"earnings\":9,\"fees\":1,\"is_in_stock\":true,\"images\":[\"https:\\/\\/dev.qasemti.com\\/wp-content\\/uploads\\/2021\\/06\\/a-Horizontal-and-b-vertical-roof-top-Polyethylene-Water-Storage-Tanks-available-in-the.png\"],\"stock_quantity\":0},{\"size-capacity\":\"3000 " +
                    "Gallons\",\"types\":\"مياه غير نقية \",\"price\":20,\"earnings\":18,\"fees\":2,\"is_in_stock\":true,\"images\":[\"https:\\/\\/dev.qasemti.com\\/wp-content\\/uploads\\/2021\\/06\\/a-Horizontal-and-b-vertical-roof-top-Polyethylene-Water-Storage-Tanks-available-in-the.png\"],\"stock_quantity\":0},{\"size-capacity\":\"5000 " +
                    "Gallons\",\"types\":\"Not Fresh " +
                    "Water\",\"price\":30,\"earnings\":27,\"fees\":3,\"is_in_stock\":true,\"images\":[\"https:\\/\\/dev.qasemti.com\\/wp-content\\/uploads\\/2021\\/06\\/a-Horizontal-and-b-vertical-roof-top-Polyethylene-Water-Storage-Tanks-available-in-the.png\"],\"stock_quantity\":0}],\"booking_min\":1,\"booking_max\":1,\"additional_price_if_distance__10km\":\"10\",\"additional_price_if_distance__20km\":\"20\",\"additional_price_if_distance__30km\":\"30\",\"additional_price_if_distance__40km\":\"40\",\"additional_price_if_distance__50km\":\"50\",\"files\":[{\"id\":\"1\",\"url\":\"https:\\/\\/dev.qasemti.com\\/wp-content\\/uploads\\/media_files\\/2_tree-gc0eeadec0_1280.jpgFile\",\"name\":\"driving " +
                    "licence\"},{\"id\":\"2\",\"url\":\"https:\\/\\/dev.qasemti.com\\/wp-content\\/uploads\\/media_files\\/2_tree-gc0eeadec0_1280.jpgFile\",\"name\":\"vehicle " +
                    "licence\"}]}"

            var datum = Gson().fromJson(data, ResponseService::class.java)
            arrayFiltered.add(datum)
                tvNoDataClient.hide()
                var adapter = AdapterServices(arrayFiltered, this, requireContext())
                rvServices.layoutManager = LinearLayoutManager(requireContext())
                rvServices.adapter = adapter
                rvServices.isNestedScrollingEnabled = false*/
            //}



            if (arrayFiltered.size == 0) {
                rvServices.hide()
                btFilter.hide()
                tvNoDataClient.show()
            } else {
                if (adapter != null) {
                    adapter!!.notifyDataSetChanged()
                } else {
                    tvNoDataClient.hide()
                    adapter = AdapterServices(arrayFiltered, this, requireContext())
                    rvServices.layoutManager = LinearLayoutManager(requireContext())
                    rvServices.adapter = adapter
                    rvServices.isNestedScrollingEnabled = false
                }
            }


        }catch (ex:Exception){}

        try {
            listHomeClient.isRefreshing = false
        }catch (ex:Exception){}

        if(isTimer){
            isTimer = false
            timer!!.start()
        }
    }

    override fun onResume() {
        super.onResume()
        if(MyApplication.selectedFragmentTag==AppConstants.FRAGMENT_SERVICE_DETAILS){
            logw("nothing","nothing Happens")
        }else{
            AppHelper.setTitle(requireActivity(),"", "Services",R.color.white)
        }
        timer!!.start()
    }

    override fun onStop() {
        super.onStop()
        timer!!.cancel()
    }

    override fun onPause() {
        super.onPause()
        timer!!.cancel()
    }
    override fun onItemClicked(view: View, position: Int) {

        if (view.id == R.id.linearService) {
            AppHelper.onOneClick {
                MyApplication.selectedFragment = FragmentServiceDetails()
                MyApplication.selectedService = arrayFiltered.get(position)
                MyApplication.rental = position == 2
                MyApplication.position = position

                (requireActivity() as ActivityHome?)!!.addFrag(
                    FragmentServiceDetails(),
                    AppConstants.FRAGMENT_SERVICE_DETAILS
                )
            }
        }

    }

    override fun onDataRetrieved(success: Boolean, response: Any, apiId: Int) {
       if(apiId == AppConstants.GET_CATEGORIES && dialog!=null)
           setCategoriesDialog()
    }
}