package com.ids.qasemti.controller.Fragments

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Adapters.AdapterGeneralSpinner
import com.ids.qasemti.controller.Adapters.AdapterServices
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import com.ids.sampleapp.model.ItemSpinner
import kotlinx.android.synthetic.main.fragment_home_client.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.service_tab_1.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentHomeClient : Fragment(), RVOnItemClickListener {

    var dialog: Dialog? = null
    var arrayAllServices: ArrayList<ResponseService> = arrayListOf()
    var arrayFiltered: ArrayList<ResponseService> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    var arraySpinnerServices: ArrayList<ItemSpinner> = arrayListOf()
    var arraySpinnerTypes: ArrayList<ItemSpinner> = arrayListOf()
    var arraySpinnerSizes: ArrayList<ItemSpinner> = arrayListOf()
    private var selectedCategoryId=1
    private var selectedCategoryName=AppConstants.TYPE_PURCHASE
    private var selectedServiceId=0
    private var selectedServiceName=""
    private var selectedTypeId=0
    private var selectedTypeName=""
    private var selectedSizeId=0
    private var selectedSizeName=""
    private var selectedQtyId=0
    private var selectedQtyName=""

    lateinit var spServices : Spinner
    lateinit var spType : Spinner
    lateinit var spServiceCapactity : Spinner



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(com.ids.qasemti.R.layout.fragment_home_client, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutServices,requireContext())
        init()



    }

    fun init() {

        try {
            MyApplication.arrayCart = AppHelper.fromGSon()
        }catch (ex:Exception){
        }
        var x = MyApplication.arrayCart
        if (MyApplication.isSignedIn) {
            (activity as ActivityHome?)!!.showLogout(false)
            btRegisterLogin.hide()
        }else{
            btRegisterLogin.show()
            (activity as ActivityHome?)!!.showLogout(false)
        }

        btRegisterLogin.onOneClick {
            (activity as ActivityHome)!!.goRegistration(2,AppConstants.FRAGMENT_HOME_CLIENT,FragmentHomeClient(),R.color.white)
        }


        (activity as ActivityHome?)!!.showBack(false)

        btFilter.onOneClick {
            showPopupSocialMedia()
        }



        AppHelper.setTitle(requireActivity(), AppHelper.getRemoteString("our_services",requireContext()), "services")

        getServices()


    }

    private fun showPopupSocialMedia() {


        dialog = Dialog(requireContext(), R.style.Base_ThemeOverlay_AppCompat_Dialog)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.setContentView(R.layout.dialog_service)
        dialog!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialog!!.window!!.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
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
        var rbPurchase = dialog!!.findViewById<RadioButton>(R.id.rbPurchase)
        var rbRental = dialog!!.findViewById<RadioButton>(R.id.rbRental)
        var rgCategory = dialog!!.findViewById<RadioGroup>(R.id.rgCategory)

        rgCategory.setOnCheckedChangeListener { group, checkedId ->
            val rb = dialog!!.findViewById<View>(checkedId) as RadioButton
            if(checkedId==R.id.rbPurchase){
                selectedCategoryId=1
                selectedCategoryName==AppConstants.TYPE_PURCHASE

            }else{
                selectedCategoryId=2
                selectedCategoryName==AppConstants.TYPE_RENTAL

            }
            selectedCategoryName=rb.text.toString()
            if(arrayAllServices.size>0)
                setServiceSpinner()
            /*
             Toast.makeText(applicationContext, rb.text, Toast.LENGTH_SHORT).show()*/
        }



        if(arrayAllServices.size>0)
            setServiceSpinner()

        close.onOneClick {
            btFilter.show()
            dialog!!.cancel()
        }

        btApplyFilter.onOneClick {
          arrayFiltered.clear()
          arrayFiltered.addAll(arrayAllServices.filter { it.id ==selectedServiceId.toString()})
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




    private fun setServiceSpinner(){
        arraySpinnerServices.clear()
        var arrayFiltered=arrayAllServices.filter { it.type!!.lowercase()==selectedCategoryName.lowercase() }
        for (i in arrayFiltered.indices){
            if(arrayFiltered[i].name!=null && arrayFiltered[i].name!!.isNotEmpty())
                arraySpinnerServices.add(ItemSpinner(arrayFiltered[i].id!!.toInt(),arrayFiltered[i].name,""))
        }
        val adapterServices = AdapterGeneralSpinner(requireActivity(), R.layout.spinner_layout, arraySpinnerServices,0)
        spServices.adapter = adapterServices
        adapterServices.setDropDownViewResource(R.layout.item_spinner_drop_down)
        spServices.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedServiceId=arraySpinnerServices[position].id!!
                selectedServiceName=arraySpinnerServices[position].name!!
                setTypeSpinner()
                setSizeCapacitySpinner()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }


    private fun setTypeSpinner(){

        var selectedArray=arrayAllServices.find { it.id==selectedServiceId.toString() }
        var arrayTypes=selectedArray!!.variations.distinctBy { it.types  }
        arraySpinnerTypes.clear()
        for (i in arrayTypes.indices){
            if(arrayTypes[i].types!=null && arrayTypes[i].types!!.isNotEmpty())
                arraySpinnerTypes.add(ItemSpinner(i,arrayTypes[i].types,""))
        }
        val adapterTypes = AdapterGeneralSpinner(requireActivity(), R.layout.spinner_layout, arraySpinnerTypes,0)
        spType.adapter = adapterTypes
        adapterTypes.setDropDownViewResource(R.layout.item_spinner_drop_down)
        spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedTypeId=arraySpinnerTypes[position].id!!
                selectedTypeName=arraySpinnerTypes[position].name!!
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }


    private fun setSizeCapacitySpinner(){
        var selectedArray=arrayAllServices.find { it.id==selectedServiceId.toString() }
        var arrayTypes=selectedArray!!.variations.distinctBy { it.sizeCapacity }
        arraySpinnerSizes.clear()
        for (i in arrayTypes.indices){
            if(arrayTypes[i].sizeCapacity!=null && arrayTypes[i].sizeCapacity!!.isNotEmpty())
                arraySpinnerSizes.add(ItemSpinner(i,arrayTypes[i].sizeCapacity,""))
        }
        val adapterSize = AdapterGeneralSpinner(requireActivity(), R.layout.spinner_layout, arraySpinnerSizes,0)
        spServiceCapactity.adapter = adapterSize
        adapterSize.setDropDownViewResource(R.layout.item_spinner_drop_down)
        spServiceCapactity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedSizeId=arraySpinnerSizes[position].id!!
                selectedSizeName=arraySpinnerSizes[position].name!!

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }


    fun getServices(){
        loading.show()
        var newReq = RequestLanguage(MyApplication.languageCode)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getClServices(
                newReq
            )?.enqueue(object : Callback<ResponseMainServices> {
                override fun onResponse(call: Call<ResponseMainServices>, response: Response<ResponseMainServices>) {
                    try{
                        loading.hide()
                        arrayAllServices.clear()
                        arrayFiltered.clear()
                        arrayAllServices.addAll(response.body()!!.responseService!!)
                        arrayFiltered.addAll(response.body()!!.responseService!!)
                        setData()
                    }catch (E: java.lang.Exception){
                        arrayAllServices.clear()
                        arrayFiltered.clear()
                        setData()
                    }
                }
                override fun onFailure(call: Call<ResponseMainServices>, throwable: Throwable) {
                    loading.hide()
                    arrayAllServices.clear()
                    arrayFiltered.clear()
                    setData()
                }
            })
    }

    private fun setData(){
        try {
            if (arrayFiltered.size == 0) {
                rvServices.hide()
                btFilter.hide()
                llNodata.show()


            } else {
                var adapter = AdapterServices(arrayFiltered, this, requireContext())
                rvServices.layoutManager = LinearLayoutManager(requireContext())
                rvServices.adapter = adapter
                rvServices.isNestedScrollingEnabled = false
            }
        }catch (ex:Exception){

        }
    }

    override fun onItemClicked(view: View, position: Int) {

        if (view.id == R.id.linearService) {
            tvPageTitle
            AppHelper.onOneClick {
                MyApplication.selectedFragment = FragmentServiceDetails()
                MyApplication.selectedService = arrayAllServices.get(position)
                MyApplication.rental = position==2
                MyApplication.position = position
                (requireActivity() as ActivityHome?)!!.addFrag(
                    FragmentServiceDetails(),
                    AppConstants.FRAGMENT_SERVICE_DETAILS
                )
            }
        }

    }
}