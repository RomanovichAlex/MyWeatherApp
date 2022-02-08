package by.romanovich.theweatherapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey


const val ID = "id"
const val NAME = "name"
const val TEMPERATURE = "temperature"
const val FEELS_LIKE = "feelsLike"
const val ICON = "icon"
//таблица для базы данных
//название таблицы
@Entity(tableName = "history_weather_entity")
//ид наш первичный ключ авто генерируемый
data class HistoryWeatherEntity(@PrimaryKey(autoGenerate = true)
                                var id: Long = 0,
                                var name:String = "",
                                var temperature: Int = 0,
                                var feelsLike: Int=0 ,
                                var icon: String=""
)
//ид, 0, 1, 2, 3, 4