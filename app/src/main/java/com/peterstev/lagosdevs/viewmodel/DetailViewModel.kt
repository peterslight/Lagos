package com.peterstev.lagosdevs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peterstev.domain.model.User
import com.peterstev.domain.usecase.FavouritesUseCase
import com.peterstev.domain.usecase.UserUseCase
import com.peterstev.lagosdevs.routing.Router
import com.peterstev.lagosdevs.util.RxSchedulers
import com.peterstev.lagosdevs.util.thread
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val favouritesUseCase: FavouritesUseCase,
    private val router: Router,
    private val schedulers: RxSchedulers,
) : ViewModel() {

    private val mutableUserLiveData: MutableLiveData<UserState> = MutableLiveData()
    val userLiveData: LiveData<UserState> = mutableUserLiveData

    private val mutableFavLiveData: MutableLiveData<FavouriteState> = MutableLiveData()
    val favLiveData: LiveData<FavouriteState> = mutableFavLiveData

    private val disposable by lazy { CompositeDisposable() }

    sealed class UserState {
        class Success(val data: User) : UserState()
        object Error : UserState()
        class Loading(val isLoading: Boolean) : UserState()
    }

    sealed class FavouriteState {
        object Success : FavouriteState()
        object Failure : FavouriteState()
    }

    fun fetchUser(username: String) {
        val subscribe = userUseCase
            .getUser(username)
            .thread(schedulers)
            .doOnSubscribe { updateState(UserState.Loading(true)) }
            .map { UserState.Success(it) }
            .subscribe(this::updateState) {
                updateState(UserState.Error)
                getSingleFavourite(username)
            }
        disposable.add(subscribe)
    }

    private fun getSingleFavourite(username: String) {
        val subscribe = favouritesUseCase
            .getSingleFavourite(username)
            .thread(schedulers)
            .map { UserState.Success(it) }
            .subscribe(this::updateState) {
                updateState(UserState.Error)
            }
        disposable.add(subscribe)
    }

    fun addFavourite(user: User) {
        val subscribe = favouritesUseCase
            .addFavourite(user)
            .thread(schedulers)
            .map {
                if (it > -1) FavouriteState.Success
                else FavouriteState.Failure
            }
            .subscribe(this::updateFavouriteState) {
                updateFavouriteState(FavouriteState.Failure)
            }
        disposable.add(subscribe)
    }

    fun toBrowser(url: String) {
        router.toBrowser(url)
    }

    private fun updateState(state: UserState) {
        mutableUserLiveData.value = state
    }

    private fun updateFavouriteState(state: FavouriteState) {
        mutableFavLiveData.value = state
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
