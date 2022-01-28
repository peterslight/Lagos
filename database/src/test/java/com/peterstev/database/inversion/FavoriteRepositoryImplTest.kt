package com.peterstev.database.inversion

import com.peterstev.database.dao.UserDao
import com.peterstev.database.entity.UserEntity
import com.peterstev.database.transform.UserEntityMapper
import com.peterstev.domain.model.User
import com.peterstev.domain.repository.FavouriteRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class FavoriteRepositoryImplTest : Spek({

    describe("FavoriteRepository") {
        val mapper: UserEntityMapper by memoized { mockk() }
        val userDao: UserDao by memoized { mockk() }
        lateinit var repository: FavouriteRepository
        beforeEachTest {
            repository = FavoriteRepositoryImpl(userDao, mapper)
        }

        describe("adding a favourite") {
            val userEntity = mockk<UserEntity>()
            val user = mockk<User>()
            var result: Single<Long>? = null
            beforeEachTest {
                every { mapper.transform(user) } returns userEntity
                every { userDao.addFavourite(userEntity) } returns Single.just(1)
            }

            describe("addFavourite") {
                beforeEachTest {
                    result = repository.addFavourite(user)
                }

                it("should not be null") {
                    assertThat(result?.blockingGet()).isNotNull()
                }

                it("should contain the correct values") {
                    assertThat(result?.blockingGet()).isEqualTo(1)
                }
            }
        }

        describe("deleting a favourite") {
            val userEntity = mockk<UserEntity>()
            val user = mockk<User>()
            var result: Single<Int>? = null
            beforeEachTest {
                every { mapper.transform(user) } returns userEntity
                every { userDao.deleteFavourite(userEntity) } returns Single.just(1)
            }

            describe("deleteFavourite") {
                beforeEachTest {
                    result = repository.deleteFavourite(user)
                }

                it("should not be null") {
                    assertThat(result?.blockingGet()).isNotNull()
                }

                it("should contain the correct values") {
                    assertThat(result?.blockingGet()).isEqualTo(1)
                }
            }
        }

        describe("getting a single favourite") {
            val userEntity = mockk<UserEntity>()
            val user = mockk<User>()
            var result: Single<User>? = null
            val username = "peterstev"
            beforeEachTest {
                every { mapper.transform(userEntity) } returns user
                every { userDao.getSingleFavourite(username) } returns Single.just(userEntity)
            }

            describe("getSingleFavourite") {
                beforeEachTest {
                    result = repository.getSingleFavourite(username)
                }

                it("should not be null") {
                    assertThat(result?.blockingGet()).isNotNull()
                }

                it("should contain the correct values") {
                    assertThat(result?.blockingGet()).isEqualTo(user)
                }
            }
        }

        describe("getting all favourites") {
            val userEntityList = mockk<List<UserEntity>>()
            var result: Flowable<List<User>>? = null
            beforeEachTest {
                every { mapper.transform(userEntityList) } returns emptyList()
                every { userDao.getFavourites() } returns Flowable.just(userEntityList)
            }

            describe("getFavourites") {
                beforeEachTest {
                    result = repository.getFavourites()
                }

                it("should not be null") {
                    assertThat(result?.blockingFirst()).isNotNull()
                }

                it("should contain the correct values") {
                    assertThat(result?.blockingFirst()).isInstanceOf(emptyList<User>()::class.java)
                }
            }
        }

        describe("deleting all favourites") {
            var result: Completable? = null
            beforeEachTest {
                every { userDao.deleteAllFavourites() } returns Completable.complete()
            }

            describe("getFavourites") {
                beforeEachTest {
                    result = repository.deleteAllFavourites()
                }

                it("should not be null") {
                    assertThat(result).isNotNull()
                }
            }
        }
    }
})
