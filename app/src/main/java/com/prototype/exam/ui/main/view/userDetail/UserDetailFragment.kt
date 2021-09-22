package com.prototype.exam.ui.main.view.userDetail

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.prototype.exam.App
import com.prototype.exam.R
import com.prototype.exam.data.model.Geo
import com.prototype.exam.data.model.User
import com.prototype.exam.databinding.FragmentUserDetailBinding
import com.prototype.exam.ui.base.BaseFragment
import com.prototype.exam.ui.main.view.user.UserActivity
import com.prototype.exam.ui.main.viewModel.UserDetailViewModel
import com.prototype.exam.utils.Constants.BUNDLE_USER_ID_KEY
import com.prototype.exam.utils.Constants.MAP_ZOOM_LEVEL
import com.prototype.exam.utils.Status
import com.prototype.exam.utils.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject


class UserDetailFragment : BaseFragment(R.layout.fragment_user_detail),
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: UserDetailViewModel
    private var viewBinding: FragmentUserDetailBinding? = null
    private val binding get() = viewBinding!!
    private var userId: String? = null
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
        userId = arguments?.getString(BUNDLE_USER_ID_KEY)
        userId?.let { viewModel.fetchData(it.toInt()) }

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
        Snackbar.make(binding.root, "Location Permission Denied!", Snackbar.LENGTH_SHORT).show()
        updateMap()
    }

    override fun setupUi() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            map = googleMap
            setupMap()
            updateMap()
        }
    }

    private fun setupMap() {
        isMapReady = true
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            map.isMyLocationEnabled = true
        }
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isMapToolbarEnabled = false
        map.uiSettings.isMyLocationButtonEnabled = true
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
                val cu = CameraUpdateFactory.newLatLngZoom(location, MAP_ZOOM_LEVEL)
                map.animateCamera(cu)
            }
        }
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