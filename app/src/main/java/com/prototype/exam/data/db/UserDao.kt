package com.prototype.exam.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prototype.exam.data.model.User
import com.prototype.exam.data.model.forecast.ForecastItem

@Dao
interface UserDao {
    @Query("DELETE FROM user")
    fun deleteAll()

    @Query("SELECT * FROM user ORDER BY id")
    fun getUsers(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUsers(list: List<User>)

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: Int): User
}