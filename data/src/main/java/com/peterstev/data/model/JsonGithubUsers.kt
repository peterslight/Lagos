package com.peterstev.data.model

data class JsonGithubUsers(
    val incomplete_results: Boolean,
    val items: List<JsonUser>,
    val total_count: Int,
)
