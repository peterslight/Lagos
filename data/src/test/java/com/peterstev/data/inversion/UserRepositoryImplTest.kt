package com.peterstev.data.inversion

import com.peterstev.data.model.JsonGithubUsers
import com.peterstev.data.model.JsonUser
import com.peterstev.data.network.ApiService
import com.peterstev.data.transform.GithubUsersMapper
import com.peterstev.data.transform.UserMapper
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

class UserRepositoryImplTest : Spek({

    describe("UserRepositoryImpl") {
        val service: ApiService by memoized { mockk() }
        val mapper: GithubUsersMapper by memoized { mockk() }
        val userMapper: UserMapper by memoized { mockk() }
        lateinit var repository: UserRepository
        beforeEachTest {
            repository = UserRepositoryImpl(service, mapper, userMapper)
        }

        describe("a service") {
            val page = 1
            val jsonUsers: JsonGithubUsers = mockk()
            val users = GithubUsers(
                incompleteResults = true,
                totalCount = 2,
                users = emptyList()
            )
            beforeEachTest {
                every { service.getUsers(page) } returns Observable.just(jsonUsers)
                every { mapper.transform(jsonUsers) } returns users
            }

            describe("getUsers") {
                lateinit var result: Observable<GithubUsers>
                beforeEachTest {
                    result = repository.getUsers(page)
                }

                it("should return a non null value") {
                    assertThat(result.blockingFirst()).isNotNull
                }

                it("should return an Observable<GithubUsers>") {
                    assertThat(result.blockingFirst()).isInstanceOf(GithubUsers::class.java)
                }

                it("should return the correct values") {
                    val value = result.blockingFirst()
                    assertThat(value).isEqualTo(users)
                    assertThat(value.users.size).isEqualTo(0)
                    assertThat(value.incompleteResults).isEqualTo(true)
                    assertThat(value.totalCount).isEqualTo(2)
                }
            }
        }
        describe("a service") {
            val username = "Peterstev"
            val jsonUser: JsonUser = mockk()
            val user: User = mockk()
            beforeEachTest {
                every { service.getUser(username) } returns Single.just(jsonUser)
                every { userMapper.transform(jsonUser) } returns user
            }

            describe("get single user") {
                lateinit var result: Single<User>
                beforeEachTest {
                    result = repository.getUser(username)
                }

                it("should return a non null value") {
                    assertThat(result.blockingGet()).isNotNull
                }

                it("should return a Single<User>") {
                    assertThat(result.blockingGet()).isInstanceOf(User::class.java)
                }

                it("should return the correct values") {
                    val value = result.blockingGet()
                    assertThat(value).isEqualTo(user)
                }
            }
        }
    }
})
