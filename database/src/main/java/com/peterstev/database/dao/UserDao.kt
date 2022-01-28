package com.peterstev.database.dao

import androidx.room.*
import com.peterstev.database.entity.UserEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavourite(user: UserEntity): Single<Long>

    @Query("SELECT * FROM users_table")
    fun getFavourites(): Flowable<List<UserEntity>>

    @Query("SELECT * FROM users_table WHERE login = :username")
    fun getSingleFavourite(username: String): Single<UserEntity>

    @Delete
    fun deleteFavourite(user: UserEntity): Single<Int>

    @Query("DELETE FROM users_table")
    fun deleteAllFavourites(): Completable
}
