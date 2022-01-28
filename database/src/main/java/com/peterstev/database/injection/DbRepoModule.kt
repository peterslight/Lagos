package com.peterstev.database.injection

import com.peterstev.database.inversion.FavoriteRepositoryImpl
import com.peterstev.domain.repository.FavouriteRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DbRepoModule {
    @Binds
    internal abstract fun bindFavouriteRepository(
        repositoryImpl: FavoriteRepositoryImpl,
    ): FavouriteRepository
}
