package com.peterstev.lagosdevs.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.peterstev.domain.model.User
import com.peterstev.lagosdevs.R
import com.peterstev.lagosdevs.adapter.UserAdapter
import com.peterstev.lagosdevs.databinding.FragmentListMainBinding
import com.peterstev.lagosdevs.injection.FragmentComponent
import com.peterstev.lagosdevs.util.printLog
import com.peterstev.lagosdevs.viewmodel.MainViewModel
import javax.inject.Inject

class MainFragment : Fragment(), UserAdapter.OnItemClickListener {

    @Inject
    lateinit var viewModel: MainViewModel
    private val userAdapter: UserAdapter by lazy { UserAdapter(mutableListOf(), this, binding.recyclerView) }
    lateinit var binding: FragmentListMainBinding
    var totalItems = 0

    companion object {
        private const val PROGRESS_VIEW = 0
        private const val ITEM_VIEW = 1
        private const val ERROR_VIEW = 2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return if (::binding.isInitialized)
            binding.root
        else {
            binding = FragmentListMainBinding.inflate(layoutInflater)
            binding.root
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        FragmentComponent.create(this).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDisplayedView(PROGRESS_VIEW)
        setupViews()
        observeUserState()
        Toast.makeText(context, "size == ${userAdapter.itemCount}", Toast.LENGTH_SHORT).show()
        viewModel.fetchUsers()
    }

    private fun setDisplayedView(position: Int) {
        binding.root.displayedChild = position
    }

    private fun observeUserState() {
        viewModel.usersLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is MainViewModel.UsersState.Error -> {
                    showErrorView(it.throwable.message.toString())
                    setDisplayedView(ERROR_VIEW)
                }
                is MainViewModel.UsersState.Loading -> if (it.isLoading) setDisplayedView(PROGRESS_VIEW)
                is MainViewModel.UsersState.Success -> {
                    when {
                        it.data.users.isNotEmpty() -> {
                            totalItems = it.data.totalCount
                            userAdapter.updateList(it.data.users.toMutableList()).apply {
                                userAdapter.stopLoading()
                                viewModel.incrementPage()
                            }
                            setDisplayedView(ITEM_VIEW)
                        }
                        else -> {
                            showErrorView(getString(R.string.empty_users))
                            setDisplayedView(ERROR_VIEW)
                        }
                    }
                }
            }
        }
    }

    private fun setupViews() {
        with(binding) {
            favouritesFab.show()
            favouritesFab.setOnClickListener { viewModel.toFavourites() }
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = userAdapter
        }
    }

    private fun showErrorView(errorMessage: String) {
        with(binding.errorView) {
            message.text = errorMessage
            btRetry.setOnClickListener { viewModel.fetchUsers() }
        }
    }

    override fun onItemClick(user: User) {
        viewModel.toDetails(user)
    }

    override fun loadMore() {
        if (morePagesAvailable()) {
            printLog(viewModel.getPage())
            viewModel.loadMore()
        } else Toast.makeText(requireContext(), getString(R.string.caught_up), Toast.LENGTH_SHORT).show()
    }

    private fun morePagesAvailable(): Boolean {
        val totalPages = totalItems / 30
        return totalPages >= viewModel.getPage()
    }
}
