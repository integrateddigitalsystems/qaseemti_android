package com.ids.qasemti.controller.Fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Activities.ActivityMapAddress
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.RequestAddAddress
import com.ids.qasemti.model.RequestUpdateLanguage
import com.ids.qasemti.model.ResponseMessage
import com.ids.qasemti.model.ResponseUser
import com.ids.qasemti.utils.*
import com.ids.qasemti.utils.AppHelper.Companion.toEditable
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.config.Configurations
import com.jaiselrahman.filepicker.model.MediaFile
import kotlinx.android.synthetic.main.curve_layout_home.*
import kotlinx.android.synthetic.main.layout_profile.*
import kotlinx.android.synthetic.main.loading.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class FragmentProfile : Fragment(), RVOnItemClickListener, ApiListener {
    var selectedFile: MultipartBody.Part? = null
    var selectedProfilePic: MultipartBody.Part? = null
    var selectedPhoto : String ?=""
    var gender = "female"
    var  mPermissionResult : ActivityResultLauncher<Array<String>> ?=null
    var fromCam : Boolean ?= false
    var lat: Double? = 0.0
    var photoURI : Uri ?=null
    var photoFile: File? = null
    var resultLauncher: ActivityResultLauncher<Intent>? = null
    var fromProfile: Boolean? = false
    var long: Double? = 0.0
    var profilePercentage = 0
    private val IMAGE_PICK_CODE = 1000
    private val CAMERA_CODE = 1003
    private val CHOOSER_CODE = 1004
    private val PERMISSION_WRITE = 1005
    private val PERMISSION_CODE = 1001
    private val PERMISSION_CODE_CAMERA = 1002




    override fun onItemClicked(view: View, position: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.layout_profile, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutProfile, requireContext())
        init()



    }

    private fun pickImageFromGallery() {


        /*var i = Intent(Intent.ACTION_GET_CONTENT);
        i.setType("application/pdf");
        i.addCategory(Intent.CATEGORY_OPENABLE);
        resultLauncher!!.launch(i)*/
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

       resultLauncher!!.launch(intent)
       // startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        selectedPhoto = image.absolutePath
        return image
    }

    private fun pickImageFromCamera() {

        fromCam = true
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .cameraOnly()
            .maxResultSize(1080, 1080)
            .createIntent {
                resultLauncher!!.launch(it)
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

    /*private fun pickImageFromCamera() {
        fromProfile = true
        val pictureIntent = Intent(
            MediaStore.ACTION_IMAGE_CAPTURE
        ).addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)

        if (pictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            //Create a file to store the image
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
            }// Error occurred while creating the File

            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(requireActivity(), "com.ids.qasemti.provider", photoFile!!)
                pictureIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    photoURI
                )
                *//*val resInfoList: List<ResolveInfo> = requireActivity().getPackageManager().queryIntentActivities(
                    pictureIntent,
                    PackageManager.MATCH_DEFAULT_ONLY
                )
                for (resolveInfo in resInfoList) {
                    val packageName = resolveInfo.activityInfo.packageName
                    requireActivity().grantUriPermission(
                        packageName,
                        photoURI,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }*//*

               // val chooser = Intent.createChooser(pictureIntent, "Share File")

               *//* val resInfoList: List<ResolveInfo> = requireActivity().getPackageManager()
                    .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

                for (resolveInfo in resInfoList) {
                    val packageName = resolveInfo.activityInfo.packageName
                    requireActivity().grantUriPermission(
                        packageName,
                        photoURI,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }*//*

                //startActivity(chooser)
                fromCam = true
                resultLauncher!!.launch(pictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }
    }*/

    private fun selectImage(context: Context) {



        val options = arrayOf<CharSequence>("Take Photo",
            "Choose from Gallery",
            getString(R.string.cancel))

        val builder = AlertDialog.Builder(context)
        builder.setTitle(AppHelper.getRemoteString("choose_file",requireActivity()))

        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Take Photo" -> pickImageFromCamera()
                options[item] == "Choose from Gallery" -> pickImageFromGallery()
                options[item] == getString(R.string.cancel) -> dialog.dismiss()
            }
        }
        builder.show()
    }

    fun setUp(){
        mPermissionResult = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { result ->
            var permission = false
            for( item in result){
                permission = item.value
            }
            if (permission) {
                selectImage(requireContext())
                Log.e(TAG, "onActivityResult: PERMISSION GRANTED")
            } else {
                Log.e(TAG, "onActivityResult: PERMISSION DENIED")
            }
        }
    }
    private fun openChooser(){





        mPermissionResult!!.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when {
                PermissionChecker.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PermissionChecker.PERMISSION_DENIED -> {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestMultiplePermissions.launch(permissions)
                }
                PermissionChecker.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PermissionChecker.PERMISSION_DENIED -> {
                    val permissions = arrayOf(Manifest.permission.CAMERA)
                    requestMultiplePermissions.launch(permissions) }
                PermissionChecker.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_DENIED -> {
                    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestMultiplePermissions.launch(permissions) }
                else -> {}//pickFromChooser()
            }
        }
        else{}*/
            //pickFromChooser()

    }

    fun setUserData() {
        profilePercentage = 0
        // loading.show()
        // AppHelper.getUserInfo()
        try {
            etFirstNameProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.firstName)
        } catch (ex: Exception) {
            etFirstNameProfile.text = Editable.Factory.getInstance().newEditable("")
        }
        try {
            etMiddleNameProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.middleName)
        } catch (ex: Exception) {
            etMiddleNameProfile.text = Editable.Factory.getInstance().newEditable("")
        }
        try {
            etLastNameProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.lastName)
        } catch (ex: Exception) {
            etLastNameProfile.text = Editable.Factory.getInstance().newEditable("")
        }
        try {
            etEmailProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.email)
        } catch (ex: Exception) {
            etEmailProfile.text = Editable.Factory.getInstance().newEditable("")
        }
        try {
            etMobileProfile.text =
                Editable.Factory.getInstance()
                    .newEditable(MyApplication.selectedUser!!.mobileNumber)
        } catch (ex: Exception) {
            etMobileProfile.text = Editable.Factory.getInstance().newEditable("")
        }
        try {
            etCivilIdNbProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.civilId)
        } catch (ex: Exception) {
            etCivilIdNbProfile.text = Editable.Factory.getInstance().newEditable("")
        }
        try {
            etDateOfBirthProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.birthday)
        } catch (ex: Exception) {
            etDateOfBirthProfile.text = Editable.Factory.getInstance().newEditable("")
        }
        try {
            etAltContactNumberProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.altrNumb)
        } catch (ex: Exception) {
            etAltContactNumberProfile.text = Editable.Factory.getInstance().newEditable("")
        }
        try {
            etAddressProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.location)
        } catch (ex: Exception) {
            etAddressProfile.text = Editable.Factory.getInstance().newEditable("")
        }
        try {
            etAccountNumberProfile.text =
                Editable.Factory.getInstance()
                    .newEditable(MyApplication.selectedUser!!.accountNumber)
        } catch (ex: Exception) {
            etAccountNumberProfile.text = Editable.Factory.getInstance().newEditable("")
        }
        try {
            etBankNameProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.bankName)
        } catch (ex: Exception) {
            etBankNameProfile.text = Editable.Factory.getInstance().newEditable("")
        }
        try {
            etBranchNameProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.bankBranch)
        } catch (ex: Exception) {
            etBranchNameProfile.text = Editable.Factory.getInstance().newEditable("")
        }
        try {
            etDescriptionProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.desc)
        } catch (ex: Exception) {
            etDescriptionProfile.text = Editable.Factory.getInstance().newEditable("")
        }
        try {
            etCivilIdNbProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.civil_id)
        } catch (ex: Exception) {
            etCivilIdNbProfile.text = Editable.Factory.getInstance().newEditable("")
        }
        try {
            etBranchNameProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.bankBranch)
        } catch (ex: Exception) {
            etBranchNameProfile.text = Editable.Factory.getInstance().newEditable("")
        }
        try {
            etIBANProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.IBAN)
        } catch (ex: Exception) {
            etIBANProfile.text = Editable.Factory.getInstance().newEditable("")
        }
        try {
            etDateOfBirthProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.dob)
        } catch (ex: Exception) {
            etDateOfBirthProfile.text = Editable.Factory.getInstance().newEditable("")
        }

        try {
            if (!MyApplication.selectedUser!!.profilePicUrl.isNullOrEmpty())
                ivProfile.loadRoundedImage(MyApplication.selectedUser!!.profilePicUrl!!)
        } catch (e: Exception) {
        }

        try {
            if (MyApplication.selectedUser!!.gender.equals("female")) {
                rbFemaleProfile.isChecked = true
                rbMaleProfile.isChecked = false
            } else {
                rbFemaleProfile.isChecked = false
                rbMaleProfile.isChecked = true
            }
        } catch (ex: Exception) {
            rbFemaleProfile.isChecked = true
            rbMaleProfile.isChecked = false
        }

        try {
            tvUsername.text =
                MyApplication.selectedUser!!.firstName + " " + MyApplication.selectedUser!!.lastName
        } catch (e: Exception) {
        }

        if (!MyApplication.isClient) {
            if (!MyApplication.selectedUser!!.mobileNumber.isNullOrEmpty())
                profilePercentage += 25
            if (!MyApplication.selectedUser!!.firstName.isNullOrEmpty() && !MyApplication.selectedUser!!.middleName.isNullOrEmpty() && !MyApplication.selectedUser!!.lastName.isNullOrEmpty())
                profilePercentage += 25
            try{
            if (MyApplication.selectedUser!!.addresses!!.size>0){
                if(MyApplication.selectedUser!!.addresses!![0].addressId!=null && MyApplication.selectedUser!!.addresses!![0].addressName!=null)
                   profilePercentage += 25

            }}catch (e:Exception){}
            if (!MyApplication.selectedUser!!.accountNumber.isNullOrEmpty() && !MyApplication.selectedUser!!.bankName.isNullOrEmpty() && !MyApplication.selectedUser!!.bankBranch.isNullOrEmpty()  /*&& !MyApplication.selectedUser!!.iban.isNullOrEmpty()*/)
                profilePercentage += 25
        } else {
            if (!MyApplication.selectedUser!!.mobileNumber.isNullOrEmpty())
                profilePercentage += 25
            if (!MyApplication.selectedUser!!.firstName.isNullOrEmpty() && !MyApplication.selectedUser!!.lastName.isNullOrEmpty())
                profilePercentage += 25
            if (!MyApplication.selectedUser!!.email.isNullOrEmpty())
                profilePercentage += 25
            if (!MyApplication.selectedUser!!.profilePicUrl.isNullOrEmpty())
                profilePercentage += 25
        }

        try{

        pbComplete.setWeight(profilePercentage.toFloat())
        pbNotComplete.setWeight(100f - profilePercentage.toFloat())
        tvPercentageCompleted.text =
            profilePercentage.toString() + " % " + AppHelper.getRemoteString(
                "completed",
                requireActivity()
            )}catch (e:Exception){}

        if(!MyApplication.isClient){
            if(MyApplication.selectedUser!!.addresses!=null && MyApplication.selectedUser!!.addresses!!.size > 0){
                if(MyApplication.selectedUser!!.addresses!![0].addressId!=null && MyApplication.selectedUser!!.addresses!![0].addressName!=null){

                linearAddressInfo.show()

                MyApplication.addNewAddress=false
                btAddNewAddress.hide()
            //    btAddNewAddress.text = AppHelper.getRemoteString("UpdateAddress",requireActivity())


                try{
                var myAddress= MyApplication.selectedUser!!.addresses!![0]
                if(myAddress.addressName!=null && myAddress.addressName!="null")
                    etAddressName!!.setText(myAddress.addressName)

                if(myAddress.desc!=null && myAddress.desc!="null")
                    etMoreDetails!!.setText(myAddress.desc)

                if(myAddress.area!=null && myAddress.area!="null")
                    etArea!!.setText(myAddress.area)

                if(myAddress.block!=null && myAddress.block!="null")
                    etBlock!!.setText(myAddress.block)

                if(myAddress.avenue!=null && myAddress.avenue!="null")
                    etAvenue!!.setText(myAddress.avenue)

                if(myAddress.apartment!=null && myAddress.apartment!="null")
                    etApartment!!.setText(myAddress.apartment)

                if(myAddress.floor!=null && myAddress.floor!="null")
                    etFloor!!.setText(myAddress.floor)

                if(myAddress.bldg!=null && myAddress.bldg!="null")
                    etBuilding!!.setText(myAddress.bldg)

                if(myAddress.street!=null && myAddress.street!="null")
                    etStreet!!.setText(myAddress.street)

                 if(myAddress.province!=null && myAddress.province!="null")
                    etAddressProvince!!.setText(myAddress.province)

                }catch (e:java.lang.Exception){}



                btUpdateAddress.show()
                btUpdateAddress.setOnClickListener{
                    startActivity(Intent(requireActivity(),ActivityMapAddress::class.java))
                }}else{
                    MyApplication.addNewAddress=true
                    btAddNewAddress.text = AppHelper.getRemoteString("AddNewAddress",requireActivity())
                    linearAddressInfo.hide()
                    btUpdateAddress.hide()
                }

            }else{
                MyApplication.addNewAddress=true
                btAddNewAddress.text = AppHelper.getRemoteString("AddNewAddress",requireActivity())
                linearAddressInfo.hide()
                btUpdateAddress.hide()
            }
        }

    }

    fun init() {
        //tvToolbarCurveTitle.setText(getString(R.string.profile))
        (activity as ActivityHome?)!!.showBack(R.color.white)
        AppHelper.setTitle(
            requireActivity(), MyApplication.selectedTitle!!,"",R.color.white)
        rbFemaleProfile.isSelected = true
        // (activity as ActivityHome?)!!.showLogout(false)
        tvToolbarCurveTitle.visibility = View.GONE
        listeners()
        if (MyApplication.isClient)
            showClientFields()
        getUserData()


    }

    fun succUpdate(res: Int) {
        if (res == 1) {
            AppHelper.createDialog(requireActivity(), "Update Successful")
            loading.hide()
            getUserData()
        } else {
            AppHelper.createDialog(requireActivity(), "Update Failed")
        }
    }

    fun getUserData() {
        loading.show()
        var newReq = RequestUpdateLanguage(MyApplication.userId, MyApplication.languageCode)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getUser(
                newReq
            )?.enqueue(object : Callback<ResponseUser> {
                override fun onResponse(
                    call: Call<ResponseUser>,
                    response: Response<ResponseUser>
                ) {
                    try {
                        MyApplication.selectedUser = response.body()!!.user
                    } catch (e: Exception) {

                    }
                    loading.hide()
                    try{setUserData()}catch (e:Exception){}
                }

                override fun onFailure(call: Call<ResponseUser>, throwable: Throwable) {
                    loading.hide()
                }
            })
    }

    fun updateClient() {
        try {
            loading.show()
        } catch (ex: java.lang.Exception) {

        }

        val user = MyApplication.userId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val first =
            etFirstNameProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val last = etLastNameProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val email = etEmailProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val phone = etMobileProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        if (selectedProfilePic == null) {
            var empty = ""
            val attachmentEmpty = empty.toRequestBody("text/plain".toMediaTypeOrNull())

            selectedProfilePic = createFormData("profile_pic", "", attachmentEmpty)
        }
        var type = "1"
        var typeReq = type.toRequestBody()


        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.updateClientProfile(
                user,
                phone,
                first,
                last,
                email,
                selectedProfilePic!!,
                typeReq


            )?.enqueue(object : Callback<ResponseUser> {
                override fun onResponse(
                    call: Call<ResponseUser>,
                    response: Response<ResponseUser>
                ) {
                    try {
                        loading.hide()
                        succUpdate(response.body()!!.result!!)
                    } catch (E: java.lang.Exception) {
                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponseUser>, throwable: Throwable) {
                    loading.hide()
                }
            })

    }


    fun listeners() {




        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {

                    var file : File ?=null
                    if(fromCam!!) {
                        file = File(result.data!!.data!!.path)
                        fromCam = false
                    }else{
                         try {
                         /*   val files: ArrayList<MediaFile> =
                                result.data!!.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES)!!*/
                            //   var path = getPath(files.get(0).uri)
                                file = AppHelper.getFile(requireActivity(),  result.data!!.data!!)


                        } catch (e: Exception) {
                            Log.wtf("tag","tag")
                        }
                    }

                    var req = file!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    if (!fromProfile!!)

                        selectedFile = MultipartBody.Part.createFormData(
                            ApiParameters.CIVIL_ID_ATTACH,
                            file.name + "File",
                            req
                        )
                    else {
                        ivProfile.loadRoundedLocalImage(file)
                        selectedProfilePic = MultipartBody.Part.createFormData(
                            if (MyApplication.isClient) ApiParameters.PROFILE_PIC else ApiParameters.FILE,
                            file.name + "File",
                            req
                        )
                    }
                    }

                  /*  if (requestCode == 1000) {
                        if (resultCode == Activity.RESULT_OK) {
                            val extras = data!!.extras
                            if (extras != null) {
                                lat = extras.getDouble("lat")
                                long = extras.getDouble("long")
                                etAddressProfile.text = Editable.Factory.getInstance()
                                    .newEditable(AppHelper.getAddress(lat!!, long!!, requireContext()))
                            }

                        }
                    } else {*/

            }
        btSaveProfile.onOneClick {
            if(etFirstNameProfile.text.isNullOrEmpty() || (!MyApplication.isClient && etMiddleNameProfile.text.isNullOrEmpty() )||etLastNameProfile.text.isNullOrEmpty() || etEmailProfile.text.isNullOrEmpty()||etMobileProfile.text.isNullOrEmpty())
                AppHelper.createDialog(requireActivity(),AppHelper.getRemoteString("fill_all_field",requireContext()))
            else if(!MyApplication.isClient){
                updateServiceProfile()
            }else
                updateClientProfile()
         }


        rbMaleProfile.onOneClick {
            gender = "male"
        }
        rbFemaleProfile.onOneClick {
            gender = "female"
        }

        llAddressProfile.setOnClickListener {
            startActivityForResult(Intent(requireContext(), ActivityMapAddress::class.java), 1000)
        }
        ibUploadFile.setOnClickListener {
            pickFile(1, false)
        }
        ivProfile.setOnClickListener {
            pickFile(1, true)
        }

        etDateOfBirthProfile.onOneClick {
            var mcurrentDate = Calendar.getInstance()
            var mYear = 0
            var mMonth = 0
            var mDay = 0
            mYear = mcurrentDate!![Calendar.YEAR]
            mMonth = mcurrentDate!![Calendar.MONTH]
            mDay = mcurrentDate!![Calendar.DAY_OF_MONTH]

            mcurrentDate.set(mYear, mMonth, mDay)
            val mDatePicker = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { datepicker, selectedyear, selectedmonth, selectedday ->
                    val myCalendar = Calendar.getInstance()
                    myCalendar[Calendar.YEAR] = selectedyear
                    myCalendar[Calendar.MONTH] = selectedmonth
                    myCalendar[Calendar.DAY_OF_MONTH] = selectedday
                    val myFormat = "dd/MM/yyyy" //Change as you need
                    var sdf =
                        SimpleDateFormat(myFormat, Locale.ENGLISH)
                    var date = sdf.format(myCalendar.time)
                    etDateOfBirthProfile.text =
                        (String.format("%02d", selectedday) + "/" + String.format(
                            "%02d",
                            selectedmonth
                        ) + "/" + String.format("%02d", selectedyear)).toEditable()
                }, mYear, mMonth, mDay
            )
           var cal = mcurrentDate.add(Calendar.YEAR,-18)
            mDatePicker.datePicker.maxDate = mcurrentDate.time.time
            mDatePicker.show()
        }

        btAddNewAddress.onOneClick {
            startActivity(Intent(requireActivity(),ActivityMapAddress::class.java))
        }
    }

    private fun updateServiceProfile(){
        if (selectedProfilePic == null) {
            var empty = ""
            val attachmentEmpty = empty.toRequestBody("text/plain".toMediaTypeOrNull())

            selectedProfilePic =
                MultipartBody.Part.createFormData("attachment", "", attachmentEmpty)
        }
        if(selectedFile==null){
            var empty = ""
            val attachmentEmpty = empty.toRequestBody("text/plain".toMediaTypeOrNull())

            selectedFile =
                MultipartBody.Part.createFormData("attachment", "", attachmentEmpty)
        }
        CallAPIs.updateProfileServiceProvider(
            requireContext(),
            this,
            loading,
            lat!!,
            long!!,
            gender,
            etFirstNameProfile.text.toString(),
            etMiddleNameProfile.text.toString(),
            etLastNameProfile.text.toString(),
            etEmailProfile.text.toString(),
            etMobileProfile.text.toString(),
            etAltContactNumberProfile.text.toString(),
            etCivilIdNbProfile.text.toString(),
            etDateOfBirthProfile.text.toString(),
            etAddressProfile.text.toString(),
            etAccountNumberProfile.text.toString(),
            etBankNameProfile.text.toString(),
            etBranchNameProfile.text.toString(),
            etDescriptionProfile.text.toString(),
            etIBANProfile.text.toString(),
            selectedFile!!,
            selectedProfilePic!!
        )

        if(!MyApplication.addNewAddress)
            updateAddress()
    }

    private fun updateClientProfile(){
        if (selectedProfilePic == null) {
            var empty = ""
            val attachmentEmpty = empty.toRequestBody("text/plain".toMediaTypeOrNull())

            selectedProfilePic =
                MultipartBody.Part.createFormData("attachment", "", attachmentEmpty)
        }
        CallAPIs.updateProfileClient(
            requireContext(),
            this,
            loading,
            etFirstNameProfile.text.toString(),
            etLastNameProfile.text.toString(),
            etEmailProfile.text.toString(),
            etMobileProfile.text.toString(),
            selectedProfilePic!!
        )
    }


    fun getPath(uri: Uri?): String? {
        println("IM IN getPath")
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor =
            requireActivity().getContentResolver().query(uri!!, projection, null, null, null)!!
        val column_index: Int = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                val extras = data!!.extras
                if (extras != null) {
                    lat = extras.getDouble("lat")
                    long = extras.getDouble("long")
                    etAddressProfile.text = Editable.Factory.getInstance()
                        .newEditable(AppHelper.getAddress(lat!!, long!!, requireContext()))
                }

            }
        } else {
            try {
                val files: ArrayList<MediaFile> =
                    data!!.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES)!!
                //   var path = getPath(files.get(0).uri)
                var file = AppHelper.getFile(requireActivity(), files[0].uri)
                var req = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                if (!fromProfile!!)
                   
                    selectedFile = MultipartBody.Part.createFormData(
                        ApiParameters.CIVIL_ID_ATTACH,
                        file.name + "File",
                        req
                    )
                else {
                    ivProfile.loadRoundedLocalImage(file)
                    selectedProfilePic = MultipartBody.Part.createFormData(
                        if (MyApplication.isClient) ApiParameters.PROFILE_PIC else ApiParameters.FILE,
                        file.name + "File",
                        req
                    )
                }

            } catch (e: Exception) {
            }
        }
    }

    private fun pickFile(pickCode: Int, from: Boolean) {

        /*val intent = Intent(requireContext(), FilePickerActivity::class.java)
        intent.putExtra(
            FilePickerActivity.CONFIGS, Configurations.Builder()
                .setCheckPermission(true)
                // .setSelectedMediaFiles(mediaFiles)
                // .enableImageCapture(true)
                .setShowImages(true)
                .setShowVideos(false)
                .setSkipZeroSizeFiles(true)
                .setMaxSelection(1)
                .setShowFiles(false)
                .setShowAudios(false)
                .build()
        )*/
        fromProfile = from
        openChooser()
      //  startActivityForResult(intent, pickCode)

    }

    fun updateProfile() {
        try {
            loading.show()
        } catch (ex: java.lang.Exception) {

        }
        var userId = MyApplication.userId.toString()
        var rolev = "vendor"
        var latt = lat.toString()
        var longg = long.toString()
        var add = "Sidon, Sidon District, South Governorate, 1600, Lebanon"
        var gender = "female"
        if (rbMaleProfile.isSelected) {
            gender = "male"
        }
        val user = userId.toRequestBody("text/plain".toMediaTypeOrNull())
        val first =
            etFirstNameProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val middle =
            etMiddleNameProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val last = etLastNameProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val email = etEmailProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val phone = etMobileProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val altNUm = etAltContactNumberProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val civilId =
            etCivilIdNbProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val genderr = gender.toRequestBody("text/plain".toMediaTypeOrNull())
        val dob =
            etDateOfBirthProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val role = rolev.toRequestBody("text/plain".toMediaTypeOrNull())
        val address =
            etAddressProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val lat = latt.toRequestBody("text/plain".toMediaTypeOrNull())
        val long = longg.toRequestBody("text/plain".toMediaTypeOrNull())
        val accNum =
            etAccountNumberProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val bankname =
            etBankNameProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val bankBranch =
            etBranchNameProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val description =
            etDescriptionProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val iban = etIBANProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        if (selectedFile == null) {
            var empty = ""
            val attachmentEmpty = empty.toRequestBody("text/plain".toMediaTypeOrNull())

            selectedFile = createFormData("attachment", "", attachmentEmpty)
        }
        if (selectedProfilePic == null) {
            var empty = ""
            val attachmentEmpty = empty.toRequestBody("text/plain".toMediaTypeOrNull())

            selectedProfilePic = createFormData("attachment", "", attachmentEmpty)
        }


        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.updateProfile(
                user,
                first,
                middle,
                last,
                email,
                phone,
                altNUm,
                civilId,
                selectedFile!!,
                genderr,
                dob,
                role,
                selectedFile!!,
                address,
                lat,
                long,
                accNum,
                bankname,
                bankBranch,
                iban,
                description

            )?.enqueue(object : Callback<ResponseUser> {
                override fun onResponse(
                    call: Call<ResponseUser>,
                    response: Response<ResponseUser>
                ) {
                    try {
                        succUpdate(response.body()!!.result!!)
                    } catch (E: java.lang.Exception) {
                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponseUser>, throwable: Throwable) {
                    loading.hide()
                }
            })
    }

    private fun showClientFields() {
        etMiddleNameProfile.hide()
        linearProviderInfo.hide()
    }

    override fun onDataRetrieved(success: Boolean, response: Any, apiId: Int) {
        var res = response as ResponseUser
        succUpdate(res.result!!)
    }


    fun updateAddress() {
        var addressId=0
        try{
            if(!MyApplication.isClient && !MyApplication.addNewAddress)
                addressId= MyApplication.selectedUser!!.addresses!![0].addressId!!.toInt()}catch (e: java.lang.Exception){}

        var newReq = RequestAddAddress(
            MyApplication.userId,
            MyApplication.selectedUser!!.addresses!![0].lat!!.toDouble(),
            MyApplication.selectedUser!!.addresses!![0].long!!.toDouble(),
            addressId,
            etAddressName.text.toString(),
            etStreet.text.toString(),
            etBuilding.text.toString(),
            etFloor.text.toString(),
            etMoreDetails.text.toString(),
            "",
            etAddressProvince.text.toString(),
            etArea.text.toString(),
            etBlock.text.toString(),
            etAvenue.text.toString(),
            etApartment.text.toString()
        )
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.addClAddress(
                newReq
            )?.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(
                    call: Call<ResponseMessage>,
                    response: Response<ResponseMessage>
                ) {

                }

                override fun onFailure(call: Call<ResponseMessage>, throwable: Throwable) {

                }
            })

    }

}