package com.example.githubuserfinder.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class UserRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: UserRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserRoomDatabase {
            if(instance == null) {
                synchronized(UserRoomDatabase::class.java) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserRoomDatabase::class.java, "User_Database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance as UserRoomDatabase
        }
    }
}