package com.peterstev.data.inversion

import com.peterstev.data.network.ApiService
import com.peterstev.data.transform.GithubUsersMapper
import com.peterstev.data.transform.UserMapper
import com.peterstev.domain.model.GithubUsers
import com.peterstev.domain.model.User
import com.peterstev.domain.repository.UserRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val service: ApiService,
    private val githubUsersMapper: GithubUsersMapper,
    private val userMapper: UserMapper,
) : UserRepository {

    override fun getUsers(page: Int): Observable<GithubUsers> {
        return service.getUsers(page)
            .map { githubUsersMapper.transform(it) }
    }

    override fun getUser(username: String): Single<User> {
        return service.getUser(username)
            .map { userMapper.transform(it) }
    }

}
