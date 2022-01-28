package com.peterstev.domain.usecase

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

class FavouritesUseCaseImplTest : Spek({

    describe("FavouritesUseCase") {

        val repository: FavouriteRepository by memoized { mockk() }
        lateinit var favouriteUseCase: FavouritesUseCase
        beforeEachTest {
            favouriteUseCase = FavouritesUseCaseImpl(repository)
        }

        describe("adding a favourite") {
            val user = mockk<User>()
            lateinit var result: Single<Long>
            beforeEachTest {
                every { repository.addFavourite(user) } returns Single.just(1)
            }

            describe("addFavourite") {
                beforeEachTest {
                    result = favouriteUseCase.addFavourite(user)
                }

                it("should return a value of 1") {
                    assertThat(result.blockingGet()).isEqualTo(1)
                }
            }
        }

        describe("getting all favourite") {
            lateinit var result: Flowable<List<User>>
            beforeEachTest {
                every { repository.getFavourites() } returns Flowable.just(emptyList())
            }

            describe("getFavourites") {
                beforeEachTest {
                    result = favouriteUseCase.getFavourites()
                }

                it("should return a correct value") {
                    assertThat(result.blockingFirst()).isEqualTo(emptyList<User>())
                }
            }
        }

        describe("getting a single favourite") {
            lateinit var result: Single<User>
            val user = mockk<User>()
            val username = "peterstev"
            beforeEachTest {
                every { repository.getSingleFavourite(username) } returns Single.just(user)
            }

            describe("getSingleFavourite") {
                beforeEachTest {
                    result = favouriteUseCase.getSingleFavourite(username)
                }

                it("should return a correct value") {
                    assertThat(result.blockingGet()).isInstanceOf(User::class.java)
                }
            }
        }

        describe("deleting a single favourite") {
            lateinit var result: Single<Int>
            val user = mockk<User>()
            beforeEachTest {
                every { repository.deleteFavourite(user) } returns Single.just(1)
            }

            describe("deleteFavourite") {
                beforeEachTest {
                    result = favouriteUseCase.deleteFavourite(user)
                }

                it("should return a correct value") {
                    assertThat(result.blockingGet()).isInstanceOf(Int::class.java)
                    assertThat(result.blockingGet()).isEqualTo(1)
                }
            }
        }

        describe("deleting all favourites") {
            lateinit var result: Completable
            beforeEachTest {
                every { repository.deleteAllFavourites() } returns Completable.complete()
            }

            describe("deleteAllFavourites") {
                beforeEachTest {
                    result = favouriteUseCase.deleteAllFavourites()
                }

                it("should return a non null value") {
                    assertThat(result).isNotNull
                }
            }
        }
    }
})
