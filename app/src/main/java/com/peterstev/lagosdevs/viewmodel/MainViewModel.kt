package com.peterstev.lagosdevs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peterstev.domain.model.GithubUsers
import com.peterstev.domain.model.User
import com.peterstev.domain.usecase.FavouritesUseCase
import com.peterstev.domain.usecase.UserUseCase
import com.peterstev.lagosdevs.fragment.MainFragmentDirections
import com.peterstev.lagosdevs.routing.Router
import com.peterstev.lagosdevs.util.RxSchedulers
import com.peterstev.lagosdevs.util.thread
import com.peterstev.lagosdevs.viewmodel.MainViewModel.UsersState.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val router: Router,
    private val schedulers: RxSchedulers,
) : ViewModel() {

    private val mutableUsersLiveData: MutableLiveData<UsersState> = MutableLiveData()
    val usersLiveData: LiveData<UsersState> = mutableUsersLiveData

    private val disposable by lazy { CompositeDisposable() }
    private var page = 1
    sealed class UsersState {
        class Success(val data: GithubUsers) : UsersState()
        class Error(val throwable: Throwable) : UsersState()
        class Loading(val isLoading: Boolean) : UsersState()
    }

    fun fetchUsers() {
        val subscribe = userUseCase
            .getUsers(page)
            .thread(schedulers)
            .doOnSubscribe { updateState(Loading(true)) }
            .map<UsersState> { Success(it) }
            .subscribe(this::updateState) {
                updateState(Error(it))
            }
        disposable.add(subscribe)
    }

    fun loadMore() {
        val subscribe = userUseCase
            .getUsers(page)
            .thread(schedulers)
            .map<UsersState> { Success(it) }
            .subscribe(this::updateState) {
                updateState(Error(it))
            }
        disposable.add(subscribe)
    }

    fun incrementPage(){
        page++
    }

    fun getPage() = page

    fun toDetails(user: User) {
        router.navigate(MainFragmentDirections.toDetails(user))
    }

    fun toFavourites() {
        router.navigate(MainFragmentDirections.toFavorites())
    }

    private fun updateState(state: UsersState) {
        mutableUsersLiveData.value = state
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
