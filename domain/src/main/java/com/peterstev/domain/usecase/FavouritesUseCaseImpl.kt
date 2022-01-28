package com.peterstev.domain.usecase

import com.peterstev.domain.model.User
import com.peterstev.domain.repository.FavouriteRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class FavouritesUseCaseImpl @Inject constructor(
    private val repository: FavouriteRepository,
) : FavouritesUseCase {

    override fun addFavourite(user: User): Single<Long> {
        return repository.addFavourite(user)
    }

    override fun getFavourites(): Flowable<List<User>> {
        return repository.getFavourites()
    }

    override fun getSingleFavourite(username: String): Single<User> {
        return repository.getSingleFavourite(username)
    }

    override fun deleteFavourite(user: User): Single<Int> {
        return repository.deleteFavourite(user)
    }

    override fun deleteAllFavourites(): Completable {
        return repository.deleteAllFavourites()
    }
}
