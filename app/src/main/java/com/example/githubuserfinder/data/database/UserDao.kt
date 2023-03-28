package com.example.githubuserfinder.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(user: User)

    @Query("DELETE FROM User WHERE User.id = :id")
    fun removeFavorite(id: Int)

    @Query("SELECT * FROM User ORDER BY login ASC")
    fun getAllUser(): LiveData<List<User>>

    @Query("SELECT * FROM User WHERE User.id =  :id")
    fun getUserById(id: Int): LiveData<User>
}