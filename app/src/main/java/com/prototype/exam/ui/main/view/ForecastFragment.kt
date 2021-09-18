package com.prototype.exam.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.prototype.exam.App
import com.prototype.exam.R
import com.prototype.exam.data.model.ForecastItem
import com.prototype.exam.databinding.FragmentForecastBinding
import com.prototype.exam.di.module.ViewModelFactory
import com.prototype.exam.ui.base.BaseFragment
import com.prototype.exam.ui.base.BaseViewModel
import com.prototype.exam.ui.main.adapter.MainAdapter
import com.prototype.exam.ui.main.viewModel.ForecastViewModel
import com.prototype.exam.utils.Constants.BUNDLE_LOCATION_ID
import com.prototype.exam.utils.Status
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ForecastFragment : BaseFragment(R.layout.fragment_forecast), MainAdapter.OnItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ForecastViewModel
    private lateinit var adapter: MainAdapter

    private var viewBinding: FragmentForecastBinding? = null
    private val binding get() = viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ForecastViewModel::class.java)
        viewBinding = FragmentForecastBinding.inflate(inflater, container, false)
        val view = binding.root
        setupUi()
        setupObservers()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).onShowBackButton(false)
    }

    override fun setupUi() {
        binding.swipeRefresh.setOnRefreshListener { viewModel.fetchForecasts() }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MainAdapter(arrayListOf(), this)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerView.context,
                (binding.recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding.recyclerView.adapter = adapter
    }

    override fun setupObservers() {
        viewModel.getLiveLocalForecasts().observe(requireActivity(), {
            retrieveList(it)
        })

        viewModel.forecasts.observe(requireActivity(), {
            it?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        binding.swipeRefresh.isRefreshing = false
                        result.data?.let { response -> retrieveList(response) }
                    }
//                    Status.ERROR -> {
//                        binding.swipeRefresh.isRefreshing = false
//                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
//                    }
//                    Status.LOADING -> {
//                        binding.swipeRefresh.isRefreshing = true
//                    }
                    else -> binding.swipeRefresh.isRefreshing = true
                }
            }
        })

        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is BaseViewModel.SingleEvent.ErrorEvent -> {
                        binding.swipeRefresh.isRefreshing = false
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun retrieveList(forecasts: List<ForecastItem>) {
        adapter.apply {
            addUsers(forecasts)
            notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }


    override fun onItemClick(position: Int) {
        val forecast = adapter.getItem(position)
        val bundle = Bundle()
        bundle.putString(BUNDLE_LOCATION_ID, forecast.id)
        findNavController().navigate(
            R.id.action_ForecastFragment_to_ForecastDetailFragment,
            bundle
        )
    }
}
