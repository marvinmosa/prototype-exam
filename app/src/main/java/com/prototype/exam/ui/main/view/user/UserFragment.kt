package com.prototype.exam.ui.main.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.prototype.exam.App
import com.prototype.exam.R
import com.prototype.exam.data.model.User
import com.prototype.exam.databinding.FragmentUserBinding
import com.prototype.exam.ui.base.BaseFragment
import com.prototype.exam.ui.main.adapter.MainAdapter
import com.prototype.exam.ui.main.viewModel.UserViewModel
import com.prototype.exam.utils.Constants.BUNDLE_USER_ID_KEY
import com.prototype.exam.utils.Status
import com.prototype.exam.utils.ViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserFragment : BaseFragment(), MainAdapter.OnItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: UserViewModel
    private lateinit var adapter: MainAdapter

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private var userDetailFragmentContainer: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)
        _binding = FragmentUserBinding.inflate(inflater, container, false)

        setupUi()
        setupObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDetailFragmentContainer = view.findViewById(R.id.user_detail_nav_container)

    }

    override fun setupUi() {
        binding.swipeRefresh.setOnRefreshListener { viewModel.fetchUsers() }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MainAdapter(arrayListOf(), this)

        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        divider.setDrawable(getDrawable(requireContext(), R.drawable.item_layout_divider)!!)
        binding.recyclerView.addItemDecoration(divider)
        binding.recyclerView.adapter = adapter
    }

    override fun setupObservers() {
        viewModel.getLiveLocalUsers().observe(requireActivity(), {
            retrieveList(it)
        })

        viewModel.users.observe(requireActivity(), { it ->
            it?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        binding.swipeRefresh.isRefreshing = false
                        result.data?.let { response -> retrieveList(response) }
                    }
                    Status.ERROR -> {
                        binding.swipeRefresh.isRefreshing = false
                        it.message?.let {
                            triggerErrorEvent(it)
                        }

                    }
                    Status.LOADING -> {
                        binding.swipeRefresh.isRefreshing = true
                    }
                }
            }
        })

        lifecycleScope.launchWhenStarted {
            eventFlow.collect { event ->
                when (event) {
                    is SingleEvent.ErrorEvent -> {
                        binding.swipeRefresh.isRefreshing = false
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun retrieveList(users: List<User>) {
        adapter.apply {
            addUsers(users)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onItemClick(position: Int) {
        val user = adapter.getItem(position)
        val bundle = Bundle()

        bundle.putString(BUNDLE_USER_ID_KEY, user.id.toString())

        if (userDetailFragmentContainer != null) {
            userDetailFragmentContainer?.findNavController()?.navigate(
                R.id.UserDetailFragment,
                bundle
            )
        } else {
            findNavController().navigate(
                R.id.action_show_user_detail,
                bundle
            )
        }
    }

    override fun triggerErrorEvent(message: String) = lifecycleScope.launch {
        eventChannel.send(SingleEvent.ErrorEvent(message))
    }
}
