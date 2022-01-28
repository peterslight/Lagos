package com.peterstev.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users_table")
data class UserEntity(
    @PrimaryKey val login: String,
    val id: Int,
    val avatar_url: String,
    val events_url: String,
    val followers_url: String,
    val following_url: String,
    val gists_url: String,
    val gravatar_id: String,
    val html_url: String,
    val node_id: String,
    val organizations_url: String,
    val received_events_url: String,
    val repos_url: String,
    val score: Double,
    val site_admin: Boolean,
    val starred_url: String,
    val subscriptions_url: String,
    val type: String,
    val url: String = "",
    val name: String = "",
    val company: String = "",
    val blog: String = "",
    val location: String = "",
    val email: String = "",
    val hireable: Boolean = false,
    val bio: String = "",
    val twitter_username: String = "",
    val public_repos: Int = 0,
    val public_gists: Int = 0,
    val followers: Int = 0,
    val following: Int = 0,
    val created_at: String = "",
    val updated_at: String = "",
    var is_favourite: Boolean = false,
) : Serializable
