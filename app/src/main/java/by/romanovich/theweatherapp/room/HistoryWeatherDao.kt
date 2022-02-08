package by.romanovich.theweatherapp.room

import android.database.Cursor
import androidx.room.*

@Dao
interface HistoryWeatherDao {

//команда инсерт поо вставку, в случае конфликта выбираем стратегию игнорировать(можно заменить)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    //функция вставить 1 погоду.
    fun insert(entity: HistoryWeatherEntity)

    @Delete
    fun delete(entity: HistoryWeatherEntity)

//удалять историю по ид
    @Query("DELETE FROM history_weather_entity WHERE id=:id")
    fun delete(id: Long)

    //вызов чтоб получать историю по id
    @Query("SELECT * FROM history_weather_entity WHERE id=:id")
    fun getHistoryCursor(id: Long): Cursor

    @Update
    fun update(entity: HistoryWeatherEntity)

//запрос, (получить *все данные из имя таблицы
    @Query("select * FROM history_weather_entity")
    //будет возращаться список наших погод
    fun getAllHistoryWeather():List<HistoryWeatherEntity>

    //fun getAllHistoryWeather() // TODO по какому-то полю получить, было бы неплохо
}


/*/** LESSON 9**/
@Query("DELETE FROM history_weather_entity WHERE id=:id")
fun delete(id: Long)

@Query("SELECT * FROM history_weather_entity WHERE id=:id")
fun getHistoryCursor(id: Long): Cursor
/****/*/