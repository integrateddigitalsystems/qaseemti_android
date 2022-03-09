package com.ids.qasemti.controller.Fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager

import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityCheckout
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Adapters.AdapterGeneralSpinner
import com.ids.qasemti.controller.Adapters.AdapterMediaPager
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.ServiceVariation
import com.ids.qasemti.model.SliderItem
import com.ids.qasemti.utils.*
import com.ids.sampleapp.model.ItemSpinner
import kotlinx.android.synthetic.main.fragment_service_details.*
import java.util.*
import kotlin.collections.ArrayList


class FragmentServiceDetails : Fragment() ,  com.google.android.exoplayer2.Player.EventListener {


    var arraySpinnerTypes: ArrayList<ItemSpinner> = arrayListOf()
    var arraySpinnerSizes: ArrayList<ItemSpinner> = arrayListOf()
    var arrayItems: ArrayList<SliderItem> = arrayListOf()

    private var selectedServiceId=0
    private var selectedServiceName=""
    private var selectedTypeId=0
    private var selectedTypeName=""
    private var selectedSizeId=0
    private var selectedSizeName=""


    private lateinit var adapterPager: AdapterMediaPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.qasemti.R.layout.fragment_service_details, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutServiceDetails,requireContext())
        (requireActivity() as ActivityHome).setTintLogo(R.color.white)
        init()


    }




    fun init(){
        (activity as ActivityHome?)!!.showBack(true)
        (activity as ActivityHome?)!!.showLogout(false)

        tvItalicNote.typeface = AppHelper.getTypeFaceItalic(requireContext())



        btServiceCheckout.typeface=AppHelper.getTypeFace(requireContext())
        btServiceCheckout.onOneClick {
            if(MyApplication.isSignedIn) {
                MyApplication.renewing = false
                MyApplication.repeating = false
                MyApplication.selectedVariationType = selectedTypeId
                MyApplication.selectedSize = selectedSizeId
                startActivity(Intent(requireContext(), ActivityCheckout::class.java))
            }else{
                MyApplication.selectedNotSignedInType = selectedTypeId
                MyApplication.selectedNotSignedInSize = selectedSizeId
                (activity as ActivityHome?)!!.goRegistration(2,AppConstants.FRAGMENT_SERVICE_DETAILS,FragmentServiceDetails(),R.color.white)
            }
        }
        try{
            AppHelper.setTitle(requireActivity(), MyApplication.selectedService!!.name!!, "",R.color.white)
        }catch (ex:Exception){

        }

        setTypeSpinner()
        setSizeCapacitySpinner()
        try{tvDescription.text=MyApplication.selectedService!!.desc!!}catch (e:Exception){}

    }




    private fun setMediaPager(){


        arrayItems.clear()
            var selectedVariation : ServiceVariation ?=null
            try {
                selectedVariation = MyApplication.selectedService!!.variations.find {
                    ( if(it.sizeCapacity!=null) it.sizeCapacity == selectedSizeName else true ) && (if(it.types!=null) it.types == selectedTypeName else true)
                }!!
            }catch (ex:Exception){
                Log.wtf("error","error")
            }
            var arrayMedia : ArrayList<String> = arrayListOf()
            if(selectedVariation!=null)
                arrayMedia=selectedVariation!!.images
            try{
                MyApplication.selectedPrice = selectedVariation!!.price!!
                tvPrice.text=selectedVariation!!.price+""}catch (e:Exception){}
            if(arrayMedia.size>0){
                for (i in arrayMedia.indices)
                    arrayItems.add(SliderItem(arrayMedia[i],1,""))

            }else{
                arrayItems.add(SliderItem("",1,""))
            }


        adapterPager = AdapterMediaPager(requireActivity(),arrayItems,lifecycle,requireActivity().supportFragmentManager)
        vpMedia.adapter = adapterPager
        tbMedia.setupWithViewPager(vpMedia)
        vpMedia?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                try{adapterPager.stopPlayer()}catch (e:Exception){}
            }

        })

        if(arrayItems.size<=1)
            tbMedia.hide()
        else
            tbMedia.show()

    }



    private fun setTypeSpinner(){
        var arrayTypes : List<ServiceVariation> = arrayListOf()
        try {
            arrayTypes = MyApplication.selectedService!!.variations.distinctBy { it.types }
        }catch (ex:java.lang.Exception){

        }

            arraySpinnerTypes.clear()
            for (i in arrayTypes.indices) {
                if (arrayTypes[i].types != null && arrayTypes[i].types!!.isNotEmpty())
                    arraySpinnerTypes.add(
                        ItemSpinner(
                            arrayTypes[i].typesId!!.toInt(),
                            arrayTypes[i].types,
                            ""
                        )
                    )
            }
            val adapterTypes = AdapterGeneralSpinner(
                requireActivity(),
                R.layout.spinner_layout,
                arraySpinnerTypes,
                0
            )
        if(arraySpinnerTypes.size > 0 ) {
            spServiceType.adapter = adapterTypes
            adapterTypes.setDropDownViewResource(R.layout.item_spinner_drop_down)
            spServiceType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedTypeId = arraySpinnerTypes[position].id!!
                    selectedTypeName = arraySpinnerTypes[position].name!!
                    setMediaPager()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

            if (MyApplication.selectedNotSignedInType != -1) {
                var x =
                    spServiceType.setSelection(arraySpinnerTypes.indexOf(arraySpinnerTypes.find {
                        it.id == MyApplication.selectedNotSignedInType
                    }))
                MyApplication.selectedNotSignedInType = -1
            }
        }else{
            llSpinnerType.hide()
            llMargin.hide()
        }

    }


    private fun setSizeCapacitySpinner(){

        var arrayTypes : List<ServiceVariation> = arrayListOf()
        try {
            arrayTypes =
                MyApplication.selectedService!!.variations.distinctBy { it.sizeCapacity }
        }catch (ex:Exception){

        }


            arraySpinnerSizes.clear()
            for (i in arrayTypes.indices) {
                if (arrayTypes[i].sizeCapacity != null && arrayTypes[i].sizeCapacity!!.isNotEmpty())
                    arraySpinnerSizes.add(
                        ItemSpinner(
                            arrayTypes[i].sizeCapacityId!!.toInt(),
                            arrayTypes[i].sizeCapacity,
                            ""
                        )
                    )
            }

        if(arraySpinnerSizes.size > 0 ) {
            val adapterSize = AdapterGeneralSpinner(
                requireActivity(),
                R.layout.spinner_layout,
                arraySpinnerSizes,
                0
            )
            spServiceSize.adapter = adapterSize
            adapterSize.setDropDownViewResource(R.layout.item_spinner_drop_down)
            spServiceSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedSizeId = arraySpinnerSizes[position].id!!
                    selectedSizeName = arraySpinnerSizes[position].name!!
                    setMediaPager()

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

            if (MyApplication.selectedNotSignedInSize != -1) {
                spServiceSize.setSelection(arraySpinnerSizes.indexOf(arraySpinnerSizes.find {
                    it.id == MyApplication.selectedNotSignedInSize
                }))
                MyApplication.selectedNotSignedInSize = -1
            }
        }else{
            llSpinnerSize.hide()
        }

    }



}