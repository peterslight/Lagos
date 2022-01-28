package com.peterstev.lagosdevs.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.peterstev.domain.model.User
import com.peterstev.lagosdevs.R
import com.peterstev.lagosdevs.databinding.FragmentDetailBinding
import com.peterstev.lagosdevs.injection.FragmentComponent
import com.peterstev.lagosdevs.viewmodel.DetailViewModel
import java.util.*
import javax.inject.Inject

class DetailFragment : Fragment() {

    @Inject
    lateinit var viewModel: DetailViewModel
    lateinit var binding: FragmentDetailBinding
    lateinit var userArgs: User

    companion object {
        private const val PROGRESS_VIEW = 0
        private const val ITEM_VIEW = 1
        private const val USER_KEY = "user"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        FragmentComponent.create(this).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDisplayedView(PROGRESS_VIEW)

        userArgs = requireArguments().getSerializable(USER_KEY) as User

        observeUserState()
        observeFavouriteState()
        viewModel.fetchUser(userArgs.login)
    }

    private fun observeUserState() {
        viewModel.userLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is DetailViewModel.UserState.Error -> {
                    setupViews(userArgs)
                    setDisplayedView(ITEM_VIEW)
                }
                is DetailViewModel.UserState.Loading ->
                    if (it.isLoading) setDisplayedView(PROGRESS_VIEW)
                is DetailViewModel.UserState.Success -> {
                    setupViews(it.data)
                    setDisplayedView(ITEM_VIEW)
                }
            }
        }
    }

    private fun observeFavouriteState() {
        viewModel.favLiveData.observe(viewLifecycleOwner) {
            when (it) {
                DetailViewModel.FavouriteState.Failure ->
                    showToast(getString(R.string.unable_to_favourite, userArgs.login))
                DetailViewModel.FavouriteState.Success ->
                    showToast(getString(R.string.added_to_favourite, userArgs.login))
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setDisplayedView(position: Int) {
        binding.root.displayedChild = position
    }

    private fun setupViews(user: User) {
        with(binding) {
            Glide.with(avatar).load(user.avatarUrl).into(avatar)
            Glide.with(backdrop).load(user.avatarUrl).into(backdrop)
            name.text = when {
                user.name.isNotEmpty() -> user.name
                else -> user.login.replaceFirstChar {
                    if (it.isLowerCase())
                        it.titlecase(Locale.getDefault())
                    else it.toString()
                }
            }
            username.text = getString(R.string.user_name, user.login)
            btGithub.setOnClickListener { viewModel.toBrowser(user.htmlUrl) }
            btFavourite.setOnClickListener { viewModel.addFavourite(user) }
        }
    }
}
