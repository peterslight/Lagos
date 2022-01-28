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
import com.peterstev.lagosdevs.adapter.FavouriteAdapter
import com.peterstev.lagosdevs.databinding.FragmentListFavouriteBinding
import com.peterstev.lagosdevs.injection.FragmentComponent
import com.peterstev.lagosdevs.viewmodel.FavouriteViewModel
import javax.inject.Inject

class FavouriteFragment : Fragment(), FavouriteAdapter.OnItemClickListener {

    @Inject
    lateinit var viewModel: FavouriteViewModel
    private val favAdapter = FavouriteAdapter(mutableListOf(), this)
    lateinit var binding: FragmentListFavouriteBinding

    companion object {
        private const val PROGRESS_VIEW = 0
        private const val ITEM_VIEW = 1
        private const val ERROR_VIEW = 2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentListFavouriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        FragmentComponent.create(this).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDisplayedView(PROGRESS_VIEW)
        setupViews()
        observeFavouriteState()
        viewModel.getFavourites()
    }

    private fun setDisplayedView(position: Int) {
        binding.root.displayedChild = position
    }

    private fun observeFavouriteState() {
        viewModel.favouriteLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavouriteViewModel.FavouriteState.Loading ->
                    if (state.isLoading) setDisplayedView(PROGRESS_VIEW)
                is FavouriteViewModel.FavouriteState.Error -> {
                    showErrorView(state.throwable.toString())
                    setDisplayedView(ERROR_VIEW)
                }
                is FavouriteViewModel.FavouriteState.Success<*> -> {
                    setDisplayedView(ITEM_VIEW)
                    when (state.data) {
                        is List<*> -> {
                            val list = state.data as List<User>
                            if (list.isNotEmpty())
                                favAdapter.updateList(list.toMutableList())
                            else {
                                showErrorView(getString(R.string.fav_empty))
                                setDisplayedView(ERROR_VIEW)
                            }
                        }
                        else -> {
                            Toast.makeText(requireContext(), state.data.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupViews() {
        with(binding) {
            favouritesFab.setOnClickListener { viewModel.deleteAllFavourites() }
            favouritesFab.setOnClickListener { viewModel.deleteAllFavourites() }
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = favAdapter
        }
    }

    private fun showErrorView(errorMessage: String) {
        with(binding.errorView) {
            message.text = errorMessage
            btRetry.setOnClickListener { viewModel.getFavourites() }
        }
    }

    override fun onItemClick(user: User) {
        viewModel.toDetailScreen(user)
    }

    override fun onRemoveFavourite(user: User) {
        viewModel.deleteFavourite(user)
    }
}
