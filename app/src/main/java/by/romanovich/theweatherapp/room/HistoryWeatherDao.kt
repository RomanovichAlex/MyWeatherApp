package by.romanovich.theweatherapp.room

import androidx.room.*

@Dao
interface HistoryWeatherDao {

//команда инсерт поо вставку, в случае конфликта выбираем стратегию игнорировать(можно заменить)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    //функция вставить 1 погоду.
    fun insert(entity: HistoryWeatherEntity)

    @Delete
    fun delete(entity: HistoryWeatherEntity)

    @Update
    fun update(entity: HistoryWeatherEntity)

//запрос, (получить *все данные из имя таблицы
    @Query("select * FROM history_weather_entity")
    //будет возращаться список наших погод
    fun getAllHistoryWeather():List<HistoryWeatherEntity>

    //fun getAllHistoryWeather() // TODO по какому-то полю получить, было бы неплохо
}