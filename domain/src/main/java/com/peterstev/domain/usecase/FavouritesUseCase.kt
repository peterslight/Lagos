package com.peterstev.domain.usecase

import com.peterstev.domain.model.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface FavouritesUseCase {

    fun addFavourite(user: User): Single<Long>

    fun getFavourites(): Flowable<List<User>>

    fun getSingleFavourite(username: String): Single<User>

    fun deleteFavourite(user: User): Single<Int>

    fun deleteAllFavourites(): Completable
}
