package com.peterstev.lagosdevs.viewmodel

import com.peterstev.domain.model.User
import com.peterstev.domain.usecase.FavouritesUseCase
import com.peterstev.lagosdevs.LiveDataSpek
import com.peterstev.lagosdevs.routing.Router
import com.peterstev.lagosdevs.util.RxSchedulers
import com.peterstev.lagosdevs.viewmodel.FavouriteViewModel.FavouriteState.*
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

@ExperimentalCoroutinesApi
class FavouriteViewModelTest : Spek({

    include(LiveDataSpek())

    describe("FavouriteViewModel") {

        val favouriteUseCase: FavouritesUseCase by memoized { mockk() }
        val router: Router by memoized { mockk() }
        val schedulers: RxSchedulers by memoized {
            mockk {
                every { IO } returns Schedulers.trampoline()
                every { MAIN } returns Schedulers.trampoline()
            }
        }
        lateinit var viewModel: FavouriteViewModel
        beforeEachTest {
            viewModel = FavouriteViewModel(favouriteUseCase, router, schedulers)
        }

        describe("a favourite state") {
            val state: MutableList<FavouriteViewModel.FavouriteState> = mutableListOf()
            beforeEachTest {
                viewModel.favouriteLiveData.observeForever {
                    state.add(it)
                }
            }

            afterEachTest {
                state.clear()
            }

            context("getFavourites") {
                describe("fetching favourites") {
                    val userList: List<User> = emptyList()
                    beforeEachTest {
                        every { favouriteUseCase.getFavourites() } returns Flowable.just(userList)
                    }

                    describe("getFavourites") {
                        beforeEachTest {
                            viewModel.getFavourites()
                        }

                        it("should contain the correct state") {
                            assertThat(state[1]).isInstanceOf(Success::class.java)
                        }

                        it("should contain the correct type") {
                            val result = (state[1] as Success<*>).data
                            assertThat(result).isEqualTo(emptyList<User>())
                        }
                    }
                }

                describe("error fetching favourites") {
                    beforeEachTest {
                        every { favouriteUseCase.getFavourites() } returns Flowable.error(Exception())
                    }

                    describe("getFavourites") {
                        beforeEachTest {
                            viewModel.getFavourites()
                        }

                        it("should contain the correct state") {
                            assertThat(state[0]).isInstanceOf(Loading::class.java)
                            assertThat(state[1]).isInstanceOf(Error::class.java)
                        }
                    }
                }
            }

            context("deleteAllFavourites") {
                describe("deleting favourites") {
                    beforeEachTest {
                        every { favouriteUseCase.deleteAllFavourites() } returns Completable.complete()
                    }

                    describe("deleteAllFavourites") {
                        beforeEachTest {
                            viewModel.deleteAllFavourites()
                        }

                        it("should contain the correct state") {
                            assertThat(state[0]).isInstanceOf(Loading::class.java)
                            assertThat(state[1]).isInstanceOf(Success::class.java)
                        }
                    }
                }

                describe("error deleting favourites") {
                    beforeEachTest {
                        every { favouriteUseCase.deleteAllFavourites() } returns Completable.error(Throwable())
                    }

                    describe("deleteAllFavourites") {
                        beforeEachTest {
                            viewModel.deleteAllFavourites()
                        }

                        it("should contain the correct state") {
                            assertThat(state[0]).isInstanceOf(Loading::class.java)
                            assertThat(state[1]).isInstanceOf(Error::class.java)
                        }
                    }
                }
            }

            context("deleteFavourite") {
                describe("deleting a favourite") {
                    val result = 1
                    val user = mockk<User>()
                    beforeEachTest {
                        every { favouriteUseCase.deleteFavourite(user) } returns Single.just(result)
                    }

                    describe("deleteFavourite") {
                        beforeEachTest {
                            viewModel.deleteFavourite(user)
                        }

                        it("should contain the correct state") {
                            assertThat(state[0]).isInstanceOf(Success::class.java)
                        }

                        it("should contain the correct success message") {
                            assertThat((state[0] as Success<*>).data).isEqualTo("Success")
                        }
                    }
                }

                describe("error deleting a favourite") {
                    val result = -1
                    val user = mockk<User>()
                    beforeEachTest {
                        every { favouriteUseCase.deleteFavourite(user) } returns Single.just(result)
                    }

                    describe("deleteFavourite") {
                        beforeEachTest {
                            viewModel.deleteFavourite(user)
                        }

                        it("should contain the correct state") {
                            assertThat(state[0]).isInstanceOf(Success::class.java)
                        }

                        it("should contain the correct success message") {
                            assertThat((state[0] as Success<*>).data).isEqualTo("failure")
                        }
                    }
                }

                describe("error thrown while deleting favourites") {
                    val user = mockk<User>()
                    beforeEachTest {
                        every { favouriteUseCase.deleteFavourite(user) } returns Single.error(Throwable())
                    }

                    describe("deleteFavourite") {
                        beforeEachTest {
                            viewModel.deleteFavourite(user)
                        }

                        it("should contain the correct state") {
                            assertThat(state[0]).isInstanceOf(Error::class.java)
                        }
                    }
                }
            }
        }
    }
})
