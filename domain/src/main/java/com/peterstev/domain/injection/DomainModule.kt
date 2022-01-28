package com.peterstev.domain.injection

import com.peterstev.domain.usecase.FavouritesUseCase
import com.peterstev.domain.usecase.FavouritesUseCaseImpl
import com.peterstev.domain.usecase.UserUseCase
import com.peterstev.domain.usecase.UserUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
abstract class DomainModule {

    @Binds
    internal abstract fun bindUserUseCase(
        useCaseImpl: UserUseCaseImpl,
    ): UserUseCase

    @Binds
    internal abstract fun bindFavouritesUseCase(
        useCaseImpl: FavouritesUseCaseImpl,
    ): FavouritesUseCase
}
