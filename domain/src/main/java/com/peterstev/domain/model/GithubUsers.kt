package com.peterstev.domain.model

import java.io.Serializable

data class GithubUsers(
    val incompleteResults: Boolean,
    val totalCount: Int,
    val users: List<User>,
) : Serializable
