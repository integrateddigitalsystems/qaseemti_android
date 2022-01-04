package com.ids.qasemti.controller.Fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Activities.ActivityMapAddress
import com.ids.qasemti.controller.Adapters.AdapterGeneralSpinner
import com.ids.qasemti.controller.Adapters.AdapterGridFiles
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.controller.MyApplication.Companion.arraySelectedImage
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import com.ids.qasemti.utils.AppHelper.Companion.toEditable
import com.ids.sampleapp.model.ItemSpinner
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.model.MediaFile
import kotlinx.android.synthetic.main.activity_new_address.*
import kotlinx.android.synthetic.main.activity_place_order.*
import kotlinx.android.synthetic.main.curve_layout_home.*
import kotlinx.android.synthetic.main.layout_profile.*
import kotlinx.android.synthetic.main.layout_profile.etAddressName
import kotlinx.android.synthetic.main.layout_profile.etAddressProvince
import kotlinx.android.synthetic.main.layout_profile.etApartment
import kotlinx.android.synthetic.main.layout_profile.etArea
import kotlinx.android.synthetic.main.layout_profile.etAvenue
import kotlinx.android.synthetic.main.layout_profile.etBlock
import kotlinx.android.synthetic.main.layout_profile.etBuilding
import kotlinx.android.synthetic.main.layout_profile.etFloor
import kotlinx.android.synthetic.main.layout_profile.etMoreDetails
import kotlinx.android.synthetic.main.layout_profile.etStreet
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.service_tab_1.*
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
import kotlin.collections.ArrayList


class FragmentProfile : Fragment(), RVOnItemClickListener, ApiListener {
    var selectedFile: MultipartBody.Part? = null
    var selectedFile2: MultipartBody.Part? = null
    var civilImageAvailable : Boolean = false
    var civilImageBackAvailable : Boolean =false
    var emptyCivilIdFront : String ?=""
    var emptyCivilIdBack : String ?=""
    var selectedProfilePic: MultipartBody.Part? = null
    var adapterSelectedImages: AdapterGridFiles? = null
    var selectedPhoto: String? = ""
    var gender = ""
    var tempProfile: String? = null
    var selectedProvince: String? = ""
    var mPermissionResult: ActivityResultLauncher<Array<String>>? = null
    var fromCam: Boolean? = false
    var lat: Double? = 0.0
    var photoURI: Uri? = null
    var arraySpinner: ArrayList<ItemSpinner> = arrayListOf()
    var selectedBankId: Int? = 0
    var arrayBankSpinner: ArrayList<ItemSpinner> = arrayListOf()
    var photoFile: File? = null
    val BLOCKED = -1
    var resultLauncher: ActivityResultLauncher<Intent>? = null
    var fromProfile: Int? = 0
    var long: Double? = 0.0
    var banks: ArrayList<BankItem> = arrayListOf()
    var profilePercentage = 0
    private val IMAGE_PICK_CODE = 1000
    private val CAMERA_CODE = 1003
    private val CHOOSER_CODE = 1004
    private val PERMISSION_WRITE = 1005
    private val PERMISSION_CODE = 1001
    private val PERMISSION_CODE_CAMERA = 1002


    override fun onItemClicked(view: View, position: Int) {


        if (position == 0) {
            selectedFile = null
            arraySelectedImage.removeAt(position)
            adapterSelectedImages!!.notifyDataSetChanged()
        } else {
            selectedFile2 = null
            arraySelectedImage.removeAt(position)
            adapterSelectedImages!!.notifyDataSetChanged()
        }
        if (arraySelectedImage.size < 2) {
            ibUploadFile.show()
        }

        if (arraySelectedImage.size == 0) {
            rvCivilIdData.hide()

        }

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

    override fun onResume() {
        super.onResume()



    }

    fun setUpSpinner(user: User) {
        try {
            arrayBankSpinner.clear()
            for (item in banks) {
                arrayBankSpinner.add(ItemSpinner(item.id!!.toInt(), item.value, ""))

            }
            arrayBankSpinner.add(
                0,
                ItemSpinner(-1, AppHelper.getRemoteString("please__select", requireContext()), "")
            )
            selectedBankId = 0
            val adapterServices =
                AdapterGeneralSpinner(
                    requireContext(),
                    R.layout.spinner_layout,
                    arrayBankSpinner,
                    0
                )
            spBanks.adapter = adapterServices
            adapterServices.setDropDownViewResource(R.layout.item_spinner_drop_down)
            spBanks.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedBankId = arrayBankSpinner.get(position).id

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

            if(!user!!.bankId.isNullOrEmpty()) {
                try {
                    spBanks.setSelection(arrayBankSpinner.indexOf(arrayBankSpinner.find {
                        it.id.toString() == user!!.bankId!!
                    }))
                } catch (ex: Exception) {
                    logw("bankId?", "NO BANK ID")
                }
            }


            loading.hide()
        } catch (ex: Exception) {

        }
    }

    fun getBankList(user: User) {
        loading.show()
        var req = RequestLanguage(MyApplication.languageCode)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getBankList(req)?.enqueue(object : Callback<ResponseMainBank> {
                override fun onResponse(
                    call: Call<ResponseMainBank>,
                    response: Response<ResponseMainBank>
                ) {
                    try {
                        banks.clear()
                        banks.addAll(response.body()!!.banks)
                        setUpSpinner(user)
                    } catch (E: java.lang.Exception) {
                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponseMainBank>, throwable: Throwable) {
                    loading.hide()

                }
            })

    }

    private fun pickImageFromGallery() {


        /*var i = Intent(Intent.ACTION_GET_CONTENT);
        i.setType("application/pdf");
        i.addCategory(Intent.CATEGORY_OPENABLE);
        resultLauncher!!.launch(i)*/
        fromCam = false
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


        val options = arrayOf<CharSequence>(
            AppHelper.getRemoteString("takePhoto", requireContext()),
            AppHelper.getRemoteString("chooseGallery", requireContext()),
            AppHelper.getRemoteString("cancel", requireContext())
        )

        val builder = AlertDialog.Builder(context)
        builder.setTitle(AppHelper.getRemoteString("choose_file", requireActivity()))

        builder.setItems(options) { dialog, item ->
            when {
                options[item] == AppHelper.getRemoteString(
                    "takePhoto",
                    requireContext()
                ) -> pickImageFromCamera()
                options[item] == AppHelper.getRemoteString(
                    "chooseGallery",
                    requireContext()
                ) -> pickImageFromGallery()
                options[item] == AppHelper.getRemoteString(
                    "cancel",
                    requireContext()
                ) -> dialog.dismiss()
            }
        }
        builder.show()
    }

    fun setUp() {
        mPermissionResult =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
            { result ->
                var permission = false
                for (item in result) {
                    permission = item.value
                }
                if (permission) {
                    selectImage(requireContext())
                    Log.e(TAG, "onActivityResult: PERMISSION GRANTED")
                    MyApplication.permissionAllow11 = 0
                } else {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        if (MyApplication.permissionAllow11!! >= 2) {
                            for (item in result) {
                                if (checkSelfPermission(requireContext(), item.key) == BLOCKED) {
                                    resultLauncher!!.launch(
                                        Intent(android.provider.Settings.ACTION_SETTINGS)
                                    );

                                    Toast.makeText(
                                        requireContext(),
                                        AppHelper.getRemoteString(
                                            "grant_settings_permission",
                                            requireContext()
                                        ),
                                        Toast.LENGTH_LONG
                                    ).show()
                                    break
                                }
                            }
                        } else {
                            MyApplication.permissionAllow11 = MyApplication.permissionAllow11!! + 1
                        }
                    }
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as ActivityHome).showTitle(false)
    }

    private fun openChooser() {


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

    fun setUpSpinnerGovs(user: User) {
        arraySpinner.clear()
        for (item in MyApplication.kuwaitGovs) {
            arraySpinner.add(
                ItemSpinner(
                    item.govId!!.toInt(),
                    if (MyApplication.languageCode == AppConstants.LANG_ENGLISH) {
                        item.govEn
                    } else {
                        item.govAr
                    }, ""
                )
            )

        }
        selectedProvince = ""
        val adapterProvince =
            AdapterGeneralSpinner(requireContext(), R.layout.spinner_layout, arraySpinner, 0)
        arraySpinner.add(
            0,
            ItemSpinner(-1, AppHelper.getRemoteString("please__select", requireActivity()), "")
        )
        spProvinceProfile.adapter = adapterProvince
        adapterProvince.setDropDownViewResource(R.layout.item_spinner_drop_down)
        spProvinceProfile.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if(arraySpinner.get(position).id==-1){
                    selectedProvince = ""
                }else {
                    selectedProvince = arraySpinner.get(position).name
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        /*spBanks.setSelection(arraySpinner.indexOf(arraySpinner.find {
        it.id.toString() == user!!.bankId!!
    }))*/




        if (MyApplication.selectedUser!!.addresses!!.get(0).province != null && MyApplication.selectedUser!!.addresses!!.get(
                0
            ).province != "null"
        ) {
            try {
                var indx = arraySpinner.indexOf(arraySpinner.find {
                    it.name!!.equals(MyApplication.selectedUser!!.addresses!!.get(0).province)
                }
                )
                if (indx != 0) {
                    spProvinceProfile.setSelection(indx)
                    selectedProvince = arraySpinner.get(indx).name
                    //spProvinceProfile.isEnabled = false
                } else {
                    spProvinceProfile.setSelection(0)
                   // spProvinceProfile.isEnabled = true
                }
            } catch (ex: Exception) {

            }
        }

    }

    fun setUserData(user: User) {
        setUpSpinnerGovs(user)
        try {
            profilePercentage = 0
            // loading.show()
            // AppHelper.getUserInfo()
            try {
                etFirstNameProfile.text =
                    Editable.Factory.getInstance()
                        .newEditable(user.firstName)
            } catch (ex: Exception) {
                etFirstNameProfile.text = Editable.Factory.getInstance().newEditable("")
            }
            try {
                etMiddleNameProfile.text =
                    Editable.Factory.getInstance()
                        .newEditable(user.middleName)
            } catch (ex: Exception) {
                etMiddleNameProfile.text = Editable.Factory.getInstance().newEditable("")
            }
            try {
                etLastNameProfile.text =
                    Editable.Factory.getInstance()
                        .newEditable(user.lastName)
            } catch (ex: Exception) {
                etLastNameProfile.text = Editable.Factory.getInstance().newEditable("")
            }
            try {
                etEmailProfile.text =
                    Editable.Factory.getInstance().newEditable(user.email)
            } catch (ex: Exception) {
                etEmailProfile.text = Editable.Factory.getInstance().newEditable("")
            }
            try {
                etMobileProfile.text =
                    Editable.Factory.getInstance()
                        .newEditable(user.mobileNumber)
            } catch (ex: Exception) {
                etMobileProfile.text = Editable.Factory.getInstance().newEditable("")
            }
            try {
                etCivilIdNbProfile.text =
                    Editable.Factory.getInstance().newEditable(user.civilId)
            } catch (ex: Exception) {
                etCivilIdNbProfile.text = Editable.Factory.getInstance().newEditable("")
            }
            try {
                etDateOfBirthProfile.text =
                    Editable.Factory.getInstance()
                        .newEditable(user.birthday)
            } catch (ex: Exception) {
                etDateOfBirthProfile.text = Editable.Factory.getInstance().newEditable("")
            }
            try {
                etAltContactNumberProfile.text =
                    Editable.Factory.getInstance()
                        .newEditable(user.altrNumb)
            } catch (ex: Exception) {
                etAltContactNumberProfile.text = Editable.Factory.getInstance().newEditable("")
            }
            try {
                etAddressProfile.text =
                    Editable.Factory.getInstance()
                        .newEditable(user.location)
            } catch (ex: Exception) {
                etAddressProfile.text = Editable.Factory.getInstance().newEditable("")
            }
            try {
                etAccountNumberProfile.text =
                    Editable.Factory.getInstance()
                        .newEditable(user.accountNumber)
            } catch (ex: Exception) {
                etAccountNumberProfile.text = Editable.Factory.getInstance().newEditable("")
            }
            /*try {
                etBankNameProfile.text =
                    Editable.Factory.getInstance()
                        .newEditable(user.bankName)
            } catch (ex: Exception) {
                etBankNameProfile.text = Editable.Factory.getInstance().newEditable("")
            }*/
            try {
                etBranchNameProfile.text =
                    Editable.Factory.getInstance()
                        .newEditable(user.bankBranch)
            } catch (ex: Exception) {
                etBranchNameProfile.text = Editable.Factory.getInstance().newEditable("")
            }
            try {
                etDescriptionProfile.text =
                    Editable.Factory.getInstance().newEditable(user.desc)
            } catch (ex: Exception) {
                etDescriptionProfile.text = Editable.Factory.getInstance().newEditable("")
            }
            try {
                etCivilIdNbProfile.text =
                    Editable.Factory.getInstance().newEditable(user.civilId)
            } catch (ex: Exception) {
                etCivilIdNbProfile.text = Editable.Factory.getInstance().newEditable("")
            }
            try {
                /*if (!MyApplication.selectedUser!!.civilIdAttach.isNullOrEmpty()) {*/

                   /* if(MyApplication.temporaryProfile==null) {

                       // AppHelper.setImage(requireContext(),ivCivilBack,MyApplication.selectedUser!!.civilAttachBack!!,false)
                       // AppHelper.setImage(requireContext(),ivCivilFront,MyApplication.selectedUser!!.civilIdAttach!!,false)
                        *//*arraySelectedImage.clear()
                        if (!MyApplication.selectedUser!!.civilIdAttach.isNullOrEmpty())
                            arraySelectedImage.add(
                                FilesSelected(
                                    MyApplication.selectedUser!!.civilIdAttach,
                                    null,
                                    null,
                                    1
                                )
                            )
                        if (!MyApplication.selectedUser!!.civilAttachBack.isNullOrEmpty())
                            arraySelectedImage.add(
                                FilesSelected(
                                    MyApplication.selectedUser!!.civilAttachBack,
                                    null,
                                    null,
                                    2
                                )
                            )*//*
                    }else{
                        AppHelper.setImage(requireContext(),ivCivilBack,MyApplication.temporaryProfile!!.civilAttachBack!!,true)
                        AppHelper.setImage(requireContext(),ivCivilFront,MyApplication.temporaryProfile!!.civilIdAttach!!,true)
                    }*/
                if (arraySelectedImage.size == 0)
                    rvCivilIdData.hide()
                else
                    rvCivilIdData.show()

                adapterSelectedImages =
                    AdapterGridFiles(arraySelectedImage, this, requireActivity(), true)
                rvCivilIdData.layoutManager = GridLayoutManager(requireContext(), 3)
                rvCivilIdData.adapter = adapterSelectedImages
                rvCivilIdData.isNestedScrollingEnabled = false
                if (arraySelectedImage.size == 2)
                    ibUploadFile.hide()
                /*tvCivilIdFile.show()
                tvCivilIdFile.text =
                    MyApplication.selectedUser!!.civilIdAttach
                tvCivilIdFile.onOneClick {
                    try {
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(tvCivilIdFile.text.toString())
                        startActivity(i)
                    } catch (ex: Exception) {
                        Log.wtf("ExceptionCIVIL", ex.toString())
                    }
                }*/
                /* } else {
                     //tvCivilIdFile.hide()
                 }*/
            } catch (ex: Exception) {

            }
            try {
                etBranchNameProfile.text =
                    Editable.Factory.getInstance()
                        .newEditable(user.bankBranch)
            } catch (ex: Exception) {
                etBranchNameProfile.text = Editable.Factory.getInstance().newEditable("")
            }
            try {
                etIBANProfile.text =
                    Editable.Factory.getInstance().newEditable(user.IBAN)
            } catch (ex: Exception) {
                etIBANProfile.text = Editable.Factory.getInstance().newEditable("")
            }
            try {
                etDateOfBirthProfile.text =
                    Editable.Factory.getInstance().newEditable(user.dob)
            } catch (ex: Exception) {
                etDateOfBirthProfile.text = Editable.Factory.getInstance().newEditable("")
            }

            if(MyApplication.tempCivilIdBack == null ){
                try {
                    if (!MyApplication.selectedUser!!.civilAttachBack.isNullOrEmpty()) {
                        ivCivilBack.loadImagesUrl(MyApplication.selectedUser!!.civilAttachBack!!)
                        btCancelCivilBack.show()
                        civilImageBackAvailable = true
                    }else{
                        civilImageBackAvailable = false
                    }
                } catch (e: Exception) {
                }
            }else{
                try {
                    ivCivilBack.loadLocalImage(MyApplication.tempCivilIdBack!!)
                    var req =
                        MyApplication.tempCivilIdBack!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    selectedFile2 = MultipartBody.Part.createFormData(
                        ApiParameters.CIVIL_ATTACH_BACK,
                        MyApplication.tempCivilIdBack!!.name ,
                        req
                    )
                    btCancelCivilBack.show()
                    civilImageBackAvailable = true
                } catch (ex: java.lang.Exception) {
                }
            }

            if(MyApplication.tempCivilId == null ){
                try {
                    if (!MyApplication.selectedUser!!.civilIdAttach.isNullOrEmpty()) {
                        ivCivilFront.loadImagesUrl(MyApplication.selectedUser!!.civilIdAttach!!)
                        btCancelCivilFront.show()
                        civilImageAvailable = true
                    }else{
                        civilImageAvailable = false
                    }
                } catch (e: Exception) {
                }
            }else{
                try {
                    civilImageAvailable = true
                    ivCivilFront.loadLocalImage(MyApplication.tempCivilId!!)
                    var req =
                        MyApplication.tempCivilId!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    selectedFile = MultipartBody.Part.createFormData(
                        ApiParameters.CIVIL_ID_ATTACH,
                        MyApplication.tempCivilId!!.name,
                        req
                    )
                    btCancelCivilFront.show()
                } catch (ex: java.lang.Exception) {
                }
            }

            if (MyApplication.temporaryProfile == null) {
                try {
                    if (!MyApplication.selectedUser!!.profilePicUrl.isNullOrEmpty())
                        ivProfile.loadRoundedImage(MyApplication.selectedUser!!.profilePicUrl!!)
                } catch (e: Exception) {
                }
            } else {

                try {
                    ivProfile.loadRoundedLocalImage(MyApplication.tempProfilePic!!)
                    var req =
                        MyApplication.tempProfilePic!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    selectedFile = MultipartBody.Part.createFormData(
                        ApiParameters.CIVIL_ID_ATTACH,
                        MyApplication.tempProfilePic!!.name ,
                        req
                    )
                } catch (ex: java.lang.Exception) {
                }
            }

            try {
                if (user.gender!!.lowercase().equals("female")) {
                    rbFemaleProfile.isChecked = true
                    rbMaleProfile.isChecked = false
                } else {
                    rbFemaleProfile.isChecked = false
                    rbMaleProfile.isChecked = true
                }
            } catch (ex: Exception) {
                rbFemaleProfile.isChecked = false
                rbMaleProfile.isChecked = true
            }

            try {
                tvUsername.text =
                    user.firstName + " " + user.lastName
            } catch (e: Exception) {
            }

            if (!MyApplication.isClient) {
                if (!user.mobileNumber.isNullOrEmpty())
                    profilePercentage += 25
                if (!user.firstName.isNullOrEmpty() && !user.lastName.isNullOrEmpty() && !user.email.isNullOrEmpty() && !user.dob.isNullOrEmpty() && !user.altrNumb.isNullOrEmpty() && (!user.civilId.isNullOrEmpty() || !user.civilIdAttach.isNullOrEmpty()))
                    profilePercentage += 25
                try {
                    if (MyApplication.selectedUser!!.addresses!!.size > 0) {
                        if (MyApplication.selectedUser!!.addresses!![0].addressId != null && MyApplication.selectedUser!!.addresses!![0].addressName != null)
                            profilePercentage += 25

                    }
                } catch (e: Exception) {
                }
                if (!user.accountNumber.isNullOrEmpty() && !user.bankId.equals("-1") && !user.bankBranch.isNullOrEmpty() && !user.IBAN.isNullOrEmpty())
                    profilePercentage += 25
            } else {
                if (!user.mobileNumber.isNullOrEmpty())
                    profilePercentage += 25
                if (!user.firstName.isNullOrEmpty() && !user.lastName.isNullOrEmpty())
                    profilePercentage += 25
                if (!user.email.isNullOrEmpty())
                    profilePercentage += 25
                if (!user.profilePicUrl.isNullOrEmpty())
                    profilePercentage += 25
            }

            try {

                pbComplete.setWeight(profilePercentage.toFloat())
                pbNotComplete.setWeight(100f - profilePercentage.toFloat())
                tvPercentageCompleted.text =
                    profilePercentage.toString() + " % " + AppHelper.getRemoteString(
                        "completed",
                        requireActivity()
                    )
            } catch (e: Exception) {
            }

            loading.hide()

            if (!MyApplication.isClient) {
                if (MyApplication.selectedUser!!.addresses != null && MyApplication.selectedUser!!.addresses!!.size > 0) {
                    if (MyApplication.selectedUser!!.addresses!![0].addressId != null && MyApplication.selectedUser!!.addresses!![0].addressName != null) {

                        linearAddressInfo.show()

                        MyApplication.addNewAddress = false
                        btAddNewAddress.hide()
                        //    btAddNewAddress.text = AppHelper.getRemoteString("UpdateAddress",requireActivity())


                        try {
                            var myAddress = MyApplication.selectedUser!!.addresses!![0]
                            if (myAddress.addressName != null && myAddress.addressName != "null") {
                                etAddressName!!.setText(myAddress.addressName)
                               // etAddressName.isEnabled = false
                            }

                            if (myAddress.desc != null && myAddress.desc != "null") {
                                etMoreDetails!!.setText(myAddress.desc)
                               // etMoreDetails.isEnabled = false
                            }

                            if (myAddress.area != null && myAddress.area != "null") {
                                etArea!!.setText(myAddress.area)
                              //  etArea.isEnabled = false
                            }

                            if (myAddress.block != null && myAddress.block != "null") {
                                etBlock!!.setText(myAddress.block)
                               // etBlock.isEnabled = false
                            }

                            if (myAddress.avenue != null && myAddress.avenue != "null") {
                                etAvenue!!.setText(myAddress.avenue)
                              //  etAvenue.isEnabled = false
                            }

                            if (myAddress.apartment != null && myAddress.apartment != "null" && !myAddress.apartment!!.isEmpty()) {
                                etApartment!!.setText(myAddress.apartment)
                              //  etApartment.isEnabled = false
                            }

                            if (myAddress.floor != null && myAddress.floor != "null") {
                                etFloor!!.setText(myAddress.floor)
                             //   etFloor.isEnabled = false
                            }

                            if (myAddress.bldg != null && myAddress.bldg != "null") {
                                etBuilding!!.setText(myAddress.bldg)
                              //  etBuilding.isEnabled = false
                            }

                            if (myAddress.street != null && myAddress.street != "null") {
                                etStreet!!.setText(myAddress.street)
                             //   etStreet.isEnabled = false
                            }


                        } catch (e: java.lang.Exception) {
                        }



                        btUpdateAddress.show()
                        btUpdateAddress.setOnClickListener {
                            MyApplication.temporaryProfile = User(
                                "",
                                "",
                                etMiddleNameProfile.text.toString(),
                                etLastNameProfile.text.toString(),
                                gender,
                                etMobileProfile.text.toString(),
                                etAltContactNumberProfile.text.toString(),
                                selectedBankId.toString(),
                                "",
                                "",
                                etAccountNumberProfile.text.toString(),
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                etDateOfBirthProfile.text.toString(),
                                "",
                                etFirstNameProfile.text.toString(),
                                etEmailProfile.text.toString(),
                                "",
                                "",
                                "",
                                "",
                                "",
                                etCivilIdNbProfile.text.toString(),
                                "",
                                "",
                                "",
                                etDateOfBirthProfile.text.toString(),
                                0,
                                null,
                                0.0,
                                "0",
                                0,
                                0,
                                etDescriptionProfile.text.toString(),
                                etIBANProfile.text.toString(),
                                etBranchNameProfile.text.toString(),
                                selectedBankId.toString(),
                                arrayListOf()
                            )
                            resultLauncher!!.launch(
                                Intent(requireActivity(), ActivityMapAddress::class.java)
                            )
                         //   startActivity(Intent(requireActivity(), ActivityMapAddress::class.java))
                        }
                    } else {
                        MyApplication.addNewAddress = true
                        btAddNewAddress.text =
                            AppHelper.getRemoteString("AddNewAddress", requireActivity())
                        linearAddressInfo.hide()
                        btUpdateAddress.hide()
                    }

                } else {
                    MyApplication.addNewAddress = true
                    btAddNewAddress.text =
                        AppHelper.getRemoteString("AddNewAddress", requireActivity())
                    linearAddressInfo.hide()
                    btUpdateAddress.hide()
                }
            }

            getBankList(user)

            if (MyApplication.temporaryProfile != null)
                MyApplication.temporaryProfile = null


        } catch (ex: Exception) {
            loading.hide()
        }
    }

    fun setHint() {
        etFirstNameProfile.hint = etFirstNameProfile.hint.toString() + "*"
        etLastNameProfile.hint = etLastNameProfile.hint.toString() + "*"
        etEmailProfile.hint = etEmailProfile.hint.toString() + "*"
    }

    fun init() {
        //tvToolbarCurveTitle.setText(getString(R.string.profile))
        (activity as ActivityHome?)!!.showBack(R.color.white)
        AppHelper.setTitle(
            requireActivity(), MyApplication.selectedTitle!!, "", R.color.white
        )
        rbFemaleProfile.isSelected = true
        // (activity as ActivityHome?)!!.showLogout(false)
        tvToolbarCurveTitle.visibility = View.GONE
        listeners()
        if (MyApplication.isClient) {
            showClientFields()
            llProfilePercent.hide()
            tvPercentageCompleted.hide()
        } else {
            llProfilePercent.show()
            tvPercentageCompleted.show()
        }
        setHint()
        getUserData()


    }

    fun succUpdate(res: ResponseUser) {
        if (res.result == 1) {
            AppHelper.createDialog(
                requireActivity(),
                AppHelper.getRemoteString("successfully_updated", requireContext())
            )
            {
                requireActivity().onBackPressed()
            }
            loading.hide()
            // getUserData()
        } else {
            AppHelper.createDialog(
                requireActivity(),
                res.message!!
            )

            loading.hide()
        }
    }

    fun getUserData() {
        loading.show()
        logw("MY-USER-ID", MyApplication.userId.toString())
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

                    if (MyApplication.temporaryProfile != null)
                        try {
                            setUserData(MyApplication.temporaryProfile!!)
                        } catch (e: Exception) {
                        }
                    else
                        try {
                            setUserData(MyApplication.selectedUser!!)
                        } catch (e: Exception) {
                        }
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

        val user =
            MyApplication.userId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val first =
            etFirstNameProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val last =
            etLastNameProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val email =
            etEmailProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val phone =
            etMobileProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        var lang = MyApplication.languageCode.toRequestBody("text/plain".toMediaTypeOrNull())
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
                typeReq,
                lang


            )?.enqueue(object : Callback<ResponseUser> {
                override fun onResponse(
                    call: Call<ResponseUser>,
                    response: Response<ResponseUser>
                ) {
                    try {
                        loading.hide()
                        succUpdate(response.body()!!)
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


        btCancelCivilBack.onOneClick {
            emptyCivilIdBack = "-1"
            btCancelCivilBack.hide()
            civilImageBackAvailable = false
            selectedFile2= null
            ivCivilBack.setImageResource(R.drawable.icon_add_image)
        }
        btCancelCivilFront.onOneClick {
            emptyCivilIdFront = "-1"
            btCancelCivilFront.hide()
            civilImageAvailable = false
            selectedFile = null
            ivCivilFront.setImageResource(R.drawable.icon_add_image)
        }
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

                    if (result.resultCode == 1000) {

                            val extras =result.data
                            if (extras != null) {
                                lat = extras.getDoubleExtra("lat",0.0)
                                long = extras.getDoubleExtra("long",0.0)
                                /*etAddressProfile.text = Editable.Factory.getInstance()
                                    .newEditable(AppHelper.getAddress(lat!!, long!!, requireContext()))*/
                            }


                    } else {
                        try {
                            var file: File? = null
                            if (fromCam!!) {
                                file = File(result.data!!.data!!.path)
                                fromCam = false
                            } else {
                                try {
                                    /*   val files: ArrayList<MediaFile> =
                                   result.data!!.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES)!!*/
                                    //   var path = getPath(files.get(0).uri)
                                    file =
                                        AppHelper.getFile(requireActivity(), result.data!!.data!!)


                                } catch (e: Exception) {
                                    Log.wtf("tag", "tag")
                                }
                            }

                            var req =
                                file!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            if (fromProfile != 1) {

                                if (fromProfile == 2) {
                                    ivCivilFront.loadLocalImage(file)
                                    selectedFile = MultipartBody.Part.createFormData(
                                        ApiParameters.CIVIL_ID_ATTACH,
                                        file.name,
                                        req
                                    )
                                    MyApplication.tempCivilId = file
                                    civilImageAvailable = true
                                    btCancelCivilFront.show()

                                } else {
                                    btCancelCivilBack.show()
                                    civilImageBackAvailable = true
                                    ivCivilBack.loadLocalImage(file)
                                    selectedFile2 = MultipartBody.Part.createFormData(
                                        ApiParameters.CIVIL_ATTACH_BACK,
                                        file.name,
                                        req
                                    )
                                    MyApplication.tempCivilIdBack = file
                                }
                                /* tvCivilIdFile.show()
                         tvCivilIdFile.text = result.data!!.data!!.path
                         MyApplication.tempCivilId = file
                         tvCivilIdFile.onOneClick {
                             MyApplication.selectedImage = ""
                             val bottom_fragment = FragmentBottomSheetImage()
                             bottom_fragment.show(
                                 requireActivity().supportFragmentManager,
                                 "frag_image"
                             )
                         }*/
                                /* var firstFile = false
                        if (arraySelectedImage.size > 0 && arraySelectedImage.get(0).id == 2)
                            firstFile = true
                        else if (arraySelectedImage.size == 0)
                            firstFile = true
                        if (selectedFile == null && firstFile) {
                            selectedFile = MultipartBody.Part.createFormData(
                                ApiParameters.CIVIL_ID_ATTACH,
                                file.name + "File",
                                req
                            )
                            arraySelectedImage.add(FilesSelected("", file, selectedFile, 1))
                            arraySelectedImage.sortBy { it.id }
                            rvCivilIdData.show()
                            adapterSelectedImages!!.notifyDataSetChanged()
                            if (arraySelectedImage.size == 2) {
                                ibUploadFile.hide()
                            }
                        } else if (selectedFile2 == null) {
                            selectedFile2 = MultipartBody.Part.createFormData(
                                ApiParameters.CIVIL_ATTACH_BACK,
                                file.name + "File",
                                req
                            )
                            arraySelectedImage.add(FilesSelected("", file, selectedFile, 2))
                            arraySelectedImage.sortBy { it.id }
                            adapterSelectedImages!!.notifyDataSetChanged()
                            if (arraySelectedImage.size == 2) {
                                ibUploadFile.hide()
                            }
                            rvCivilIdData.show()*/

                            } else {
                                MyApplication.tempProfilePic = file
                                ivProfile.loadRoundedLocalImage(file)
                                selectedProfilePic = MultipartBody.Part.createFormData(
                                    if (MyApplication.isClient) ApiParameters.PROFILE_PIC else ApiParameters.FILE,
                                    file.name + "File",
                                    req
                                )
                            }

                        } catch (ex: Exception) {
                            logw("tets", "error")
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

            if (AppHelper.isOnline(requireActivity())) {


                if (etFirstNameProfile.text.isNullOrEmpty() || etLastNameProfile.text.isNullOrEmpty() || etEmailProfile.text.isNullOrEmpty() || !AppHelper.isEmailValid(
                        etEmailProfile.text.toString()
                    )
                ) {

                    AppHelper.createDialog(
                        requireActivity(),
                        AppHelper.getRemoteString("fill_all_field", requireContext())
                    )

                } else if (!AppHelper.isEmailValid(etEmailProfile.text.toString())) {
                    AppHelper.createDialog(
                        requireActivity(),
                        AppHelper.getRemoteString("email_valid_error", requireContext())
                    )
                } else if (!MyApplication.isClient) {
                    var x = etCivilIdNbProfile.text
                    if(!etAddressName.text.isNullOrEmpty() && !selectedProvince.isNullOrEmpty() && !etStreet.text.isNullOrEmpty() && !etBuilding.text.isNullOrEmpty() && !etArea.text.isNullOrEmpty() && !etBlock.text.isNullOrEmpty()) {
                        /* if((!etAccountNumberProfile.text.toString().isNullOrEmpty() || !etBranchNameProfile.text.isNullOrEmpty() || selectedBankId!=0 || !etIBANProfile.text.isNullOrEmpty()) && (etAccountNumberProfile.text.toString().isNullOrEmpty() || selectedBankId!=0 || etIBANProfile.text.isNullOrEmpty()))*/
                        if ((!etCivilIdNbProfile.text.isNullOrEmpty() && etCivilIdNbProfile.text.length == 12) || (civilImageAvailable && civilImageBackAvailable)) {
                            if(civilImageBackAvailable == civilImageAvailable) {
                                if (etIBANProfile.text.isNullOrEmpty() || etIBANProfile.text.length == 30)
                                    updateServiceProfile()
                                else
                                    AppHelper.createDialog(
                                        requireActivity(),
                                        AppHelper.getRemoteString(
                                            "iban_must_be_30",
                                            requireActivity()
                                        )
                                    )
                            }else{
                                AppHelper.createDialog(requireActivity(),AppHelper.getRemoteString("you_must_enter_civil_front_back",requireContext()))
                            }
                        } else {
                            AppHelper.createDialog(
                                requireActivity(),
                                AppHelper.getRemoteString("fill_all_field", requireActivity())
                            )

                        }
                    }else{
                        AppHelper.createDialog(
                            requireActivity(),
                            AppHelper.getRemoteString("fill_all_field", requireActivity())
                        )
                    }
                } else
                    updateClientProfile()
            } else {

                AppHelper.createDialog(
                    requireActivity(),
                    AppHelper.getRemoteString("no_internet", requireContext())
                )
            }
        }


        rbMaleProfile.onOneClick {
            gender = "male"
        }
        rbFemaleProfile.onOneClick {
            gender = "female"
        }

        llAddressProfile.setOnClickListener {
            resultLauncher!!.launch(Intent(requireContext(), ActivityMapAddress::class.java))
        }
        ibUploadFile.setOnClickListener {
            if (arraySelectedImage.size < 2)
                pickFile(1, 2)

        }
        ivProfile.onOneClick {
            pickFile(1, 1)
        }
        ivCivilFront.onOneClick {
            pickFile(1, 2)
        }
        ivCivilBack.onOneClick {
            pickFile(1, 3)
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
                    etDateOfBirthProfile.text = date.toEditable()
                }, mYear, mMonth, mDay
            )
            var cal = mcurrentDate.add(Calendar.YEAR, -18)
            mDatePicker.datePicker.maxDate = mcurrentDate.time.time
            mDatePicker.show()
        }

        btAddNewAddress.onOneClick {
            MyApplication.temporaryProfile = User(
                "",
                "",
                etMiddleNameProfile.text.toString(),
                etLastNameProfile.text.toString(),
                gender,
                etMobileProfile.text.toString(),
                etAltContactNumberProfile.text.toString(),
                selectedBankId.toString(),
                "",
                "",
                etAccountNumberProfile.text.toString(),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                etDateOfBirthProfile.text.toString(),
                "",
                etFirstNameProfile.text.toString(),
                etEmailProfile.text.toString(),
                "",
                "",
                "",
                "",
                "",
                etCivilIdNbProfile.text.toString(),
                "",
                "",
                "",
                etDateOfBirthProfile.text.toString(),
                0,
                null,
                0.0,
                "0",
                0,
                0,
                etDescriptionProfile.text.toString(),
                etIBANProfile.text.toString(),
                etBranchNameProfile.text.toString(),
                selectedBankId.toString(),
                arrayListOf()
            )
            startActivity(Intent(requireActivity(), ActivityMapAddress::class.java))
        }
    }

    private fun updateServiceProfile() {
        if (selectedProfilePic == null) {
            var empty = ""
            val attachmentEmpty = empty.toRequestBody("text/plain".toMediaTypeOrNull())

            selectedProfilePic =
                MultipartBody.Part.createFormData("attachment", "", attachmentEmpty)
        }
        if (selectedFile == null) {
            val attachmentEmpty = emptyCivilIdFront!!.toRequestBody("text/plain".toMediaTypeOrNull())

            selectedFile = createFormData( ApiParameters.CIVIL_ID_ATTACH, emptyCivilIdFront!!,attachmentEmpty)
            /*selectedFile =
                MultipartBody.Part.createFormData(emptyCivilIdFront!!, emptyCivilIdFront, "-1")*/
        }
        if (selectedFile2 == null) {
            val attachmentEmpty = emptyCivilIdBack!!.toRequestBody("text/plain"!!.toMediaTypeOrNull())

            selectedFile2 = createFormData(ApiParameters.CIVIL_ATTACH_BACK, emptyCivilIdBack!!,attachmentEmpty)
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
            selectedBankId.toString(),
            etBranchNameProfile.text.toString(),
            etDescriptionProfile.text.toString(),
            etIBANProfile.text.toString(),
            selectedFile!!,
            selectedProfilePic!!,
            selectedFile2!!
        )

        if (!MyApplication.addNewAddress)
            updateAddress()
    }

    private fun updateClientProfile() {
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

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        *//*else {
            try {
                val files: ArrayList<MediaFile> =
                    data!!.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES)!!
                //   var path = getPath(files.get(0).uri)
                var file = AppHelper.getFile(requireActivity(), files[0].uri)
                var req = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                if (fromProfile!=1) {

                    if(fromProfile==2) {
                        civilImageAvailable = true
                        selectedFile = MultipartBody.Part.createFormData(
                            ApiParameters.CIVIL_ID_ATTACH,
                            file.name ,
                            req
                        )
                        btCancelCivilFront.show()
                        ivCivilFront.loadLocalImage(file)
                    }else{
                        emptyCivilIdBack = ""
                        civilImageBackAvailable = true
                        selectedFile2 = MultipartBody.Part.createFormData(
                            ApiParameters.CIVIL_ATTACH_BACK,
                            file.name,
                            req
                        )
                        btCancelCivilBack.show()
                        ivCivilBack.loadLocalImage(file)
                    }
                    //tvCivilIdFile.show()
                    //  tvCivilIdFile.text = file.name
                } else {
                    ivProfile.loadRoundedLocalImage(file)
                    selectedProfilePic = MultipartBody.Part.createFormData(
                        if (MyApplication.isClient) ApiParameters.PROFILE_PIC else ApiParameters.FILE,
                        file.name + "File",
                        req
                    )
                }

            } catch (e: Exception) {
            }
        }*//*
    }*/

    private fun pickFile(pickCode: Int, from: Int) {

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
        } else {
            gender = "female"
        }
        val user = userId.toRequestBody("text/plain".toMediaTypeOrNull())
        val first =
            etFirstNameProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val middle =
            etMiddleNameProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val last =
            etLastNameProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val email =
            etEmailProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val phone =
            etMobileProfile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val altNUm = etAltContactNumberProfile.text.toString()
            .toRequestBody("text/plain".toMediaTypeOrNull())
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
            etAccountNumberProfile.text.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
        val bankname =
            selectedBankId.toString().toString().toRequestBody("text/plain".toMediaTypeOrNull())
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

        var lang = MyApplication.languageCode.toRequestBody("text/plain".toMediaTypeOrNull())

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
                description,
                lang,
                selectedFile2!!
            )?.enqueue(object : Callback<ResponseUser> {
                override fun onResponse(
                    call: Call<ResponseUser>,
                    response: Response<ResponseUser>
                ) {
                    try {
                        succUpdate(response.body()!!)
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
        selectedProfilePic = null
        selectedFile = null
        succUpdate(res)
    }


    fun updateAddress() {
        var addressId = 0
        try {
            if (!MyApplication.isClient && !MyApplication.addNewAddress)
                addressId = MyApplication.selectedUser!!.addresses!![0].addressId!!.toInt()
        } catch (e: java.lang.Exception) {
        }

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
            selectedProvince,
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