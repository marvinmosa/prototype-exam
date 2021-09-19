package com.prototype.exam

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.prototype.exam.data.api.ApiHelper
import com.prototype.exam.data.db.AppDatabase
import com.prototype.exam.data.model.forecast.ForecastItem
import com.prototype.exam.data.model.forecast.Main
import com.prototype.exam.data.repository.UserRepository
import com.prototype.exam.data.repository.UserRepositoryImpl
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class MainRepositoryTest {

    private lateinit var mainRepository: UserRepository

    private lateinit var appDatabase: AppDatabase

    @Before
    fun setUp() {

        appDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AppDatabase::class.java).build()
        val apiHelper = Mockito.mock(ApiHelper::class.java)
        mainRepository = UserRepositoryImpl(apiHelper, appDatabase.forecastDao())
    }

    @Test
    fun getForecasts() {
    }

    @Test
    fun getForecast() {
    }

    @Test
    fun getLiveLocalForecasts() {
    }



    @Test
    fun add_and_update_forecast() {
        val finalForecastItem =
            ForecastItem("101", Main("1", "2", "3"), "test", emptyList(), false)
        assertEquals(0, mainRepository.getLocalForecasts().size)

        mainRepository.addForecast(finalForecastItem)
        assertEquals(1, mainRepository.getLocalForecasts().size)

        val actualForecastItem = mainRepository.getLocalForecast(finalForecastItem.id.toInt())
        assertEquals(finalForecastItem, actualForecastItem)

        mainRepository.updateForecastFavorite(finalForecastItem.id, true)
        assertEquals(
            true,
            mainRepository.getLocalForecast(finalForecastItem.id.toInt()).favorite
        )

    }

    @Test
    fun updateForecastFavorite() {
    }
}