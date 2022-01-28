package com.peterstev.data.transform

import com.peterstev.data.model.JsonUser
import com.peterstev.domain.model.User
import javax.inject.Inject

class UserMapper @Inject constructor() {

    fun transform(items: List<JsonUser>): List<User> {
        return items.map { toUser(it) }
    }

    fun transform(item: JsonUser): User {
        return toUser(item)
    }

    private fun toUser(item: JsonUser): User {
        return item.run {
            User(
                avatarUrl = avatar_url ?: "",
                eventsUrl = events_url ?: "",
                followersUrl = followers_url ?: "",
                followingUrl = following_url ?: "",
                gistsUrl = gists_url ?: "",
                gravatarId = gravatar_id ?: "",
                htmlUrl = html_url ?: "",
                id = id ?: 0,
                login = login ?: "",
                nodeId = node_id ?: "",
                organizationsUrl = organizations_url ?: "",
                receivedEventsUrl = received_events_url ?: "",
                reposUrl = repos_url ?: "",
                score = score ?: 0.0,
                starredUrl = starred_url ?: "",
                siteAdmin = site_admin ?: false,
                subscriptionsUrl = subscriptions_url ?: "",
                type = type ?: "",
                url = url ?: "",
                name = name ?: "",
                company = company ?: "",
                blog = blog ?: "",
                location = location ?: "",
                email = email ?: "",
                hireable = hireable ?: false,
                bio = bio ?: "",
                twitterUsername = twitter_username ?: "",
                publicRepos = public_repos ?: 0,
                publicGists = public_gists ?: 0,
                followers = followers ?: 0,
                following = following ?: 0,
                createdAt = created_at ?: "",
                updatedAt = updated_at ?: ""
            )
        }
    }
}
