package com.peterstev.data.transform

import com.peterstev.data.model.JsonGithubUsers
import com.peterstev.domain.model.GithubUsers
import javax.inject.Inject

class GithubUsersMapper @Inject constructor(
    private val userMapper: UserMapper,
) {

    fun transform(item: JsonGithubUsers): GithubUsers {
        return GithubUsers(
            incompleteResults = item.incomplete_results,
            totalCount = item.total_count,
            users = userMapper.transform(item.items)
        )
    }
}
