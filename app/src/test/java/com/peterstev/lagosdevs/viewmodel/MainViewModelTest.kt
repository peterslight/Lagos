package com.peterstev.lagosdevs.viewmodel

import com.peterstev.domain.model.GithubUsers
import com.peterstev.domain.usecase.FavouritesUseCase
import com.peterstev.domain.usecase.UserUseCase
import com.peterstev.lagosdevs.LiveDataSpek
import com.peterstev.lagosdevs.util.RxSchedulers
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

@ExperimentalCoroutinesApi
class MainViewModelTest : Spek({
    include(LiveDataSpek())

    describe("MainViewModel") {

        val userUseCase: UserUseCase by memoized { mockk() }
        val favouritesUseCase: FavouritesUseCase by memoized { mockk() }
        val schedulers: RxSchedulers by memoized {
            mockk {
                every { IO } returns Schedulers.trampoline()
                every { MAIN } returns Schedulers.trampoline()
            }
        }

        lateinit var viewModel: MainViewModel
        beforeEachTest {
            viewModel = MainViewModel(userUseCase, favouritesUseCase, schedulers)
        }

        describe("users state") {
            val state: MutableList<MainViewModel.UsersState> = mutableListOf()
            beforeEachTest {
                viewModel.usersLiveData.observeForever {
                    state.add(it)
                }
            }

            afterEachTest {
                state.clear()
            }

            describe("a successful fetch") {
                val page = 1
                val githubUser = GithubUsers(
                    incompleteResults = true,
                    totalCount = 10,
                    users = emptyList()
                )
                beforeEachTest {
                    every { userUseCase.getUsers(page) } returns Observable.just(githubUser)
                }

                describe("fetchUsers") {
                    beforeEachTest {
                        viewModel.fetchUsers()
                    }

                    it("should contain the correct state") {
                        assertThat(state[1]).isInstanceOf(MainViewModel.UsersState.Success::class.java)
                    }

                    it("should contain the correct values") {
                        assertThat((state[1] as MainViewModel.UsersState.Success).data.totalCount).isEqualTo(10)
                        assertThat((state[1] as MainViewModel.UsersState.Success).data.incompleteResults).isTrue
                        assertThat((state[1] as MainViewModel.UsersState.Success).data.users).hasSize(0)
                    }
                }
            }

            describe("a failed fetch") {
                val page = 1
                val errorMessage = "an error occurred"
                beforeEachTest {
                    every { userUseCase.getUsers(page) } returns Observable.error(Exception(errorMessage))
                }

                describe("fetchUsers") {
                    beforeEachTest {
                        viewModel.fetchUsers()
                    }

                    it("should contain the correct state") {
                        assertThat(state[1]).isInstanceOf(MainViewModel.UsersState.Error::class.java)
                    }

                    it("should contain the correct error message") {
                        assertThat((state[1] as MainViewModel.UsersState.Error).throwable.message).isEqualTo("an error occurred")
                    }
                }
            }
        }
    }
})
