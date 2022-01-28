package com.peterstev.data.network

import com.peterstev.data.model.JsonGithubUsers
import com.peterstev.data.model.JsonUser
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users?q=lagos")
    fun getUsers(@Query("page") page: Int): Observable<JsonGithubUsers>

    @GET("users/{username}")
    fun getUser(@Path("username") username: String): Single<JsonUser>
}
