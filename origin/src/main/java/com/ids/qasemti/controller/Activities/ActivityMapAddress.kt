package com.ids.qasemti.controller.Activities

import android.Manifest
import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.ids.qasemti.R
import com.ids.qasemti.controller.Adapters.AdapterCountryCodes
import com.ids.qasemti.controller.Adapters.AdapterMapLocations
import com.ids.qasemti.controller.Adapters.PlacesAutoCompleteAdapter
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.Base.AppCompactBase
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.CountryArray
import com.ids.qasemti.model.ResponseNominatim
import com.ids.qasemti.utils.*
import com.ids.qasemti.utils.AppHelper.Companion.toEditable
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.activity_map.rootLayout
import kotlinx.android.synthetic.main.activity_services.*
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.popup_location_search.*
import kotlinx.android.synthetic.main.toolbar.*


class ActivityMapAddress : AppCompactBase(), OnMapReadyCallback,
    PlacesAutoCompleteAdapter.ClickListener, ApiListener, RVOnItemClickListener {


    var REQUEST_ADDRESS = 1005
    var resultLauncher: ActivityResultLauncher<Intent>? = null
    var mAutoCompleteAdapter: PlacesAutoCompleteAdapter? = null
    var adapter: AdapterMapLocations? = null
    var dialog: Dialog? = null
    var send = 0
    var clearFlag = false
    var Loading: LinearLayout? = null
    var arrayMapLocs: ArrayList<ResponseNominatim> = arrayListOf()
    var first = true
    var searchEdit: EditText? = null
    var Recycle: RecyclerView? = null
    var latLng: LatLng? = null
    var seeOnly = false
    private lateinit var mPlacesClient: PlacesClient
    var gmap: GoogleMap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        AppHelper.setAllTexts(rootLayout, this)

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(getString(R.string.googleKey))
        }

        mvLocation.onCreate(mapViewBundle);

        init()
        //  initGooglePlacesApi()


    }


    private val filterTextWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            mAutoCompleteAdapter!!.getFilter().filter(s.toString());
            if (s.toString() != "") {
                mAutoCompleteAdapter!!.getFilter().filter(s.toString())
                if (rv_place_results.getVisibility() === View.GONE) {
                    rv_place_results.setVisibility(View.VISIBLE)
                }
            } else {
                if (rv_place_results.getVisibility() === View.VISIBLE) {
                    rv_place_results.setVisibility(View.GONE)
                }
            }
        }
    }

    fun initGooglePlacesApi() {

        Places.initialize(this, getString(R.string.googleKey))
        etMapSearch.addTextChangedListener(filterTextWatcher)
        etMapSearch.show()


        mAutoCompleteAdapter = PlacesAutoCompleteAdapter(this)
        rv_place_results.setLayoutManager(LinearLayoutManager(this));
        //mAutoCompleteAdapter.setClickListener(this);
        Places.initialize(this, getString(R.string.googleKey))
        rv_place_results.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteAdapter!!.notifyDataSetChanged();
        /* RetroFitMap.client?.create(RetrofitInterface::class.java)
             ?.getLocationNames("Beirut",getString(R.string.googleKey))?.enqueue(object :
                 Callback<Any> {
                 override fun onResponse(
                     call: Call<Any>,
                     response: retrofit2.Response<Any>
                 ) {
                    Log.wtf("tag",response.toString())
                 }

                 override fun onFailure(call: Call<Any>, throwable: Throwable) {
                     loading.hide()
                 }
             })*/
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED, intent)
        super.onBackPressed()
    }


    private fun showPopUp() {
        dialog = Dialog(this, R.style.Base_ThemeOverlay_AppCompat_Dialog)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_location_search)
        dialog!!.setCancelable(true)
        dialog!!.window!!.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
        dialog!!.window!!.setBackgroundDrawableResource(R.color.transparent)


        var hide = dialog!!.findViewById<FloatingActionButton>(R.id.btClose)
        searchEdit = dialog!!.findViewById(R.id.etSearchLocation)
        var clear = dialog!!.findViewById<ImageView>(R.id.btClearLocation)
        Recycle = dialog!!.findViewById(R.id.rvLocationSearch)
        Loading = dialog!!.findViewById(R.id.loading)

        hide.onOneClick {
            dialog!!.dismiss()
        }
        clear.onOneClick {
            send = 0
            searchEdit!!.text.clear()
        }

        searchEdit!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.length > 0) {
                    send++
                    Recycle!!.hide()
                    clear.show()
                    Loading!!.show()
                    CallAPIs.getMapLocations(s.toString(), this@ActivityMapAddress, Loading!!)
                } else {
                    clear.hide()
                    Recycle!!.hide()
                    send = 5
                    clearFlag = true
                    Loading!!.show()
                    CallAPIs.getMapLocations("", this@ActivityMapAddress, Loading!!)
                }

            }
        })

        dialog!!.show()
    }

    fun init() {
        mvLocation.getMapAsync(this);

        btDrawer.hide()
        btBackTool.show()
        btBackTool.onOneClick {
            onBackPressed()
        }
        var title = ""

        try {
            seeOnly = intent.getBooleanExtra("seeOnly", false)
        } catch (ex: Exception) {
            seeOnly = false
        }

        if (seeOnly) {
            btSavePosition.hide()
        }
        AppHelper.setLogoTint(btBackTool, this, R.color.primary)
        try {
            title = intent.getStringExtra("mapTitle")!!
        } catch (ex: java.lang.Exception) {
            title = AppHelper.getRemoteString("LocationOnMap", this)
        }
        AppHelper.setLogoTint(btBackTool, this, R.color.primary)
        tvPageTitle.setColorTypeface(this, R.color.primary, title!!, true)
        listeners()
    }

    fun listeners() {
        btSavePosition.setOnClickListener {

            if (MyApplication.isClient) {
                var x = latLng
                if (MyApplication.finish!!) {
                    MyApplication.latSelected = latLng!!.latitude
                    MyApplication.longSelected = latLng!!.longitude
                    if (!MyApplication.addNew) {
                        finish()
                    }
                    resultLauncher!!.launch(Intent(this, ActivityAddNewAddress::class.java))
                } else {
                    val intent = Intent()
                    intent.putExtra("lat", latLng!!.latitude)
                    intent.putExtra("long", latLng!!.longitude)
                    intent.putExtra(
                        "address",
                        AppHelper.getAddress(latLng!!.latitude, latLng!!.longitude, this)
                    )
                    setResult(RESULT_OK, intent)
                    finish()
                }
            } else {

                startActivity(
                    Intent(this, ActivityAddNewAddress::class.java)
                        .putExtra("lat", latLng!!.latitude)
                        .putExtra("long", latLng!!.longitude)
                        .putExtra(
                            "address",
                            AppHelper.getAddress(latLng!!.latitude, latLng!!.longitude, this)
                        )
                )


            }
        }

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {

                }
            }

        btClear.onOneClick {
            etMapSearch.text.clear()
        }
        etMapSearch.setOnClickListener {
            showPopUp()
        }
        /*etMapSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if(s.length>0){
                    btClear.show()
                    rv_place_results.hide()
                    CallAPIs.getMapLocations(s.toString(),this@ActivityMapAddress,loading)
                }else{
                    btClear.hide()
                    rv_place_results.hide()
                }

            }
        })*/
    }


    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(getString(R.string.googleKey))
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(getString(R.string.googleKey), mapViewBundle)
        }

        mvLocation!!.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        mvLocation!!.onResume()


        if ((MyApplication.addNew && !first) && MyApplication.isClient) {
            try {
                MyApplication.addNew = false
                MyApplication.fromAdd = false
                val intent = Intent()
                intent.putExtra("lat", MyApplication.selectedAddress!!.lat!!.toDouble())
                intent.putExtra("long", MyApplication.selectedAddress!!.long!!.toDouble())

                /*var latLng = com.google.android.gms.maps.model.LatLng(
                array.get(position).lat!!.toDouble(),
                array.get(position).long!!.toDouble()
            )*/
                //   MyApplication.selectedAddress = array.get(position)
                intent.putExtra(
                    "address",
                    MyApplication.selectedAddress!!.desc + " ," + MyApplication.selectedAddress!!.street + " ," + MyApplication.selectedAddress!!.bldg + " ," + MyApplication.selectedAddress!!.floor
                )
                MyApplication.submitted = true
                intent.putExtra("submitted", MyApplication.submitted)
                setResult(RESULT_OK, intent)
                finish()
            } catch (ex: Exception) {
            }
        } else {
            first = false
        }
    }

    override fun onStart() {
        super.onStart()
        mvLocation!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        mvLocation!!.onStop()
    }

    override fun onPause() {
        mvLocation!!.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mvLocation!!.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mvLocation!!.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gmap = googleMap
        gmap!!.setMinZoomPreference(12f)

        if (!seeOnly) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

            }
            try {
                val locationManager =
                    getSystemService(Context.LOCATION_SERVICE) as LocationManager
                var gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                latLng = LatLng(gps_loc!!.latitude, gps_loc!!.longitude)
                //   addressName = AppHelper.getAddress(latLng!!.latitude, latLng!!.longitude, this)
            } catch (e: Exception) {
                latLng = LatLng(33.8658486, 35.5483189)
                e.printStackTrace()
            }

            gmap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            gmap!!.addMarker(markerOptions)

            gmap!!.setOnMapClickListener {
                gmap!!.moveCamera(CameraUpdateFactory.newLatLng(it))
                val marker = MarkerOptions()
                marker.position(it)
                gmap!!.clear()
                gmap!!.addMarker(marker)
                latLng = it
            }
        } else {
            var ll: LatLng? = null
            try {
                ll = LatLng(
                    MyApplication.selectedOrder!!.customerLat!!.toDouble(),
                    MyApplication.selectedOrder!!.customerLong!!.toDouble()
                )
            } catch (ex: Exception) {

            }

            if (ll != null) {
                gmap!!.moveCamera(CameraUpdateFactory.newLatLng(ll))
                val markerOptions = MarkerOptions()
                markerOptions.position(ll)
                gmap!!.addMarker(markerOptions)
            } else {
                AppHelper.createDialog(this, "No location date connected to selected order")
                latLng = LatLng(33.8658486, 35.5483189)
                gmap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            }

        }

    }

    private fun setData() {
        Recycle!!.show()
        //  if(adapter==null) {
        adapter = AdapterMapLocations(arrayMapLocs, this, this)
        Recycle!!.layoutManager = LinearLayoutManager(this)
        Recycle!!.adapter = adapter
        Recycle!!.isNestedScrollingEnabled = false
        /* }else{
             adapter!!.notifyDataSetChanged()
         }*/
      //  Loading!!.hide()
    }

    override fun click(place: com.google.android.libraries.places.api.model.Place?) {
        Toast.makeText(
            this,
            place!!.getAddress()
                .toString() + ", " + place.getLatLng().latitude + place.getLatLng().longitude,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDataRetrieved(success: Boolean, response: Any, apiId: Int) {
        arrayMapLocs.clear()
        arrayMapLocs.addAll(response as ArrayList<ResponseNominatim>)

        setData()


    }

    fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    override fun onItemClicked(view: View, position: Int) {
        if (arrayMapLocs.size > 0) {
            latLng = LatLng(
                arrayMapLocs.get(position).lat!!.toDouble(),
                arrayMapLocs.get(position).lon!!.toDouble()
            )
            gmap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            val marker = MarkerOptions()
            marker.position(latLng)
            gmap!!.clear()
            gmap!!.addMarker(marker)
            rv_place_results.hide()
            etMapSearch.text.clear()
            etMapSearch.text = arrayMapLocs.get(position).displayName!!.toEditable()
            hideSoftKeyboard()
            dialog!!.dismiss()
        }
    }

}