package com.prototype.exam.ui.main.view.userDetail

import android.Manifest
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
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager

import android.location.LocationManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.maps.CameraUpdate
import com.prototype.exam.utils.Constants.MAP_ZOOM_LEVEL


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

    override fun onUserGranted() {
        setupMap()
        updateMap()
    }

    override fun onUserDenied() {
        updateMap()
    }

    override fun setupUi() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            map = googleMap
            map.setOnMyLocationButtonClickListener(this)
            map.setOnMyLocationClickListener(this)
            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isMapToolbarEnabled = false
            map.uiSettings.isMyLocationButtonEnabled = true
            setupMap()
            updateMap()
        }
    }

    private fun setupMap() {
        isMapReady = true
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            map.isMyLocationEnabled = true
//            val locationManager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
//            val locationProvider = LocationManager.NETWORK_PROVIDER
//            val lastKnownLocation =
//                locationManager.getLastKnownLocation(locationProvider)
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        //Do nothing
    }

    private fun updateMap() {
        if (isMapReady) {
            geo?.let {
                val location = LatLng(it.lat.toDouble(), it.lng.toDouble())
                map.addMarker(MarkerOptions().position(location))
//                val builder = LatLngBounds.Builder()
//                builder.include(LatLng(-35.0, 138.58))
//                map.addMarker(MarkerOptions().position(LatLng(-35.0, 138.58)).title("Marker in Sydney"))
//                builder.include(sydney)
//                map.animateCamera(createCameraUpdate(builder.build()))

                val cu = CameraUpdateFactory.newLatLngZoom(location, MAP_ZOOM_LEVEL)
                map.animateCamera(cu)
            }
        }
    }

    private fun createCameraUpdate(bounds: LatLngBounds): CameraUpdate {
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val padding = (width * 0.20).toInt()
        return CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
    }

    override fun setupObservers() {
        viewModel.user.observe(requireActivity(), {
            it?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        result.data?.let { data -> displayData(data) }
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