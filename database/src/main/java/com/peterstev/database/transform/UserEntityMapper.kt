package com.peterstev.database.transform

import com.peterstev.database.entity.UserEntity
import com.peterstev.domain.model.User
import javax.inject.Inject

class UserEntityMapper @Inject constructor() {

    fun transform(entity: UserEntity): User {
        return entity.run {
            transformEntityToUser(this)
        }
    }

    fun transform(entity: List<UserEntity>): List<User> {
        return entity.map {
            it.run {
                transformEntityToUser(it)
            }
        }
    }

    fun transform(user: User): UserEntity {
        return user.run {
            UserEntity(
                avatar_url = avatarUrl,
                events_url = eventsUrl,
                followers_url = followersUrl,
                following_url = followingUrl,
                gists_url = gistsUrl,
                gravatar_id = gravatarId,
                html_url = htmlUrl,
                id = id,
                login = login,
                node_id = nodeId,
                organizations_url = organizationsUrl,
                received_events_url = receivedEventsUrl,
                repos_url = reposUrl,
                score = score,
                starred_url = starredUrl,
                site_admin = siteAdmin,
                subscriptions_url = subscriptionsUrl,
                type = type,
                url = url,
                name = name,
                company = company,
                blog = blog,
                location = location,
                email = email,
                hireable = hireable,
                bio = bio,
                twitter_username = twitterUsername,
                public_repos = publicRepos,
                public_gists = publicGists,
                followers = followers,
                following = following,
                created_at = createdAt,
                updated_at = updatedAt,
                is_favourite = isFavourite
            )
        }
    }

    private fun transformEntityToUser(entity: UserEntity): User {
        return entity.run {
            User(
                avatarUrl = avatar_url,
                eventsUrl = events_url,
                followersUrl = followers_url,
                followingUrl = following_url,
                gistsUrl = gists_url,
                gravatarId = gravatar_id,
                htmlUrl = html_url,
                id = id,
                login = login,
                nodeId = node_id,
                organizationsUrl = organizations_url,
                receivedEventsUrl = received_events_url,
                reposUrl = repos_url,
                score = score,
                starredUrl = starred_url,
                siteAdmin = site_admin,
                subscriptionsUrl = subscriptions_url,
                type = type,
                url = url,
                name = name,
                company = company,
                blog = blog,
                location = location,
                email = email,
                hireable = hireable,
                bio = bio,
                twitterUsername = twitter_username,
                publicRepos = public_repos,
                publicGists = public_gists,
                followers = followers,
                following = following,
                createdAt = created_at,
                updatedAt = updated_at,
                isFavourite = is_favourite
            )
        }
    }
}
