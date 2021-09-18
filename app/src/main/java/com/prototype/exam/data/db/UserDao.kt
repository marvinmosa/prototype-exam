package com.prototype.exam.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prototype.exam.data.model.forecast.ForecastItem

@Dao
interface UserDao {
    @Query("SELECT * FROM forecast ORDER BY id")
    fun getAllLiveForecasts(): LiveData<List<ForecastItem>>

    @Query("SELECT * FROM forecast ORDER BY id")
    fun getAllForecasts(): List<ForecastItem>

    @Query("SELECT * FROM forecast WHERE id = :id")
    fun getForecast(id: Int): ForecastItem

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addForecast(forecast: ForecastItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addForecastList(forecastList: List<ForecastItem>)

    @Query("UPDATE forecast SET favorite = :isToggled WHERE id = :id")
    fun updateForecastFavorite(id: String, isToggled: Boolean)

    @Query("DELETE FROM forecast")
    fun deleteAll()
}