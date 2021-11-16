package com.ids.qasemti.controller.Activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.RadioButton
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.*
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
import kotlinx.android.synthetic.main.actiivity_service_information.*
import kotlinx.android.synthetic.main.layout_profile.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.no_logo_layout.btBck
import kotlinx.android.synthetic.main.service_tab_1.*
import kotlinx.android.synthetic.main.service_tab_2.*
import kotlinx.android.synthetic.main.service_tab_3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class ActivityServiceInformation : ActivityBase(), RVOnItemClickListener {
    var array : ArrayList<ServiceItem> = arrayListOf()
    private val CODE_IMAGE = 1001
    var fromCam = false
    var type : Int ?=0
    var selectedType : Boolean = false
    var selectedSize : Boolean = false
    var selectedService : Boolean = false
    private val CODE_DRIVING_LICENSE = 1002
    private val CODE_WORK_LICENSE = 1003
    private val CODE_VEHICLE_LICENSE = 1004
    private val CODE_REQUIRED_FILES = 1005
    private val CODE_CAMERA = 1006
    val GRANTED = 0
    val DENIED = 1
    val BLOCKED = -1
    var  mPermissionResult : ActivityResultLauncher<Array<String>>?=null
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
    private var requireFilePosition=0
    var arrayData: ArrayList<ServicesData> = arrayListOf()
    var arrayAllServices: ArrayList<ResponseService> = arrayListOf()

    var arraySpinnerServices: ArrayList<ItemSpinner> = arrayListOf()
    var arraySpinnerTypes: ArrayList<ItemSpinner> = arrayListOf()
    var arraySpinnerStockAvailable: ArrayList<ItemSpinner> = arrayListOf()
    var arraySpinnerSizes: ArrayList<ItemSpinner> = arrayListOf()

    var arrayImagesSelected: ArrayList<FilesSelected> = arrayListOf()
    var selectedFileImage : MultipartBody.Part ?=null
    var selectedFileDrivingLicence : MultipartBody.Part ?=null
    var selectedFileWorkLicence : MultipartBody.Part ?=null
    var selectedFileVehicleLicence : MultipartBody.Part ?=null
    var image : Boolean ?= false
    var adapterSelectedImages : AdapterGridFiles?=null

    var adapterRequiredFiles : AdapterRequiredFiles?=null
    var arrayRequiredFiles: ArrayList<RequiredFiles> = arrayListOf()
    var countFilesUploaded=0
    var addServiceDone=false
    var price=""
    var earning=""
    lateinit var arrayBody: java.util.ArrayList<MultipartBody.Part>
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actiivity_service_information)
        AppHelper.setAllTexts(rootLayout,this)
        init()
        listeners()
    }


    fun getPermissionStatus(activity: Activity?, androidPermissionName: String?): Int {
        return if (ContextCompat.checkSelfPermission(
                activity!!,
                androidPermissionName!!
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    androidPermissionName
                )
            ) {
                BLOCKED
            } else DENIED
        } else GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setUp(){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),CODE_IMAGE)
        } else {
            ActivityCompat.requestPermissions(this,  arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),CODE_IMAGE)
        }

        /*
        mPermissionResult = onRequestPermissionsResult(ActivityResultContracts.RequestMultiplePermissions())
        { result ->
            var permission = false
            for( item in result){
                permission = item.value
            }
            if (permission) {
              //  selectImage(this)
                Log.e(ContentValues.TAG, "onActivityResult: PERMISSION GRANTED")
            } else {
                Log.e(ContentValues.TAG, "onActivityResult: PERMISSION DENIED")
            }
        }*/
    }

    private fun init(){

        arrayAllServices.clear()
        selectedCategoryName= AppHelper.getRemoteString("purchase",this)
        btBck.show()
        setTabs()
        if(MyApplication.languageCode==AppConstants.LANG_ARABIC)
            selectedCategoryName="بيع"
        getAllServices()
        setPickedImages()


    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun listeners(){
        btBck.onOneClick{super.onBackPressed()}
        btPickImage.onOneClick{
          //  pickFile(CODE_IMAGE,false)
            type = CODE_IMAGE
            image = true
            setUp()
           // selectImage(this,CODE_IMAGE)
        }

        btSave.onOneClick{setTab2()}
        btPreViews1.onOneClick{setTab1()}
        btPreViews1.typeface = AppHelper.getTypeFace(this)
        btNext1.onOneClick{setTab3()}
        btNext1.typeface = AppHelper.getTypeFace(this)
        btPreviews2.onOneClick{setTab2()}
        btPreviews2.typeface = AppHelper.getTypeFace(this)
        btNext2.onOneClick{
            if(!MyApplication.isEditService){
            if(arrayData.count { it.value!!.isEmpty() } > 0)
                createDialog(this,"Please fill all Data")
            else if(arrayRequiredFiles.size>0 && !requiredFilesUploaded())
                createDialog(this,"Please fill all Required files")
            else{

                addService()

            }}else{
                addServiceDone=true
                updateService()
            }
        }

        btNext2.typeface = AppHelper.getTypeFace(this)
        rgCategory.setOnCheckedChangeListener { _, checkedId ->
            val rb = findViewById<View>(checkedId) as RadioButton
            if(checkedId==R.id.rbPurchase){
                selectedCategoryId=1
                selectedCategoryName==AppConstants.TYPE_PURCHASE

            }else{
                selectedCategoryId=2
                selectedCategoryName==AppConstants.TYPE_RENTAL

            }
            if(MyApplication.languageCode==AppConstants.LANG_ARABIC){
                if(selectedCategoryId==1){
                    selectedCategoryName="بيع"
                }else{
                    selectedCategoryName="ايجار"
                }

            }else{
                selectedCategoryName=rb.text.toString()
            }

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





    @RequiresApi(Build.VERSION_CODES.M)
    override fun onItemClicked(view: View, position: Int) {
       if(view.id==R.id.btPickFile && !MyApplication.isEditService){
           requireFilePosition=position
           //pickFile(CODE_REQUIRED_FILES,true)
           type = CODE_REQUIRED_FILES
           setUp()
          // selectImage(this,CODE_REQUIRED_FILES)
       }
    }

    private fun setServiceSpinner(){
        arraySpinnerServices.clear()
        var arrayFiltered=arrayAllServices.filter { it.type!!.lowercase()==selectedCategoryName.lowercase() }
        for (i in arrayFiltered.indices){
            if(arrayFiltered[i].name!=null && arrayFiltered[i].name!!.isNotEmpty())
               arraySpinnerServices.add(ItemSpinner(arrayFiltered[i].id!!.toInt(),arrayFiltered[i].name,""))
        }
        arraySpinnerServices.add(0,
            ItemSpinner(0,AppHelper.getRemoteString("please__select",this),"")
        )
        selectedServiceId = 0
        val adapterServices = AdapterGeneralSpinner(this, R.layout.spinner_layout, arraySpinnerServices,0)
        spService.adapter = adapterServices
        adapterServices.setDropDownViewResource(R.layout.item_spinner_drop_down)
        spService.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
              selectedServiceId=arraySpinnerServices[position].id!!
              selectedServiceName=arraySpinnerServices[position].name!!
                if(position==0)
                    selectedService = false
                else
                    selectedService = true
              setTypeSpinner()
              setSizeCapacitySpinner()
              try{tvDescription.text=arrayFiltered.find {
                  it.id!!.toInt() == selectedServiceId
              }!!.desc}catch (e:Exception){}
                if(!MyApplication.isEditService)
                    getRequiredFiles()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        loading.hide()
    }


    private fun setTypeSpinner(){

        var selectedArray=arrayAllServices.find { it.id==selectedServiceId.toString() }
        var arrayTypes = arrayListOf<ServiceVariation>()
        try {
            arrayTypes.addAll(selectedArray!!.variations.distinctBy { it.types })
        }catch (ex:Exception){

        }

            llSpType.show()
            arraySpinnerTypes.clear()
            for (i in arrayTypes.indices) {
                if (arrayTypes[i].types != null && arrayTypes[i].types!!.isNotEmpty())
                    arraySpinnerTypes.add(ItemSpinner(i, arrayTypes[i].types, ""))
            }
            arraySpinnerTypes.add(0,
                ItemSpinner(0,AppHelper.getRemoteString("please__select",this),"")
            )
        if(arraySpinnerTypes.size >1) {
            selectedTypeId = 0
            val adapterTypes =
                AdapterGeneralSpinner(this, R.layout.spinner_layout, arraySpinnerTypes, 0)
            spType.adapter = adapterTypes
            adapterTypes.setDropDownViewResource(R.layout.item_spinner_drop_down)
            spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if(position==0)
                        selectedType = false
                    else
                        selectedType = true
                    selectedTypeId = arraySpinnerTypes[position].id!!
                    selectedTypeName = arraySpinnerTypes[position].name!!
                    //setStockSpinner(arrayTypes[position].stockQuantity!!)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }else{
            selectedTypeId = -1
            llSpType.hide()
        }

    }


    private fun setSizeCapacitySpinner(){
        var selectedArray=arrayAllServices.find { it.id==selectedServiceId.toString() }
        var arrayTypes = arrayListOf<ServiceVariation>()
        try {
            arrayTypes.addAll(selectedArray!!.variations.distinctBy { it.sizeCapacity })
        }catch (ex:java.lang.Exception){}
            llSpSizeCap.show()
            arraySpinnerSizes.clear()
            for (i in arrayTypes.indices) {
                if (arrayTypes[i].sizeCapacity != null && arrayTypes[i].sizeCapacity!!.isNotEmpty())
                    arraySpinnerSizes.add(ItemSpinner(i, arrayTypes[i].sizeCapacity, ""))
            }
            arraySpinnerSizes.add(0,
                ItemSpinner(0,AppHelper.getRemoteString("please__select",this),"")
            )
        if(arraySpinnerSizes.size >1){
            selectedSizeId = 0
            val adapterSize =
                AdapterGeneralSpinner(this, R.layout.spinner_layout, arraySpinnerSizes, 0)
            spSize.adapter = adapterSize
            adapterSize.setDropDownViewResource(R.layout.item_spinner_drop_down)
            spSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if(position==0)
                        selectedSize = false
                    else
                        selectedSize = true
                    selectedSizeId = arraySpinnerSizes[position].id!!
                    selectedSizeName = arraySpinnerSizes[position].name!!

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }else{
            selectedSizeId = -1
            llSpSizeCap.hide()
        }


        if(MyApplication.isEditService)
            setEditData()

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


    private fun pickFile(pickCode:Int,enableFiles:Boolean){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED){
             ActivityCompat.requestPermissions(this as Activity,
                arrayOf( Manifest.permission.CAMERA),CODE_CAMERA)

        }else{
        val intent = Intent(this, FilePickerActivity::class.java)
        intent.putExtra(
            FilePickerActivity.CONFIGS, Configurations.Builder()
                .setCheckPermission(true)
               // .setSelectedMediaFiles(mediaFiles)
                .enableImageCapture(true)
                .setShowVideos(false)
                .setSkipZeroSizeFiles(true)
                .setMaxSelection(1)
                .setShowFiles(enableFiles)
                .setShowAudios(false)
                .build()
        )
        startActivityForResult(intent,pickCode)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
           // val files: ArrayList<MediaFile> =data!!.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES)!!
            when (requestCode) {

                CODE_IMAGE -> {
                    try {
                        var file : File ?=null
                        if(fromCam){
                            file = File(data!!.data!!.path)
                        }else {
                            file = getFile(this,data!!.data!!)
                        }
                        tvPickedImage.text = file.name
                        var req=file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        selectedFileImage =  MultipartBody.Part.createFormData(ApiParameters.GALLERY, file.name+"File", req)
                        arrayImagesSelected.add(FilesSelected(file.name,file,selectedFileImage))
                        adapterSelectedImages!!.notifyDataSetChanged()
                      } catch (e: Exception) {
                    }
                }

                CODE_REQUIRED_FILES -> {
                    try {

                        var file : File ?=null
                        var FileName : String ?=""
                        if(fromCam){
                            file = File(data!!.data!!.path)
                            FileName = file.name
                        }else {
                            file = getFile(this, data!!.data!!)
                            FileName = file.name

                        }
                        var req=file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        var selectedFile =  MultipartBody.Part.createFormData(ApiParameters.FILE, file.name+"File", req)
                        arrayRequiredFiles[requireFilePosition].multipart=selectedFile
                        arrayRequiredFiles[requireFilePosition].selectedFileName=FileName
                        adapterRequiredFiles!!.notifyDataSetChanged()
                     } catch (e: Exception) {
                    }
                }

               /* CODE_CAMERA ->{
                    try{

                    }catch (e:Exception){

                    }
                }*/


            }
        }catch (e: Exception){

            var x = e
        }
    }

    private fun pickImageFromCamera() {

        fromCam = true
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .cameraOnly()
            .maxResultSize(1080, 1080)
            .createIntent {
               startActivityForResult(it,type!!)
            }
        /*   val pictureIntent = Intent(
               MediaStore.ACTION_IMAGE_CAPTURE
           ).addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
           if (pictureIntent.resolveActivity(requireActivity().packageManager) != null) {
               //Create a file to store the image
               var photoFile: File? = null
               try {
                   photoFile = createImageFile()
               } catch (ex: IOException) {
               }// Error occurred while creating the File

               if (photoFile != null) {
                   val photoURI = FileProvider.getUriForFile(requireActivity(), "com.ids.qasemti.provider", photoFile)
                   pictureIntent.putExtra(
                       MediaStore.EXTRA_OUTPUT,
                       photoURI
                   )

                   resultLauncher!!.launch(pictureIntent)
                   *//*startActivityForResult(
                    pictureIntent,
                    PERMISSION_CODE_CAMERA
                )*//*
            }
        }*/
    }


    private fun pickImageFromGallery() {
        fromCam = false
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,type!!)
        //  startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    fun pickPDFFile(){
        var i = Intent(Intent.ACTION_GET_CONTENT);
        i.setType("application/pdf");
        i.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(i,type!!)
    }

    private fun selectImage(context: Context) {
        var options = arrayOf<CharSequence>()
        if(!image!!) {
            var tempOp = arrayOf<CharSequence>(
                "Take Photo",
                "Choose from Gallery",
                "Select File",
                getString(R.string.cancel)
            )
            options = tempOp
        }else{
             var tempOp = arrayOf<CharSequence>(
                "Take Photo",
                "Choose from Gallery",
                getString(R.string.cancel)
            )
            options = tempOp
        }
        image = false
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose Profile Pic")

        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Take Photo" -> pickImageFromCamera()
                options[item] == "Choose from Gallery" -> pickImageFromGallery()
                options[item] == "Select File" -> pickPDFFile()
                options[item] == getString(R.string.cancel) -> dialog.dismiss()
            }
        }
        builder.show()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){



            CODE_IMAGE -> {
                var permissioned = false

                for(item in grantResults){
                    if(item == PackageManager.PERMISSION_GRANTED)
                        permissioned = true
                    else
                        permissioned = false
                }
                if(permissioned) {
                    selectImage(this)
                    MyApplication.permissionAllow11 = 0
                } else
                {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        if (MyApplication.permissionAllow11!! >= 2) {
                            for (item in permissions) {

                                var x = checkSelfPermission(item)
                                if (checkSelfPermission(item) == BLOCKED) {
                                    startActivityForResult(
                                        Intent(android.provider.Settings.ACTION_SETTINGS),
                                        0
                                    );
                                    toast(
                                        AppHelper.getRemoteString(
                                            "grant_settings_permission",
                                            this
                                        )
                                    )
                                    break
                                }
                            }
                        } else {
                            MyApplication.permissionAllow11 = MyApplication.permissionAllow11!! + 1
                        }
                    }
                }

               /* if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    pickFile(CODE_IMAGE,false)
                }
                else{
                    Toast.makeText(this, AppHelper.getRemoteString("permission_denied",this), Toast.LENGTH_SHORT).show()
                }*/
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

        if(selectedService && selectedSize && selectedType ) {
            if (arrayImagesSelected.size == 0 && !MyApplication.isEditService)
                createDialog(this, "Please upload image")
            else if (etStockAvailable.text.toString()
                    .isEmpty() || (!MyApplication.isEditService && etStockAvailable.text.toString() == "0")
            )
                createDialog(this, "Please fill stock available")
            else {
                linearProgress1.setBackgroundColor(ContextCompat.getColor(this, R.color.primary))
                linearProgress2.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.gray_progress
                    )
                )
                linearService1.hide()
                linearService2.show()
                linearService3.hide()

                btTab2.setBackgroundResource(R.drawable.primary_circle)
                btTab3.setBackgroundResource(R.drawable.circle_gray)

                ivTab2.setColorFilter(
                    ContextCompat.getColor(this, R.color.white),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                ivTab3.setColorFilter(
                    ContextCompat.getColor(this, R.color.gray_font),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                tvTabTitle.text = AppHelper.getRemoteString("ownership_proof", this)
            }
        }else{
            createDialog(this,AppHelper.getRemoteString("fill_all_field",this))
        }
    }

    private fun setTab3(){

        if(arrayRequiredFiles.size>0 && !requiredFilesUploaded() && !MyApplication.isEditService)
            createDialog(this,"Please fill all Required files")
        else if(etStockAvailable.text.toString().isEmpty() ||(!MyApplication.isEditService && etStockAvailable.text.toString() == "0"))
            createDialog(this,"Please fill stock available")
        else{
        linearProgress1.setBackgroundColor(ContextCompat.getColor(this,R.color.primary))
        linearProgress2.setBackgroundColor(ContextCompat.getColor(this,R.color.primary))
        linearService1.hide()
        linearService2.hide()
        linearService3.show()

        btTab2.setBackgroundResource(R.drawable.primary_circle)
        btTab3.setBackgroundResource(R.drawable.primary_circle)

        ivTab2.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
        ivTab3.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
        tvTabTitle.text =  AppHelper.getRemoteString("price_earning",this)

        setDataPicked()


        var service=arrayAllServices.find { it.id == selectedServiceId.toString() }
        var variation=service!!.variations.find { it.types == selectedTypeName && it.sizeCapacity == selectedSizeName }
        price=""
        earning=""
            var edt=""
        try{price = variation!!.price!!}catch (e:Exception){}
        try{earning = variation!!.earnings!!}catch (e:Exception){}
            try{edt = service.eta!!}catch (e:Exception){}
        tvPrice.text=AppHelper.getRemoteString("Price",this)+": "+price
        tvEarning.text=AppHelper.getRemoteString("Earning",this)+": "+earning
        tvEstimatedDeliveryTime.text=AppHelper.getRemoteString("expected_delivery",this)+": "+edt
        }
    }

    private fun setDataPicked(){
        arrayData.clear()
        arrayData.add(ServicesData(1, getString(R.string.category),selectedCategoryName, selectedCategoryId))
        arrayData.add(ServicesData(2, getString(R.string.service),selectedServiceName, selectedServiceId))
        arrayData.add(ServicesData(3, getString(R.string.type),selectedTypeName, selectedTypeId))
        arrayData.add(ServicesData(4, getString(R.string.SizeCapacity),selectedSizeName, selectedSizeId))
        arrayData.add(ServicesData(5, getString(R.string.Quantity),etStockAvailable.text.toString(), 1))
        var adapter = AdapterServicesData(arrayData, this, this)
        rvData.layoutManager = LinearLayoutManager(this)
        rvData.adapter = adapter
        rvData.isNestedScrollingEnabled = false
    }



    private fun setPickedImages(){
        arrayImagesSelected.clear()
        adapterSelectedImages = AdapterGridFiles(arrayImagesSelected, this, this)
        rvSelectedImages.layoutManager = GridLayoutManager(this,3)
        rvSelectedImages.adapter = adapterSelectedImages
        rvSelectedImages.isNestedScrollingEnabled = false
    }

    private fun setRequiredFiles(){
        if(arrayRequiredFiles.size>0){
            rvRequiredFiles.show()
            no_required.hide()
            adapterRequiredFiles = AdapterRequiredFiles(arrayRequiredFiles, this, this)
            rvRequiredFiles.layoutManager = LinearLayoutManager(this)
            rvRequiredFiles.adapter = adapterRequiredFiles
            rvRequiredFiles.isNestedScrollingEnabled = false
        }
        else
            noRequiredDocuments()

    }

    fun getAllServices(){
        loading.show()
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


    fun getRequiredFiles(){
        var newReq = RequestProductId(selectedServiceId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.get_required_docs(
                newReq
            )?.enqueue(object : Callback<ResponseRequiredFiles> {
                override fun onResponse(call: Call<ResponseRequiredFiles>, response: Response<ResponseRequiredFiles>) {
                    try{
                        arrayRequiredFiles.clear()
                        arrayRequiredFiles.addAll(response.body()!!.files!!)
                        if(arrayRequiredFiles.size>0)
                            setRequiredFiles()
                        else
                            noRequiredDocuments()

                    }catch (E: java.lang.Exception){
                        noRequiredDocuments()
                    }
                }
                override fun onFailure(call: Call<ResponseRequiredFiles>, throwable: Throwable) {
                    noRequiredDocuments()
                }
            })
    }

    private fun noRequiredDocuments(){
        rvRequiredFiles.hide()
        no_required.show()
    }

    fun addService(){
        loading.show()
        arrayBody= arrayListOf()
        if(arrayImagesSelected.size>0){
        for (i in arrayImagesSelected.indices)
            arrayBody.add(arrayImagesSelected[i].multipart!!)
        }
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
                        addServiceDone=true
                       if(response.body()!!.result==1){
                         // checkData()
                           if(arrayRequiredFiles.size>0){
                               for (i in arrayRequiredFiles.indices) {
                                   countFilesUploaded=0
                                   if(response.body()!!.product_id!=null && response.body()!!.product_id!!.isNotEmpty())
                                      uploadFiles(arrayRequiredFiles[i],response.body()!!.product_id!!.toInt())
                               }
                           }else{
                               createDialog(this@ActivityServiceInformation, AppHelper.getRemoteString("services_is_inactive", this@ActivityServiceInformation)) {
                                   this@ActivityServiceInformation.onBackPressed()
                               }
                           }

                       }

                        else{

                           loading.hide()
                            toast(AppHelper.getRemoteString("adding_error",this@ActivityServiceInformation))
                        }
                    }catch (E: java.lang.Exception){
                        loading.hide()
                        toast("failed 2")
                    }
                }
                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    loading.hide()
                    toast("failed 3")
                }
            })
    }

    fun uploadFiles(requiredFiles: RequiredFiles,product_id: Int ) {
        loading.show()
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.spUploadFiles(
                MyApplication.userId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                requiredFiles.getFileTitle()!!.toRequestBody("text/plain".toMediaTypeOrNull()),
                requiredFiles.multipart!!,
                product_id.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                requiredFiles.id!!.toRequestBody("text/plain".toMediaTypeOrNull())
            )?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                    try{

                        if(response.body()!!.result==1){
                            countFilesUploaded++
                            checkData()
                        }

                        else {
                            loading.hide()
                            toast("failed to upload required files")
                        }
                    }catch (E: java.lang.Exception){
                        loading.hide()
                        toast("failed to upload required files")
                    }
                }
                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    loading.hide()
                    toast("failed to upload required files")
                }
            })
    }


    private fun checkData(){
        if(!MyApplication.isEditService){
        if((addServiceDone && arrayRequiredFiles.size==0) || (addServiceDone && arrayRequiredFiles.size>0 && countFilesUploaded == arrayRequiredFiles.size))
            createDialog(this, AppHelper.getRemoteString("services_is_inactive", this)) {
                this@ActivityServiceInformation.onBackPressed()
            }

        }else{
            var coutMultipart=arrayRequiredFiles.count { it.multipart != null }
            if((addServiceDone && arrayRequiredFiles.size==0) || (addServiceDone && arrayRequiredFiles.size>0 && countFilesUploaded == coutMultipart))
                this@ActivityServiceInformation.onBackPressed()
        }
    }


    fun updateService(){
        loading.show()
        var stockStatus=""
        try{if(etStockAvailable.text.toString().toInt() == 0)
              stockStatus="outofstock"
            else
              stockStatus="intock"
        }catch (e:Exception){}
        var req=RequestUpdateService(MyApplication.selectedService!!.id!!.toInt(),etStockAvailable.text.toString().toInt(),stockStatus)

        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.updateService(
                 req

                )?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                    try{

                        if(response.body()!!.result==1){
                            if(arrayRequiredFiles.size > 0 && arrayRequiredFiles.count { it.multipart!=null }>0){
                               for (i in arrayRequiredFiles.indices){
                                   if(arrayRequiredFiles[i].multipart!=null){
                                       uploadFiles(arrayRequiredFiles[i],MyApplication.selectedService!!.id!!.toInt())
                                   }
                               }
                                loading.hide()
                               // this@ActivityServiceInformation.onBackPressed()

                            }else {
                                loading.hide()
                                this@ActivityServiceInformation.onBackPressed()
                            }
                        }

                        else{
                            loading.hide()
                            toast("failed 1")
                        }
                    }catch (E: java.lang.Exception){
                        loading.hide()
                        toast("failed 2")
                    }
                }
                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    loading.hide()
                    toast("failed 3")
                }
            })
    }


/*    fun uploadFiles(){
        loading.show()
        var arrayBodyRequiredFiles: java.util.ArrayList<MultipartBody.Part> = arrayListOf()
        if(arrayRequiredFiles.size>0){
            for (i in arrayRequiredFiles.indices)
                arrayBodyRequiredFiles.add(arrayRequiredFiles[i].multipart!!)
        }
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.uploadFiles(arrayBodyRequiredFiles)?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                    try{
                        loading.hide()
                        if(response.body()!!.result==1){
                            toast("required files uploaded")
                        }

                        else
                            toast("failed to upload required files")
                    }catch (E: java.lang.Exception){
                        toast("failed to upload required files")
                    }
                }
                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {
                    loading.hide()
                    toast("failed to upload required files")
                }
            })
    }*/


    private fun requiredFilesUploaded():Boolean{
        return arrayRequiredFiles.count { it.selectedFileName=="" } == 0
    }


    private fun setEditData(){
         if(MyApplication.selectedService!=null){
             if(MyApplication.selectedService!!.type == AppConstants.TYPE_PURCHASE){
                rbPurchase.isChecked=true
             }
             else{
                 rbRental.isChecked=true
             }

             try{
             var serviceSpinnerSelected=arraySpinnerServices.find { it.name == MyApplication.selectedService!!.name }
             spService.setSelection(arraySpinnerServices.indexOf(serviceSpinnerSelected))}catch (e:Exception){}

             try{
             var serviceTypesSelected=arraySpinnerTypes.find { it.name == MyApplication.selectedService!!.variations[0].types }
             spType.setSelection(arraySpinnerTypes.indexOf(serviceTypesSelected))}catch (e:Exception){}

             try{
                 var serviceSizeSelected=arraySpinnerSizes.find { it.name == MyApplication.selectedService!!.variations[0].sizeCapacity }
                 spSize.setSelection(arraySpinnerSizes.indexOf(serviceSizeSelected))
             }catch (e:Exception){}


             try{
                 var serviceSizeSelected=arraySpinnerSizes.find { it.name == MyApplication.selectedService!!.variations[0].sizeCapacity }
                 spSize.setSelection(arraySpinnerSizes.indexOf(serviceSizeSelected))
             }catch (e:Exception){}

             try{

                 etStockAvailable.setText(MyApplication.selectedService!!.variations[0].stockQuantity!!.toString())
             }catch (e:Exception){}

             try{
                 arrayImagesSelected.clear()
                 var images=MyApplication.selectedService!!.variations[0].images
                 if(images.size>0){
                    for (i in images.indices)
                      arrayImagesSelected.add(FilesSelected(images[i],null,null))
                 }
                      adapterSelectedImages!!.notifyDataSetChanged()
                 }
             catch (e:Exception){}


             try{
                 arrayRequiredFiles.clear()
                 if(MyApplication.selectedService!!.files.size>0){
                     var arrayFiles=MyApplication.selectedService!!.files
                     for (i in arrayFiles.indices)
                         arrayRequiredFiles.add(RequiredFiles(
                             arrayFiles[i].id,
                             arrayFiles[i].name!!,
                             arrayFiles[i].name!!,
                             arrayFiles[i].name!!,
                             null,
                             if(arrayFiles[i].url!=null) arrayFiles[i].url!! else ""
                         ))

                 }
                 if(arrayRequiredFiles.size>0)
                     setRequiredFiles()
                 else
                     noRequiredDocuments()
             }catch (e:Exception){}


             rbRental.isEnabled=false
             rbPurchase.isEnabled=false
             spService.isEnabled=false
             spType.isEnabled=false
             spSize.isEnabled=false
             spStock.isEnabled=false
             btPickImage.isEnabled=false
             btNext2.text=AppHelper.getRemoteString("UpdateService",this)

         }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
       // loading.hide()
    }

}