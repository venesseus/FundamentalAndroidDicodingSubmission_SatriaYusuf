package com.example.githubuserfinder.data.util

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuserfinder.data.database.User
import com.example.githubuserfinder.data.database.UserDao
import com.example.githubuserfinder.data.database.UserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val mUserDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserRoomDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }
    fun getAllFavorites(): LiveData<List<User>> = mUserDao.getAllUser()

    fun insert(user: User) {
        executorService.execute { mUserDao.insertFavorite(user) }
    }

    fun delete(id: Int) {
        executorService.execute { mUserDao.removeFavorite(id) }
    }
}