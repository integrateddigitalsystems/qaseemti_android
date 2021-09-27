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
import com.ids.qasemti.controller.Adapters.SampleFragmentPagerAdapter
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.ActivityBase
import com.ids.qasemti.model.ServiceItem
import com.ids.qasemti.model.ServicesData
import com.ids.qasemti.utils.*
import com.ids.sampleapp.model.ItemSpinner
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.config.Configurations
import com.jaiselrahman.filepicker.model.MediaFile
import kotlinx.android.synthetic.main.actiivity_service_information.*
import kotlinx.android.synthetic.main.activity_services.rootLayout
import kotlinx.android.synthetic.main.no_logo_layout.btBck
import kotlinx.android.synthetic.main.service_tab_1.*
import kotlinx.android.synthetic.main.service_tab_2.*
import kotlinx.android.synthetic.main.service_tab_3.*


class ActivityServiceInformation : ActivityBase(), RVOnItemClickListener {
    var array : ArrayList<ServiceItem> = arrayListOf()
    private val CODE_IMAGE = 1001
    private val CODE_DRIVING_LICENSE = 1002
    private val CODE_WORK_LICENSE = 1003
    private val CODE_VEHICLE_LICENSE = 1004
    private var selectedCategoryId=1
    private var selectedCategoryName=""
    private var selectedServiceId=0
    private var selectedServiceName=""
    private var selectedTypeId=0
    private var selectedTypeName=""
    private var selectedSizeId=0
    private var selectedSizeName=""
    private var selectedQtyId=0
    private var selectedQtyName=""
    var arrayData: ArrayList<ServicesData> = arrayListOf()
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
        setServiceSpinner()
        setTypeSpinner()
        setSizeCapacitySpinner()
        setStockSpinner()

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
            toast("sending data")
        }
        rgCategory.setOnCheckedChangeListener { group, checkedId ->
            val rb = findViewById<View>(checkedId) as RadioButton
            if(checkedId==R.id.rbPurchase){
                selectedCategoryId=1
            }else{
                selectedCategoryId=2
            }
            selectedCategoryName=rb.text.toString()
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
        val arrayService : java.util.ArrayList<ItemSpinner> = arrayListOf()

        arrayService.add(ItemSpinner(0,"Water Tank",""))
        arrayService.add(ItemSpinner(1,"Sand Truck",""))
        arrayService.add(ItemSpinner(2,"Gravel Truck",""))
        arrayService.add(ItemSpinner(3,"Rubble Truck Pickup",""))
        arrayService.add(ItemSpinner(4,"Rubble Truck Long Team",""))
        arrayService.add(ItemSpinner(5,"Sewage Tank",""))
        val adapterServices = AdapterGeneralSpinner(this, R.layout.spinner_layout, arrayService,0)
        spService.adapter = adapterServices
        adapterServices.setDropDownViewResource(R.layout.item_spinner_drop_down)
        spService.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
              selectedServiceId=arrayService[position].id!!
              selectedServiceName=arrayService[position].name!!
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }


    private fun setTypeSpinner(){
        val arrayTypes : java.util.ArrayList<ItemSpinner> = arrayListOf()

        arrayTypes.add(ItemSpinner(0,"Fresh",""))
        arrayTypes.add(ItemSpinner(1,"Not fresh",""))
        arrayTypes.add(ItemSpinner(2,"Other",""))
        val adapterTypes = AdapterGeneralSpinner(this, R.layout.spinner_layout, arrayTypes,0)
        spType.adapter = adapterTypes
        adapterTypes.setDropDownViewResource(R.layout.item_spinner_drop_down)
        spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedTypeId=arrayTypes[position].id!!
                selectedTypeName=arrayTypes[position].name!!
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }


    private fun setSizeCapacitySpinner(){
        val arraySize : java.util.ArrayList<ItemSpinner> = arrayListOf()

        arraySize.add(ItemSpinner(0,"Size 1",""))
        arraySize.add(ItemSpinner(1,"Size 2",""))
        arraySize.add(ItemSpinner(2,"Size 3",""))
        val adapterSize = AdapterGeneralSpinner(this, R.layout.spinner_layout, arraySize,0)
        spSize.adapter = adapterSize
        adapterSize.setDropDownViewResource(R.layout.item_spinner_drop_down)
        spSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedSizeId=arraySize[position].id!!
                selectedSizeName=arraySize[position].name!!
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }


    private fun setStockSpinner(){
        val arrayStock : java.util.ArrayList<ItemSpinner> = arrayListOf()

        arrayStock.add(ItemSpinner(0,"Stock 1",""))
        arrayStock.add(ItemSpinner(1,"Stock 2",""))
        arrayStock.add(ItemSpinner(2,"Stock 3",""))
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
                       // pickedFile=Files(-1,files[0].path.toString(),"",files[0].mimeType,requestId,MyApplication.selectedRequest.citizenId,AppConstants.FORM_ID_MAHDAR,1)
                     } catch (e: Exception) {
                    }
                }

                CODE_DRIVING_LICENSE -> {
                    try {
                        tvPickedDrivingLicense.text = files[0].name
                        // pickedFile=Files(-1,files[0].path.toString(),"",files[0].mimeType,requestId,MyApplication.selectedRequest.citizenId,AppConstants.FORM_ID_MAHDAR,1)
                    } catch (e: Exception) {
                    }
                }

                CODE_WORK_LICENSE -> {
                    try {
                        tvPickedWorkLicense.text = files[0].name
                        // pickedFile=Files(-1,files[0].path.toString(),"",files[0].mimeType,requestId,MyApplication.selectedRequest.citizenId,AppConstants.FORM_ID_MAHDAR,1)
                    } catch (e: Exception) {
                    }
                }

                CODE_VEHICLE_LICENSE -> {
                    try {
                        tvPickedVehicle.text = files[0].name
                        // pickedFile=Files(-1,files[0].path.toString(),"",files[0].mimeType,requestId,MyApplication.selectedRequest.citizenId,AppConstants.FORM_ID_MAHDAR,1)
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
}