package com.prototype.exam.ui.main.view.userDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.prototype.exam.App
import com.prototype.exam.R
import com.prototype.exam.data.model.forecast.ForecastItem
import com.prototype.exam.databinding.FragmentUserDetailBinding
import com.prototype.exam.ui.base.BaseFragment
import com.prototype.exam.ui.main.view.UserActivity
import com.prototype.exam.ui.main.viewModel.UserDetailViewModel
import com.prototype.exam.utils.Constants.BUNDLE_LOCATION_ID
import com.prototype.exam.utils.FormatterUtils.CURRENT_TEMPERATURE_FORMAT
import com.prototype.exam.utils.FormatterUtils.HI_TEMPERATURE_FORMAT
import com.prototype.exam.utils.FormatterUtils.LOW_TEMPERATURE_FORMAT
import com.prototype.exam.utils.FormatterUtils.getTemperature
import com.prototype.exam.utils.Status
import com.prototype.exam.utils.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserDetailFragment : BaseFragment(R.layout.fragment_user_detail) {

    @Inject
    lateinit  var viewModelFactory: ViewModelFactory
    lateinit  var viewModel: UserDetailViewModel
    private var viewBinding: FragmentUserDetailBinding? = null
    private val binding get() = viewBinding!!
    private var locationId: String? = null
    private lateinit var mMap : GoogleMap
    private var mapReady = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(UserDetailViewModel::class.java)
        viewBinding = FragmentUserDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        locationId = arguments?.getString(BUNDLE_LOCATION_ID)
       // viewModel.fetchForecast(locationId)
        setupUi()
        setupObservers()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as UserActivity).onShowBackButton(true)
    }

    override fun setupUi() {
        binding.btnFavorite.setOnClickListener {
         //   viewModel.onToggleFavorite(locationId, binding.btnFavorite.isChecked)
        }
    }

    override fun setupObservers() {
        viewModel.forecasts.observe(requireActivity(), {
            it?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        result.data?.let { response -> displayData(response) }
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

    private fun displayData(forecast: ForecastItem) {
        binding.textCity.text = forecast.name
        binding.textTemperature.text = resources.getString(
            R.string.unit_celsius,
            getTemperature(CURRENT_TEMPERATURE_FORMAT, forecast.main.temperature)
        )
        binding.textTempRange.text = resources.getString(
            R.string.detail_hi_low_temp,
            getTemperature(HI_TEMPERATURE_FORMAT, forecast.main.maxTemperature),
            getTemperature(LOW_TEMPERATURE_FORMAT, forecast.main.minTemperature)
        )
        binding.textWeather.text = forecast.weatherList[0].weather
        binding.btnFavorite.isChecked = forecast.favorite
    }

    override fun triggerErrorEvent(message: String) = lifecycleScope.launch {
        eventChannel.send(SingleEvent.ErrorEvent(message))
    }
}