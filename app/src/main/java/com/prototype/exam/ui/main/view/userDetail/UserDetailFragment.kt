package com.prototype.exam.ui.main.view.userDetail

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.prototype.exam.App
import com.prototype.exam.R
import com.prototype.exam.data.model.Geo
import com.prototype.exam.data.model.User
import com.prototype.exam.databinding.FragmentUserDetailBinding
import com.prototype.exam.ui.base.BaseFragment
import com.prototype.exam.ui.main.view.UserActivity
import com.prototype.exam.ui.main.viewModel.UserDetailViewModel
import com.prototype.exam.utils.Constants.BUNDLE_USER_ID_KEY
import com.prototype.exam.utils.Status
import com.prototype.exam.utils.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.annotation.SuppressLint

import android.location.LocationManager





class UserDetailFragment : BaseFragment(R.layout.fragment_user_detail),
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: UserDetailViewModel
    private var viewBinding: FragmentUserDetailBinding? = null
    private val binding get() = viewBinding!!
    private var locationId: String? = null
    private lateinit var map: GoogleMap
    private var isMapReady = false
    private var geo: Geo? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(UserDetailViewModel::class.java)
        viewBinding = FragmentUserDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        locationId = arguments?.getString(BUNDLE_USER_ID_KEY)
        locationId?.let { viewModel.fetchData(it.toInt()) }

        setupUi()
        setupObservers()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as UserActivity).onShowBackButton(true)
    }

    override fun setupUi() {
        setupMap()
    }

    private fun setupMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            map = googleMap
            isMapReady = true
            updateMap()
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
//        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        val locationProvider = LocationManager.NETWORK_PROVIDER
//        // I suppressed the missing-permission warning because this wouldn't be executed in my
//        // case without location services being enabled
//        // I suppressed the missing-permission warning because this wouldn't be executed in my
//        // case without location services being enabled
//        @SuppressLint("MissingPermission") val lastKnownLocation =
//            locationManager.getLastKnownLocation(locationProvider)
//        val userLat = lastKnownLocation!!.latitude
//        val userLong = lastKnownLocation!!.longitude
        return false
    }

    override fun onMyLocationClick(p0: Location) {

    }

    private fun updateMap() {
        if (isMapReady) {
            geo?.let {
                val sydney = LatLng(it.lat.toDouble(), it.lng.toDouble())

                map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
                val builder = LatLngBounds.Builder()

                builder.include(sydney)


                val bounds = builder.build()

                val width = resources.displayMetrics.widthPixels
                val height = resources.displayMetrics.heightPixels
                val padding = (width * 0.20).toInt()


                val cameraUpdate =
                    CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
                // map.setLatLngBoundsForCameraTarget(cameraUpdate)
                map.isMyLocationEnabled = true
                map.setOnMyLocationButtonClickListener(this)
                map.setOnMyLocationClickListener(this)
                map.uiSettings.isZoomControlsEnabled = true
                map.uiSettings.isMapToolbarEnabled = false
                map.uiSettings.isMyLocationButtonEnabled = true
                map.animateCamera(cameraUpdate)
            }
        }
    }

    override fun setupObservers() {
        viewModel.user.observe(requireActivity(), {
            it?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        result.data?.let { displayData(it) }
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        //Do nothing
                    }
                }
            }
        })
    }

    private fun displayData(user: User) {
        binding.textCity.text = user.name
        binding.textTemperature.text = user.username
        binding.textTempRange.text = user.company.catchPhrase
        binding.textWeather.text = user.address.suite
        geo = user.address.geo
        updateMap()
    }

    override fun triggerErrorEvent(message: String) = lifecycleScope.launch {
        eventChannel.send(SingleEvent.ErrorEvent(message))
    }
}