package com.ids.qasemti.controller.Activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterGeneralSpinner
import com.ids.qasemti.controller.Adapters.AdapterServicesData
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import com.ids.qasemti.utils.AppHelper.Companion.createDialog
import com.ids.qasemti.utils.AppHelper.Companion.getFile
import com.ids.sampleapp.model.ItemSpinner
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.config.Configurations
import com.jaiselrahman.filepicker.model.MediaFile
import kotlinx.android.synthetic.main.actiivity_service_information.*
import kotlinx.android.synthetic.main.activity_services.rootLayout
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.no_logo_layout.btBck
import kotlinx.android.synthetic.main.service_tab_1.*
import kotlinx.android.synthetic.main.service_tab_2.*
import kotlinx.android.synthetic.main.service_tab_3.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class ActivityServiceInformation : ActivityBase(), RVOnItemClickListener {
    var array : ArrayList<ServiceItem> = arrayListOf()
    private val CODE_IMAGE = 1001
    private val CODE_DRIVING_LICENSE = 1002
    private val CODE_WORK_LICENSE = 1003
    private val CODE_VEHICLE_LICENSE = 1004
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
    var arrayData: ArrayList<ServicesData> = arrayListOf()
    var arrayAllServices: ArrayList<ResponseService> = arrayListOf()

    var arraySpinnerServices: ArrayList<ItemSpinner> = arrayListOf()
    var arraySpinnerTypes: ArrayList<ItemSpinner> = arrayListOf()
    var arraySpinnerStockAvailable: ArrayList<ItemSpinner> = arrayListOf()
    var arraySpinnerSizes: ArrayList<ItemSpinner> = arrayListOf()

    var selectedFileImage : MultipartBody.Part ?=null
    var selectedFileDrivingLicence : MultipartBody.Part ?=null
    var selectedFileWorkLicence : MultipartBody.Part ?=null
    var selectedFileVehicleLicence : MultipartBody.Part ?=null
    lateinit var arrayBody: java.util.ArrayList<MultipartBody.Part>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actiivity_service_information)
        AppHelper.setAllTexts(rootLayout,this)
        init()
        listeners()



    }

    private fun init(){
        selectedCategoryName= AppHelper.getRemoteString("purchase",this)
        btBck.show()
        setTabs()
        getAllServices()
    }

    private fun listeners(){
        btBck.onOneClick{super.onBackPressed()}
        btPickImage.onOneClick{pickFile(CODE_IMAGE)}
        btPickDrivingLicense.onOneClick{pickFile(CODE_DRIVING_LICENSE)}
        btPickWorkLisence.onOneClick{pickFile(CODE_WORK_LICENSE)}
        btPickVehicle.onOneClick{pickFile(CODE_VEHICLE_LICENSE)}
        btSave.onOneClick{setTab2()}
        btPreViews1.onOneClick{setTab1()}
        btNext1.onOneClick{setTab3()}
        btPreviews2.onOneClick{setTab2()}
        btNext2.onOneClick{
            if(arrayData.count { it.value!!.isEmpty() } > 0)
                createDialog(this,"Please fill all Data")
            else
                addService()
        }
        rgCategory.setOnCheckedChangeListener { group, checkedId ->
            val rb = findViewById<View>(checkedId) as RadioButton
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

    }

    private fun setTabs(){
        btTab1.onOneClick{
          setTab1()
        }

        btTab2.onOneClick{
          setTab2()
        }


        btTab3.onOneClick{
          setTab3()
        }

    }



    override fun onItemClicked(view: View, position: Int) {

    }

    private fun setServiceSpinner(){
        arraySpinnerServices.clear()
        var arrayFiltered=arrayAllServices.filter { it.type!!.lowercase()==selectedCategoryName.lowercase() }
        for (i in arrayFiltered.indices){
            if(arrayFiltered[i].name!=null && arrayFiltered[i].name!!.isNotEmpty())
               arraySpinnerServices.add(ItemSpinner(arrayFiltered[i].id!!.toInt(),arrayFiltered[i].name,""))
        }
        val adapterServices = AdapterGeneralSpinner(this, R.layout.spinner_layout, arraySpinnerServices,0)
        spService.adapter = adapterServices
        adapterServices.setDropDownViewResource(R.layout.item_spinner_drop_down)
        spService.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
              selectedServiceId=arraySpinnerServices[position].id!!
              selectedServiceName=arraySpinnerServices[position].name!!
              setTypeSpinner()
              setSizeCapacitySpinner()
              try{tvDescription.text=arrayFiltered[position].desc!!}catch (e:Exception){}
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
        val adapterTypes = AdapterGeneralSpinner(this, R.layout.spinner_layout, arraySpinnerTypes,0)
        spType.adapter = adapterTypes
        adapterTypes.setDropDownViewResource(R.layout.item_spinner_drop_down)
        spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedTypeId=arraySpinnerTypes[position].id!!
                selectedTypeName=arraySpinnerTypes[position].name!!
                setStockSpinner(arrayTypes[position].stockQuantity!!)
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
        val adapterSize = AdapterGeneralSpinner(this, R.layout.spinner_layout, arraySpinnerSizes,0)
        spSize.adapter = adapterSize
        adapterSize.setDropDownViewResource(R.layout.item_spinner_drop_down)
        spSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedSizeId=arraySpinnerSizes[position].id!!
                selectedSizeName=arraySpinnerSizes[position].name!!

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }


    private fun setStockSpinner(stockQuantity: String) {
        val arrayStock : java.util.ArrayList<ItemSpinner> = arrayListOf()
        arrayStock.add(ItemSpinner(0,stockQuantity,""))
        val adapterStock = AdapterGeneralSpinner(this, R.layout.spinner_layout, arrayStock,0)
        spStock.adapter = adapterStock
        adapterStock.setDropDownViewResource(R.layout.item_spinner_drop_down)
        spStock.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedQtyId=arrayStock[position].id!!
                selectedQtyName=arrayStock[position].name!!
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }


    private fun pickFile(pickCode:Int){

        val intent = Intent(this, FilePickerActivity::class.java)
        intent.putExtra(
            FilePickerActivity.CONFIGS, Configurations.Builder()
                .setCheckPermission(true)
               // .setSelectedMediaFiles(mediaFiles)
               // .enableImageCapture(true)
                .setShowVideos(false)
                .setSkipZeroSizeFiles(true)
                .setMaxSelection(1)
                .setShowFiles(true)
                .build()
        )
        startActivityForResult(intent,pickCode)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            val files: ArrayList<MediaFile> =data!!.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES)!!
            when (requestCode) {

                CODE_IMAGE -> {
                    try {
                        tvPickedImage.text = files[0].name
                        var file = getFile(this,files[0].uri)
                        var req=file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        selectedFileImage =  MultipartBody.Part.createFormData(ApiParameters.GALLERY, file.name, req)
                      } catch (e: Exception) {
                    }
                }

                CODE_DRIVING_LICENSE -> {
                    try {
                        tvPickedDrivingLicense.text = files[0].name
                        var file = getFile(this,files[0].uri)
                        var req=file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        selectedFileDrivingLicence =  MultipartBody.Part.createFormData(ApiParameters.GALLERY, file.name, req)
                     } catch (e: Exception) {
                    }
                }

                CODE_WORK_LICENSE -> {
                    try {
                        tvPickedWorkLicense.text = files[0].name
                        var file = getFile(this,files[0].uri)
                        var req=file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        selectedFileWorkLicence =  MultipartBody.Part.createFormData(ApiParameters.GALLERY, file.name, req)
                    } catch (e: Exception) {
                    }
                }

                CODE_VEHICLE_LICENSE -> {
                    try {
                        tvPickedVehicle.text = files[0].name
                        var file = getFile(this,files[0].uri)
                        var req=file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        selectedFileVehicleLicence =  MultipartBody.Part.createFormData(ApiParameters.GALLERY, file.name, req)
                    } catch (e: Exception) {
                    }
                }
            }
        }catch (e: Exception){}
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){

            CODE_IMAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    pickFile(CODE_IMAGE)
                }
                else{
                    Toast.makeText(this, AppHelper.getRemoteString("permission_denied",this), Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun setTab1(){
        linearProgress1.setBackgroundColor(ContextCompat.getColor(this,R.color.gray_progress))
        linearProgress2.setBackgroundColor(ContextCompat.getColor(this,R.color.gray_progress))
        linearService1.show()
        linearService2.hide()
        linearService3.hide()

        btTab2.setBackgroundResource(R.drawable.circle_gray)
        btTab3.setBackgroundResource(R.drawable.circle_gray)

        ivTab2.setColorFilter(ContextCompat.getColor(this, R.color.gray_font), android.graphics.PorterDuff.Mode.SRC_IN)
        ivTab3.setColorFilter(ContextCompat.getColor(this, R.color.gray_font), android.graphics.PorterDuff.Mode.SRC_IN)

        tvTabTitle.text = AppHelper.getRemoteString("service_information",this)
    }

    private fun setTab2(){
        linearProgress1.setBackgroundColor(ContextCompat.getColor(this,R.color.redPrimary))
        linearProgress2.setBackgroundColor(ContextCompat.getColor(this,R.color.gray_progress))
        linearService1.hide()
        linearService2.show()
        linearService3.hide()

        btTab2.setBackgroundResource(R.drawable.primary_circle)
        btTab3.setBackgroundResource(R.drawable.circle_gray)

        ivTab2.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
        ivTab3.setColorFilter(ContextCompat.getColor(this, R.color.gray_font), android.graphics.PorterDuff.Mode.SRC_IN)
        tvTabTitle.text =AppHelper.getRemoteString("ownership_proof",this)
    }

    private fun setTab3(){
        linearProgress1.setBackgroundColor(ContextCompat.getColor(this,R.color.redPrimary))
        linearProgress2.setBackgroundColor(ContextCompat.getColor(this,R.color.redPrimary))
        linearService1.hide()
        linearService2.hide()
        linearService3.show()

        btTab2.setBackgroundResource(R.drawable.primary_circle)
        btTab3.setBackgroundResource(R.drawable.primary_circle)

        ivTab2.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
        ivTab3.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
        tvTabTitle.text =  AppHelper.getRemoteString("price_earning",this)

        setDataPicked()
    }

    private fun setDataPicked(){
        arrayData.clear()
        arrayData.add(ServicesData(1, getString(R.string.category),selectedCategoryName, selectedCategoryId))
        arrayData.add(ServicesData(2, getString(R.string.service),selectedServiceName, selectedServiceId))
        arrayData.add(ServicesData(3, getString(R.string.type),selectedTypeName, selectedTypeId))
        arrayData.add(ServicesData(4, getString(R.string.SizeCapacity),selectedSizeName, selectedSizeId))
        arrayData.add(ServicesData(5, getString(R.string.Quantity),selectedQtyName, selectedQtyId))
        var adapter = AdapterServicesData(arrayData, this, this)
        rvData.layoutManager = LinearLayoutManager(this)
        rvData.adapter = adapter
        rvData.isNestedScrollingEnabled = false
    }



    fun getAllServices(){
        var newReq = RequestServices(1, MyApplication.languageCode)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getServices(
                newReq
            )?.enqueue(object : Callback<ResponseMainServices> {
                override fun onResponse(call: Call<ResponseMainServices>, response: Response<ResponseMainServices>) {
                    try{
                       arrayAllServices.clear()
                        arrayAllServices.addAll(response.body()!!.responseService!!)
                        if(arrayAllServices.size>0)
                           setServiceSpinner()
                    }catch (E: java.lang.Exception){

                    }
                }
                override fun onFailure(call: Call<ResponseMainServices>, throwable: Throwable) {

                }
            })
    }



    fun addService(){
        loading.show()
        arrayBody= arrayListOf()
        if(selectedFileImage!=null)
            arrayBody.add(selectedFileImage!!)
        if(selectedFileDrivingLicence!=null)
            arrayBody.add(selectedFileDrivingLicence!!)
        if(selectedFileWorkLicence!=null)
            arrayBody.add(selectedFileWorkLicence!!)
        if(selectedFileVehicleLicence!=null)
            arrayBody.add(selectedFileVehicleLicence!!)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.addService(
                MyApplication.userId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                selectedCategoryName.toRequestBody("text/plain".toMediaTypeOrNull()),
                selectedServiceName.toRequestBody("text/plain".toMediaTypeOrNull()),
                selectedSizeName.toRequestBody("text/plain".toMediaTypeOrNull()),
                selectedTypeName.toRequestBody("text/plain".toMediaTypeOrNull()),
                selectedQtyName.toRequestBody("text/plain".toMediaTypeOrNull()),
                tvDescription.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                arrayBody,
                MyApplication.languageCode.toRequestBody("text/plain".toMediaTypeOrNull()),
                selectedServiceId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),

            )?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                    try{
                        loading.hide()
                       if(response.body()!!.result==1)
                           toast("success")
                        else
                            toast("failed 1")
                    }catch (E: java.lang.Exception){
                        toast("failed 2")
                    }
                }
                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    loading.hide()
                    toast("failed 3")
                }
            })
    }

}