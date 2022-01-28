package com.peterstev.data.injection

import com.peterstev.data.inversion.UserRepositoryImpl
import com.peterstev.domain.repository.UserRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {

    @Binds
    internal abstract fun bindUserRepository(
        repositoryImpl: UserRepositoryImpl,
    ): UserRepository
}
