package com.peterstev.domain.repository

import com.peterstev.domain.model.GithubUsers
import com.peterstev.domain.model.User
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface UserRepository {

    fun getUsers(page: Int): Observable<GithubUsers>

    fun getUser(username: String): Single<User>
}
