package com.prototype.exam.data.model.forecast

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.prototype.exam.data.db.DataConverter

@Entity(tableName = "forecast")
data class ForecastItem(
    @PrimaryKey
    var id: String,
    @Embedded
    var main: Main,
    var name: String,
    @TypeConverters(DataConverter::class)
    @SerializedName("weather")
    var weatherList: List<WeatherItem>,
    var favorite: Boolean
)