package com.peterstev.database.injection

import android.app.Application
import android.content.Context
import com.peterstev.database.dao.UserDao
import com.peterstev.database.database.UserDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providesUserDatabase(application: Application): UserDatabase {
        return UserDatabase.getDatabase(application.applicationContext)
    }

    @Provides
    internal fun providesUserDao(database: UserDatabase): UserDao {
        return database.usersDao()
    }
}
