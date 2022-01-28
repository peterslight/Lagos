package com.peterstev.lagosdevs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peterstev.domain.model.User
import com.peterstev.domain.usecase.FavouritesUseCase
import com.peterstev.lagosdevs.fragment.FavouriteFragmentDirections
import com.peterstev.lagosdevs.fragment.MainFragmentDirections
import com.peterstev.lagosdevs.routing.Router
import com.peterstev.lagosdevs.util.RxSchedulers
import com.peterstev.lagosdevs.util.printLog
import com.peterstev.lagosdevs.util.thread
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject
import kotlin.concurrent.thread

class FavouriteViewModel @Inject constructor(
    private val favouritesUseCase: FavouritesUseCase,
    private val router: Router,
    private val schedulers: RxSchedulers,
) : ViewModel() {

    private val mutableFavouriteLiveData: MutableLiveData<FavouriteState> = MutableLiveData()
    val favouriteLiveData: LiveData<FavouriteState> = mutableFavouriteLiveData

    private val disposable by lazy { CompositeDisposable() }

    sealed class FavouriteState {
        class Success<T>(val data: T? = null) : FavouriteState()
        class Loading(val isLoading: Boolean) : FavouriteState()
        class Error(val throwable: Throwable) : FavouriteState()
    }

    fun getSingleFavourite(username: String) {
        val subscribe = favouritesUseCase
            .getSingleFavourite(username)
            .thread(schedulers)
            .map { FavouriteState.Success(it) }
            .subscribe(this::updateFavouriteState) {
                updateFavouriteState(FavouriteState.Error(it))
            }
        disposable.add(subscribe)
    }

    //doesnt not work yet, test in fav page
    fun deleteFavourite(user: User) {
        val subscribe = favouritesUseCase
            .deleteFavourite(user)
            .thread(schedulers)
            .map {
                if (it > -1) FavouriteState.Success("Success")
                else FavouriteState.Success("failure")
            }
            .subscribe(this::updateFavouriteState) {
                updateFavouriteState(FavouriteState.Error(it))
            }
        disposable.add(subscribe)
    }

    fun deleteAllFavourites() {
        val subscribe = favouritesUseCase
            .deleteAllFavourites()
            .thread(schedulers)
            .doOnSubscribe { updateFavouriteState(FavouriteState.Loading(true)) }
            .doOnComplete {
                updateFavouriteState(
                    FavouriteState.Success("Success")
                )
            }
            .onErrorComplete {
                updateFavouriteState(FavouriteState.Error(it))
                true
            }
            .subscribe()
        disposable.add(subscribe)
    }

    fun getFavourites() {
        val subscribe = favouritesUseCase
            .getFavourites()
            .thread(schedulers)
            .map { FavouriteState.Success(it) }
            .doOnSubscribe { updateFavouriteState(FavouriteState.Loading(true)) }
            .subscribe(this::updateFavouriteState) {
                updateFavouriteState(FavouriteState.Error(it))
            }
        disposable.add(subscribe)
    }

    fun toDetailScreen(user: User) {
        router.navigate(FavouriteFragmentDirections.toDetails(user))
    }

    private fun updateFavouriteState(state: FavouriteState) {
        mutableFavouriteLiveData.value = state
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
