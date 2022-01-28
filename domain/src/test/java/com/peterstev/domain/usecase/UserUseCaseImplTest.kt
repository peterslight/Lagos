package com.peterstev.domain.usecase

import com.peterstev.domain.model.GithubUsers
import com.peterstev.domain.model.User
import com.peterstev.domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class UserUseCaseImplTest : Spek({

    describe("UserUseCase") {
        val userRepository: UserRepository by memoized { mockk() }
        lateinit var userUseCase: UserUseCase
        beforeEachTest {
            userUseCase = UserUseCaseImpl(userRepository)
        }

        describe("fetching users") {
            val page = 1
            lateinit var users: GithubUsers
            beforeEachTest {
                users = GithubUsers(incompleteResults = false, totalCount = 20, users = emptyList())
                every { userRepository.getUsers(page) } returns Observable.just(users)
            }

            describe("getUsers") {
                lateinit var result: Observable<GithubUsers>
                beforeEachTest {
                    result = userUseCase.getUsers(page)
                }

                it("should not be null") {
                    assertThat(result.blockingFirst()).isNotNull
                }

                it("should contain correct values") {
                    val value = result.blockingFirst()
                    assertThat(value.totalCount).isEqualTo(20)
                    assertThat(value.incompleteResults).isEqualTo(false)
                    assertThat(value.users).hasSize(0)
                }
            }
        }

        describe("fetching a single user") {
            val username = "Peterstev"
            lateinit var user: User
            beforeEachTest {
                user = mockk()
                every { userRepository.getUser(username) } returns Single.just(user)
            }

            describe("getUser") {
                lateinit var result: Single<User>
                beforeEachTest {
                    result = userUseCase.getUser(username)
                }

                it("should not be null") {
                    assertThat(result.blockingGet()).isNotNull
                }

                it("should contain correct values") {
                    val value = result.blockingGet()
                    assertThat(value).isEqualTo(User::class.java)
                }
            }
        }
    }
})
