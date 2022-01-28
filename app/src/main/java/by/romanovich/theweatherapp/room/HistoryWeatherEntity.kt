package by.romanovich.theweatherapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey
//таблица для базы данных
//название таблицы
@Entity(tableName = "history_weather_entity")
//ид наш первичный ключ авто генерируемый
data class HistoryWeatherEntity(@PrimaryKey(autoGenerate = true) val id: Long, val city:String, val temperature: Int, val feelsLike: Int, val icon: String
)
//ид, 0, 1, 2, 3, 4