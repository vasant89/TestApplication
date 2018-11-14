package com.test.testapplication.main.searchplace

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import com.test.testapplication.R
import com.test.testapplication.adapters.PlaceAutoCompleteAdapter
import com.test.testapplication.adapters.PlaceRvAdapter
import com.test.testapplication.databinding.SearchPlaceViewBinding
import com.test.testapplication.di.scope.ActivityScoped
import com.test.testapplication.extentions.hideKeyBoard
import com.test.testapplication.location.PermissionHelper
import com.test.testapplication.main.MainActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import kotlinx.android.synthetic.main.content_search_place.view.*
import javax.inject.Inject

@ActivityScoped
class SearchPlaceView
@Inject
constructor() : DaggerFragment(),
    OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    @Inject
    lateinit var picasso: Picasso

    lateinit var mBinding: SearchPlaceViewBinding
    private var mMap: GoogleMap? = null
    var sheetBehavior: BottomSheetBehavior<*>? = null
    private var googleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectGoogleApiClient()
    }


    private fun connectGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(activity!!)
            .addApi(Places.GEO_DATA_API)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()

        googleApiClient?.connect()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = SearchPlaceViewBinding.inflate(inflater, container, false).apply {
            this.viewModel = (activity as MainActivity).obtainSearchPlaceViewModel()

            this.bottomSheet.rvPlace.apply {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                itemAnimator = DefaultItemAnimator()
                adapter = PlaceRvAdapter(picasso)
            }

        }

        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment)
            .getMapAsync(this)

        sheetBehavior = BottomSheetBehavior.from(mBinding.bottomSheet.myPlaceList)

        sheetBehavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        activity?.title = getString(R.string.nearby_place)
                        mBinding.root.tvShowList.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        activity?.title = getString(R.string.place_search_hint)
                        mBinding.root.tvShowList.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mBinding.viewModel?.apply {
            start()

            showToastMessage.observe(this@SearchPlaceView, Observer {
                Toast.makeText(context, "$it", Toast.LENGTH_LONG).show()
            })

            showPlaceListEvent.observe(this@SearchPlaceView, Observer {
                sheetBehavior?.apply {
                    state = if (state != BottomSheetBehavior.STATE_EXPANDED) {
                        BottomSheetBehavior.STATE_EXPANDED
                    } else {
                        BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
            })

            mBinding.bottomSheet.rvPlace.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    if (!isLoading.get() && !nextPageToken.isNullOrEmpty()) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                            getPlaceDetailNextPage()
                        }
                    }
                }
            })

            replacePlacesEvent.observe(this@SearchPlaceView, Observer {
                (mBinding.bottomSheet.rvPlace.adapter as PlaceRvAdapter).apply {
                    replaceList(it!!)
                }
            })
            addPlacesEvent.observe(this@SearchPlaceView, Observer {
                (mBinding.bottomSheet.rvPlace.adapter as PlaceRvAdapter).apply {
                    addList(it!!)
                }
            })

        }
    }

    var mLocationRequest: LocationRequest? = null
    var mLastLocation: Location? = null

    var mFusedLocationProviderClient: FusedLocationProviderClient? = null

    var showCurrentLocation = true

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mBinding.viewModel?.apply {
            latLng?.let {
                showLocationOnMap(it)
            }
        }
    }

    override fun onConnected(p0: Bundle?) {
        Log.e(TAG, "Connected")
        mBinding.contentSearchPlace.tvSearchPlace.apply {

            threshold = 3

            setAdapter(
                PlaceAutoCompleteAdapter(
                    context,
                    googleApiClient,
                    LatLngBounds(LatLng(-85.0, 180.0), LatLng(85.0, -180.0)),
                    null
                )
            )
            onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, position, _ ->
                (adapterView.adapter as PlaceAutoCompleteAdapter).getItem(position)?.let {
                    Places
                        .GeoDataApi
                        .getPlaceById(googleApiClient!!, it.placeId)
                        .setResultCallback { placeBuffer ->
                            val place = placeBuffer[0]
                            val latLng = place.latLng
                            showCurrentLocation = false
                            showLocationOnMap(latLng)
                            Log.e(TAG, "Lat" + latLng.latitude + " lng " + latLng.longitude)
                            placeBuffer.release()
                        }
                }
                hideKeyBoard()
            }
        }
        activity?.let {
            init(15000)
        }
    }

    @SuppressLint("MissingPermission")
    private fun init(milliSeconds: Long) {

        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = milliSeconds
        mLocationRequest!!.fastestInterval = milliSeconds
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)

        PermissionHelper().checkPermission(
            activity!!,
            Manifest.permission.ACCESS_FINE_LOCATION,
            getString(R.string.grant_permission_get_current_location),
            object : PermissionHelper.PermissionInterface {
                override fun granted(granted: Boolean) {
                    if (granted) {
                        requestLocationUpdate()
                    }
                }
            }
        )
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdate() {
        mFusedLocationProviderClient?.requestLocationUpdates(
            mLocationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    mLastLocation = locationResult?.lastLocation
                    if (showCurrentLocation) {
                        showLocationOnMap(LatLng(mLastLocation!!.latitude, mLastLocation!!.longitude))
                    }
                    Log.e(
                        TAG,
                        mLastLocation!!.latitude.toString()
                                + "" + mLastLocation!!.longitude.toString()
                    )
                }
            },
            Looper.getMainLooper()
        )
    }

    private val mMarkers: MutableList<Marker> = mutableListOf()

    private fun showLocationOnMap(latLng: LatLng?) {
        // 5 mile == 8046.72 mtr
        latLng?.let {
            mBinding.viewModel?.getPlaceDetails(it)

            mMap?.apply {

                mMarkers.forEach { marker ->
                    marker.remove()
                }

                mMarkers.add(
                    addMarker(
                        MarkerOptions()
                            .position(it)
                    )
                )
                moveCamera(CameraUpdateFactory.newLatLng(it))
                animateCamera(CameraUpdateFactory.newLatLngZoom(it, 17.0f))

            }
        }

    }

    override fun onConnectionSuspended(p0: Int) {
        Log.e(TAG, "Suspended")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.e(TAG, "Failed")
    }

    companion object {
        private val TAG = SearchPlaceView::class.java.simpleName
    }
}