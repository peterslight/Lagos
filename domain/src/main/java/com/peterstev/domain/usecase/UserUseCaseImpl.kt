package com.peterstev.domain.usecase

import com.peterstev.domain.model.GithubUsers
import com.peterstev.domain.model.User
import com.peterstev.domain.repository.UserRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


class UserUseCaseImpl @Inject constructor(
    private val repository: UserRepository,
) : UserUseCase {

    override fun getUsers(page: Int): Observable<GithubUsers> {
        return repository.getUsers(page)
    }

    override fun getUser(username: String): Single<User> {
        return repository.getUser(username)
    }
}
