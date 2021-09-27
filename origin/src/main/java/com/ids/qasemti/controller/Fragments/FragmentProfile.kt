package com.ids.qasemti.controller.Fragments

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Activities.ActivityMapLocation
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.model.ResponseUpdate
import com.ids.qasemti.utils.*
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.config.Configurations
import com.jaiselrahman.filepicker.model.MediaFile
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.curve_layout_home.*
import kotlinx.android.synthetic.main.layout_profile.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.service_tab_1.*
import kotlinx.android.synthetic.main.service_tab_2.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.white_logo_layout.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class FragmentProfile : Fragment(), RVOnItemClickListener {
    var selectedFile : MultipartBody.Part ?=null
    var gender = "female"
    override fun onItemClicked(view: View, position: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.layout_profile, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutProfile,requireContext())
        init()

    }

    fun init(){
        //tvToolbarCurveTitle.setText(getString(R.string.profile))
        (activity as ActivityHome?)!!.showBack(R.color.white)
         AppHelper.setTitle(requireActivity(),AppHelper.getRemoteString("Profile",requireContext()),"profile")
        rbFemaleProfile.isSelected=true
       // (activity as ActivityHome?)!!.showLogout(false)
        tvToolbarCurveTitle.visibility = View.GONE
        listeners()


    }

    fun listeners(){
        ivMapButton.onOneClick {
            startActivity(Intent(requireContext(),ActivityMapLocation::class.java))
        }

        btSaveProfile.onOneClick {

            if(etFirstNameProfile.text.isNullOrEmpty()||etMiddleNameProfile.text.isNullOrEmpty()||etLastNameProfile.text.isNullOrEmpty()||etEmailProfile.text.isNullOrEmpty()||etMobileProfile.text.isNullOrEmpty()||etCivilIdNbProfile.text.isNullOrEmpty()||etDateOfBirthProfile.text.isNullOrEmpty()||etAltContactNumberProfile.text.isNullOrEmpty()||etAddressProfile.text.isNullOrEmpty()||etDescriptionProfile.text.isNullOrEmpty()||etAccountNumberProfile.text.isNullOrEmpty()||etBankNameProfile.text.isNullOrEmpty()||etBranchNameProfile.text.isNullOrEmpty()||etIBANProfile.text.isNullOrEmpty()){
                AppHelper.createDialog(requireActivity(),AppHelper.getRemoteString("fill_all_field",requireContext()))
            }else if(!AppHelper.isEmailValid(etEmailProfile.text.toString())){
                AppHelper.createDialog(requireActivity(),AppHelper.getRemoteString("email_valid_error",requireContext()))
            }else if(selectedFile==null) {
                var str ="No File Selected"
                AppHelper.createDialog(requireActivity(),str)
            }else
            {
                updateProfile()
            }
        }
        rbMaleProfile.onOneClick {
            gender= "male"
        }
        rbFemaleProfile.onOneClick {
            gender="female"
        }

        ibUploadFile.setOnClickListener {
            pickFile(1)
        }
    }

    fun getPath(uri: Uri?): String? {
        println("IM IN getPath")
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = requireActivity().getContentResolver().query(uri!!, projection, null, null, null)!!
        val column_index: Int = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            val files: ArrayList<MediaFile> =data!!.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES)!!
         //   var path = getPath(files.get(0).uri)
            var file = File(files.get(0).path)
            var yes = file.exists()
            var body1 : MultipartBody.Part?=null
          var req =  RequestBody.create("multipart/form-data".toMediaType(),file)
            try {
                body1 = MultipartBody.Part.createFormData("file", file.name, req)
            }catch (ex:Exception){
                body1 = MultipartBody.Part.createFormData("file", "Upload",req)
            }
            selectedFile = body1


        }catch (e: Exception){}
    }
    private fun pickFile(pickCode:Int){

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
        startActivityForResult(intent,pickCode)

    }

    fun updateProfile(){
        loading.show()
        var userId = "1"
        var rolev = "vendor"
        var latt = "33.5618345"
        var longg = "35.3780338"
        var add = "Sidon, Sidon District, South Governorate, 1600, Lebanon"
        var gender = "female"
        if(rbMaleProfile.isSelected){
            gender="male"
        }
        val user = RequestBody.create("text/plain".toMediaTypeOrNull(),userId)
        val first = RequestBody.create("text/plain".toMediaTypeOrNull(),etFirstNameProfile.text.toString())
        val middle = RequestBody.create("text/plain".toMediaTypeOrNull(),etMiddleNameProfile.text.toString())
        val last = RequestBody.create("text/plain".toMediaTypeOrNull(),etLastNameProfile.text.toString())
        val email = RequestBody.create("text/plain".toMediaTypeOrNull(),etEmailProfile.text.toString())
        val phone = RequestBody.create("text/plain".toMediaTypeOrNull(),etMobileProfile.text.toString())
        val civilId = RequestBody.create("text/plain".toMediaTypeOrNull(),etCivilIdNbProfile.text.toString())
        val genderr = RequestBody.create("text/plain".toMediaTypeOrNull(),gender)
        val dob = RequestBody.create("text/plain".toMediaTypeOrNull(),etDateOfBirthProfile.text.toString())
        val role = RequestBody.create("text/plain".toMediaTypeOrNull(),rolev)
        val address = RequestBody.create("text/plain".toMediaTypeOrNull(),etAddressProfile.text.toString())
        val lat = RequestBody.create("text/plain".toMediaTypeOrNull(),latt)
        val long = RequestBody.create("text/plain".toMediaTypeOrNull(),longg)
        val accNum = RequestBody.create("text/plain".toMediaTypeOrNull(),etAccountNumberProfile.text.toString())
        val bankname = RequestBody.create("text/plain".toMediaTypeOrNull(),etBankNameProfile.text.toString())
        val bankBranch = RequestBody.create("text/plain".toMediaTypeOrNull(),etBranchNameProfile.text.toString())
        val iban = RequestBody.create("text/plain".toMediaTypeOrNull(),etIBANProfile.text.toString())


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
                bankBranch ,
              iban

            )?.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    try{

                    }catch (E: java.lang.Exception){

                    }
                }
                override fun onFailure(call: Call<String>, throwable: Throwable) {
                    var x = 1
                }
            })
    }
}