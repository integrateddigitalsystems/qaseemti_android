package com.ids.qasemti.controller.Fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ids.qasemti.R
import com.ids.qasemti.controller.Activities.ActivityHome
import com.ids.qasemti.controller.Activities.ActivityOrderDetails
import com.ids.qasemti.controller.Adapters.AdapterOrders
import com.ids.qasemti.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.qasemti.controller.MyApplication
import com.ids.qasemti.model.*
import com.ids.qasemti.utils.*
import kotlinx.android.synthetic.main.activity_code_verification.*
import kotlinx.android.synthetic.main.fragment_home_client.*
import kotlinx.android.synthetic.main.layout_home_orders.*
import kotlinx.android.synthetic.main.loading.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.collections.ArrayList

class FragmentHomeSP : Fragment(), RVOnItemClickListener {

    private var ordersArray: ArrayList<ResponseOrders> = arrayListOf()
    var adapter: AdapterOrders? = null
    val BLOCKED = -1
    var trackorders: ArrayList<ResponseOrders> = arrayListOf()
    var timer: CountDownTimer? = null
    var mPermissionResult: ActivityResultLauncher<Array<String>>? = null
    var resultLauncher: ActivityResultLauncher<Intent>? = null
    val GRANTED = 0
    val DENIED = 1
    val BLOCKED_OR_NEVER_ASKED = 2
    var isTimer = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.layout_home_orders, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppHelper.setAllTexts(rootLayoutOrders, requireContext())
        setUpPermission()
        init()


    }

    override fun onResume() {
        super.onResume()


        if (MyApplication.toDetails) {
            MyApplication.isBroadcast = true
            MyApplication.toDetails = false
            startActivity(
                Intent(requireActivity(), ActivityOrderDetails::class.java)
                    .putExtra("orderId", MyApplication.selectedOrderId)
            )
        } else {
            loading.show()
            timer!!.start()
            checkCallData()
            getAfterOrders()
        }


    }

    private fun checkCallData() {
        if (MyApplication.selectedUser != null) {
            if (MyApplication.selectedUser!!.available == null || MyApplication.selectedUser!!.available!!.isEmpty()) {
                setAvailability(1)
                swAvailable.text = AppHelper.getRemoteString("available", requireContext())
                swAvailable.isChecked = true
            } else {
                if (MyApplication.selectedUser!!.available == "1") {
                    getRating()
                    getData()
                    getOrders(false)
                } else {
                    loading.hide()
                    slRefreshBroad.hide()
                    llNodata.show()
                    tvNoDataHome.hide()
                    swAvailable.text = AppHelper.getRemoteString("unavailable", requireContext())
                    if (timer != null)
                        timer!!.cancel()
                }
            }
        } else {
            AppHelper.triggerRebirth(requireActivity())
        }

    }

    fun getBroadcastedOrders() {
        var newReq = RequestServices(MyApplication.userId, MyApplication.languageCode)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getBroadcastedOrders(newReq)?.enqueue(object : Callback<ResponseMainOrder> {
                override fun onResponse(
                    call: Call<ResponseMainOrder>,
                    response: Response<ResponseMainOrder>
                ) {

                }

                override fun onFailure(call: Call<ResponseMainOrder>, throwable: Throwable) {
                }
            })
    }

    fun setAvailability(available: Int) {
        var newReq = RequestAvailability(MyApplication.userId, available)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.updateAvailability(newReq)?.enqueue(object : Callback<ResponseCancel> {
                override fun onResponse(
                    call: Call<ResponseCancel>,
                    response: Response<ResponseCancel>
                ) {
                    if (available == 1) {
                        try {
                            getRating()
                            getData()
                            getOrders(false)
                        } catch (ex: Exception) {
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseCancel>, throwable: Throwable) {
                }
            })
    }

    fun getRating() {
        //loading.show()
        var newReq = RequestUserStatus(MyApplication.userId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getRatings(newReq)?.enqueue(object : Callback<ResponseRatings> {
                override fun onResponse(
                    call: Call<ResponseRatings>,
                    response: Response<ResponseRatings>
                ) {
                    try {
                        if (response.body()!!.rate != null) {
                            rbMainUser.rating =
                                AppHelper.getFloorRatingBar(response.body()!!.rate!!)
                            val decimal = BigDecimal(response.body()!!.rate!!).setScale(
                                1,
                                RoundingMode.HALF_EVEN
                            )
                            tvRatingValue.text = decimal.toString()
                        } else {
                            tvRatingValue.text = "0.0"
                        }
                    } catch (E: java.lang.Exception) {
                        // rbMainUser.rating = 0f
                    }
                }

                override fun onFailure(call: Call<ResponseRatings>, throwable: Throwable) {
                    //  rbMainUser.rating = 0f

                }
            })
    }

    fun setUpTimer() {
        timer = object : CountDownTimer(30000, 1015) {
            override fun onTick(millisUntilFinished: Long) {
                //logw("tick","second")
            }

            override fun onFinish() {
                getRating()
                getData()
                isTimer = true
                getOrders(isTimer)
            }
        }.start()
    }

    fun init() {
        (activity as ActivityHome?)!!.showLogout(false)
        (activity as ActivityHome?)!!.setTintLogo(R.color.primary)
        //   AppHelper.setTitle(requireActivity(), MyApplication.selectedTitle!!, "",R.color.redPrimary)
        setListeners()
        setUpTimer()


    }

    fun getData() {

        // loading.show()

        var newReq = RequestUserStatus(MyApplication.userId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getOrdersCount(newReq)?.enqueue(object : Callback<ResponeOrderCount> {
                override fun onResponse(
                    call: Call<ResponeOrderCount>,
                    response: Response<ResponeOrderCount>
                ) {
                    try {
                        tvActiveOrdersNbr.text = response.body()!!.activeOrders.toString()
                        tvUpcomingOrderNumber.text = response.body()!!.upcomingOrders.toString()

                    } catch (E: java.lang.Exception) {

                    }
                }

                override fun onFailure(call: Call<ResponeOrderCount>, throwable: Throwable) {
                    try {
                        loading.hide()
                    } catch (ex: Exception) {

                    }
                }
            })
    }

    fun setListeners() {

        slRefreshBroad.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            getOrders(false)
        })
        rlActive.onOneClick {
            MyApplication.fromFooterOrder = false
            MyApplication.selectedFragment = FragmentOrders()
            (requireActivity() as ActivityHome?)!!.addFrag(
                FragmentOrders(),
                AppConstants.FRAGMENT_ORDER_FROM
            )
            MyApplication.selectedTitle = AppHelper.getRemoteString("orders", requireContext())
            MyApplication.typeSelected = 0

        }
        rlUpcoming.onOneClick {
            MyApplication.fromFooterOrder = false
            MyApplication.fromHome = true
            MyApplication.selectedFragment = FragmentOrders()
            (requireActivity() as ActivityHome?)!!.addFrag(
                FragmentOrders(),
                AppConstants.FRAGMENT_ORDER_FROM
            )
            MyApplication.selectedTitle = AppHelper.getRemoteString("orders", requireContext())
            MyApplication.typeSelected = 1
        }
        try {
            swAvailable.typeface = AppHelper.getTypeFace(requireContext())
            swAvailable.isChecked = MyApplication.selectedUser!!.available != "0"
        } catch (ex: Exception) {
            swAvailable.isChecked = false
        }
        swAvailable.setOnCheckedChangeListener { compoundButton, b ->
            if (AppHelper.isOnline(requireActivity())) {
                try {
                    if (swAvailable.isChecked) {
                        slRefreshBroad.show()
                        // getOrders()
                        setAvailability(1)
                        swAvailable.text = AppHelper.getRemoteString("available", requireContext())
                        llNodata.hide()
                    } else {
                        slRefreshBroad.hide()
                        llNodata.show()
                        tvNoDataHome.hide()
                        setAvailability(0)
                        swAvailable.text =
                            AppHelper.getRemoteString("unavailable", requireContext())
                    }
                } catch (ex: Exception) {

                }
            } else {
                swAvailable.isChecked = !swAvailable.isChecked
                AppHelper.createDialog(
                    requireActivity(),
                    AppHelper.getRemoteString("no_internet", requireContext())
                )
            }

        }
    }

    fun getOrders(timer: Boolean) {

      //  loading.show()
        try {
            slRefreshBroad.isRefreshing = false
        } catch (ex: Exception) {

        }
        if (!timer)
            loading.show()
        var newReq = RequestServices(MyApplication.userId, MyApplication.languageCode)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getBroadcastedOrders(newReq)?.enqueue(object : Callback<ResponseMainOrder> {
                override fun onResponse(
                    call: Call<ResponseMainOrder>,
                    response: Response<ResponseMainOrder>
                ) {
                    try {
                        if (ordersArray.size != 0 && rvOrders.canScrollVertically(-1)) {
                            if (ordersArray.size < response.body()!!.orders!!.size)
                                Toast.makeText(
                                    requireContext(),
                                    "New Data Added",
                                    Toast.LENGTH_LONG
                                ).show()
                        }
                        ordersArray.clear()
                        ordersArray.addAll(response.body()!!.orders)


                        setOrders()


                    } catch (E: java.lang.Exception) {
                        try {
                            loading.hide()
                            if (swAvailable.isChecked)
                                setOrders()
                            else
                                setNotAvailable()
                        } catch (ex: Exception) {

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseMainOrder>, throwable: Throwable) {
                    try {
                        logw("Error_Load",throwable.toString())
                        loading.hide()
                        if (!swAvailable.isChecked)
                            setNotAvailable()
                        else
                            setOrders()
                    } catch (ex: Exception) {

                    }
                }
            })
    }

    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    fun getPermissionStatus(androidPermissionName: String?): Int {
        return if (ContextCompat.checkSelfPermission(
                requireActivity(),
                androidPermissionName!!
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    androidPermissionName
                )
            ) {
                BLOCKED_OR_NEVER_ASKED
            } else DENIED
        } else GRANTED
    }

    fun setUpPermission() {
        mPermissionResult =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
            { result ->
                var permission = false
                for (item in result) {
                    permission = item.value
                }
                if (permission) {
                    setUp()
                    //   selectImage(requireContext())
                    //  Log.e(TAG, "onActivityResult: PERMISSION GRANTED")
                    //  MyApplication.permissionAllow11 = 0
                } else {
                    for (item in result) {
                        if (ContextCompat.checkSelfPermission(
                                requireContext(),
                                item.key
                            ) == BLOCKED
                        ) {

                            if (getPermissionStatus(Manifest.permission.ACCESS_FINE_LOCATION) == BLOCKED_OR_NEVER_ASKED) {
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
                    }

                }
            }


    }

    private fun openChooser() {


        mPermissionResult!!.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
        /*mPermissionResult!!.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )*/


    }

    fun checkSetUp() {
        var gps_enabled = false
        var mLocationManager =
            requireActivity().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

        try {
            gps_enabled = mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }
        if (!MyApplication.isClient) {
            if (foregroundPermissionApproved() && gps_enabled) {
                setUp()
            } else {
                if (gps_enabled) {
                    val builder = AlertDialog.Builder(requireContext())
                    builder
                        .setMessage(
                            AppHelper.getRemoteString(
                                "permission_background_android",
                                requireActivity()
                            )
                        )
                        .setCancelable(true)
                        .setNegativeButton(
                            AppHelper.getRemoteString(
                                "cancel",
                                requireActivity()
                            )
                        ) { dialog, _ ->
                            // getOrders()
                        }
                        .setPositiveButton(
                            AppHelper.getRemoteString(
                                "ok",
                                requireActivity()
                            )
                        ) { dialog, _ ->
                            openChooser()
                        }
                    val alert = builder.create()
                    alert.show()

                } else {
                    requireActivity().toast(
                        AppHelper.getRemoteString(
                            "GpsDisabled",
                            requireContext()
                        )
                    )
                    //getOrders()
                }
            }
        }
    }

    fun setUp() {

        MyApplication.listOrderTrack.clear()

        for (item in trackorders) {
            if (item.onTrack!! && !item.delivered!! && !item.paymentMethod.isNullOrEmpty()) {
                MyApplication.listOrderTrack.add(item.orderId!!)
            }
        }

        AppHelper.toGsonArrString()
        if (MyApplication.listOrderTrack.size > 0) {
            (activity as ActivityHome).changeState(true, 0)
        }
    }

    fun getAfterOrders() {


        var newReq = RequestOrders(
            MyApplication.userId,
            MyApplication.languageCode,
            AppConstants.ORDER_TYPE_ACTIVE
        )
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.getOrders(
                newReq
            )?.enqueue(object : Callback<ResponseMainOrder> {
                override fun onResponse(
                    call: Call<ResponseMainOrder>,
                    response: Response<ResponseMainOrder>
                ) {
                    try {
                        trackorders.clear()
                        trackorders.addAll(response!!.body()!!.orders)
                        checkSetUp()
                    } catch (E: java.lang.Exception) {
                        logw("TRACKING_ERROR", E.toString())
                    }
                }

                override fun onFailure(call: Call<ResponseMainOrder>, throwable: Throwable) {

                }
            })
    }

    fun accepted(res: Int) {
        if (res == 1) {
            AppHelper.createDialog(requireActivity(), getString(R.string.order_accept_succ))
            loading.show()
            getOrders(false)
            getData()
            getAfterOrders()

        } else {
            AppHelper.createDialog(requireActivity(), getString(R.string.error_acc_order))
        }
    }

    fun acceptOrder(orderId: Int, additional: Int) {
        loading.show()

        var newReq = RequestAcceptBroadccast(MyApplication.userId, orderId)
        RetrofitClient.client?.create(RetrofitInterface::class.java)
            ?.acceptBroadcast(newReq)?.enqueue(object : Callback<ResponseUser> {
                override fun onResponse(
                    call: Call<ResponseUser>,
                    response: Response<ResponseUser>
                ) {
                    try {
                        accepted(response.body()!!.result!!)
                    } catch (E: java.lang.Exception) {
                        accepted(0)
                        loading.hide()
                    }
                }

                override fun onFailure(call: Call<ResponseUser>, throwable: Throwable) {
                    accepted(0)
                    loading.hide()
                }
            })
    }

    override fun onStop() {
        super.onStop()
        timer!!.cancel()
    }

    override fun onPause() {
        super.onPause()
        timer!!.cancel()
    }

    private fun setOrders() {
        try {
            if (swAvailable.isChecked) {


                try {
                    //  ordersArray.add(ResponseOrders())
                    if (adapter != null) {
                        adapter!!.notifyDataSetChanged()
                    } else {
                        adapter = AdapterOrders(ordersArray, this, requireContext())
                        rvOrders.adapter = adapter
                        var glm2 = GridLayoutManager(requireContext(), 1)
                        rvOrders.layoutManager = glm2

                    }

                    if (ordersArray.size == 0) {
                        rvOrders.hide()
                        llNodata.hide()
                        tvNoDataHome.show()
                    } else {
                        rvOrders.show()
                        tvNoDataHome.hide()
                    }

                    loading.hide()

                    if (isTimer) {
                        isTimer = false
                        timer!!.start()
                    }
                } catch (ex: Exception) {
                    loading.hide()
                }

            } else {
                loading.hide()
                setNotAvailable()
            }
        } catch (ex: Exception) {

        }
    }

    private fun setNotAvailable() {
        loading.hide()
        slRefreshBroad.hide()
        llNodata.show()
        tvNoDataHome.hide()
        swAvailable.text = AppHelper.getRemoteString("unavailable", requireContext())
        if (timer != null)
            timer!!.cancel()
    }


    override fun onItemClicked(view: View, position: Int) {


        if (view.id == R.id.llLocation) {
            AppHelper.onOneClick {


                val urlAddress =
                    "http://maps.google.com/maps?q=" + ordersArray.get(position).shipping_latitude!! + "," + ordersArray.get(
                        position
                    ).shipping_longitude!! + "(" + "" + ")&iwloc=A&hl=es"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlAddress))
                startActivity(intent)
                /* val req: String =
                     java.lang.String.format(
                         Locale.ENGLISH,
                         "geo:%f,%f",
                         ordersArray.get(position).shipping_latitude!!.toDouble(),
                         ordersArray.get(position).shipping_longitude!!.toDouble()
                     )
                 val intentt = Intent(Intent.ACTION_VIEW, Uri.parse(req))
                 requireActivity().startActivity(intentt)*/
                /*MyApplication.selectedOrder = ordersArray[position]
                if (!MyApplication.selectedOrder!!.customerLocation.isNullOrEmpty() && !MyApplication.selectedOrder!!.customerLocation.equals(
                        "null"
                    )
                ) {
                    startActivity(
                        Intent(requireActivity(), ActivityMapAddress::class.java)
                            .putExtra(
                                "mapTitle",
                                AppHelper.getRemoteString("view_address", requireContext())
                            )
                            .putExtra("seeOnly", true)
                    )
                }*/

            }
        } else if (view.id == R.id.llViewOrderDetails) {
            AppHelper.onOneClick {
                MyApplication.isBroadcast = true
                MyApplication.selectedOrder = ordersArray[position]
                startActivity(
                    Intent(requireActivity(), ActivityOrderDetails::class.java)
                        .putExtra("orderId", MyApplication.selectedOrder!!.orderId!!.toInt())
                )
            }
        } else if (view.id == R.id.btAcceptOrder) {
            AppHelper.onOneClick {
                if (MyApplication.selectedUser!!.active == 1)
                    showAcceptOrderPopup(requireActivity(), position)
                else
                    AppHelper.createDialog(
                        requireActivity(),
                        AppHelper.getRemoteString("inactive_user_msg", requireActivity())
                    )

            }
        }
    }

    fun showAcceptOrderPopup(context: Activity, position: Int) {
        AppHelper.createYesNoDialog(
            context,
            AppHelper.getRemoteString("yes", context),
            AppHelper.getRemoteString("cancel", context),
            AppHelper.getRemoteString("sure_accept", context)
        ) {
            if (AppHelper.isOnline(requireActivity())) {
                acceptOrder(
                    ordersArray[position].orderId!!.toInt(),
                    ordersArray[position].total!!.toDouble()
                        .toInt() + ordersArray[position].shippingTotal!!.toDouble().toInt()
                )
            } else {
                AppHelper.createDialog(
                    requireActivity(),
                    AppHelper.getRemoteString("no_internet", requireContext())
                )
            }

        }
    }
}