package by.romanovich.theweatherapp.room

import androidx.room.Database
import androidx.room.RoomDatabase

//передаём масив наших таблиц(у нас она одна), и что за версия баз данных
//используем этот класс как дата басе и в нем есть таблица, версии 1,
@Database( entities = [HistoryWeatherEntity::class],
    version = 1, exportSchema = false)
//если таблиц много - @Database( entities = [HistoryWeatherEntity::class],[HistoryWeatherEntity::class],[HistoryWeatherEntity::class],
  //  version = 1, exportSchema = false)

abstract class HistoryDatabase: RoomDatabase() {
    //функция доступа к нашей погоде,
    abstract fun historyWeatherDao():HistoryWeatherDao


}