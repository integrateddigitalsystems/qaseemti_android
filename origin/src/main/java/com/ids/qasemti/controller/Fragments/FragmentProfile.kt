package com.ids.qasemti.controller.Fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Activities.ActivityMapAddress
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.RequestUpdateLanguage
import com.ids.qasemti.model.ResponseUser
import com.ids.qasemti.utils.*
import com.ids.qasemti.utils.AppHelper.Companion.toEditable
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.config.Configurations
import com.jaiselrahman.filepicker.model.MediaFile
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.activity_new_address.*
import kotlinx.android.synthetic.main.curve_layout_home.*
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.android.synthetic.main.layout_profile.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.service_tab_1.*
import kotlinx.android.synthetic.main.service_tab_2.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.white_logo_layout.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class FragmentProfile : Fragment(), RVOnItemClickListener {
    var selectedFile: MultipartBody.Part? = null
    var selectedProfilePic: MultipartBody.Part? = null
    var gender = "female"
    var lat: Double? = 0.0
    var fromProfile: Boolean? = false
    var long: Double? = 0.0
    var profilePercentage=0
    override fun onItemClicked(view: View, position: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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



    fun setUserData(){
        profilePercentage=0
       // loading.show()
       // AppHelper.getUserInfo()
        try {
            etFirstNameProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.firstName)
        }catch (ex:Exception){
            etFirstNameProfile.text =  Editable.Factory.getInstance().newEditable("")
        }
        try {
            etMiddleNameProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.middleName)
        }catch (ex:Exception){
            etMiddleNameProfile.text =  Editable.Factory.getInstance().newEditable("")
        }
        try {
            etLastNameProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.lastName)
        }catch (ex:Exception){
            etLastNameProfile.text =  Editable.Factory.getInstance().newEditable("")
        }
        try {
            etEmailProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.email)
        }catch (ex:Exception){
            etEmailProfile.text =  Editable.Factory.getInstance().newEditable("")
        }
        try {
            etMobileProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.mobileNumber)
        }catch (ex:Exception){
            etMobileProfile.text =  Editable.Factory.getInstance().newEditable("")
        }
        try {
            etCivilIdNbProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.civilId)
        }catch (ex:Exception){
            etCivilIdNbProfile.text =  Editable.Factory.getInstance().newEditable("")
        }
        try {
            etDateOfBirthProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.birthday)
        }catch (ex:Exception){
            etDateOfBirthProfile.text =  Editable.Factory.getInstance().newEditable("")
        }
        try {
            etAltContactNumberProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.altrNumb)
        }catch (ex:Exception){
            etAltContactNumberProfile.text =  Editable.Factory.getInstance().newEditable("")
        }
        try {
            etAddressProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.location)
        }catch (ex:Exception){
            etAddressProfile.text =  Editable.Factory.getInstance().newEditable("")
        }
        try {
            etAccountNumberProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.accountNumber)
        }catch (ex:Exception){
            etAccountNumberProfile.text =  Editable.Factory.getInstance().newEditable("")
        }
        try {
            etBankNameProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.bankName)
        }catch (ex:Exception){
            etBankNameProfile.text =  Editable.Factory.getInstance().newEditable("")
        }
        try {
            etBranchNameProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.bankAddress)
        }catch (ex:Exception){
            etBranchNameProfile.text =  Editable.Factory.getInstance().newEditable("")
        }
        try {
            etDescriptionProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.desc)
        }catch (ex:Exception){
            etDescriptionProfile.text =  Editable.Factory.getInstance().newEditable("")
        }
        try {
            etCivilIdNbProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.civilId)
        }catch (ex:Exception){
            etCivilIdNbProfile.text =  Editable.Factory.getInstance().newEditable("")
        }
        try {
            etBranchNameProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.bankBranch)
        }catch (ex:Exception){
            etBranchNameProfile.text =  Editable.Factory.getInstance().newEditable("")
        }
        try {
            etIBANProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.IBAN)
        }catch (ex:Exception){
            etIBANProfile.text =  Editable.Factory.getInstance().newEditable("")
        }
        try {
            etDateOfBirthProfile.text =
                Editable.Factory.getInstance().newEditable(MyApplication.selectedUser!!.dob)
        }catch (ex:Exception){
            etDateOfBirthProfile.text =  Editable.Factory.getInstance().newEditable("")
        }

        if(MyApplication.selectedUser!!.gender.equals("female")){
            rbFemaleProfile.isChecked = true
            rbMaleProfile.isChecked = false
        }else{
            rbFemaleProfile.isChecked = false
            rbMaleProfile.isChecked = true
        }

        if(!MyApplication.isClient){
        if(!MyApplication.selectedUser!!.mobileNumber.isNullOrEmpty())
            profilePercentage+=25
        if(!MyApplication.selectedUser!!.firstName.isNullOrEmpty() && !MyApplication.selectedUser!!.middleName.isNullOrEmpty() && !MyApplication.selectedUser!!.lastName.isNullOrEmpty())
            profilePercentage+=25
        if(!MyApplication.selectedUser!!.location.isNullOrEmpty())
            profilePercentage+=25
        if(!MyApplication.selectedUser!!.accountNumber.isNullOrEmpty() && !MyApplication.selectedUser!!.bankName.isNullOrEmpty() && !MyApplication.selectedUser!!.bankAddress.isNullOrEmpty()  /*&& !MyApplication.selectedUser!!.iban.isNullOrEmpty()*/)
            profilePercentage+=25
        }else{
            if(!MyApplication.selectedUser!!.mobileNumber.isNullOrEmpty())
                profilePercentage+=25
            if(!MyApplication.selectedUser!!.firstName.isNullOrEmpty() && !MyApplication.selectedUser!!.lastName.isNullOrEmpty())
                profilePercentage+=25
            if(!MyApplication.selectedUser!!.email.isNullOrEmpty())
                profilePercentage+=25
            if(!MyApplication.selectedUser!!.profilePicUrl.isNullOrEmpty())
                profilePercentage+=25
        }

        pbComplete.setWeight(profilePercentage.toFloat())
        pbNotComplete.setWeight(100f-profilePercentage.toFloat())
        tvPercentageCompleted.text = profilePercentage.toString()+" % "+AppHelper.getRemoteString("completed",requireActivity())

    }

    fun init() {
        //tvToolbarCurveTitle.setText(getString(R.string.profile))
        (activity as ActivityHome?)!!.showBack(R.color.white)
        AppHelper.setTitle(
            requireActivity(),
            AppHelper.getRemoteString("Profile", requireContext()),
            "profile"
        )
        rbFemaleProfile.isSelected = true
        // (activity as ActivityHome?)!!.showLogout(false)
        tvToolbarCurveTitle.visibility = View.GONE
        listeners()
        if (MyApplication.isClient)
            showClientFields()
        getUserData()


    }

    fun getUserData(){
        loading.show()
        var newReq = RequestUpdateLanguage(MyApplication.userId,MyApplication.languageCode)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getUser(newReq
            )?.enqueue(object : Callback<ResponseUser> {
                override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                    try{
                        MyApplication.selectedUser=response.body()!!.user}catch (e:Exception){

                        }
                    loading.hide()
                    setUserData()
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
        if(selectedProfilePic==null){
            var empty =""
            val attachmentEmpty = empty.toRequestBody("text/plain".toMediaTypeOrNull())

            selectedProfilePic =createFormData("profile_pic", "", attachmentEmpty)
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


            )?.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    try {
                        loading.hide()
                        getUserData()
                    } catch (E: java.lang.Exception) {
                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<String>, throwable: Throwable) {
                 loading.hide()
                }
            })

    }


    fun listeners() {


        btSaveProfile.onOneClick {

            /*  if(etFirstNameProfile.text.isNullOrEmpty()||etMiddleNameProfile.text.isNullOrEmpty()||etLastNameProfile.text.isNullOrEmpty()||etEmailProfile.text.isNullOrEmpty()||etMobileProfile.text.isNullOrEmpty()||etCivilIdNbProfile.text.isNullOrEmpty()||etDateOfBirthProfile.text.isNullOrEmpty()||etAltContactNumberProfile.text.isNullOrEmpty()||etAddressProfile.text.isNullOrEmpty()||etDescriptionProfile.text.isNullOrEmpty()||etAccountNumberProfile.text.isNullOrEmpty()||etBankNameProfile.text.isNullOrEmpty()||etBranchNameProfile.text.isNullOrEmpty()||etIBANProfile.text.isNullOrEmpty()){
                  AppHelper.createDialog(requireActivity(),AppHelper.getRemoteString("fill_all_field",requireContext()))
              }else if(!AppHelper.isEmailValid(etEmailProfile.text.toString())){
                  AppHelper.createDialog(requireActivity(),AppHelper.getRemoteString("email_valid_error",requireContext()))
              }else if(selectedFile==null || selectedProfilePic==null ) {
                  var str ="No File Selected"
                  AppHelper.createDialog(requireActivity(),str)
              }else
              {*/
            if (!MyApplication.isClient)
                updateProfile()
            else
                updateClient()
            //   }
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
            mDatePicker.show()
        }
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
                var file = AppHelper.getFile(requireContext(), files[0].uri)
                var req = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                if (!fromProfile!!)
                    selectedFile = MultipartBody.Part.createFormData(
                        ApiParameters.CIVIL_ID_ATTACH,
                        file.name + "File",
                        req
                    )
                else{
                    selectedProfilePic = MultipartBody.Part.createFormData(
                       if(MyApplication.isClient) ApiParameters.PROFILE_PIC else ApiParameters.FILE,
                        file.name + "File",
                        req
                    )
                }

            } catch (e: Exception) {
            }
        }
    }

    private fun pickFile(pickCode: Int, from: Boolean) {

        val intent = Intent(requireContext(), FilePickerActivity::class.java)
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
        fromProfile = from
        startActivityForResult(intent, pickCode)

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
        if(selectedFile==null){
            var empty =""
            val attachmentEmpty = empty.toRequestBody("text/plain".toMediaTypeOrNull())

            selectedFile =createFormData("attachment", "", attachmentEmpty)
        }


        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.updateProfile(
                user,
                first,
                middle,
                last,
                email,
                phone,
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

            )?.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    try {
                        getUserData()
                    } catch (E: java.lang.Exception) {
                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<String>, throwable: Throwable) {
                    loading.hide()
                }
            })
    }

    private fun showClientFields(){
        etMiddleNameProfile.hide()
        linearProviderInfo.hide()
    }
}