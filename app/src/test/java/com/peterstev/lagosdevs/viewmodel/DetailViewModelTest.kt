package com.peterstev.lagosdevs.viewmodel

import com.peterstev.domain.model.User
import com.peterstev.domain.usecase.FavouritesUseCase
import com.peterstev.domain.usecase.UserUseCase
import com.peterstev.lagosdevs.LiveDataSpek
import com.peterstev.lagosdevs.routing.Router
import com.peterstev.lagosdevs.util.RxSchedulers
import io.mockk.*
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

@ExperimentalCoroutinesApi
class DetailViewModelTest : Spek({
    include(LiveDataSpek())
    describe("DetailViewModel") {

        val userUseCase: UserUseCase by memoized { mockk() }
        val favouriteUseCase: FavouritesUseCase by memoized { mockk() }
        val router: Router by memoized { mockk() }
        val schedulers: RxSchedulers by memoized {
            mockk {
                every { IO } returns Schedulers.trampoline()
                every { MAIN } returns Schedulers.trampoline()
            }
        }
        lateinit var viewModel: DetailViewModel
        beforeEachTest {
            viewModel = DetailViewModel(userUseCase, favouriteUseCase, router, schedulers)
        }

        describe("a user state") {
            val state: MutableList<DetailViewModel.UserState> = mutableListOf()
            beforeEachTest {
                viewModel.userLiveData.observeForever {
                    state.add(it)
                }
            }

            afterEachTest {
                state.clear()
            }

            describe("a successful fetch") {
                val username = "peterstev"
                val user = mockk<User>()
                beforeEachTest {
                    every { userUseCase.getUser(username) } returns Single.just(user)
                }

                describe("fetchUser") {
                    beforeEachTest {
                        viewModel.fetchUser(username)
                    }

                    it("should contain the correct state") {
                        assertThat(state[1]).isInstanceOf(DetailViewModel.UserState.Success::class.java)
                    }
                }
            }

            describe("a failed fetch") {
                val errorMessage = "an error occurred"
                val username = "peterstev"
                val user = mockk<User>()
                beforeEachTest {
                    every { userUseCase.getUser(username) } returns Single.error(Exception(errorMessage))
                    every { favouriteUseCase.getSingleFavourite(username) } returns Single.just(user)
                }

                describe("fetchUser") {
                    beforeEachTest {
                        viewModel.fetchUser(username)
                    }

                    it("should contain the correct state") {
                        assertThat(state[1]).isInstanceOf(DetailViewModel.UserState.Error::class.java)
                    }

                    it("should call the getFavourite function") {
                        verify { favouriteUseCase.getSingleFavourite("peterstev") }
                    }
                }
            }
        }

        describe("a favourite state"){
            val state: MutableList<DetailViewModel.FavouriteState> = mutableListOf()
            beforeEachTest {
                viewModel.favLiveData.observeForever {
                    state.add(it)
                }
            }

            afterEachTest {
                state.clear()
            }

            describe("successfully adding a favourite") {
                val user = mockk<User>()
                beforeEachTest {
                    every { favouriteUseCase.addFavourite(user) } returns Single.just(1)
                }

                describe("addFavourite") {
                    beforeEachTest {
                        viewModel.addFavourite(user)
                    }

                    it("should contain the correct state") {
                        assertThat(state[0]).isInstanceOf(DetailViewModel.FavouriteState.Success::class.java)
                    }
                }
            }

            describe("failure adding a favourite") {
                val user = mockk<User>()
                beforeEachTest {
                    every { favouriteUseCase.addFavourite(user) } returns Single.just(-1)
                }

                describe("addFavourite") {
                    beforeEachTest {
                        viewModel.addFavourite(user)
                    }

                    it("should contain the correct state") {
                        assertThat(state[0]).isInstanceOf(DetailViewModel.FavouriteState.Failure::class.java)
                    }
                }
            }
        }

        describe("toBrowser") {
            beforeEachTest {
                every { router.toBrowser("url") } just runs
                viewModel.toBrowser("url")
            }

            it("should call router.Browser"){
                verify { router.toBrowser("url") }
            }
        }
    }
})
