package com.peterstev.database.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.peterstev.database.dao.UserDao
import com.peterstev.database.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun usersDao(): UserDao

    companion object {
        private const val databaseName = "users_database"

        @Volatile
        private var database: UserDatabase? = null

        fun getDatabase(context: Context) =
            database ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    UserDatabase::class.java,
                    databaseName
                ).build()
                database = instance
                instance
            }
    }
}
