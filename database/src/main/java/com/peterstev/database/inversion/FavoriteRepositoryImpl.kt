package com.peterstev.database.inversion

import com.peterstev.database.dao.UserDao
import com.peterstev.database.transform.UserEntityMapper
import com.peterstev.domain.model.User
import com.peterstev.domain.repository.FavouriteRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val entityMapper: UserEntityMapper,
) : FavouriteRepository {

    override fun addFavourite(user: User): Single<Long> {
        val favourite = updateFavourite(user)
        return userDao.addFavourite(entityMapper.transform(favourite))
    }

    override fun getFavourites(): Flowable<List<User>> {
        return userDao.getFavourites()
            .map { entityMapper.transform(it) }
    }

    override fun getSingleFavourite(username: String): Single<User> {
        return userDao.getSingleFavourite(username)
            .map { entityMapper.transform(it) }
    }

    override fun deleteFavourite(user: User): Single<Int> {
        return userDao.deleteFavourite(entityMapper.transform(user))
    }

    override fun deleteAllFavourites(): Completable {
        return userDao.deleteAllFavourites()
    }

    private fun updateFavourite(user: User): User {
        user.isFavourite = true
        return user
    }
}
